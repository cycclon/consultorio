/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Gastos;

import com.PRS.Consultorio.CentrosCosto.clsCentroCosto;
import com.PRS.Consultorio.Gastos.clsAsignacionGasto;
import com.PRS.Consultorio.Gastos.clsGasto;
import com.PRS.Consultorio.Gastos.clsGestorGastos;
import com.PRS.Consultorio.Gastos.enFrecuencia;
import com.PRS.Framework.FormulariosFX.clsGestorValidacion;
import com.PRS.Framework.FormulariosFX.enTipoTextField;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import com.PRS.Framework.Monedas.clsValor;
import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author ARSPIDALIERI
 */
public class ctrlAddGasto implements Initializable {

    @FXML private ComboBox cbFrecuencia;
    @FXML private DatePicker dpFechaGasto;
    @FXML private TextField txtConcepto;
    @FXML private TextField txtMonto;
    @FXML private ListView lstAsignaciones;
    @FXML private Label lblEstadoAsignaciones;
    @FXML private ComboBox cbCC;
    @FXML private TextField txtPorcentaje;
    
    
    private List<iReceptorError> ListenersError;
    private List<iGastos> ListenersGastos;
    private List<clsAsignacionGasto> Asignaciones;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.ListenersError = new ArrayList<>();
        this.Asignaciones = new ArrayList<>();
        this.ListenersGastos = new ArrayList<>();
        this.MostrarFrecuencias();
        this.MostrarCentrosCosto();
        this.ComprobarAsignaciones();
    }
    
    public void addListenerGasto(iGastos listener)
    {this.ListenersGastos.add(listener);}
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    private void MostrarFrecuencias()
    {
        this.cbFrecuencia.getItems().clear();
        for(enFrecuencia xF : enFrecuencia.values())
        {this.cbFrecuencia.getItems().add(xF.toString());}
    }
    
    private void RaiseError(String prMensaje)
    {
        ListenersError.stream().forEach((xRE) -> {
            xRE.ErrorOcurred(prMensaje);
        });
    }
    
    @FXML
    private void btnRegistrar_Click(ActionEvent event)
    {
        try
        {            
            clsGestorValidacion.ValidarTextField(txtMonto, "Monto", true, enTipoTextField.Decimal);
            clsGestorValidacion.ValidarTextField(txtConcepto, "Concepto", true, enTipoTextField.Texto);
            clsGestorValidacion.ValidarComboBox(cbFrecuencia, "Frecuencia", true);                        

            JOptionPane confirm = new JOptionPane("¿Está seguro que desea registrar este gasto?", JOptionPane.QUESTION_MESSAGE, 
            JOptionPane.YES_NO_OPTION);
            JDialog dialog = confirm.createDialog("Registrar gasto");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            if((int)confirm.getValue() == JOptionPane.YES_OPTION) {
                dialog.dispose();
                clsGasto xG = new clsGasto();
                xG.setAsignacion(Asignaciones);
                xG.setConcepto(this.txtConcepto.getText());
                xG.setFecha(Date.from(this.dpFechaGasto.getValue().atStartOfDay()
                            .atZone(ZoneId.systemDefault()).toInstant()));
                xG.setMonto(new clsValor(Float.parseFloat(this.txtMonto.getText())));

                xG.Registrar(enFrecuencia.valueOf(this.cbFrecuencia.getSelectionModel().getSelectedItem().toString()));
                this.NotificarRegistro(xG);
            }
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }
    
    @FXML
    private void btnAgregarAsignacion(ActionEvent event)
    {
        try
        {
            clsGestorValidacion.ValidarComboBox(cbCC, "Centro de costos", true);
            clsGestorValidacion.ValidarTextField(txtPorcentaje, "Porcentaje de asignación", 
                true, enTipoTextField.Decimal);
            clsAsignacionGasto xAG = new clsAsignacionGasto();
            xAG.setPorcentaje(Float.valueOf(this.txtPorcentaje.getText()));
            xAG.setcentro((clsCentroCosto)this.cbCC.getSelectionModel().getSelectedItem());
            this.Asignaciones.add(xAG);

            this.MostrarAsignaciones();
            this.MostrarCentrosCosto();
            this.txtPorcentaje.setText("");
            this.ComprobarAsignaciones();
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
        
    }
    
    @FXML
    private void btnQuitarAsignacion(ActionEvent event)
    {
        if(!this.lstAsignaciones.getSelectionModel().isEmpty())
        {
            this.Asignaciones.remove((clsAsignacionGasto)this.lstAsignaciones
                .getSelectionModel().getSelectedItem());
            this.MostrarAsignaciones();
            this.ComprobarAsignaciones();
        }
    }
    
    private void ComprobarAsignaciones()
    {
        float xP = clsGestorGastos.Instanciar()
                .ComprobarAsignaciones(this.Asignaciones);
        this.lblEstadoAsignaciones.setText("Porcentaje asignado: " + xP + "%");
        if(xP != 100)
        {this.lblEstadoAsignaciones.setStyle("-fx-background-color: #CC0000;");}
        else
        {this.lblEstadoAsignaciones.setStyle("-fx-background-color: #00CC00;");}
    }
    
       
    private void MostrarAsignaciones()
    {
        this.lstAsignaciones.getItems().clear();
        Asignaciones.stream().forEach((xAG) -> {
            this.lstAsignaciones.getItems().add(xAG);
        });
        
        if(Asignaciones.isEmpty()){this.lstAsignaciones.getItems().add("No hay asignaciones");}
    }
    
    private void MostrarCentrosCosto()
    {
        try
        {
            this.cbCC.getItems().clear();
            List<clsCentroCosto> xCCs = clsCentroCosto.Listar(false);
            xCCs.stream().forEach((xCC) -> {
                this.cbCC.getItems().add(xCC);
            });
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }
    
    private void NotificarRegistro(clsGasto gasto)
    {
        ListenersGastos.stream().forEach((xG) -> {
            xG.GastoRegistrado(gasto);
        });
    }
}
