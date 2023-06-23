/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Pacientes.clsPaciente;
import com.PRS.Consultorio.Pacientes.clsPacienteLazy;
import com.PRS.Consultorio.Practicas.clsCampoTrabajo;
import com.PRS.Consultorio.Practicas.clsPractica;
import com.PRS.Consultorio.Practicas.clsPracticaLazy;
import com.PRS.Consultorio.Practicas.clsTrabajo;
import com.PRS.Consultorio.Practicas.clsValorCampo;
import com.PRS.Consultorio.Profesionales.clsFirmante;
import com.PRS.Consultorio.Profesionales.clsPracticante;
import com.PRS.Consultorio.Profesionales.clsProfesional;
import com.PRS.Consultorio.Profesionales.clsProfesionalLazy;
import com.PRS.Consultorio.Profesionales.enTipoProfesional;
import com.PRS.Framework.FormulariosFX.clsGestorCarga;
import com.PRS.Framework.FormulariosFX.clsGestorValidacion;
import com.PRS.Framework.FormulariosFX.enTipoTextField;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import com.PRS.Framework.FormulariosFX.iRegistryListener;
import com.PRS.Framework.Horarios.clsHorario;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxconsultorio.Pacientes.ctrlAddPaciente;
import jfxconsultorio.Pacientes.ctrlAddPaciente.iListenerPaciente;
import jfxconsultorio.Pacientes.ctrlBuscarPaciente;
import jfxconsultorio.Pacientes.ctrlPacienteLazy;
import jfxconsultorio.Profesionales.ctrlRegistrarProfesional;
import jfxconsultorio.Profesionales.ctrlRegistrarProfesional.iListenerProfesional;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlCrearTurno implements Initializable, iReceptorError, 
        ctrlBuscarPaciente.iBusquedaPaciente, iRegistryListener, iListenerPaciente, 
        ctrlBuscarPractica.iBusquedaPractica, iListenerPractica, iListenerProfesional {

    @Override
    public void PacienteModificado(clsPaciente paciente) {}
    
    public interface iListenerTurnos
    {public void TurnoRegistrado(clsTrabajo trabajo);}

    @FXML private ComboBox cbHora;
    @FXML private ComboBox cbMinutos;
    
    @FXML private HBox hbBuscarPaciente;
    @FXML private HBox hbBuscarPractica;
    
    @FXML private VBox vbBusquedaPacientes;
    @FXML private VBox vbBusquedaPracticas;
    @FXML private VBox vbCampos;
    
    @FXML private TitledPane tpPaciente;
    @FXML private TitledPane tpPractica;
    
    @FXML private Label lblDocumento;
    @FXML private Label lblObraSocial;
    @FXML private Label lblSaldo;
    @FXML private Label lblLeyendaOS;
    @FXML private Label lblCostoOS;
    @FXML private Label lblDuracion;
    @FXML private Label lblFecha;
    
    @FXML private ComboBox cbSolicitante;
    @FXML private ComboBox cbEncargado;
    @FXML private ComboBox cbFirmante;
    
    @FXML private TextField txtCostoCoseguro;
    @FXML private TextField txtPagado;
    
    @FXML private ScrollPane sp;
    
    private Date Dia;
    private List<iReceptorError> ListenersError;
    private List<ctrlPacienteLazy> Pacientes;
    private List<ctrlPracticaLazy> Practicas;
    private clsPaciente PacienteSeleccionado;
    private List<iRegistryListener> ListenersMensaje;
    private List<iListenerTurnos> ListenerTurnos;
    private clsPractica PracticaSeleccionada;
    private List<ctrlValorCampo> Valores;
    
    
    void addListenerError(iReceptorError listener){this.ListenersError.add(listener);}
    
    void RaiseError(String prError)
    {
        ListenersError.stream().forEach((xR) -> {
            xR.ErrorOcurred(prError);
        });
    }
    
    public void setHeight(double height)
    {this.sp.setPrefHeight(height);}
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.MostrarHora();
        this.MostrarMinutos();
        this.ListenersError = new ArrayList<>();
        this.ListenersMensaje = new ArrayList<>();
        this.ListenerTurnos = new ArrayList<>();
        this.tpPaciente.setVisible(false);
        this.Valores = new ArrayList<>();
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Pacientes/fxBuscarPaciente.fxml"));
            this.hbBuscarPaciente.getChildren().clear();
            this.hbBuscarPaciente.getChildren().add(xL.load());
            ctrlBuscarPaciente xBP = xL.<ctrlBuscarPaciente>getController();
            xBP.addListenerBusqueda(this);
            xBP.addListenerError(this);
            
            FXMLLoader xL2 = new FXMLLoader(getClass()
            .getResource("/jfxconsultorio/Practicas/fxBuscarPractica.fxml"));
            
            this.hbBuscarPractica.getChildren().clear();
            this.hbBuscarPractica.getChildren().add(xL2.load());
            ctrlBuscarPractica xBPra = xL2.<ctrlBuscarPractica>getController();
            xBPra.addListenerBusqueda(this);
            xBPra.addListenerError(this);
            
            this.MostrarSolicitantes();
            this.MostrarEncargados();
            this.MostrarFirmantes();
            this.MostrarCamposAdicionales();
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }    
    
    private void MostrarCamposAdicionales()
    {
        try
        {
        
            List<clsCampoTrabajo> xCampos = clsCampoTrabajo.Listar(false);
            for(clsCampoTrabajo xCT :xCampos)
            {
                FXMLLoader xL = new FXMLLoader(getClass()
                        .getResource("/jfxconsultorio/Practicas/fxValorCampo.fxml"));
                this.vbCampos.getChildren().add(xL.load());
                
                ctrlValorCampo xVC = xL.<ctrlValorCampo>getController();
                xVC.setValor(new clsValorCampo(xCT));
                this.Valores.add(xVC);
            }
            
            if(xCampos.isEmpty()){this.vbCampos.getChildren()
                    .add(new Label("No hay campos adicionales a completar"));}
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }
    
    private void MostrarHora()
    {
        this.cbHora.getItems().clear();
        for(int i = 0; i<=23; i++)
        {this.cbHora.getItems().add(String.format("%02d", i));}
    }
    
    private void MostrarMinutos()
    {
        this.cbMinutos.getItems().clear();
        for(int i = 0; i<=59; i += 5)
        {this.cbMinutos.getItems().add(String.format("%02d", i));}
    }
    
    void setDia(Date dia){Dia = dia;this.lblFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(dia) + " a las: ");}

    @Override
    public void ErrorOcurred(String prMensaje) {RaiseError(prMensaje);}

    @Override
    public void VerResultados(List<clsPacienteLazy> resultados) {
        try
        {
            this.vbBusquedaPacientes.getChildren().clear();
            Pacientes = new ArrayList<>();
            for(clsPacienteLazy xP : resultados)
            {
                FXMLLoader xL = new FXMLLoader(getClass()
                        .getResource("/jfxconsultorio/Pacientes/fxPacienteLazy.fxml"));

                this.vbBusquedaPacientes.getChildren().add(xL.load());
                ctrlPacienteLazy xPL = xL.<ctrlPacienteLazy>getController();

                xPL.setPacienteLazy(xP);
                xPL.setAccion(ctrlPacienteLazy.enAccion.Seleccionar);
                xPL.addListenerError(this);
                xPL.addListenerPaciente(this);
                                
                Pacientes.add(xPL);
            }
            
            Pacientes.stream().forEach((xP) -> {
                xP.setResultados(Pacientes);
            });
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }
    
    public void btnCrearPaciente_Click(ActionEvent event)
    {
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Pacientes/fxAddPaciente.fxml"));
            Parent root = xL.load();
        
            ctrlAddPaciente xAP = xL.<ctrlAddPaciente>getController();
            xAP.setBotonCancelar(true);
            xAP.setCerrarAlRegistrar(true);
            xAP.addListenerPaciente(this);
            xAP.addListenerError(this);
            xAP.addListenerMensaje(this);
            Scene scene = new Scene(root);
            
            Stage st = new Stage();
            
            st.setScene(scene);
            st.setWidth(550);
            st.setHeight(650);
            st.setTitle("Registrar un nuevo paciente");
            st.show();
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}        
    }

    @Override
    public void PacienteRegistrado(clsPaciente xP) 
    {
         
        try
        {
            this.PacienteSeleccionado = xP;
            this.MostrarPaciente();
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}        
    }

    @Override
    public void RegistryUpdated(String prMensaje) {this.RaiseMensaje(prMensaje);}

    @Override
    public short getIDListerner() {return 2;}
    
    public void addListenerMensaje(iRegistryListener listener)
    {this.ListenersMensaje.add(listener);}
    
    private void RaiseMensaje(String prMensaje)
    {
        ListenersMensaje.stream().forEach((xRL) -> {
            xRL.RegistryUpdated(prMensaje);
        });
    }

    @Override
    public void PacienteSeleccionado(clsPaciente xP) {
        try
        {this.PacienteSeleccionado = xP;
        this.MostrarPaciente();}
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
        
    }
    
    private void MostrarPaciente() throws Exception
    {
        this.tpPaciente.setVisible(true);
        this.tpPaciente.setText(this.PacienteSeleccionado.getNombreCompleto());
        this.lblDocumento.setText(this.PacienteSeleccionado.getDocumento().toString());
        this.lblObraSocial.setText(this.PacienteSeleccionado.getAfiliacionStr());
        this.lblSaldo.setText(this.PacienteSeleccionado.getCuenta().getSaldo().pdValorString());
        this.tpPaciente.setExpanded(true);
        PracticaSeleccionada(this.PracticaSeleccionada);
    }

    @Override
    public void VerResultadosPractica(List<clsPracticaLazy> resultados) {
        try
        {
            this.vbBusquedaPracticas.getChildren().clear();
            Practicas = new ArrayList<>();
            for(clsPracticaLazy xP : resultados)
            {
                FXMLLoader xL = new FXMLLoader(getClass()
                        .getResource("/jfxconsultorio/Practicas/fxPracticaLazy.fxml"));

                this.vbBusquedaPracticas.getChildren().add(xL.load());
                ctrlPracticaLazy xPL = xL.<ctrlPracticaLazy>getController();

                xPL.setPracticaLazy(xP);
                xPL.addListenerError(this);
                xPL.addListenerPractica(this);
                
                Practicas.add(xPL);
            }
            
            Practicas.stream().forEach((xP) -> {
                xP.setResultados(Practicas);
            });
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }

    @Override
    public void PracticaSeleccionada(clsPractica practica) {
        try
        {
            if(practica != null)
            {
                PracticaSeleccionada = practica;
                this.tpPractica.setText(practica.getNombreyCodigo());
                this.tpPractica.setVisible(true);
                this.tpPractica.setExpanded(true);

                this.lblLeyendaOS.setText("Obra Social (" 
                        + PacienteSeleccionado.getAfiliacionStr() + ")");
                this.lblCostoOS.setText(practica.getCosto()
                        .getValorVigente(PacienteSeleccionado.getOS(), Dia)
                        .getCostoOS().pdValorString());
                this.txtCostoCoseguro.setText(String.valueOf(practica.getCosto()
                        .getValorVigente(PacienteSeleccionado.getOS(), Dia)
                        .getCoseguro().pdValor()));
                this.lblDuracion.setText(practica.getDuracion().getHoraStr() + " hs.");
            }
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}        
    }
    
    private void MostrarSolicitantes() throws Exception
    {
        this.cbSolicitante.getItems().clear();
        List<clsProfesionalLazy> xPs = clsProfesional
                .Listar(true, enTipoProfesional.Solicitante);
        
        xPs.stream().forEach((xP) -> {
            this.cbSolicitante.getItems().add(xP);
        });
    }
    
    public void btnNuevoSolicitante_Click(ActionEvent event)
    {
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Profesionales/fxRegistrarProfesional.fxml"));
            Parent root = xL.load();
        
            ctrlRegistrarProfesional xAP = xL.<ctrlRegistrarProfesional>getController();
            xAP.setTipo(enTipoProfesional.Solicitante);
            xAP.setCerrarAlRegistrar(true);
            xAP.addRegistryListerner(this);
            xAP.addListenerProfesional(this);
            Scene scene = new Scene(root);
            
            Stage st = new Stage();
            
            st.setScene(scene);
            st.setWidth(500);
            st.setHeight(550);
            st.setTitle("Registrar un nuevo solicitante");
            st.show();
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }

    @Override
    public void ProfesionalRegistrado(clsProfesional xP) {
        try
        {
            switch(xP.getTipo())
            {
                case Firmante:
                case Practicante:
                case Solicitante:
                    this.MostrarSolicitantes();
                    cbSolicitante.getItems().stream().filter((xLI) -> (((clsProfesionalLazy)xLI).getID() == xP.getID())).forEach((xLI) -> {
                        cbSolicitante.getSelectionModel().select(xLI);
            });
                    break;
                default:
            }
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
        
    }
    
    private void MostrarEncargados() throws Exception
    {
        this.cbEncargado.getItems().clear();
        List<clsProfesionalLazy> xPs = clsProfesional
                .ListarEncargados(true);
        
        xPs.stream().forEach((xP) -> {
            this.cbEncargado.getItems().add(xP);
        });
    }
    
    private void MostrarFirmantes() throws Exception
    {
        this.cbFirmante.getItems().clear();
        List<clsProfesionalLazy> xPs = clsProfesional
                .Listar(true, enTipoProfesional.Firmante);
        
        xPs.stream().forEach((xP) -> {
            this.cbFirmante.getItems().add(xP);
        });
    }
    
    @FXML
    private void btnBorrarEncargado_Click(ActionEvent event)
    {try{this.MostrarEncargados();}catch(Exception ex){RaiseError(ex.getMessage());}}
    
    @FXML
    private void btnBorrarFirmante_Click(ActionEvent event)
    {try{this.MostrarFirmantes();}catch(Exception ex){RaiseError(ex.getMessage());}}
    
    @FXML
    private void btnBorrarSolicitante_Click(ActionEvent event)
    {try{this.MostrarSolicitantes();}catch(Exception ex){RaiseError(ex.getMessage());}}

    @FXML
    private void btnRegistrar_Click(ActionEvent event)
    {
        try
        {
            clsGestorValidacion.ValidarComboBox(cbHora, "Hora de inicio", true);
            clsGestorValidacion.ValidarComboBox(cbMinutos, "Minutos", true);
            clsGestorValidacion.ValidarComboBox(cbSolicitante, "Solicitante", true);
            clsGestorValidacion.ValidarTextField(txtCostoCoseguro, "Costo de coseguro", true, enTipoTextField.Decimal);
            clsGestorValidacion.ValidarTextField(txtPagado, "Pagado de coseguro", true, enTipoTextField.Decimal);
            
            clsGestorCarga.getInstancia().startLoading("Registrando turno...");
            
            clsTrabajo trabajo = new clsTrabajo();
            List<clsValorCampo> xVs = new ArrayList<>();
            trabajo.setPractica(PracticaSeleccionada);
            trabajo.setPaciente(PacienteSeleccionado);
            
            clsProfesional xEncargado = new clsPracticante();
            if(!this.cbEncargado.getSelectionModel().isEmpty())
            {xEncargado = ((clsProfesionalLazy)this.cbEncargado.getSelectionModel()
                            .getSelectedItem()).getFullProfesional();}
            
            clsProfesional xFirmante = new clsFirmante();
            if(!this.cbFirmante.getSelectionModel().isEmpty())
            {xFirmante = ((clsProfesionalLazy)cbFirmante.getSelectionModel()
                    .getSelectedItem()).getFullProfesional();}
            
            Valores.stream().forEach((xVC) -> {
                xVs.add(xVC.getValorCampo());
            });
            
            float pago = Float.parseFloat(this.txtPagado.getText());
            if(this.PacienteSeleccionado.getCuenta().getSaldo().pdValor() > 0)
            {
                pago+=this.PacienteSeleccionado.getCuenta().getSaldo().pdValor();
                this.PacienteSeleccionado.SaldarCuenta();
            }
            
            trabajo.Registrar(((clsProfesionalLazy)this.cbSolicitante.getSelectionModel()
                    .getSelectedItem()).getFullProfesional(), Dia, 
                    new clsHorario(Integer.parseInt(this.cbHora
                            .getSelectionModel().getSelectedItem().toString()), 
                            Integer.parseInt(this.cbMinutos.getSelectionModel()
                                    .getSelectedItem().toString())), 
                    Float.parseFloat(this.txtCostoCoseguro.getText()), 
                    pago, new Date(), 
                    xEncargado, xFirmante, xVs);
            
            RaiseMensaje("Se registró un nuevo turno");
            

            /*FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Practicas/fxImprimirComprobante.fxml"));
            Parent root = xL.load();
        
            ctrlImprimirComprobante xPP = xL.<ctrlImprimirComprobante>getController();
            xPP.setTrabajo(trabajo);
            
            Scene scene = new Scene(root);
            
            Stage st = new Stage();
            
            st.setScene(scene);
            st.setWidth(800);
            st.setHeight(800);
            st.setTitle("Imprimir comprobante");
            st.show();*/
            
            clsGestorCarga.getInstancia().endLoading();

            this.NotificarRegistro(trabajo);
            
            
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());clsGestorCarga.getInstancia().endLoading();}
    }
    
     
    public void addListenerTurno(iListenerTurnos listener)
    {this.ListenerTurnos.add(listener);}
    
    public void NotificarRegistro(clsTrabajo trabajo)
    {
        ListenerTurnos.stream().forEach((xLT) -> {
            xLT.TurnoRegistrado(trabajo);
        });
    }
    
    public void setTrabajo(clsTrabajo trabajo)
    {
        try
        {   
            this.cbHora.getSelectionModel().select(String.format("%02d",
                    trabajo.getPractica().getDuracion().getFinalTime(
                            trabajo.getProgramacion().getHorario()).getHoras()));
            this.cbMinutos.getSelectionModel().select(String.format("%02d",
                    trabajo.getPractica().getDuracion().getFinalTime(
                            trabajo.getProgramacion().getHorario()).getMinutos()));
            
            this.PacienteSeleccionado(trabajo.getPaciente());
            for(clsProfesionalLazy xP : (List<clsProfesionalLazy>)cbSolicitante.getItems())
            {
                if(xP.getID() == trabajo.getSolicitud().getSolicitante()
                        .getID())cbSolicitante.getSelectionModel().select(xP);
            }
            
            if(trabajo.getRealizacion().getID() > 0)
            {
                for(clsProfesionalLazy xP : (List<clsProfesionalLazy>)cbEncargado.getItems())
                {
                    if(xP.getID() == trabajo.getRealizacion().getRealizador()
                            .getID())cbEncargado.getSelectionModel().select(xP);
                }
            }
            
            if(trabajo.getFirma().getID() > 0)
            {
                for(clsProfesionalLazy xP : (List<clsProfesionalLazy>)cbFirmante.getItems())
                {
                    if(xP.getID() == trabajo.getFirma().getFirmante()
                            .getID())cbFirmante.getSelectionModel().select(xP);
                }
            }
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }
}
