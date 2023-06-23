/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Pacientes;

import com.PRS.Consultorio.ObrasSociales.clsOS;
import com.PRS.Consultorio.Pacientes.clsAfiliado;
import com.PRS.Consultorio.Pacientes.clsContactoPaciente;
import com.PRS.Consultorio.Pacientes.clsDireccionPaciente;
import com.PRS.Consultorio.Pacientes.clsEmailPaciente;
import com.PRS.Consultorio.Pacientes.clsPaciente;
import com.PRS.Consultorio.Pacientes.clsParticular;
import com.PRS.Consultorio.Pacientes.clsTelefonoPaciente;
import com.PRS.Framework.FormulariosFX.clsGestorValidacion;
import com.PRS.Framework.FormulariosFX.enTipoTextField;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import com.PRS.Framework.FormulariosFX.iRegistryListener;
import com.PRS.Framework.Identificacion.clsIdentificacion;
import com.PRS.Framework.Identificacion.enTipoIdentificacion;
import com.PRS.Framework.Monedas.clsValor;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlAddPaciente implements Initializable, iReceptorError, 
        ctrlContactoPaciente.iFormularioContacto {

    
    
    public interface iListenerPaciente
    {
        public void PacienteRegistrado(clsPaciente xP);
        
        public void PacienteSeleccionado(clsPaciente xP);
        
        public void PacienteModificado(clsPaciente paciente);
    }

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido; 
    @FXML private TextField txtNroDoc;
    @FXML private TextField txtSaldo;
    
    @FXML private ComboBox cbOS;
    @FXML private ComboBox cbTipoDoc;
    
    @FXML private Tooltip ttTipoDocumento;
    
    @FXML private Button btnCancelar;
    
    @FXML private VBox vbContacto;
    
    private List<iReceptorError> ListenersError;
    private List<iRegistryListener> ListenersMensaje;
    private List<iListenerPaciente> ListenerPaciente;
    private boolean CerrarAlRegistrar;
    private List<clsContactoPaciente> Contacto;
    private ctrlContactoPaciente xCP;
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    public void addListenerMensaje(iRegistryListener listener)
    {this.ListenersMensaje.add(listener);}
    
    void RaiseError(String prError)
    {
        ListenersError.stream().forEach((xR) -> {
            xR.ErrorOcurred(prError);
        });
    }
    
    void RaiseMensaje(String prMensaje)
    {
        ListenersMensaje.stream().forEach((xR) -> {
            xR.RegistryUpdated(prMensaje);
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.ListenersError = new ArrayList<>();
        this.ListenersMensaje = new ArrayList<>();
        this.btnCancelar.setVisible(false);
        this.ListenerPaciente = new ArrayList<>();
        this.CerrarAlRegistrar = false;
        try
        {
            this.MostrarOSs();
            this.MostrarTiposDoc();
            
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Pacientes/fxContactoPaciente.fxml"));
            this.vbContacto.getChildren().add(xL.load());
            
            xCP = xL.<ctrlContactoPaciente>getController();
            xCP.addListenerContacto(this);
            xCP.addListenerError(this);
            this.Contacto = new ArrayList<>();
            xCP.setContacto(Contacto);
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }    
    
    @FXML
    private void btnRegistrar_Click(ActionEvent event)
    {
        try
        {
            clsGestorValidacion.ValidarTextField(txtNombre, "Nombre", 
                    true, enTipoTextField.Texto);
            clsGestorValidacion.ValidarTextField(txtApellido, "Apellido", 
                    true, enTipoTextField.Texto);
            clsGestorValidacion.ValidarComboBox(cbOS, "Obra social", true);
            clsGestorValidacion.ValidarComboBox(cbTipoDoc, "Tipo de documento", true);
            clsGestorValidacion.ValidarTextField(txtNroDoc, "Número de documento", 
                    true, enTipoTextField.Numerico);
            clsGestorValidacion.ValidarTextField(txtSaldo, "Saldo inicial", 
                    true, enTipoTextField.Decimal);
            clsParticular xP = new clsParticular();
            clsAfiliado xA = new clsAfiliado();
            xP.setApellido(this.txtApellido.getText());
            xP.setNombre(this.txtNombre.getText());
            xP.setDocumento(clsIdentificacion.Instanciar(enTipoIdentificacion
                    .valueOf(cbTipoDoc.getSelectionModel().getSelectedItem()
                            .toString()), Long.parseLong(this.txtNroDoc.getText())));
            xP.getCuenta().setSaldoInicial(new clsValor(Float.parseFloat(this.txtSaldo.getText())));
            
            xP.Registrar();
            
            if(((clsOS)this.cbOS.getSelectionModel().getSelectedItem()).getID() != 0)
            {xA = xP.Afiliar((clsOS)this.cbOS.getSelectionModel().getSelectedItem());}
            
            for(clsContactoPaciente xC : Contacto)
            {
                xC.Registrar(xP.getID());
                xP.getContacto().add(xC);
            }
            
            this.Contacto = new ArrayList<>();
            this.xCP.setContacto(Contacto);
            
            RaiseMensaje("Se registró un nuevo paciente.");
            
            if(((clsOS)this.cbOS.getSelectionModel().getSelectedItem()).getID() != 0)
            {this.NotificarListeners(xA);}
            else
            {this.NotificarListeners(xP);}
            
            
            if(this.CerrarAlRegistrar)
            {
                Stage stage = (Stage) btnCancelar.getScene().getWindow();
                stage.close();
            }
            else
            {this.LimpiarFormulario();}           
            
        }
        catch(Exception ex)
        {this.RaiseError(ex.getMessage());}
    }
    
    private void MostrarTiposDoc()
    {
        this.cbTipoDoc.getItems().clear();
        this.ttTipoDocumento.setText("Seleccionar un tipo de documento: \r");
        for(enTipoIdentificacion xTD : enTipoIdentificacion.values())
        {
            this.cbTipoDoc.getItems().add(xTD);
            this.ttTipoDocumento.setText(this.ttTipoDocumento.getText() + 
                    " " + xTD.toString() + ";");
        }
        this.cbTipoDoc.getSelectionModel().select("DNI");
    }
    
    private void MostrarOSs() throws Exception
    {
        List<clsOS> xOSs = clsOS.Listar(true);
        xOSs.add(0, new clsOS());
        this.cbOS.getItems().clear();
        xOSs.stream().forEach((xOS) -> {
            this.cbOS.getItems().add(xOS);
        });
    }
    
    private void LimpiarFormulario() throws Exception
    {
        this.txtApellido.setText("");
        this.txtNombre.setText("");
        this.txtNroDoc.setText("");
        this.txtSaldo.setText("0");
        this.MostrarOSs();
        this.MostrarTiposDoc();
    }
    
    public void setBotonCancelar(boolean visible)
    {this.btnCancelar.setVisible(visible);}
    
    public void setCerrarAlRegistrar(boolean cerrar)
    {this.CerrarAlRegistrar = cerrar;}
    
    @FXML
    private void btnCancelar_Click(ActionEvent event)
    {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
    
    public void addListenerPaciente(iListenerPaciente listener)
    {this.ListenerPaciente.add(listener);}
    
    private void NotificarListeners(clsPaciente xP)
    {
        ListenerPaciente.stream().forEach((xLP) -> {
            xLP.PacienteRegistrado(xP);
        });
    }
    
    @Override
    public void ErrorOcurred(String prMensaje) {RaiseError(prMensaje);}

    @Override
    public void TelefonoAgregado(clsTelefonoPaciente telefono) {
        this.Contacto.add(telefono);
        xCP.LimpiarTelefono();
        xCP.setContacto(Contacto);
    }

    @Override
    public void DireccionAgregada(clsDireccionPaciente direccion) {
        this.Contacto.add(direccion);
        xCP.LimpiarDireccion();
        xCP.setContacto(Contacto);
    }

    @Override
    public void EmailAgregado(clsEmailPaciente email) {
        this.Contacto.add(email);
        xCP.LimpiarEmail();
        xCP.setContacto(Contacto);
    }

    @Override
    public void ElementoQuitado(clsContactoPaciente elemento) {
        for(clsContactoPaciente xC : Contacto)
        {
            if(xC.getContactoStr().equals(elemento.getContactoStr()))
            {
                Contacto.remove(xC);
                break;
            }
        }
        xCP.setContacto(Contacto);
    }
}
