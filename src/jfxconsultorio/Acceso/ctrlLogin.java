/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jfxconsultorio.Acceso;


import com.PRS.Framework.Acceso.*;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.clsGestorNavegacion;
import com.PRS.Framework.FormulariosFX.clsGestorValidacion;
import com.PRS.Framework.FormulariosFX.enTipoTextField;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import jfxconsultorio.clsConsultorio;


/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlLogin implements Initializable, clsGestorAcceso.iLoginListener, iMensajeable {


    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtPassword2;
    
    @FXML
    private Button btnAceptar;
    @FXML
    private Button btnSalir;
    
    @FXML
    private Label lblMensaje;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clsGestorAcceso xGA = clsGestorAcceso.Instanciar();
        xGA.addListenerContrasena(this);
        try {
            clsGestorMensajes.Instanciar().setLogPath("\\\\" + clsConsultorio
                    .Instanciar().getServerNameOrAddress() + "\\SomaShared\\Log\\");
        } catch (Exception ex) {
            
        }
    }    
    
    @Override
    public String getNombre() {return "Iniciar Sesión";}
    
    @FXML
    private void btnAceptar_Click(ActionEvent event) {
        try
        {           
            
            clsGestorAcceso xGA = clsGestorAcceso.Instanciar();
            clsGestorValidacion.ValidarTextField(txtUsername, "Nombre de usuario", true, enTipoTextField.Texto);
            clsGestorValidacion.ValidarTextField(txtPassword, "Contraseña", true, enTipoTextField.Texto);
            if(this.txtPassword2.isVisible())
            {
                if(this.txtPassword.getText().compareTo(this.txtPassword2.getText())==0)
                {
                    xGA.DefinirContrasena(this.txtUsername.getText(), this.txtPassword.getText());
                }
                else{clsGestorMensajes.Instanciar().MostrarError(this, 
                        "La contraseña no coincide con su duplicado");}
            }
            clsUsuario xU = new clsUsuario();
            xU.pdUsername(this.txtUsername.getText());
            xU.pdPassword(this.txtPassword.getText());
            
            
           if( xGA.IniciarSesion(xU))
           {
                FXMLLoader xL = new FXMLLoader(getClass().getResource("/jfxconsultorio/fxPrincipal.fxml"));
                
                Parent root = (Parent)xL.load();
                                              
                Scene scene = new Scene(root);  
                
                jfxconsultorio.JfxConsultorio.stage.setScene(scene);  
                
                clsGestorNavegacion.Instanciar().setLoader(xL);
                clsGestorNavegacion.Instanciar().Abrir("/jfxconsultorio/fxInicio.fxml", 
                        "Sesión iniciada", "Menú Principal", "home.png");                
           }
          
        }
        catch(Exception ex){ clsGestorMensajes.Instanciar().MostrarError(this, 
                        ex.getMessage());}
    }
    
    @FXML
    private void btnSalir_Click(ActionEvent event) {
        JOptionPane confirm = new JOptionPane("¿Está seguro que desea cerrar la aplicación?", JOptionPane.QUESTION_MESSAGE, 
                JOptionPane.YES_NO_OPTION);
        JDialog dialog = confirm.createDialog("Cerrar aplicación");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
        
        if((int)confirm.getValue() == JOptionPane.YES_OPTION)dialog.dispose();Platform.exit();
        
    }

    @Override
    public void ContrasenaEnBlanco() {
        clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                        "Esta es la primera vez que inicia sesión, "
                                + "por favor repita la contraseña.");
        this.txtPassword2.setVisible(true);
        this.txtUsername.setDisable(true);
        this.txtPassword2.requestFocus();
    }

    @Override
    public short getIDListerner() {return 1;}

    @Override
    public Label getCanvas() {
        return this.lblMensaje;
    }
    
    
    
}
