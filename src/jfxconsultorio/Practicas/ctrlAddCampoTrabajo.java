/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Practicas.clsCampoTrabajo;
import com.PRS.Framework.FormulariosFX.clsGestorValidacion;
import com.PRS.Framework.FormulariosFX.enTipoTextField;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ARSPIDALIERI
 */
public class ctrlAddCampoTrabajo implements Initializable {
    
    public interface iFormularioCampoTrabajo
    {
        public void CampoAgregado(clsCampoTrabajo campo);
        
        public void CampoEliminado(clsCampoTrabajo cmapo);
        
        public void CampoRestaurado(clsCampoTrabajo campo);
        
    }

    @FXML private TextField txtNombre;
    @FXML private TextField txtCodigo;
    @FXML private CheckBox chObligatorio;
    
    private List<iReceptorError> ListenersError;
    private List<iFormularioCampoTrabajo> ListenersCampo;
        
    
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
    
    public void addListenerCampo(iFormularioCampoTrabajo listener)
    {this.ListenersCampo.add(listener);}
    
    private void NotificarCampoAgregado(clsCampoTrabajo campo)
    {
        ListenersCampo.stream().forEach((xAC) -> {
            xAC.CampoAgregado(campo);
        });
    }
    
    @FXML
    private void btnAgregar_Click(ActionEvent event)
    {
        try
        {
            clsGestorValidacion.ValidarTextField(txtNombre, "Nombre de campo", true, enTipoTextField.Texto);
            clsGestorValidacion.ValidarTextField(txtCodigo, "Código para informe", true, enTipoTextField.Texto);
            clsCampoTrabajo xC = new clsCampoTrabajo();
            xC.setCodigoInforme("*-" + this.txtCodigo.getText() + "-*");
            xC.setNombre(this.txtNombre.getText());
            xC.setObligatorio(this.chObligatorio.isSelected());
            xC.Registrar();
            
            this.txtCodigo.setText("");
            this.txtNombre.setText("");
            this.chObligatorio.setSelected(false);
            
            this.NotificarCampoAgregado(xC);
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }
    
    
}
