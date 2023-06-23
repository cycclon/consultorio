/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jfxconsultorio.Profesionales;

import com.PRS.Consultorio.Profesionales.clsProfesional;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import com.PRS.Framework.FormulariosFX.iRegistryListener;
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
 * @author ARSpidalieri
 */
public class ctrlProfesionales implements Initializable, iMensajeable, 
        iRegistryListener, iReceptorError {

    @FXML private Label lblMensaje;
    
    @FXML private VBox vbProfesionales;
    
    @FXML private CheckBox chkEliminados;
    
    @FXML private VBox vbNuevo;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        try
        {           
            
            FXMLLoader xL = new FXMLLoader(getClass().getResource(
                    "/jfxconsultorio/Profesionales/fxRegistrarProfesional.fxml"));
                        
            this.vbNuevo.getChildren().add(xL.load());
            
            ctrlRegistrarProfesional xRP = xL.<ctrlRegistrarProfesional>getController();
            xRP.addRegistryListerner(this);
            
            this.MostrarProfesionales();
            
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
    @Override
    public String getNombre() {return "Profesionales";}
    
    public void MostrarProfesionales() throws Exception
    {
        
        List<clsProfesional> xPs = clsProfesional.Listar(chkEliminados.isSelected());
        
        this.vbProfesionales.getChildren().clear();

        for(clsProfesional xP : xPs)
        {
            FXMLLoader xL = new FXMLLoader(getClass().getResource(
                    "/jfxconsultorio/Profesionales/fxProfesional.fxml"));

            this.vbProfesionales.getChildren().add(xL.load());
            ctrlProfesional xCP = xL.<ctrlProfesional>getController();
            xCP.addListenerError(this);
            xCP.setProfesional(xP);
            xCP.addRegistryListerner(this); 

        }

        if(xPs.isEmpty()){
            this.vbProfesionales.getChildren().clear();
            this.vbProfesionales.getChildren().add(
                    new Label("No hay profesionales registrados"));

        }
    }

    @Override
    public Label getCanvas() {return lblMensaje;}

    @Override
    public void RegistryUpdated(String prMensaje) {
        try
        {
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, prMensaje);
            this.MostrarProfesionales();}
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}        
    }

    @Override
    public short getIDListerner() {return 1;}
    
    @FXML
    private void chkEliminadas_CheckChanged(ActionEvent event)
    {
        try{this.MostrarProfesionales();}
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
        
    }

    @Override
    public void ErrorOcurred(String prMensaje) {
        clsGestorMensajes.Instanciar().MostrarError(this, prMensaje);
    }
    
}
