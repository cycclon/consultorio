/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Gastos;

import com.PRS.Consultorio.Alertas.clsGestorAlertas;
import com.PRS.Consultorio.Gastos.clsGasto;
import com.PRS.Consultorio.Usuarios.clsGestorUsuariosConsultorio;
import com.PRS.Framework.Acceso.clsGestorAcceso;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import jfxconsultorio.Alertas.ctrlAlertas;

/**
 * FXML Controller class
 *
 * @author ARSPIDALIERI
 */
public class ctrlGastos implements Initializable, iMensajeable, iGastos, 
        iReceptorError {

    @FXML private VBox vbGastos;
    @FXML private ScrollPane spGastos;
    @FXML private Label lblMensaje;
    @FXML private DatePicker dpMes;
    @FXML private VBox vbAccion;
    @FXML private VBox vbAlertas;
    @FXML private Button btnRegistrar;
    
    private ctrlAlertas Alertas;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.dpMes.setValue(LocalDate.now());
        this.MostrarGastos();
        
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        
        spGastos.setPrefHeight(bounds.getHeight() - 150);
        
        try
        {
            this.btnRegistrar.setVisible(clsGestorAcceso.Instanciar().ComprobarPermisos
                (clsGestorUsuariosConsultorio.Instanciar().ObtenerAccesor(), (short)25));
            this.ActualizarAlertas();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}       
    }   
    
    @Override
    public String getNombre() {return "Gastos";}
    
    private void ActualizarAlertas() throws Exception
    {
        if(Alertas == null)
        {
        FXMLLoader xL = new FXMLLoader(getClass()
                .getResource("/jfxconsultorio/Alertas/fxAlertas.fxml"));
        this.vbAlertas.getChildren().clear();
        this.vbAlertas.getChildren().add(xL.load());
        Alertas = xL.<ctrlAlertas>getController();
        
        }
        Alertas.setAlertas(clsGestorAlertas.Instanciar()
                    .ObtenerAlertasGastos());
    }
    
    public void btnRegistrarGasto_Click(ActionEvent event)
    {
        try
        {
            this.vbAccion.getChildren().clear();
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Gastos/fxAddGasto.fxml"));
            this.vbAccion.getChildren().add(xL.load());
            ctrlAddGasto xAG = xL.<ctrlAddGasto>getController();
            xAG.addListenerError(this);
            xAG.addListenerGasto(this);
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public Label getCanvas() {return this.lblMensaje;}

    @Override
    public void GastoRegistrado(clsGasto gasto) {
        this.vbAccion.getChildren().clear();
        this.MostrarGastos();
        clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, "Se registró un nuevo gasto");
    }

    private void MostrarGastos()
    {
        try
        {
            this.vbGastos.getChildren().clear();
            Calendar cal = Calendar.getInstance();
            cal.setTime(Date.from(this.dpMes.getValue().atStartOfDay()
                    .atZone(ZoneId.systemDefault()).toInstant()));
            List<clsGasto> xGs = clsGasto.ListarEfectivos(
                    (byte)(cal.get(Calendar.MONTH)+1), cal.get(Calendar.YEAR));
            for(clsGasto xG : xGs)
            {
                FXMLLoader xL = new FXMLLoader(getClass().getResource("/jfxconsultorio/Gastos/fxGasto.fxml"));
                this.vbGastos.getChildren().add(xL.load());
                
                ctrlGasto xCG = xL.<ctrlGasto>getController();
                xCG.addListenerError(this);
                xCG.setGasto(xG);
                xCG.addListenerGastos(this);
            }
            
            if(xGs.isEmpty())
            {this.vbGastos.getChildren().add(new Label("No hay gastos registrados para este mes"));}
            
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void ErrorOcurred(String prMensaje) {
        clsGestorMensajes.Instanciar().MostrarError(this, prMensaje);
    }
    
    public void dpDia_ValueChanged(ActionEvent event)
    {
        try
        {
            this.MostrarGastos();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void GastoProgramadoEliminad(clsGasto gasto) {
        this.MostrarGastos();
        clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, "Se eliminó un gasto y sus demás ocurrencias programadas.");
    }

    @Override
    public void GastoEliminado(clsGasto gasto) {
        this.MostrarGastos();
        clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, "Se eliminó un gasto");
    }
}
