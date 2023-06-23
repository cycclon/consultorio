/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Colegio;

import com.PRS.Consultorio.Colegio.clsColegio;
import com.PRS.Consultorio.Cuentas.clsCobroOS;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.clsGestorValidacion;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jfxconsultorio.Cuentas.ctrlCuenta;

/**
 * FXML Controller class
 *
 * @author cycclon
 */
public class ctrlColegio implements Initializable, iMensajeable, iReceptorError {

    @FXML private Label lblMensaje;
    @FXML private ComboBox cbMes;
    @FXML private ComboBox cbAno;
    @FXML private Label lblUltimoPago;
    @FXML private VBox vbPagosPendientes;
    @FXML private VBox vbCuenta;
    @FXML private DatePicker dpFechaPago;
    @FXML private CheckBox chToogle;
    
    private List<ctrlCobroPendiente> Pendientes;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.Pendientes = new ArrayList<>();
        this.MostrarColegio();
        this.MostrarAños();
        this.MostrarMeses();
        
    }    
    
    private void MostrarColegio()
    {
        try
        {
            clsColegio c = clsColegio.Instanciar();
            this.lblUltimoPago.setText(c.getUltimoPago());
            List<clsCobroOS> coss = c.getNoSaldados();
            this.MostrarPendientes(coss);
            
            this.vbCuenta.getChildren().clear();
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Cuentas/fxCuenta.fxml"));
            this.vbCuenta.getChildren().add(xL.load());
            ctrlCuenta cc = xL.<ctrlCuenta>getController();
            cc.addListenerError(this);
            cc.setCuenta(c.getCuenta());
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
    private void MostrarPendientes(List<clsCobroOS> pendientes) throws Exception
    {
        this.vbPagosPendientes.getChildren().clear();
        Pendientes.clear();
        for(clsCobroOS cos : pendientes)
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Colegio/fxCobroPendiente.fxml"));
            this.vbPagosPendientes.getChildren().add(xL.load());
            ctrlCobroPendiente ccp = xL.<ctrlCobroPendiente>getController();
            ccp.setCobro(cos);
            Pendientes.add(ccp);
        }
    }
    
    @FXML private void btnVer_Click(ActionEvent event)
    {
        try
        {
            clsGestorValidacion.ValidarComboBox(cbMes, "Mes", true);
            clsGestorValidacion.ValidarComboBox(cbAno, "Año", true);
            List<clsCobroOS> coss = clsColegio.Instanciar().getNoSaldados(
                    (byte)cbMes.getSelectionModel().getSelectedIndex(), 
                    Integer.valueOf(cbAno.getSelectionModel().getSelectedItem().toString()));
            this.MostrarPendientes(coss);
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
    private void ToogleSeleccion()
    {
        Pendientes.stream().forEach((cp) -> {
            cp.ToogleSelection(this.chToogle.isSelected());
        });
    }
    
    @FXML private void chToogle_CheckedChanged(Event event)
    {ToogleSeleccion();}

    @Override
    public Label getCanvas() {return lblMensaje;}

    @Override
    public String getNombre() {return "Colegio";}
    
    
    private void MostrarAños()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        this.cbAno.getItems().clear();
        for(int i = 2013; i<= cal.get(Calendar.YEAR); i++)cbAno.getItems().add(String.valueOf(i));
        cbAno.getSelectionModel().select(String.valueOf(cal.get(Calendar.YEAR)));
    }
    
    private void MostrarMeses()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String[] monthNames = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", 
            "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", 
            "Diciembre"};
        this.cbMes.getItems().clear();
        for(int i = 0; i< 12; i++)cbMes.getItems().add(monthNames[i]);
        cbMes.getSelectionModel().select(cal.get(Calendar.MONTH));
        if(Integer.parseInt(cbAno.getSelectionModel().getSelectedItem()
                .toString()) != cal.get(Calendar.YEAR))cbMes.getSelectionModel().select(0);
    }

    @Override
    public void ErrorOcurred(String prMensaje) 
    {clsGestorMensajes.Instanciar().MostrarError(this, prMensaje);}
    
    @FXML private void btnRegistrarPago_Click(ActionEvent event)
    {
        try
        {
            List<clsCobroOS> coss = new ArrayList<>();
            Pendientes.stream().filter((cp) -> (cp.isSeleccionado())).forEach((cp) -> {
                coss.add(cp.getCobro());
            });
            
            clsColegio.Instanciar().Saldar(coss, Date.from(this.dpFechaPago
                    .getValue().atStartOfDay().atZone(ZoneId.systemDefault())
                    .toInstant()));
            
            this.MostrarColegio();
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                    "Se registró el pago del colegio");
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
}
