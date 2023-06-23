/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Practicas.clsCampoTrabajo;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author ARSPIDALIERI
 */
public class ctrlCamposTrabajo implements Initializable, iMensajeable, 
        iReceptorError, ctrlAddCampoTrabajo.iFormularioCampoTrabajo {

    @FXML private Label lblMensaje;
    @FXML private VBox vbCampos;
    @FXML private CheckBox chInactivos;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.MostrarCampos();
    }
    
    @Override
    public String getNombre() {return "Campos trabajo";}

    @Override
    public Label getCanvas() {return this.lblMensaje;}
    
    private void MostrarCampos()
    {
        try
        {
            List<clsCampoTrabajo> xCTs = clsCampoTrabajo.Listar(chInactivos.isSelected());
            vbCampos.getChildren().clear();
            
            FXMLLoader xL2 = new FXMLLoader(getClass().getResource("/jfxconsultorio/Practicas/fxAddCampoTrabajo.fxml"));
            vbCampos.getChildren().add(xL2.load());
            
            ctrlAddCampoTrabajo xACT = xL2.<ctrlAddCampoTrabajo>getController();
            xACT.addListenerCampo(this);
            xACT.addListenerError(this);
            
            for(clsCampoTrabajo xCT : xCTs)
            {
                FXMLLoader xL = new FXMLLoader(getClass()
                        .getResource("/jfxconsultorio/Practicas/fxCampoTrabajo.fxml"));
                this.vbCampos.getChildren().add(xL.load());
                
                ctrlCampoTrabajo xCCT = xL.<ctrlCampoTrabajo>getController();
                xCCT.addListenerCampo(this);
                xCCT.addListenerError(this);
                xCCT.setCampo(xCT);
            }
            
            if(xCTs.isEmpty()){this.vbCampos.getChildren()
                    .add(new Label("No hay campos registrados"));}
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void ErrorOcurred(String prMensaje) {
        clsGestorMensajes.Instanciar().MostrarError(this, prMensaje);
    }

    @Override
    public void CampoAgregado(clsCampoTrabajo campo) {
        this.MostrarCampos();
        clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                "Se registró un nuevo campo: " + campo.getNombre());
    }

    @Override
    public void CampoEliminado(clsCampoTrabajo campo) {
        this.MostrarCampos();
        clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                "Se eliminó el campo: " + campo.getNombre());
    }
    
    @FXML
    private void chInactivos_CheckedChanged(ActionEvent event)
    {this.MostrarCampos();}

    @Override
    public void CampoRestaurado(clsCampoTrabajo campo) {
        this.MostrarCampos();
        clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                "Se restauró el campo: " + campo.getNombre());
    }
}
