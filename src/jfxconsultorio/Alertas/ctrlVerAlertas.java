/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Alertas;

import com.PRS.Consultorio.Alertas.clsAlerta;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author ARSPIDALIERI
 */
public class ctrlVerAlertas implements Initializable, iMensajeable {

    @FXML private VBox vbAlertas;
    @FXML private Label lblMensaje;
    @FXML private ScrollPane sp;
    
    private List<clsAlerta> Alertas;
    private ctrlAlertas Notificador;
    private Stage stage;
    
    @Override
    public String getNombre() {return "Ver Alertas";}
    
    public void setNotificador(ctrlAlertas notificador)
    {this.Notificador = notificador;}
    
    public void setStage(Stage st){
        stage = st;
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          @Override
          public void handle(WindowEvent we) {
              Notificador.resetVerAlertas();
          }
      });   
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        
        sp.setPrefHeight(bounds.getHeight() - 150);        
    }    
    
    public void setAlertas(List<clsAlerta> alertas)
    {Alertas = alertas; MostrarAlertas();}
    
    private void MostrarAlertas() 
    {
        try
        {
            this.vbAlertas.getChildren().clear();
            for(clsAlerta xA : Alertas)
            {
                FXMLLoader xL = new FXMLLoader(getClass()
                        .getResource("/jfxconsultorio/Alertas/fxAlerta.fxml"));
                this.vbAlertas.getChildren().add(xL.load());
                ctrlAlerta xCA = xL.<ctrlAlerta>getController();
                xCA.setAlerta(xA);
            }
            
            if(this.Alertas.isEmpty())
            {
                Label lblEmpty = new Label("No hay alertas");
                this.vbAlertas.getChildren().add(lblEmpty);
            }
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}       
    }

    @Override
    public Label getCanvas() {return this.lblMensaje;}
    
}
