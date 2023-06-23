/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.CentrosCosto;

import com.PRS.Consultorio.CentrosCosto.clsCentroCosto;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import com.PRS.Framework.FormulariosFX.iRegistryListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
public class ctrlCentroCosto implements Initializable {

    @FXML Label lblNombre;
    @FXML Label lblCodigo;
    
    @FXML ImageView Icono;
    @FXML ImageView Boton;
    
    @FXML Tooltip ttBoton;
    
    @FXML Button btnAccion;
    
   private clsCentroCosto xCC;
   
   private List<iReceptorError> ListenersError;
    
    public void setCentroCosto(clsCentroCosto prCC){
        try
        {
            this.xCC = prCC;
            this.MostrarCC();
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
        
    }
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
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
    
    private void MostrarCC() throws Exception
    {
        this.lblCodigo.setText("("+this.xCC.getCodigo()+")");
        this.lblNombre.setText(this.xCC.getNombre());
        this.ttBoton.setText(xCC.getAyudaBoton());
        
        String img = "/jfxconsultorio/Imagenes/Iconos/"
            + this.xCC.getIconoBoton();
        Image image2 = new Image(img);
        this.Boton.setImage(image2);
        if(this.xCC.getActivo()){btnAccion.setText("Eliminar");}
        else{this.btnAccion.setText("Restaurar");}
    }
    
    @FXML
    private void btnAccion_Click(ActionEvent event)
    {
        try
        {
            JOptionPane confirm = new JOptionPane(xCC.getPregunta(), JOptionPane.QUESTION_MESSAGE, 
            JOptionPane.YES_NO_OPTION);
            JDialog dialog = confirm.createDialog(xCC.getTituloConfirmacion());
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            if((int)confirm.getValue() == JOptionPane.YES_OPTION) {
                dialog.dispose();
                this.xCC.CambiarEstado();
                this.NotificarCambio(this.xCC.getConfirmacion());
            }  
            
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
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
    
}
