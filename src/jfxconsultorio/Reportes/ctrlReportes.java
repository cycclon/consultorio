/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Reportes;

import com.PRS.Consultorio.Reportes.clsGestorReportes;
import com.PRS.Consultorio.Reportes.clsReporte;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * FXML Controller class
 *
 * @author cycclon
 */
public class ctrlReportes implements Initializable, iMensajeable, iReceptorError {

    @FXML private VBox vbDineroProfesional;    
    @FXML private Label lblMensaje;
    @FXML private TabPane tp;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try
        {
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            tp.setPrefHeight(bounds.getHeight() - 150);
            tp.setPrefWidth(bounds.getWidth() -50);
            
            tp.getTabs().clear();
            for(clsReporte r : clsGestorReportes.Instanciar().getReportes())
            {
                Tab t = new Tab(r.getTitulo());
                tp.getTabs().add(t);
                VBox vbReporte = new VBox();
                
                FXMLLoader xL = new FXMLLoader(getClass().getResource(r.getFXML()));
                vbReporte.getChildren().clear();
                vbReporte.getChildren().add(xL.load());
                
                t.setContent(vbReporte);
            }            
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }    

    @Override
    public Label getCanvas() {return this.lblMensaje;}

    @Override
    public String getNombre() {return "Reportes";}

    @Override
    public void ErrorOcurred(String prMensaje) 
    {clsGestorMensajes.Instanciar().MostrarError(this, prMensaje);}
    
}
