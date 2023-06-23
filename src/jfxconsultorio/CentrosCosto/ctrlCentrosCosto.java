/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.CentrosCosto;

import com.PRS.Consultorio.CentrosCosto.clsCentroCosto;
import com.PRS.Framework.FormulariosFX.*;
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
public class ctrlCentrosCosto implements Initializable, iRegistryListener, iMensajeable, iReceptorError {

    @FXML Label lblMensaje;
    
    @FXML VBox vbCentrosCosto;
    
    @FXML CheckBox chkEliminados;
    
    @FXML ScrollPane sp;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try
        {
            this.MostrarCCs();
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            sp.setPrefHeight(bounds.getHeight() - 150);
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }   
    
    @Override
    public String getNombre() {return "Centros de costo";}
    
    private void MostrarCCs() throws Exception
    {
        List<clsCentroCosto> xCCs = clsCentroCosto.Listar(chkEliminados.isSelected());
        this.vbCentrosCosto.getChildren().clear();
        
        for (clsCentroCosto xCC1 : xCCs) {
            FXMLLoader xL = new FXMLLoader(getClass().getResource(
                    "/jfxconsultorio/CentrosCosto/fxCentroCosto.fxml"));
            this.vbCentrosCosto.getChildren().add(xL.load());
            ctrlCentroCosto xCC = xL.<ctrlCentroCosto>getController();
            xCC.addRegistryListerner(this);
            xCC.addListenerError(this);
            xCC.setCentroCosto(xCC1);
        }
        
        
        
        FXMLLoader xL = new FXMLLoader(getClass().getResource(
                    "/jfxconsultorio/CentrosCosto/fxAddCentroCosto.fxml"));
        this.vbCentrosCosto.getChildren().add(0, xL.load());
        ctrlAddCentroCosto xACC = xL.<ctrlAddCentroCosto>getController();
        xACC.addRegistryListerner(this);
        xACC.addErrorListener(this);
                
        if(xCCs.isEmpty())
        {
            Label lblEmpty = new Label("No hay centros de costos registrados");
            this.vbCentrosCosto.getChildren().add(lblEmpty);
        }
        
    }

    @Override
    public void RegistryUpdated(String prMensaje) {
        try
        {
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, prMensaje);
            this.MostrarCCs();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public short getIDListerner() {return 1;}

    @Override
    public Label getCanvas() {return this.lblMensaje;}
    
    @FXML
    private void chkEliminadas_CheckChanged(ActionEvent event)
    {
        try
        {this.MostrarCCs();}
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void ErrorOcurred(String prMensaje) {
        clsGestorMensajes.Instanciar().MostrarError(this, prMensaje);
    }
}
