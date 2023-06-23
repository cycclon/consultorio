/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Alertas;

import com.PRS.Consultorio.Alertas.clsAlerta;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ARSPIDALIERI
 */
public class ctrlAlertas implements Initializable {

    @FXML private Label lblAlertas;
    @FXML private Button btnVerAlertas;
    @FXML private ImageView ivIcono;
    
    private List<clsAlerta> Alertas;
    private ctrlVerAlertas VerAlertas;
    
    public void setAlertas(List<clsAlerta> alertas)
    {Alertas = alertas; MostrarAlertas();}
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    private void MostrarAlertas()
    {
        this.btnVerAlertas.setVisible(Alertas.size() > 0);
        this.lblAlertas.setVisible(Alertas.size() > 0);
        this.ivIcono.setVisible(Alertas.size() > 0);
        this.lblAlertas.setText("Hay " + Alertas.size() + " alertas");
        
    }
    
    @FXML
    private void btnVerAlertas_Click(ActionEvent event)
    {
        this.VerAlertas();
    }
    
    private void VerAlertas()
    {
        try
        {
            if(VerAlertas == null && this.Alertas.size() > 0)
            {
                FXMLLoader xL = new FXMLLoader(getClass()
                        .getResource("/jfxconsultorio/Alertas/fxVerAlertas.fxml"));
                Parent root = xL.load();
                VerAlertas = xL.<ctrlVerAlertas>getController();
                 Scene scene = new Scene(root);
            
            Stage st = new Stage();
            
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            
            st.setScene(scene);
            st.setWidth(1000);
            st.setHeight(bounds.getHeight() - 150);
            st.setTitle("Alertas de turnos");
            st.show();
            VerAlertas.setStage(st);
            VerAlertas.setAlertas(Alertas);
            
            VerAlertas.setNotificador(this);
            }        
           
            
           
        }
        catch(Exception ex)
        {}
    }
    
    public void resetVerAlertas()
    {this.VerAlertas = null;}
}
