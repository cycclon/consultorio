/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Pacientes;

import com.PRS.Consultorio.Bitacoras.Pacientes.bContactoPacienteEliminado;
import com.PRS.Consultorio.Pacientes.clsContactoPaciente;
import com.PRS.Consultorio.Pacientes.clsDireccionPaciente;
import com.PRS.Consultorio.Pacientes.clsEmailPaciente;
import com.PRS.Consultorio.Pacientes.clsPaciente;
import com.PRS.Consultorio.Pacientes.clsTelefonoPaciente;
import com.PRS.Consultorio.Practicas.clsTrabajoLazy;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.clsGestorNavegacion;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import com.PRS.Framework.FormulariosFX.iParametrizable;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import jfxconsultorio.Cuentas.ctrlCuenta;
import jfxconsultorio.Pacientes.ctrlAddPaciente.iListenerPaciente;
import jfxconsultorio.Practicas.ctrlTurno;

/**
 * FXML Controller class
 *
 * @author ARSPIDALIERI
 */
public class ctrlDetallePaciente implements Initializable, 
        iParametrizable, iMensajeable, iReceptorError, ctrlContactoPaciente.iFormularioContacto,
        iListenerPaciente
        {

    
   
    @FXML private Label lblMensaje;
    
    
    @FXML private HBox hbVolver;
    
    @FXML private VBox vbCuenta;
    @FXML private VBox vbHistoria;
    @FXML private VBox vbContacto;
    @FXML private VBox vbEditarPaciente;
    
    private clsPaciente Paciente;
    private ctrlContactoPaciente xC;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        
    }    
    
    @Override
    public String getNombre() {return "Detalle de paciente";}
    
    private void MostrarPaciente() 
    {
        try
        { 
            FXMLLoader l = new FXMLLoader(getClass().getResource("/jfxconsultorio/Pacientes/fxEditarPaciente.fxml"));
            this.vbEditarPaciente.getChildren().clear();
            this.vbEditarPaciente.getChildren().add(l.load());
            
            ctrlEditarPaciente cep = l.<ctrlEditarPaciente>getController();
            cep.addListenerError(this);
            cep.addListenerPaciente(this);
            
            cep.setPaciente(Paciente);
            
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Cuentas/fxCuenta.fxml"));
            this.vbCuenta.getChildren().add(xL.load());
            ctrlCuenta xCC = xL.<ctrlCuenta>getController();
            xCC.addListenerError(this);
            xCC.setCuenta(Paciente.getCuenta());
            this.MostrarHistoriaClinica();
            
            FXMLLoader xL2 = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Pacientes/fxContactoPaciente.fxml"));
            this.vbContacto.getChildren().add(xL2.load());
            
            xC = xL2.<ctrlContactoPaciente>getController();
            xC.addListenerContacto(this);
            xC.addListenerError(this);
            xC.setContacto(this.Paciente.getContacto());
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
       
    }
    
    
    
    private void MostrarHistoriaClinica() throws Exception
    {
        
        
        List<clsTrabajoLazy> xHistoria = Paciente.getHistoriaClinica();
        
        this.vbHistoria.getChildren().clear();
        for(clsTrabajoLazy xTL : xHistoria)
        {
            FXMLLoader xL = new FXMLLoader(getClass().getResource("/jfxconsultorio/Practicas/fxTurno.fxml"));
            this.vbHistoria.getChildren().add(xL.load());
            ctrlTurno xT = xL.<ctrlTurno>getController();
            xT.addListenerError(this);
            xT.setTurno(false);
            xT.setTrabajoLazy(xTL);
        }
        
        if(xHistoria.isEmpty()){this.vbHistoria.getChildren()
                .add(new Label("Este paciente no tiene trabajos realizados"));}
    }

    @Override
    public void setParametro(Object parametro) {
        this.Paciente = (clsPaciente) parametro; this.MostrarPaciente();
    }

    @Override
    public Label getCanvas() {return this.lblMensaje;}

    @Override
    public void ErrorOcurred(String prMensaje) 
    {clsGestorMensajes.Instanciar().MostrarError(this, prMensaje);}

    @Override
    public void TelefonoAgregado(clsTelefonoPaciente telefono) {
        try
        {
            this.Paciente.AgregarTelefono(telefono);
            xC.setContacto(this.Paciente.getContacto());
            xC.LimpiarTelefono();
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, "Se agregó un nuevo teléfono " + telefono.getTipo().toString());
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void DireccionAgregada(clsDireccionPaciente direccion) {
        try
        {
            this.Paciente.AgregarDireccion(direccion);
            xC.setContacto(this.Paciente.getContacto());
            xC.LimpiarDireccion();
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, "Se agregó una nueva dirección");
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void EmailAgregado(clsEmailPaciente email) {
        try
        {
            this.Paciente.AgregarEmail(email);
            xC.setContacto(this.Paciente.getContacto());
            xC.LimpiarEmail();
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                    "Se agregó una nueva dirección de correo electrónico");
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
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
                xC.setContacto(Paciente.getContacto());
                clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                        "Se quitó el dato de contacto del paciente");
            }
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void PacienteRegistrado(clsPaciente xP) {}

    @Override
    public void PacienteSeleccionado(clsPaciente xP) {}

    @Override
    public void PacienteModificado(clsPaciente paciente) {
        clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                "Se modificaron los datos personales de " + paciente.getNombreCompleto());
    }
    
    @FXML private void btnVolver_Click(ActionEvent event)
    {
        try
        {
            clsGestorNavegacion.Instanciar().Abrir("/jfxconsultorio/Pacientes/fxPacientes.fxml", 
                    "Pacientes", "pacientes.png");
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
        
    }
    
}
