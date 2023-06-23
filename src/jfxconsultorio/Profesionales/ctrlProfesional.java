/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jfxconsultorio.Profesionales;

import com.PRS.Consultorio.Profesionales.clsProfesional;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import com.PRS.Framework.FormulariosFX.iRegistryListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlProfesional implements Initializable {
    
    @FXML private ImageView pvIcono;
    @FXML private ImageView ivBoton;
    
    @FXML private Label lblNombre;
    
    @FXML private Tooltip ttBoton;
    
    private clsProfesional Profesional;
    private List<iReceptorError> ListenersError;
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    private void RaiseError(String error)
    {
        for(iReceptorError xRE : ListenersError)
        {xRE.ErrorOcurred(error);}
    }
    
    public clsProfesional getProfesional(){return this.Profesional;}
    public void setProfesional(clsProfesional newValue){
        try
        {
            this.Profesional = newValue;
            this.MostrarProfesional();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.RegistryListeners = new ArrayList<>();
        this.ListenersError = new ArrayList<>();
    }    
    
    private void MostrarProfesional() throws IOException
    {
        this.lblNombre.setText(this.Profesional.getNombreCompleto());
        
        String img = "/jfxconsultorio/Imagenes/Iconos/"
            + this.Profesional.getIcono();
        Image image = new Image(img);
        this.pvIcono.setImage(image);
        
        this.ttBoton.setText(Profesional.getAyudaBoton());
        img = "/jfxconsultorio/Imagenes/Iconos/"
            + this.Profesional.getIconoBoton();
        Image image2 = new Image(img);
        this.ivBoton.setImage(image2);
    }
    
    @FXML
    private void btnAccion_Click(ActionEvent event)
    {
        try
        {
            JOptionPane confirm = new JOptionPane(this.Profesional.getPregunta(), JOptionPane.QUESTION_MESSAGE, 
            JOptionPane.YES_NO_OPTION);
            JDialog dialog = confirm.createDialog(this.Profesional.getTituloConfirmacion());
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            if((int)confirm.getValue() == JOptionPane.YES_OPTION){
                dialog.dispose();
                this.Profesional.CambiarEstado();
                this.NotificarCambio(this.Profesional.getMensajeConfirmacion());
            }      
        }
        catch(Exception ex){RaiseError(ex.getMessage());}
    }
    
    private List<iRegistryListener> RegistryListeners;
    
    public void addRegistryListerner(iRegistryListener prListener)
    {
        if(!this.checkUpdateListener(prListener))
        {this.RegistryListeners.add(prListener);}
    }
    
    private boolean checkUpdateListener(iRegistryListener prListener)
    {
        boolean xF = false;
        for(int i= 0; i<this.RegistryListeners.size(); i++)
        {
            if(RegistryListeners.get(i).getIDListerner() == 
                    prListener.getIDListerner())
            {xF = true; break;}
        }
        
        return xF;
    }
    
    private void NotificarCambio(String prMensaje)
    {
        for(int i=0; i<this.RegistryListeners.size(); i++)
        {this.RegistryListeners.get(i).RegistryUpdated(prMensaje);}
    }
}
