/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.CentrosCosto;

import com.PRS.Consultorio.CentrosCosto.clsCentroCosto;
import com.PRS.Framework.FormulariosFX.clsGestorValidacion;
import com.PRS.Framework.FormulariosFX.enTipoTextField;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import com.PRS.Framework.FormulariosFX.iRegistryListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javax.swing.JDialog;
import javax.swing.JOptionPane;


/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlAddCentroCosto implements Initializable {

    @FXML TextField txtNombre;
    @FXML TextField txtCodigo;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.RegistryListeners = new ArrayList<>();
        this.ErrorListeners = new ArrayList<>();
    }    
    
    @FXML
    private void btnAgregar_Click(ActionEvent event)
    {
        try
        {
            clsGestorValidacion.ValidarTextField(txtNombre, "Nombre", true, 
                    enTipoTextField.Texto);
            clsGestorValidacion.ValidarTextField(txtCodigo, "Código", true, 
                    enTipoTextField.Numerico);
            

            JOptionPane confirm = new JOptionPane("¿Está seguro que desea registrar este centro de costos?", JOptionPane.QUESTION_MESSAGE, 
            JOptionPane.YES_NO_OPTION);
            JDialog dialog = confirm.createDialog("Centros de costo");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            if((int)confirm.getValue() == JOptionPane.YES_OPTION) {
                dialog.dispose();
                clsCentroCosto xC = new clsCentroCosto(this.txtCodigo.getText(), 
                        this.txtNombre.getText());
                xC.Registrar();
                this.NotificarCambio("Se registró un nuevo centro de costos");
            }
        }
        catch(Exception ex)
        {this.NotificarError(ex.getMessage());}
    }
    
    private List<iRegistryListener> RegistryListeners;
    private List<iReceptorError> ErrorListeners;
    
    public void addErrorListener(iReceptorError prListener)
    {this.ErrorListeners.add(prListener);}
    
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
    
    private void NotificarError(String prError)
    {
        for (iReceptorError ErrorListener : this.ErrorListeners) {
            ErrorListener.ErrorOcurred(prError);
        }
    }
    
    private void NotificarCambio(String prMensaje)
    {
        for (iRegistryListener RegistryListener : this.RegistryListeners) {
            RegistryListener.RegistryUpdated(prMensaje);
        }
    }
}
