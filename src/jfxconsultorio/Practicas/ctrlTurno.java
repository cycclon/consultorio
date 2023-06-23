/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Practicas.clsTrabajo;
import com.PRS.Consultorio.Practicas.clsTrabajoLazy;
import com.PRS.Consultorio.Practicas.enEstadoTrabajo;
import com.PRS.Consultorio.Usuarios.clsGestorUsuariosConsultorio;
import com.PRS.Framework.Acceso.clsGestorAcceso;
import com.PRS.Framework.FormulariosFX.clsGestorNavegacion;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlTurno implements Initializable {
    
    public interface iListenerTurno
    {
        public void btnPagarPressed(clsTrabajo trabajo);
        
        public void btnReprogramarPressed(clsTrabajo trabajo);
        
        public void btnRealizarPressed(clsTrabajo trabajo);
        
        public void btnFirmarPressed(clsTrabajo trabajo);
        
        public void btnCancelarPressed(clsTrabajo trabajo);
        
        public void btnPacientePressed(clsTrabajo trabajo);
        
        public void btnDuplicarPressed(clsTrabajo trabajo);
    }

    @FXML private Label lblHorario;
    
    @FXML private Label lblSolicitante;
    @FXML private Label lblPracticante;
    @FXML private Label lblFinalizacion;
    @FXML private Label lblSaldo;
    @FXML private Label lblDocumento;
    
    @FXML private Button btnPagar;
    @FXML private Button btnRealizar;
    @FXML private Button btnReprogramar;
    @FXML private Button btnFirmar;
    @FXML private Button btnCancelar;
    @FXML private Button btnInforme;
    @FXML private Button lblPractica;
    @FXML private Button btnComprobante;
    @FXML private Button btnPaciente;
    @FXML private Button btnDuplicar;
    
    @FXML private ImageView ivCancel;
    
    private clsTrabajoLazy Trabajo;
    private List<iListenerTurno> ListenersTurno;
    private List<iReceptorError> ListenerError;
    private boolean Turno;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.ListenersTurno = new ArrayList<>();
        this.ListenerError = new ArrayList<>();
        Turno = true;
    }  
    
    public void setTurno(boolean turno)
    {this.Turno = turno;}
    
    public void setTrabajoLazy(clsTrabajoLazy trabajo)
    {Trabajo = trabajo; this.MostrarTrabajo(); this.HabilitarBotones();}
    
    private void MostrarTrabajo()
    {
        if(Turno)
        {
            this.lblHorario.setText(Trabajo.getHorario());
            this.lblFinalizacion.setText("Finaliza: " + 
                Trabajo.getHoraFinalizacion() + " hs.");
        }
        else{
            this.lblHorario.setText(new SimpleDateFormat("dd/MM/yyyy")
                .format(this.Trabajo.getDia()));
            this.lblFinalizacion.setText(this.Trabajo.getHorario());
        }
        
        this.btnPaciente.setText(Trabajo.getPaciente());
        this.lblPractica.setText(Trabajo.getNombrePractica());
        this.lblPracticante.setText(Trabajo.getPracticante());
        this.lblSolicitante.setText(Trabajo.getSolicitante());
        this.lblSaldo.setText(Trabajo.getSaldo());
        this.lblDocumento.setText(Trabajo.getDocumentoPaciente());
        
        this.ivCancel.setVisible(this.Trabajo.getEstado() == enEstadoTrabajo.Cancelado);
        
        this.btnPaciente.setVisible(Turno);
        
    }
    
    /*private void HabilitarBotones()
    {
        try
        {
            this.btnPagar.setVisible(this.Trabajo.isPagable());
            this.btnRealizar.setVisible(this.Trabajo.isTrabajable());
            this.btnReprogramar.setVisible(this.Trabajo.isProgramable());
            this.btnFirmar.setVisible(this.Trabajo.isFirmable());
            this.btnCancelar.setVisible(this.Trabajo.isCancelable());
            this.btnInforme.setVisible(this.Trabajo.isInformable());
            this.btnComprobante.setVisible(this.Trabajo.isComprobanteImprimible());
            
            this.btnDuplicar.setVisible(this.Trabajo.isDuplicable());
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
        
    }*/
    
    private void HabilitarBotones()
    {
        try
        {
            this.btnPagar.setVisible(this.Trabajo.isPagable()&&clsGestorAcceso.Instanciar()
                        .ComprobarPermisos(clsGestorUsuariosConsultorio.Instanciar()
                                .ObtenerAccesor(), (short)27));
            this.btnRealizar.setVisible(this.Trabajo.isTrabajable()&&clsGestorAcceso.Instanciar()
                        .ComprobarPermisos(clsGestorUsuariosConsultorio.Instanciar()
                                .ObtenerAccesor(), (short)20));
            this.btnReprogramar.setVisible(this.Trabajo.isProgramable()&&clsGestorAcceso.Instanciar()
                        .ComprobarPermisos(clsGestorUsuariosConsultorio.Instanciar()
                                .ObtenerAccesor(), (short)19));
            this.btnFirmar.setVisible(this.Trabajo.isFirmable()&&clsGestorAcceso.Instanciar()
                        .ComprobarPermisos(clsGestorUsuariosConsultorio.Instanciar()
                                .ObtenerAccesor(), (short)21));
            this.btnCancelar.setVisible(this.Trabajo.isCancelable()&&clsGestorAcceso.Instanciar()
                        .ComprobarPermisos(clsGestorUsuariosConsultorio.Instanciar()
                                .ObtenerAccesor(), (short)18));
            this.btnInforme.setVisible(this.Trabajo.isInformable()&&clsGestorAcceso.Instanciar()
                        .ComprobarPermisos(clsGestorUsuariosConsultorio.Instanciar()
                                .ObtenerAccesor(), (short)17));
            this.btnComprobante.setVisible(this.Trabajo.isComprobanteImprimible()&&clsGestorAcceso.Instanciar()
                        .ComprobarPermisos(clsGestorUsuariosConsultorio.Instanciar()
                                .ObtenerAccesor(), (short)28));
            this.lblPractica.setDisable(!clsGestorAcceso.Instanciar()
                        .ComprobarPermisos(clsGestorUsuariosConsultorio.Instanciar()
                                .ObtenerAccesor(), (short)23));
            this.btnDuplicar.setVisible(this.Trabajo.isDuplicable());
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
        
    }
    
    public void addListenerTurno(iListenerTurno listener)
    {this.ListenersTurno.add(listener);}
    
    @FXML
    private void btnPagar_Click(ActionEvent evetn)
    {
        try
        {for(iListenerTurno xLT : ListenersTurno)
        {
            xLT.btnPagarPressed(this.Trabajo.getTrabajoFull());}}
        catch(Exception ex){RaiseError(ex.getMessage());}
    }
    
    @FXML
    private void btnRealizar_click(ActionEvent event)
    {
        try
        {
            for(iListenerTurno xLT : ListenersTurno)
            {
                xLT.btnRealizarPressed(this.Trabajo.getTrabajoFull());}
            }
        catch(Exception ex){RaiseError(ex.getMessage());}
    }
    
    @FXML
    private void btnReprogramar_Click(ActionEvent event)
    {
        try
        {for(iListenerTurno xLT : ListenersTurno)
        {xLT.btnReprogramarPressed(this.Trabajo.getTrabajoFull());}}
        catch(Exception ex){RaiseError(ex.getMessage());}
    }
    
    @FXML
    private void btnFirmar_Click(ActionEvent event)
    {
        try
        {for(iListenerTurno xLT : ListenersTurno)
        {xLT.btnFirmarPressed(this.Trabajo.getTrabajoFull());}}
        catch(Exception ex){RaiseError(ex.getMessage());}
    }
    
    @FXML
    private void btnCancelar_Click(ActionEvent event)
    {
        try
        {for(iListenerTurno xLT : ListenersTurno)
        {xLT.btnCancelarPressed(this.Trabajo.getTrabajoFull());}}
        catch(Exception ex){RaiseError(ex.getMessage());}
    }
    
    public void addListenerError(iReceptorError listener)
    {this.ListenerError.add(listener);}
    
    private void RaiseError(String error)
    {
        for(iReceptorError xRE : ListenerError)
        {xRE.ErrorOcurred(error);}
    }
    
    @FXML
    private void btnVerInforme_Click(ActionEvent event)
    {
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Practicas/fxVerInforme.fxml"));
            Parent root = xL.load();
        
            ctrlVerInforme xPP = xL.<ctrlVerInforme>getController();
            xPP.setTrabajo(this.Trabajo.getTrabajoFull());
            
            Scene scene = new Scene(root);
            
            Stage st = new Stage();
            
            st.setScene(scene);
            st.setWidth(800);
            st.setHeight(800);
            st.setTitle("Ver informe");
            st.show();
        }
        catch(Exception ex)
        {this.RaiseError(ex.getMessage());}
    }
    
    @FXML
    private void btnPractica_Click(ActionEvent event)
    {
        try
        {
            clsGestorNavegacion.Instanciar()
                    .Abrir("/jfxconsultorio/Practicas/fxDetalleTrabajo.fxml", 
                            "Hoja de trabajo", "practicante.png", Trabajo.getTrabajoFull());
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }
    
    @FXML
    private void btnImprimirComprobante_Click(ActionEvent event)
    {
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Practicas/fxImprimirComprobante.fxml"));
            Parent root = xL.load();
        
            ctrlImprimirComprobante xPP = xL.<ctrlImprimirComprobante>getController();
            xPP.setTrabajo(this.Trabajo.getTrabajoFull());
            
            Scene scene = new Scene(root);
            
            Stage st = new Stage();
            
            st.setScene(scene);
            st.setWidth(800);
            st.setHeight(800);
            st.setTitle("Imprimir comprobante");
            st.show();
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }
    
    @FXML private void btnPaciente_Click(ActionEvent event)
    {
        try
        {for(iListenerTurno xLT : ListenersTurno)
        {xLT.btnPacientePressed(this.Trabajo.getTrabajoFull());}}
        catch(Exception ex){RaiseError(ex.getMessage());}
    }
    
    @FXML private void btnDuplicar_Click(ActionEvent event)
    {
        try
        {
            for(iListenerTurno lt : ListenersTurno)lt.btnDuplicarPressed(this.Trabajo.getTrabajoFull());
        }
        catch(Exception ex)
        {
            RaiseError(ex.getMessage());
        }
    }
}
