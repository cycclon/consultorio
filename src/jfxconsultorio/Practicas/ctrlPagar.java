/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Bitacoras.Cuentas.bPagoRegistrado;
import com.PRS.Consultorio.Colegio.clsPagoColegio;
import com.PRS.Consultorio.Cuentas.clsCobro;
import com.PRS.Consultorio.Cuentas.clsPago;
import com.PRS.Consultorio.Practicas.clsTrabajo;
import com.PRS.Framework.FormulariosFX.clsGestorValidacion;
import com.PRS.Framework.FormulariosFX.enTipoTextField;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlPagar implements Initializable {
    
    public interface iListenerPago
    {public void PagoEfectuado(clsTrabajo trabajo, clsCobro cobro, clsPago pago);}

    @FXML private Label lblTrabajo;
    @FXML private Label lblFecha;
    @FXML private Label lblHora;
    @FXML private Label lblCoseguro;
    @FXML private Label lblPagado;
    @FXML private Label lblPaciente;
    @FXML private Label lblSaldo;
    
    @FXML private TextField txtPago;
    
    @FXML private Button btnPagar;
    
    @FXML private DatePicker dpFecha;
    
    private clsTrabajo Trabajo;
    private List<iReceptorError> ListenersError;
    private List<iListenerPago> ListenersPago;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ListenersError = new ArrayList<>();
        this.ListenersPago = new ArrayList<>();
        this.dpFecha.setValue(LocalDate.now());
        
    }    

    void setTrabajo(clsTrabajo trabajo) throws Exception
    {this.Trabajo = trabajo; this.MostrarTrabajo();}
    
    private void MostrarTrabajo() throws Exception
    {
        this.lblTrabajo.setText(Trabajo.getPractica().getNombreyCodigo());
        this.lblFecha.setText(new SimpleDateFormat("dd/MM/yyyy")
                .format(Trabajo.getProgramacion().getFecha()));
        this.lblHora.setText(Trabajo.getProgramacion().getHorario().getHoraStr());
        this.lblCoseguro.setText(Trabajo.getCobroCoseguro().getMonto().pdValorString());
        this.lblPagado.setText(Trabajo.getCobroCoseguro().getTotalPagado().pdValorString());
        this.lblPaciente.setText(Trabajo.getPaciente().getNombreCompleto());
        this.lblSaldo.setText(Trabajo.getPaciente().getCuenta().getSaldo().pdValorString());
        this.txtPago.setText(String.valueOf(Trabajo.getCobroCoseguro().getRestante().pdValor()));
        this.dpFecha.setValue(this.Trabajo.getProgramacion().getFecha().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate());
    }    
    
    @FXML
    private void btnPagar_Click(ActionEvent event)
    {
        try
        { 
            JOptionPane confirm = new JOptionPane("¿Está seguro que desea "
                    + "registrar el pago de este trabajo?", JOptionPane.QUESTION_MESSAGE, 
                    JOptionPane.YES_NO_OPTION);
            JDialog dialog = confirm.createDialog("Pagar coseguro");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);            
            if((int)confirm.getValue() == JOptionPane.YES_OPTION)
            {
                dialog.dispose();
                clsGestorValidacion.ValidarTextField(txtPago, "Monto a pagar", 
                        true, enTipoTextField.Decimal);

                Trabajo.getCobroCoseguro().Pagar(Float.parseFloat(this.txtPago
                        .getText()), Date.from(this.dpFecha.getValue().atStartOfDay()
                            .atZone(ZoneId.systemDefault()).toInstant()));
                
                new bPagoRegistrado(Trabajo, Trabajo.getCobroCoseguro().getPagos()
                        .get(Trabajo.getCobroCoseguro().getPagos().size()-1)).Registrar();

                this.NotificarPago(Trabajo.getCobroCoseguro(), new clsPago(
                        Float.parseFloat(this.txtPago.getText()), Date.from(this
                                .dpFecha.getValue().atStartOfDay()
                            .atZone(ZoneId.systemDefault()).toInstant())));
            }
        }
        
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    private void RaiseError(String error)
    {
        for(iReceptorError xRE : ListenersError)
        {xRE.ErrorOcurred(error);}
    }
    
    public void addListenerPago(iListenerPago listener)
    {this.ListenersPago.add(listener);}
    
    private void NotificarPago(clsCobro cobro, clsPago pago)
    {
        for(iListenerPago xLP : ListenersPago)
        {xLP.PagoEfectuado(Trabajo, cobro, pago);}
    }
}
