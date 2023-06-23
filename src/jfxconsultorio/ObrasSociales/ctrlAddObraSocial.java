/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jfxconsultorio.ObrasSociales;

import com.PRS.Consultorio.ObrasSociales.clsOS;
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

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlAddObraSocial implements Initializable {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCodigo;
    
    private List<iReceptorError> ListenersError;
    private List<iRegistryListener> ListenersUpdate;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.ListenersError = new ArrayList<>();
        this.ListenersUpdate = new ArrayList<>();
    }    
    @FXML
    private void btnRegistrar_Click(ActionEvent event)
    {
        try
        {
            clsGestorValidacion.ValidarTextField(txtNombre, "Nombre", 
                    true, enTipoTextField.Texto);
            clsGestorValidacion.ValidarTextField(txtCodigo, "Código", 
                    true, enTipoTextField.Numerico);
            clsOS xOS = new clsOS(Integer.parseInt(this.txtCodigo.getText()),
                    this.txtNombre.getText());
            xOS.Registrar();
            this.NotificarUpdate();
        }
        catch(Exception ex){this.NotificarError(ex.getMessage());}
    }
    
    private void NotificarError(String prError)
    {
        for (iReceptorError ListenersError1 : this.ListenersError) {
            ListenersError1.ErrorOcurred(prError);
        }
    }
    
    private void NotificarUpdate()
    {
        for(int i=0;i<this.ListenersError.size();i++)
        {
            ListenersUpdate.get(i).RegistryUpdated("Se registró una nueva obra social");
        }
    }

    public void addListenerError(iReceptorError prListener)
    {this.ListenersError.add(prListener);}
    
    public void addListenerUpdate(iRegistryListener prListener)
    {this.ListenersUpdate.add(prListener);}
}
