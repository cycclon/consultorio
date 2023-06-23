/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Pacientes;

import com.PRS.Consultorio.Pacientes.clsPacienteLazy;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import com.PRS.Framework.FormulariosFX.iRegistryListener;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlPacientes implements Initializable, iReceptorError, 
        iRegistryListener, iMensajeable, ctrlBuscarPaciente.iBusquedaPaciente {

    @FXML private VBox vbAdd;
    @FXML private VBox vbPacientes;
    
    @FXML private HBox hbBusqueda;
    
    @FXML private Label lblMensaje;
    
    @FXML private ScrollPane sp;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try
        {
             Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        
        sp.setPrefHeight(bounds.getHeight() - 50);
            
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Pacientes/fxAddPaciente.fxml"));
            this.vbAdd.getChildren().add(xL.load());

            ctrlAddPaciente xAP = xL.<ctrlAddPaciente>getController();

            xAP.addListenerError(this);
            xAP.addListenerMensaje(this);
            
            FXMLLoader xL2 = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Pacientes/fxBuscarPaciente.fxml"));
            this.hbBusqueda.getChildren().add(xL2.load());
            ctrlBuscarPaciente xBP = xL2.<ctrlBuscarPaciente>getController();
            xBP.addListenerBusqueda(this);
            xBP.addListenerError(this);
        }
        catch(IOException ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
        
    }    
    
    @Override
    public String getNombre() {return "Pacientes";}

    @Override
    public void ErrorOcurred(String prMensaje) {
        clsGestorMensajes.Instanciar().MostrarError(this, prMensaje);
    }

    @Override
    public void RegistryUpdated(String prMensaje) {
        clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, prMensaje);
    }

    @Override
    public short getIDListerner() {return 1;}

    @Override
    public Label getCanvas() {return this.lblMensaje;}

    @Override
    public void VerResultados(List<clsPacienteLazy> resultados) {
        try
        {
            this.vbPacientes.getChildren().clear();

            for(clsPacienteLazy xP : resultados)
            {
                FXMLLoader xL = new FXMLLoader(getClass()
                        .getResource("/jfxconsultorio/Pacientes/fxPacienteLazy.fxml"));

                this.vbPacientes.getChildren().add(xL.load());
                ctrlPacienteLazy xPL = xL.<ctrlPacienteLazy>getController();

                xPL.setPacienteLazy(xP);
            }
            
            if(resultados.isEmpty()){this.vbPacientes.getChildren()
                    .add(new Label("No hay pacientes registrados"));}
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
        
    }
    
}
