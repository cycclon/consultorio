/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Practicas.clsPractica;
import com.PRS.Consultorio.Practicas.clsPracticaLazy;
import com.PRS.Framework.FormulariosFX.clsGestorNavegacion;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import com.PRS.Framework.FormulariosFX.iRegistryListener;
import java.io.File;
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
public class ctrlPractica implements Initializable {

    @FXML private Label lblNombre;
    
    @FXML private ImageView ivBoton;
    
    @FXML private Tooltip ttBoton;
    
    @FXML private Label lblCodigo;
    
    private clsPracticaLazy xP;
    
    private List<iRegistryListener> ListenersRegistro;
    
    private List<iReceptorError> ListenersError;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.ListenersRegistro = new ArrayList<>();
        this.ListenersError = new ArrayList<>();
    }    
    
    public void setPractica(clsPracticaLazy practica) throws IOException
    {
        this.xP = practica;
        this.lblNombre.setText(xP.getNombre());
        this.ttBoton.setText(xP.getAyudaBoton());
        
        String img = "/jfxconsultorio/Imagenes/Iconos/"
            + this.xP.getIconoBoton();
        Image image2 = new Image(img);
        this.ivBoton.setImage(image2);
        this.lblCodigo.setText("("+this.xP.getCodigo()+")");       
        
    }
    
    public void addListenerRegistro(iRegistryListener listener)
    {this.ListenersRegistro.add(listener);}
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    public void RaiseMensjae(String prMensaje)
    {
        for(iRegistryListener xR : ListenersRegistro)
        {xR.RegistryUpdated(prMensaje);}
    }
    
    public void RaiseError(String prError)
    {
        for(iReceptorError xR : ListenersError)
        {xR.ErrorOcurred(prError);}
    }
    
    @FXML
    private void btnAccion_Click(ActionEvent event)
    {
        try
        {
            JOptionPane confirm = new JOptionPane(this.xP.getPregunta(), JOptionPane.QUESTION_MESSAGE, 
            JOptionPane.YES_NO_OPTION);
            JDialog dialog = confirm.createDialog(this.xP.getTituloConfirmacion());
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            if((int)confirm.getValue() == JOptionPane.YES_OPTION){
                dialog.dispose();
                this.xP.getFullPractica().CambiarEstado();
                this.RaiseMensjae(this.xP.getConfirmacion());
            }  
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }
    
    @FXML
    private void btnDetalle_Click(ActionEvent event)
    {
        try
        {
           ctrlDetallePractica xCDP = (ctrlDetallePractica)clsGestorNavegacion.
                   Instanciar().AbrirControler("/jfxconsultorio/Practicas/fxDetallePractica.fxml");           
                       
           xCDP.setPractica(xP.getFullPractica());                   
            
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }
}
