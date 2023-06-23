/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Practicas.clsCampoTrabajo;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author ARSPIDALIERI
 */
public class ctrlCampoTrabajo implements Initializable {

    @FXML private Label lblNombre;
    @FXML private Label lblObligatorio;
    @FXML private ImageView ivBoton;
    
    private List<iReceptorError> ListenersError;
    private List<ctrlAddCampoTrabajo.iFormularioCampoTrabajo> ListenersCampo;
    
    private clsCampoTrabajo Campo;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
         this.ListenersError = new ArrayList<>();
        this.ListenersCampo = new ArrayList<>();
    }    
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    private void RaiseError(String error)
    {
        ListenersError.stream().forEach((xRE) -> {
            xRE.ErrorOcurred(error);
        });
    }
    
    public void addListenerCampo(ctrlAddCampoTrabajo.iFormularioCampoTrabajo listener)
    {this.ListenersCampo.add(listener);}
    
    private void NotificarCampoEliminado(clsCampoTrabajo campo)
    {
        ListenersCampo.stream().forEach((xAC) -> {
            xAC.CampoEliminado(campo);
        });
    }
    
     private void NotificarCampoRestaurado(clsCampoTrabajo campo)
    {
        ListenersCampo.stream().forEach((xAC) -> {
            xAC.CampoRestaurado(campo);
        });
    }
    
    public void setCampo(clsCampoTrabajo campo)
    {this.Campo = campo; this.MostrarCampo();}
    
    private void MostrarCampo()
    {
        try
        {
            this.lblNombre.setText(Campo.getNombreyCodigo());
        
            String obl = "Si";
            if(!Campo.isObligatorio()){obl = "No";}

            this.lblObligatorio.setText("Obligatorio: " + obl);
            
            String img = "/jfxconsultorio/Imagenes/Iconos/";
            if(Campo.isActivo())
            {this.ivBoton.setImage(new Image(img + "/remove.png"));}
            else
            {this.ivBoton.setImage(new Image(img + "/add.png"));}
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
        
    }
    
    @FXML
    private void btnEliminar_Click(ActionEvent event)
    {
        try
        {
            if(this.Campo.isActivo())
        {
            JOptionPane confirm = new JOptionPane("¿Está seguro que desea eliminar el campo" + this.Campo.getNombre() + "?", JOptionPane.QUESTION_MESSAGE, 
            JOptionPane.YES_NO_OPTION);
            JDialog dialog = confirm.createDialog("Eliminar campo");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            if((int)confirm.getValue() == JOptionPane.YES_OPTION){
                dialog.dispose();
                this.Campo.Eliminar();
                this.NotificarCampoEliminado(Campo);
            }
        }
        else
        {
            JOptionPane confirm = new JOptionPane("¿Está seguro que desea restaurar el campo" + this.Campo.getNombre() + "?" + this.Campo.getNombre() + "?", JOptionPane.QUESTION_MESSAGE, 
            JOptionPane.YES_NO_OPTION);
            JDialog dialog = confirm.createDialog("Restaurar campo");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            if((int)confirm.getValue() == JOptionPane.YES_OPTION){
                dialog.dispose();
                this.Campo.Restaurar();
                this.NotificarCampoRestaurado(Campo);
            }
        }
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }
    
}
