/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jfxconsultorio.Usuarios;

import com.PRS.Framework.Acceso.clsUsuario;
import com.PRS.Framework.FormulariosFX.clsGestorValidacion;
import com.PRS.Framework.FormulariosFX.enTipoTextField;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlRegistrarUsuario implements Initializable {

    private clsUsuario Usuario;
    
    @FXML private TextField txtUsername;
    
    private List<iReceptorError> ListenersError;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.ListenersError = new ArrayList<>();
    }
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    private void RaiseError(String error)
    {
        for(iReceptorError xRE : ListenersError)
        {
            xRE.ErrorOcurred(error);
        }
    }
    
    
    public clsUsuario getUsuario()
    {
        try
        {
            clsGestorValidacion.ValidarTextField(txtUsername, "Nombre de usuario", true, enTipoTextField.Texto);
            this.Usuario = new clsUsuario();
            Usuario.pdUsername(this.txtUsername.getText());
            return Usuario;
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());return null;}        
    }
    
    public void LimpiarFormulario(){this.txtUsername.setText("");}
}
