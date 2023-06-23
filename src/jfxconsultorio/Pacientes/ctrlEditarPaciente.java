/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Pacientes;

import com.PRS.Consultorio.Bitacoras.Pacientes.bContactoPacienteEliminado;
import com.PRS.Consultorio.ObrasSociales.clsOS;
import com.PRS.Consultorio.Pacientes.clsContactoPaciente;
import com.PRS.Consultorio.Pacientes.clsDireccionPaciente;
import com.PRS.Consultorio.Pacientes.clsEmailPaciente;
import com.PRS.Consultorio.Pacientes.clsPaciente;
import com.PRS.Consultorio.Pacientes.clsTelefonoPaciente;
import com.PRS.Framework.FormulariosFX.clsGestorValidacion;
import com.PRS.Framework.FormulariosFX.enTipoTextField;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import com.PRS.Framework.Identificacion.clsIdentificacion;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import jfxconsultorio.Pacientes.ctrlAddPaciente.iListenerPaciente;
import jfxconsultorio.Pacientes.ctrlContactoPaciente.iFormularioContacto;

/**
 * FXML Controller class
 *
 * @author ARSPIDALIERI
 */
public class ctrlEditarPaciente implements Initializable, iReceptorError, iFormularioContacto {    

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtDocumento;
    
    @FXML private Label lblTipoDocumento;
    
    @FXML private ComboBox cbOS;
    
    @FXML private VBox vbContacto;
    
    private List<iReceptorError> LEs;
    private List<iListenerPaciente> LPs;
    private List<iFormularioContacto> LCs;
    
    private clsPaciente Paciente;
    private ctrlContactoPaciente ccp;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try
        {
            LEs = new ArrayList<>();
            LPs = new ArrayList<>();
            LCs = new ArrayList<>();
            this.MostrarOSs();
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }    
    
    public void setPaciente(clsPaciente paciente)
    {Paciente = paciente;this.MostrarPaciente();}
    
    public void addListenerError(iReceptorError listener)
    {this.LEs.add(listener);}
    
    public void addListenerPaciente(iListenerPaciente listener)
    {this.LPs.add(listener);}
    
    public void addListenerContacto(iFormularioContacto listener)
    {this.LCs.add(listener);}
    
    private void RaiseError(String error)
    {   LEs.stream().forEach((re) -> {
        re.ErrorOcurred(error);
        });
}
    
    private void MostrarPaciente() 
    {
        try
        { 
            this.txtDocumento.setText(String.valueOf(this.Paciente.getDocumento().pdNumero()));
            this.lblTipoDocumento.setText(this.Paciente.getDocumento().pdTipo().toString());
            this.txtNombre.setText(this.Paciente.getNombre());
            this.txtApellido.setText(this.Paciente.getApellido());
            
            cbOS.getItems().stream().filter((os) -> (((clsOS)os).getID() == Paciente.getOS().getID())).forEach((os) -> {
                cbOS.getSelectionModel().select(os);
            });           
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
       
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
    
    @FXML
    private void btnGuardar_Click(ActionEvent event)
    {
        try
        {
            clsGestorValidacion.ValidarTextField(txtNombre, "Nombre", 
                    true, enTipoTextField.Texto);
            clsGestorValidacion.ValidarTextField(txtApellido, "Apellido", 
                    true, enTipoTextField.Texto);
            clsGestorValidacion.ValidarTextField(txtDocumento, "Número de documento", 
                    true, enTipoTextField.Numerico);
            this.Paciente.setApellido(this.txtApellido.getText());
            this.Paciente.setNombre(this.txtNombre.getText());
            this.Paciente.setDocumento(clsIdentificacion.Instanciar(Paciente.getDocumento()
                    .pdTipo(), Long.parseLong(this.txtDocumento.getText())));
            Paciente.GuardarCambios();
            
            if(((clsOS)cbOS.getSelectionModel().getSelectedItem()).getID() != Paciente.getOS().getID())
            {Paciente.CambiarAfiliacion((clsOS)cbOS.getSelectionModel().getSelectedItem());}
            
            LPs.stream().forEach((lp) -> {
                lp.PacienteModificado(Paciente);
            });
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }

    public void MostrarEdicionContacto()
    {
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Pacientes/fxContactoPaciente.fxml"));
            this.vbContacto.getChildren().clear();
            this.vbContacto.getChildren().add(xL.load());
            
            ccp = xL.<ctrlContactoPaciente>getController();
            ccp.addListenerContacto(this);
            ccp.addListenerError(this);
            ccp.setContacto(Paciente.getContacto());
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }

    @Override
    public void ErrorOcurred(String prMensaje) {
        RaiseError(prMensaje);
    }

    @Override
    public void TelefonoAgregado(clsTelefonoPaciente telefono) {
        try
        {
            this.Paciente.AgregarTelefono(telefono);
            ccp.setContacto(this.Paciente.getContacto());
            ccp.LimpiarTelefono();            
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
        LCs.stream().forEach((fc) -> {
            fc.TelefonoAgregado(telefono);
        });
    }

    @Override
    public void DireccionAgregada(clsDireccionPaciente direccion) {
        try
        {
            this.Paciente.AgregarDireccion(direccion);
            ccp.setContacto(this.Paciente.getContacto());
            ccp.LimpiarDireccion();
            
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
        LCs.stream().forEach((fc) -> {
            fc.DireccionAgregada(direccion);
        });
    }

    @Override
    public void EmailAgregado(clsEmailPaciente email) {
        try
        {
            this.Paciente.AgregarEmail(email);
            ccp.setContacto(this.Paciente.getContacto());
            ccp.LimpiarEmail();
            
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
        LCs.stream().forEach((fc) -> {
            fc.EmailAgregado(email);
        });
    }

    @Override
    public void ElementoQuitado(clsContactoPaciente elemento) {
        try
        {
            JOptionPane confirm = new JOptionPane("¿Está seguro que desea eliminar el elemento seleccionado?", JOptionPane.QUESTION_MESSAGE, 
            JOptionPane.YES_NO_OPTION);
            JDialog dialog = confirm.createDialog("Borrar dato de contacto");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            if((int)confirm.getValue() == JOptionPane.YES_OPTION){
                dialog.dispose();
                elemento.Eliminar();
                new bContactoPacienteEliminado(Paciente, elemento).Registrar();
                this.Paciente.ActualizarContacto();
                ccp.setContacto(Paciente.getContacto());                
            }
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
        LCs.stream().forEach((fc) -> {
            fc.ElementoQuitado(elemento);
        });
    }
}
