/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Pacientes;

import com.PRS.Consultorio.Pacientes.clsContactoPaciente;
import com.PRS.Consultorio.Pacientes.clsDireccionPaciente;
import com.PRS.Consultorio.Pacientes.clsEmailPaciente;
import com.PRS.Consultorio.Pacientes.clsTelefonoPaciente;
import com.PRS.Consultorio.Pacientes.enTipoTelefono;
import com.PRS.Framework.FormulariosFX.clsGestorValidacion;
import com.PRS.Framework.FormulariosFX.enTipoTextField;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ARSPIDALIERI
 */
public class ctrlContactoPaciente implements Initializable {
    
    public interface iFormularioContacto
    {
        public void TelefonoAgregado(clsTelefonoPaciente telefono);
        
        public void DireccionAgregada(clsDireccionPaciente direccion);
        
        public void EmailAgregado(clsEmailPaciente email);
        
        public void ElementoQuitado(clsContactoPaciente elemento);
    }
    
    @FXML private ListView lstContacto;
    
    @FXML private ComboBox cbTipoTelefono;
    
    @FXML private TextField txtCalle;
    @FXML private TextField txtAltura;
    @FXML private TextField txtPisoDpto;
    @FXML private TextField txtLocalidad;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccionEmail;
    @FXML private TextField txtServidorEmail;
    
    @FXML private Button btnQuitar;

    private List<clsContactoPaciente> xContacto;
    private List<iFormularioContacto> ListenersContacto;
    private List<iReceptorError> ListenersError;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.ListenersContacto = new ArrayList<>();
        this.MostrarTiposTelefonos();
        this.ListenersError = new ArrayList<>();
    }    
    
    public void setContacto(List<clsContactoPaciente> contacto)
    {this.xContacto = contacto; this.MostrarContacto();}
    
    private void MostrarContacto()
    {
        this.lstContacto.getItems().clear();
        xContacto.stream().forEach((xC) -> {
            this.lstContacto.getItems().add(xC);
        });
        btnQuitar.setDisable(this.xContacto.isEmpty());
        if(xContacto.isEmpty()){this.lstContacto.getItems()
                .add("No hay datos de contacto para este paciente.");}
    }
    
    public void addListenerContacto(iFormularioContacto listener)
    {this.ListenersContacto.add(listener);}
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    private void RaiseError(String error)
    {
        ListenersError.stream().forEach((xRE) -> {
            xRE.ErrorOcurred(error);
        });
    }
    
    private void MostrarTiposTelefonos()
    {
        this.cbTipoTelefono.getItems().clear();
        this.cbTipoTelefono.getItems().addAll(Arrays.asList(enTipoTelefono.values()));
    }
    
    @FXML
    private void btnAgregarTelefono_Click(ActionEvent event)
    {
        try
        {
            clsGestorValidacion.ValidarComboBox(cbTipoTelefono, "Tipo de teléfono ", true);
            clsGestorValidacion.ValidarTextField(txtTelefono, 
                    "Número de teléfono", true, enTipoTextField.Texto);
            if(!this.cbTipoTelefono.getSelectionModel().isEmpty())
            {
                clsTelefonoPaciente xT = clsTelefonoPaciente
                        .Fabricar((enTipoTelefono)this.cbTipoTelefono
                                .getSelectionModel().getSelectedItem());

                xT.setTelefono(this.txtTelefono.getText());

                ListenersContacto.stream().forEach((xFC) -> {
                    xFC.TelefonoAgregado(xT);
                });
            }
            else{this.RaiseError("Debe seleccionar un tipo de teléfono");}
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
        
    }
    
    @FXML
    private void btnAgregarDireccion_Click(ActionEvent event)
    {
        try
        {
            clsGestorValidacion.ValidarTextField(txtCalle, "Calle", true, enTipoTextField.Texto);
            clsGestorValidacion.ValidarTextField(txtAltura, "Altura", true, enTipoTextField.Texto);
            clsGestorValidacion.ValidarTextField(txtLocalidad, "Localidad", true, enTipoTextField.Texto);
            clsDireccionPaciente direccion = new clsDireccionPaciente(this.txtCalle.getText(), 
                    this.txtAltura.getText(), this.txtPisoDpto.getText(), 
                    this.txtLocalidad.getText());
            ListenersContacto.stream().forEach((xFC) -> {
                xFC.DireccionAgregada(direccion);
            });
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
        
    }
    
    @FXML
    private void btnAgregarEmail_Click(ActionEvent event)
    {
        try
        {
            clsGestorValidacion.ValidarTextField(txtDireccionEmail, 
                    "Dirección de e-mail", true, enTipoTextField.Texto);
            clsGestorValidacion.ValidarTextField(txtServidorEmail, 
                    "Servidor", true, enTipoTextField.Texto);
            clsEmailPaciente email = new clsEmailPaciente(this.txtDireccionEmail.getText(), 
                    this.txtServidorEmail.getText());
            ListenersContacto.stream().forEach((xFC) -> {
                xFC.EmailAgregado(email);
            });
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
        
    }
    
    @FXML
    private void btnQuitarElemento_Click(ActionEvent event)
    {
        if(!this.lstContacto.getSelectionModel().isEmpty())
        {   
            ListenersContacto.stream().forEach((xFC) -> {
                xFC.ElementoQuitado((clsContactoPaciente)this.lstContacto
                        .getSelectionModel().getSelectedItem());
            });
        }
        else{RaiseError("Debe seleccionar un elemento de la lista");}
    }
    
    void LimpiarDireccion()
    {
        this.txtAltura.setText("");
        this.txtCalle.setText("");
        this.txtLocalidad.setText("");
        this.txtPisoDpto.setText("");
    }
    void LimpiarTelefono()
    {
        this.MostrarTiposTelefonos();
        this.txtTelefono.setText("");
    }
    void LimpiarEmail()
    {
        this.txtDireccionEmail.setText("");
        this.txtServidorEmail.setText("");
    }
}
