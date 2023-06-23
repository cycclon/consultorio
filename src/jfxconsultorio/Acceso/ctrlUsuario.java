/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Acceso;

import com.PRS.Consultorio.Usuarios.clsGestorUsuariosConsultorio;
import com.PRS.Consultorio.Usuarios.iUsuarioConsultorio;
import com.PRS.Framework.Acceso.clsGestorAcceso;
import com.PRS.Framework.Acceso.clsPermiso;
import com.PRS.Framework.Acceso.iAccesor;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.clsGestorValidacion;
import com.PRS.Framework.FormulariosFX.enTipoTextField;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author ARSPIDALIERI
 */
public class ctrlUsuario implements Initializable, iMensajeable {
    
    

    @FXML private PasswordField txtPassActual;
    @FXML private PasswordField txtNuevaPass;
    @FXML private PasswordField txtDuplicado;
    
    @FXML private Label lblMensaje;
    @FXML private Label lblNombre;
    @FXML private Label lblUsername;
    @FXML private Label lblTipoUsuario;
    
    @FXML private ListView lstPermisos;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try
        {
            iUsuarioConsultorio xU = clsGestorUsuariosConsultorio.Instanciar()
                .ObtenerUsuarioConsultorio();
            this.lblNombre.setText(xU.getNombreCompletoUC());
            this.lblUsername.setText(clsGestorAcceso.Instanciar().getSesion()
                    .pdUsuario().pdUsername());
            this.lblTipoUsuario.setText(xU.getTipoUsuario().toString());
            
            
            lstPermisos.getItems().clear();
            iAccesor xA = clsGestorUsuariosConsultorio.Instanciar().ObtenerAccesor();
            
            xA.pdPermisos().stream().forEach((xP) -> {
                lstPermisos.getItems().add(xP.pdNombre());
            });
            
            if(xA.pdPermisos().isEmpty())
            {
                this.lstPermisos.getItems().add("Este usuario no tiene permisos asignados.");
            }
            
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}        
    }    
    
    @Override
    public String getNombre() {return "Opciones de usuario";}

    @Override
    public Label getCanvas() {return this.lblMensaje;}
    
    
    @FXML
    private void btnActualizarContraseña_Click(ActionEvent event)
    {
        try
        {
            clsGestorAcceso xGA = clsGestorAcceso.Instanciar();
            clsGestorValidacion.ValidarTextField(txtPassActual, "Contraseña actual", 
                    true, enTipoTextField.Texto);
            clsGestorValidacion.ValidarTextField(txtNuevaPass, "Nueva contraseña",
                    true, enTipoTextField.Texto);
            if(xGA.VerificarContraseña(this.txtPassActual.getText()))
            {
                if(this.txtDuplicado.getText().compareTo(this.txtNuevaPass.getText()) == 0)
                {
                    

                    JOptionPane confirm = new JOptionPane("¿Está seguro que desea cambiar su contraseña?", JOptionPane.QUESTION_MESSAGE, 
                    JOptionPane.YES_NO_OPTION);
                    JDialog dialog = confirm.createDialog("Cambiar contraseña");
                    dialog.setAlwaysOnTop(true);
                    dialog.setVisible(true);
                    
                    if((int)confirm.getValue() == JOptionPane.YES_OPTION) {
                        dialog.dispose();
                        xGA.DefinirContrasena(this.txtNuevaPass.getText());
                        clsGestorMensajes.Instanciar()
                                .MostrarMensaje(lblMensaje, "Se modificó la contraseña");
                        this.txtDuplicado.setText("");
                        this.txtNuevaPass.setText("");
                        this.txtPassActual.setText("");
                    }
                }
                else
                {clsGestorMensajes.Instanciar().MostrarError(this, 
                        "La nueva contraseña no coincide con su duplicado");
                this.txtDuplicado.setText("");this.txtNuevaPass.setText("");}               
                
            }
            else
            {clsGestorMensajes.Instanciar().MostrarError(this, 
                    "La contraseña actual es incorrecta");this.txtPassActual.setText("");}
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, ex.getMessage());}
    }
}
