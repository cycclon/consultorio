/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jfxconsultorio;

import com.PRS.Consultorio.Usuarios.clsGestorUsuariosConsultorio;
import com.PRS.Consultorio.Usuarios.iUsuarioConsultorio;
import com.PRS.Framework.Acceso.clsGestorAcceso;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.clsGestorNavegacion;
import com.PRS.Framework.FormulariosFX.iPrincipal;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javax.swing.JDialog;
import jfxconsultorio.Acceso.ctrlLogin;
import javax.swing.JOptionPane;

public class ctrlPrincipal implements Initializable, iPrincipal {

    @FXML private Label lblUsuario;
    @FXML private Label lblTitulo;
        
    @FXML private Tooltip ttInicioSesion;
    
    @FXML private Button btnSalir;
    
    @FXML private HBox apPrincipal;
    
    @FXML private ProgressIndicator pi;
    

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try
        {
            
            clsGestorAcceso xGA = clsGestorAcceso.Instanciar();
            iUsuarioConsultorio xUC = clsGestorUsuariosConsultorio.Instanciar()
                    .ObtenerUsuarioConsultorio();
            this.lblUsuario.setText(xUC.getNombreCompletoUC() + 
                    " (" + xGA.getSesion().pdUsuario().pdUsername() + ")");
            this.ttInicioSesion.setText("Sesión iniciada: " + 
                    new SimpleDateFormat("dd/MM/yyyy hh:mm").
                            format(xGA.getSesion().pdInicio()));
            
        }
        catch(Exception ex)
        {System.out.println(ex.getMessage());}
        
    }
    
    @FXML
    private void btnSalir_Click(ActionEvent event) {
        try
        {
            JOptionPane confirm = new JOptionPane("¿Está seguro que desea cerrar su sesión?", JOptionPane.QUESTION_MESSAGE, 
            JOptionPane.YES_NO_OPTION);
            JDialog dialog = confirm.createDialog("Cerrar sesión");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            if((int)confirm.getValue() == JOptionPane.YES_OPTION){
                dialog.dispose();
                clsGestorAcceso.Instanciar().CerrarSesion();
                FXMLLoader xL = new FXMLLoader(getClass().getResource("/jfxconsultorio/Acceso/fxLogin.fxml"));
                Parent root = (Parent)xL.load();
                ctrlLogin xC = xL.<ctrlLogin>getController();

                Scene scene = new Scene(root);

                JfxConsultorio.stage.setScene(scene);
                clsGestorMensajes.Instanciar().MostrarMensaje(xC.getCanvas(), "Sesión cerrada");
            }

        }
        catch(IOException ex){}
    }
        
    @FXML
    private void btnHome_Click(ActionEvent event)
    {
        try
        {
            
            clsGestorNavegacion.Instanciar().Abrir("/jfxconsultorio/fxInicio.fxml", "Menú Principal", "home.png");
            
        }
        catch(Exception ex)
        {}
        
    }
    
    @FXML
    private void btnUsuario_Click(ActionEvent event)
    {
        try
        {clsGestorNavegacion.Instanciar().Abrir("/jfxconsultorio/Acceso/fxUsuario.fxml", 
                "Menú de usuario", "acceso.png");}
        catch(Exception ex)
        {}
    }

    @Override
    public Pane getPane() {return this.apPrincipal;}
    
    @Override
    public void setTitulo(String titulo, String icono) throws IOException
    {
        this.lblTitulo.setText(titulo);
                  
    }

    
    
   
}
