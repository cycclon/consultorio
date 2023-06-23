/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Configuracion;

import com.PRS.Framework.AccesoADatos.clsGestorBases;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlBase implements Initializable, iMensajeable {

    @FXML private TextField txtServidor;
    @FXML private Label lblMensaje;
    @FXML private Button btnProbar;
    @FXML private Button btnEstablecer;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.btnEstablecer.setDisable(true);
    }   
    
    @Override
    public String getNombre() {return "Configurar Base";}
    
    @FXML
    private void btnProbar_Click(ActionEvent event)
    {
        try
        {
            this.btnProbar.setDisable(true);
            this.txtServidor.setDisable(true);
            
            clsGestorBases xG = clsGestorBases.Instanciar();
            xG.AgregarBaseSQLServer((byte)1, this.txtServidor.getText() + "\\SQLEXPRESS", 
                         "consultorio", "doctor", "SosaMagnano");
            
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                    "Prueba de conexión satisfactoria. Presione el botón "
                            + "\"Establecer\" para comenzar a utilizar el sistema.");
            this.btnEstablecer.setDisable(false);
        }
        catch(Exception ex)
        {
            clsGestorMensajes.Instanciar().MostrarError(this, "*No es posible"
                + " conectarse con la base de datos: " + ex.getMessage());
            this.btnProbar.setDisable(false);
            this.txtServidor.setDisable(false);
            this.btnEstablecer.setDisable(true);
        }
    }
    
    @FXML
    private void btnEstablecar_Click(ActionEvent event)
    {
        try
        {
            String path = new File(".").getCanonicalPath();
            String fullPath = path.replace("\\", "/") + "/";
            File file = new File(fullPath + "base.txt");
            
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(this.txtServidor.getText());
            }            
                            
            Parent root = FXMLLoader.load(getClass()
                            .getResource("/jfxconsultorio/Acceso/fxLogin.fxml"));

            Scene scene = new Scene(root);  

            jfxconsultorio.JfxConsultorio.stage.setScene(scene);
            
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public Label getCanvas() {return this.lblMensaje;}
}
