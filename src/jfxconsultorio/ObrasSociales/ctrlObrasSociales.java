/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jfxconsultorio.ObrasSociales;

import com.PRS.Consultorio.ObrasSociales.clsOS;
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
import javafx.geometry.Rectangle2D;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlObrasSociales implements Initializable, iMensajeable, 
        iRegistryListener, iReceptorError {
    
          
    @FXML private Label lblMensaje;
    
    @FXML CheckBox chkEliminadas;    
        
    @FXML private VBox vbOSs;
    
    @FXML private ScrollPane sp;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO        
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        sp.setPrefHeight(bounds.getHeight() - 150);
        this.MostrarOSs();        
    }    
    
    @Override
    public String getNombre() {return "Obras Sociales";}

    @Override
    public Label getCanvas() {return this.lblMensaje;}  
    
      
    private void MostrarOSs()
    {
        try
        {   
            List<clsOS> xOSs;
            if(!chkEliminadas.isSelected())
            {xOSs = clsOS.Listar(true);}
            else{xOSs = clsOS.Listar();}
            this.vbOSs.getChildren().clear();
            for (clsOS xOS : xOSs) {                
                FXMLLoader xL = new FXMLLoader(getClass()
                        .getResource("/jfxconsultorio/ObrasSociales/fxCtrlObraSocial.fxml"));
                this.vbOSs.getChildren().add(xL.load());
                ctrlObraSocial xCOS = xL.<ctrlObraSocial>getController();
                xCOS.addListenerError(this);
                xCOS.setOS(xOS);
                xCOS.addRegistryListerner(this);
            }
            
            if(xOSs.isEmpty()){
                this.vbOSs.getChildren().clear();
                this.vbOSs.getChildren().add(
                        new Label("No hay obras sociales registradas"));
            
            }
            
            FXMLLoader xL = new FXMLLoader(getClass().getResource("/jfxconsultorio/ObrasSociales/fxCtrlAddObraSocial.fxml"));

            this.vbOSs.getChildren().add(0, xL.load());
            ctrlAddObraSocial xAOS = xL.<ctrlAddObraSocial>getController();
            xAOS.addListenerError(this);
            xAOS.addListenerUpdate(this);
        }
        catch(Exception ex)
        {
            clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());
        }
        
    }
    
    @Override
    public void RegistryUpdated(String prMensaje) {
        clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, prMensaje);
        this.MostrarOSs();
    }

    @Override
    public short getIDListerner() {
        return 1;
    }
    
    @FXML
    private void chkEliminadas_CheckChanged(ActionEvent event)
    {
        this.MostrarOSs();
    }

    @Override
    public void ErrorOcurred(String prMensaje) {
        clsGestorMensajes.Instanciar().MostrarError(this, prMensaje);
    }
}
