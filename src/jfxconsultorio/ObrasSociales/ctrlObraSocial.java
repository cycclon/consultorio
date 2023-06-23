/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jfxconsultorio.ObrasSociales;

import com.PRS.Consultorio.ObrasSociales.clsOS;
import com.PRS.Framework.Archivos.clsGestorArchivos;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import com.PRS.Framework.FormulariosFX.iRegistryListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import jfxconsultorio.clsConsultorio;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlObraSocial implements Initializable {
    
// <editor-fold>
   
    private List<iRegistryListener> RegistryListeners;
    
    public void addRegistryListerner(iRegistryListener prListener)
    {
        if(!this.checkUpdateListener(prListener))
        {this.RegistryListeners.add(prListener);}
    }
    
    private boolean checkUpdateListener(iRegistryListener prListener)
    {
        boolean xF = false;
        for (iRegistryListener RegistryListener : this.RegistryListeners) {
            if (RegistryListener.getIDListerner() == prListener.getIDListerner()) {
                xF = true; break;
            }
        }
        
        return xF;
    }
    
    private void NotificarCambio(String prMensaje)
    {
        for (iRegistryListener RegistryListener : this.RegistryListeners) {
            RegistryListener.RegistryUpdated(prMensaje);
        }
    }
// </editor-fold>

    @FXML private Label lblOS;
    @FXML private Label lblCodigo;
    
    @FXML private AnchorPane ap;

    @FXML private Button btnCambiarImagen;
    @FXML private Button btnModificar;
    
    @FXML ImageView imgLogo;
    @FXML private ImageView ivIcono;
    
    @FXML Tooltip tlModificar;
    
    private clsOS OS;
    private List<iReceptorError> ListenersError;
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    public clsOS getOS(){return this.OS;}
    public void setOS(clsOS prOS){this.OS = prOS;this.MostrarOS();}
    
    private void RaiseError(String error)
    {
        ListenersError.stream().forEach((xRE) -> {
            xRE.ErrorOcurred(error);
        });
    }
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.RegistryListeners = new ArrayList<>();
        this.ListenersError = new ArrayList<>();
    }    
    
    private void MostrarOS()
    {
        this.lblOS.setText(this.OS.getNombre());
        this.lblCodigo.setText("(" + this.OS.getCodigo() + ")");
        try
        {
             /*String fullPath = "file:\\\\" + clsConsultorio.Instanciar()
                    .getServerNameOrAddress() + "\\SomaShared\\Logos\\"
                + this.OS.getImagen().trim();
           
            Image image = new Image(fullPath);
            this.imgLogo.setImage(image);
            */
            
            String img = "/jfxconsultorio/Imagenes/Iconos/"
                + this.OS.getIcono().trim();
            
            Image imIcono = new Image(img);
            this.ivIcono.setImage(imIcono);
                
        }
        catch(Exception ex){RaiseError(ex.getMessage());}
        
        
        if(!this.OS.getActiva()){
            this.lblOS.getStyleClass().add("OSEliminada");
            this.lblCodigo.getStyleClass().add("OSEliminada");
            this.btnModificar.setText("Restaurar");            
            this.tlModificar.setText("Presione este botón para restaurar esta obra social");
            try
            {
               
                String img = "/jfxconsultorio/Imagenes/Iconos/cancel.png";
                this.imgLogo.setImage(new Image(img));
            }
        catch(Exception ex){RaiseError(ex.getMessage());}
        }
        else{
            this.btnModificar.setText("Eliminar");
            this.tlModificar.setText("Presione este botón para eliminar esta obra social");
        }
    }
    
    public void Mouse_Over(Event event)
    {
        this.ap.getStyleClass().clear();
        this.ap.getStyleClass().add("apActiva");
        this.ap.getStyleClass().add("MouseOver");
        this.btnCambiarImagen.setVisible(false);
    }
    
    public void Mouse_Out(Event event)
    {
        this.ap.getStyleClass().clear();
        this.ap.getStyleClass().add("apActiva");
        this.ap.getStyleClass().add("MouseOut");
        if(!this.OS.getActiva()){}
        this.btnCambiarImagen.setVisible(false);
    }
    
    @FXML
    private void btnCambiarImagen_Click(ActionEvent event)
    {
        try{
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar logo para " + this.OS.getNombre());
            Stage stage = new Stage();
            File file = fileChooser.showOpenDialog(stage);
            if(file != null)
            {
                String fileName = clsGestorArchivos.Instanciar().
                        RandomName(file.getName());
                String fullPath = "\\\\" + clsConsultorio.Instanciar()
                    .getServerNameOrAddress() + "\\SomaShared\\Logos\\" + fileName;
                File newFile = new File(fullPath);
                clsGestorArchivos.Instanciar().copyFile(file, 
                        newFile);

                this.OS.RegistrarNuevaImagen(fileName);
                
                this.MostrarOS();
                
                this.NotificarCambio("Se actualizó el logo para " + this.OS.getNombre());
            }
        }
        catch(Exception ex){RaiseError(ex.getMessage());}
    }
    
    
    @FXML
    private void btnEliminar_Click(ActionEvent event) throws Exception
    {
        if(this.OS.getActiva())
        {
            JOptionPane confirm = new JOptionPane("¿Está seguro que desea eliminar esta obra social?", JOptionPane.QUESTION_MESSAGE, 
            JOptionPane.YES_NO_OPTION);
            JDialog dialog = confirm.createDialog("Eliminar obra social");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            if((int)confirm.getValue() == JOptionPane.YES_OPTION){
                dialog.dispose();
                this.OS.Eliminar();
                this.NotificarCambio("Se eliminó la obra social: " + this.OS.getNombre());
            }
        }
        else
        {
            JOptionPane confirm = new JOptionPane("¿Está seguro que desea restaurar esta obra social?", JOptionPane.QUESTION_MESSAGE, 
            JOptionPane.YES_NO_OPTION);
            JDialog dialog = confirm.createDialog("Restaurar obra social");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            if((int)confirm.getValue() == JOptionPane.YES_OPTION){
                dialog.dispose();
                this.OS.Restaurar();
                this.NotificarCambio("Se restauró la obra social: " + this.OS.getNombre());
            }
        }
    }
    
   
}