/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Alertas.clsGestorAlertas;
import com.PRS.Consultorio.CentrosCosto.clsCentroCosto;
import com.PRS.Consultorio.Cuentas.clsCobro;
import com.PRS.Consultorio.Cuentas.clsPago;
import com.PRS.Consultorio.Pacientes.clsContactoPaciente;
import com.PRS.Consultorio.Pacientes.clsDireccionPaciente;
import com.PRS.Consultorio.Pacientes.clsEmailPaciente;
import com.PRS.Consultorio.Pacientes.clsPaciente;
import com.PRS.Consultorio.Pacientes.clsTelefonoPaciente;
import com.PRS.Consultorio.Practicas.clsProgramador;
import com.PRS.Consultorio.Practicas.clsTrabajo;
import com.PRS.Consultorio.Practicas.clsTrabajoLazy;
import com.PRS.Consultorio.Profesionales.clsProfesional;
import com.PRS.Consultorio.Profesionales.clsProfesionalLazy;
import com.PRS.Consultorio.Usuarios.clsGestorUsuariosConsultorio;
import com.PRS.Framework.Acceso.clsGestorAcceso;
import com.PRS.Framework.FormulariosFX.clsGestorCarga;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import com.PRS.Framework.FormulariosFX.iParametrizable;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import com.PRS.Framework.FormulariosFX.iRegistryListener;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import jfxconsultorio.Alertas.ctrlAlertas;
import jfxconsultorio.Pacientes.ctrlAddPaciente.iListenerPaciente;
import jfxconsultorio.Pacientes.ctrlContactoPaciente;
import jfxconsultorio.Pacientes.ctrlEditarPaciente;
import jfxconsultorio.Practicas.ctrlCancelarTurno.iListenerCancelacion;
import jfxconsultorio.Practicas.ctrlCrearTurno.iListenerTurnos;
import jfxconsultorio.Practicas.ctrlFirmar.iListenerFirma;
import jfxconsultorio.Practicas.ctrlPagar.iListenerPago;
import jfxconsultorio.Practicas.ctrlRealizar.iListenerRealizacion;
import jfxconsultorio.Practicas.ctrlReprogramar.iListenerProgramacion;
import jfxconsultorio.Practicas.ctrlTurno.iListenerTurno;


/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlTurnos implements Initializable, iMensajeable, iReceptorError, 
        iRegistryListener, iListenerTurnos, iListenerTurno, iListenerPago, 
        iListenerProgramacion, iListenerRealizacion, iListenerFirma, iListenerCancelacion,
        ctrlContactoPaciente.iFormularioContacto, iListenerPaciente, iParametrizable{

    @FXML private Label lblMensaje;
    
    @FXML private DatePicker dpDia;
    
    @FXML private VBox vbPrincipal;    
    @FXML private VBox vbTurnos;
    
    @FXML private ScrollPane sp;
    
    @FXML private ComboBox cbRealizador;
    @FXML private ComboBox cbCC;
    
    @FXML private Button btnRegistrarTurno;
    
    @FXML private HBox hbAlertas;
    
    private ctrlAlertas Alertas;
       
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();        
        sp.setPrefHeight(bounds.getHeight() - 150);        
        
        try
        {
            this.MostrarRealizadores();
            this.MostrarCCs();
            this.btnRegistrarTurno.setVisible(clsGestorAcceso.Instanciar()
                    .ComprobarPermisos(clsGestorUsuariosConsultorio.Instanciar()
                            .ObtenerAccesor(), (short)26));
            this.ActualizarAlertas();
            //this.dpDia.setValue(LocalDate.now());
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
       
    private void MostrarCCs() throws Exception
    {
        List<clsCentroCosto> xCCs = clsCentroCosto.Listar(false);
        this.cbCC.getItems().clear();               
        
        for(int i = 0; i<xCCs.size(); i++)
        {
            this.cbCC.getItems().add(xCCs.get(i).getNombre());
        }
    }
            
    
    @Override
    public String getNombre() {return "Turnos";}
    
    private void ActualizarAlertas() throws Exception
    {
        if(Alertas == null)
        {
        FXMLLoader xL = new FXMLLoader(getClass()
                .getResource("/jfxconsultorio/Alertas/fxAlertas.fxml"));
        this.hbAlertas.getChildren().clear();
        this.hbAlertas.getChildren().add(xL.load());
        Alertas = xL.<ctrlAlertas>getController();
        
        }
        Alertas.setAlertas(clsGestorAlertas.Instanciar()
                    .ObtenerAlertasTrabajo());
    }
    
    @FXML
    private void btnAgregarTurno_Click(ActionEvent event)
    {
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Practicas/fxCrearTurno.fxml"));
            this.vbPrincipal.getChildren().clear();
            this.vbPrincipal.getChildren().add(xL.load());
            ctrlCrearTurno xCT = xL.<ctrlCrearTurno>getController();
            xCT.setDia(Date.from(this.dpDia.getValue().atStartOfDay()
                    .atZone(ZoneId.systemDefault()).toInstant()));
            xCT.addListenerError(this);
            xCT.addListenerMensaje(this);
            xCT.addListenerTurno(this);
            xCT.setHeight(sp.getPrefHeight());
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public Label getCanvas() {return this.lblMensaje;}

    @Override
    public void ErrorOcurred(String prMensaje) {
        clsGestorMensajes.Instanciar().MostrarError(this, prMensaje);
    }

    @Override
    public void RegistryUpdated(String prMensaje) {
        clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, prMensaje);
    }

    @Override
    public short getIDListerner() {return 3;}
    
    private void MostrarTurnosDia() throws Exception
    {
        try
        {
            clsGestorCarga.getInstancia().startLoading("Cargando turnos del día " 
                    + new SimpleDateFormat("dd/MM/yyyy").format(Date.from(
                            this.dpDia.getValue().atStartOfDay()
                            .atZone(ZoneId.systemDefault()).toInstant())) + "...");
            List<clsTrabajoLazy> xTs = clsProgramador.Instanciar().getTurnos(
                    Date.from(this.dpDia.getValue().atStartOfDay()
                            .atZone(ZoneId.systemDefault()).toInstant()));

            List<clsProgramador.clsFiltoTrabajoLazy> xFs = new ArrayList<>();

            if(!cbRealizador.getSelectionModel().isEmpty())
            {            
                clsProgramador.clsFTLRealizador xFR;
                xFR = new clsProgramador.clsFTLRealizador((String)cbRealizador
                        .getSelectionModel().getSelectedItem());
                xFs.add(xFR);            
            }

            if(!cbCC.getSelectionModel().isEmpty())
            {
                clsProgramador.clsFTLCC xFR;
                xFR = new clsProgramador.clsFTLCC((String)cbCC.getSelectionModel()
                        .getSelectedItem());
                xFs.add(xFR);
            }

            xTs = clsProgramador.Instanciar().Filtrar(xTs, xFs);
            this.vbTurnos.getChildren().clear();
            for(clsTrabajoLazy xTL : xTs)
            {
                FXMLLoader xL = new FXMLLoader(getClass()
                        .getResource("/jfxconsultorio/Practicas/fxTurno.fxml"));
                this.vbTurnos.getChildren().add(xL.load());
                ctrlTurno xT = xL.<ctrlTurno>getController();

                xT.setTrabajoLazy(xTL);
                xT.addListenerError(this);
                xT.addListenerTurno(this);
            }
            if(xTs.isEmpty())
            {
                Label lblVacio = new Label();
                lblVacio.setText("No hay turnos para este día");
                this.vbTurnos.getChildren().add(lblVacio);
            }
            clsGestorCarga.getInstancia().endLoading(); 
        }
        catch(Exception ex)
        {
            clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());
            clsGestorCarga.getInstancia().endLoading(); 
        }        
    }
    
    @FXML private void dpDia_ValueChanged(ActionEvent event)
    {
        try
        {
            this.MostrarTurnosDia();
            this.MostrarRealizadores();
        }
        catch(Exception ex)
        {
            clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());            
        }
    }

    @Override
    public void TurnoRegistrado(clsTrabajo trabajo) {
        try
        {
            this.ActualizarAlertas();
            this.MostrarTurnosDia();
            this.btnAgregarTurno_Click(new ActionEvent());
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void btnPagarPressed(clsTrabajo trabajo) {
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Practicas/fxPagar.fxml"));
            this.vbPrincipal.getChildren().clear();
            this.vbPrincipal.getChildren().add(xL.load());
            
            ctrlPagar xPagar = xL.<ctrlPagar>getController();
            xPagar.setTrabajo(trabajo);
            xPagar.addListenerError(this);
            xPagar.addListenerPago(this);
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void btnReprogramarPressed(clsTrabajo trabajo) {
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Practicas/fxReprogramar.fxml"));
            this.vbPrincipal.getChildren().clear();
            this.vbPrincipal.getChildren().add(xL.load());
            
            ctrlReprogramar xReprogramar = xL.<ctrlReprogramar>getController();
            xReprogramar.setTrabajo(trabajo);
            xReprogramar.addListenerError(this);
            xReprogramar.addListenerProgramacion(this);
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void btnRealizarPressed(clsTrabajo trabajo) {
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Practicas/fxRealizar.fxml"));
            this.vbPrincipal.getChildren().clear();
            this.vbPrincipal.getChildren().add(xL.load());
            
            ctrlRealizar xRealizar = xL.<ctrlRealizar>getController();
            xRealizar.setTrabajo(trabajo);
            xRealizar.addListenerError(this);
            xRealizar.addListenerRealizacion(this);
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void btnFirmarPressed(clsTrabajo trabajo) {
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Practicas/fxFirmar.fxml"));
            this.vbPrincipal.getChildren().clear();
            this.vbPrincipal.getChildren().add(xL.load());
            
            ctrlFirmar xRealizar = xL.<ctrlFirmar>getController();
            xRealizar.setTrabajo(trabajo);
            xRealizar.addListenerError(this);
            xRealizar.addListenerFirma(this);
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
    @Override
    public void btnPacientePressed(clsTrabajo trabajo) {
        try
        {
            FXMLLoader l = new FXMLLoader(getClass().getResource("/jfxconsultorio/Pacientes/fxEditarPaciente.fxml"));
            this.vbPrincipal.getChildren().clear();
            this.vbPrincipal.getChildren().add(l.load());
            
            ctrlEditarPaciente cep = l.<ctrlEditarPaciente>getController();
            cep.addListenerError(this);
            cep.addListenerContacto(this);
            cep.addListenerPaciente(this);
            cep.setPaciente(trabajo.getPaciente());
            cep.MostrarEdicionContacto();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void btnCancelarPressed(clsTrabajo trabajo) {
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Practicas/fxCancelarTurno.fxml"));
            this.vbPrincipal.getChildren().clear();
            this.vbPrincipal.getChildren().add(xL.load());
            
            ctrlCancelarTurno xRealizar = xL.<ctrlCancelarTurno>getController();
            xRealizar.setTrabajo(trabajo);
            xRealizar.addListenerError(this);
            xRealizar.addListenerCancelacion(this);
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void PagoEfectuado(clsTrabajo trabajo, clsCobro cobro, clsPago pago) {
        try
        {
            this.ActualizarAlertas();
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                    "Se registró el pago de " + trabajo.getPaciente()
                            .getNombreCompleto() + " por " + pago.getMonto().pdValorString() + " en concepto de " + trabajo.getPractica().getNombre());
            this.vbPrincipal.getChildren().clear();
            this.MostrarTurnosDia();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void TrabajoReprogramado(clsTrabajo trabajo) {
        try
        {
            this.ActualizarAlertas();
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                "Se reprogramó el turno de " + trabajo.getPaciente()
                        .getNombreCompleto() + " para el día " + 
                        new SimpleDateFormat("dd/MM/yyyy").format(trabajo
                                .getProgramacion().getFecha()) + " a las " + 
                        trabajo.getProgramacion().getHorario().getHoraStr());
            this.vbPrincipal.getChildren().clear();
            this.MostrarTurnosDia();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
        
    }

    @Override
    public void TrabajoRealizado(clsTrabajo trabajo) {
        try
        {
            this.ActualizarAlertas();
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
               trabajo.getPractica().getNombre() + " realizado para " + trabajo.getPaciente()
                        .getNombreCompleto() + " por " + trabajo.getRealizacion()
                                .getRealizador().getNombreCompleto());
            this.vbPrincipal.getChildren().clear();
            this.MostrarTurnosDia();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void TrabajoFirmado(clsTrabajo trabajo) {
        try
        {
            this.ActualizarAlertas();
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
               trabajo.getPractica().getNombre() + " realizada por " + trabajo.getRealizacion()
                                .getRealizador().getNombreCompleto() + 
                       " y firmada por " + trabajo.getFirma().getFirmante()
                               .getNombreCompleto());
            
            this.vbPrincipal.getChildren().clear();
            this.MostrarTurnosDia();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
    

    @Override
    public void TrabajoCancelado(clsTrabajo trabajo) {
        try
        {
            this.ActualizarAlertas();
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
               "Turno de " + trabajo.getPractica().getNombre() 
                       + " para " + trabajo.getPaciente().getNombreCompleto() + 
                       " cancelado correctamente");
            
            this.vbPrincipal.getChildren().clear();
            this.MostrarTurnosDia();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}        
    }
    
    @FXML
    private void cbRealizador_SelectedItemChanged(ActionEvent event)
    {
        try
        {this.MostrarTurnosDia();}
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
    @FXML
    private void cbCC_SelectedItemChanged(ActionEvent event)
    {
        try
        {this.MostrarTurnosDia();}
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
    private void MostrarRealizadores() throws Exception
    {
        List<clsProfesionalLazy> xPs = clsProfesional.ListarEncargados(true);
        this.cbRealizador.getItems().clear();
        this.cbRealizador.getSelectionModel().clearSelection();
        for(clsProfesionalLazy xP : xPs)
        {this.cbRealizador.getItems().add(xP.getNombreCompleto());}    
        
    }
    
    @FXML
    private void btnBorrarSelRealizador_Click(ActionEvent event)
    {
        try
        {
            this.MostrarRealizadores();
            this.MostrarTurnosDia();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
    @FXML
    private void btnBorrarSelCC_Click(ActionEvent event)
    {
        try
        {
            this.MostrarCCs();
            this.MostrarTurnosDia();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void TelefonoAgregado(clsTelefonoPaciente telefono) {
        clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, "Se agregó un nuevo teléfono " + telefono.getTipo().toString());
    }

    @Override
    public void DireccionAgregada(clsDireccionPaciente direccion) {
        clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, "Se agregó una nueva dirección");
    }

    @Override
    public void EmailAgregado(clsEmailPaciente email) {
        clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                    "Se agregó una nueva dirección de correo electrónico");
    }

    @Override
    public void ElementoQuitado(clsContactoPaciente elemento) {
        clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                        "Se quitó el dato de contacto del paciente");
    }

    @Override
    public void PacienteRegistrado(clsPaciente xP) {}

    @Override
    public void PacienteSeleccionado(clsPaciente xP) {}

    @Override
    public void PacienteModificado(clsPaciente paciente) {
        try
        {
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                "Se modificaron los datos personales de " + paciente.getNombreCompleto());
            this.vbPrincipal.getChildren().clear();
            this.MostrarTurnosDia();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}        
    }    

    @Override
    public void btnDuplicarPressed(clsTrabajo trabajo) {
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Practicas/fxCrearTurno.fxml"));
            this.vbPrincipal.getChildren().clear();
            this.vbPrincipal.getChildren().add(xL.load());
            ctrlCrearTurno xCT = xL.<ctrlCrearTurno>getController();
            xCT.setDia(Date.from(this.dpDia.getValue().atStartOfDay()
                    .atZone(ZoneId.systemDefault()).toInstant()));
            xCT.addListenerError(this);
            xCT.addListenerMensaje(this);
            xCT.addListenerTurno(this);
            xCT.setHeight(sp.getPrefHeight());
            xCT.setTrabajo(trabajo);
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void setParametro(Object parametro) {
        this.dpDia.setValue(((Date)parametro).toInstant().atZone(
                ZoneId.systemDefault()).toLocalDate());
        
    }
}
