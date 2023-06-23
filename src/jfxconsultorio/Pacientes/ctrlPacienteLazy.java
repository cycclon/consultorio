/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Pacientes;

import com.PRS.Consultorio.Pacientes.clsPacienteLazy;
import com.PRS.Framework.FormulariosFX.clsGestorNavegacion;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlPacienteLazy implements Initializable {
    
    public enum enEstadoPacienteLazy
    {
        Seleccionado,
        Deseleccionado
    }
    
    private abstract class clsEstadoPacienteLazy
    {
        public abstract enEstadoPacienteLazy getEstado();
        
    }
    
    private class clsEPLSeleccionado extends clsEstadoPacienteLazy
    {

        @Override
        public enEstadoPacienteLazy getEstado() 
        {return enEstadoPacienteLazy.Seleccionado;}

    }
    
    private class clsEPLDeseleccionado extends clsEstadoPacienteLazy
    {

        @Override
        public enEstadoPacienteLazy getEstado() 
        {return enEstadoPacienteLazy.Deseleccionado;}

    }
    
    private abstract class clsAccionPacienteLazy
    {
        
        public abstract void ConfigurarBoton(ctrlPacienteLazy pl);
        
        public abstract void Accion(ctrlPacienteLazy pl) throws Exception;
    }
    
    private class clsAPLVerDetalle extends clsAccionPacienteLazy
    {
        private clsAPLVerDetalle(ctrlPacienteLazy pl)
        {this.ConfigurarBoton(pl);}

        @Override
        public final void ConfigurarBoton(ctrlPacienteLazy pl) {
            pl.btnAccion.setText("Ver detalle");
        }

        @Override
        public void Accion(ctrlPacienteLazy pl) throws Exception {
            clsGestorNavegacion.Instanciar().Abrir(
                    "/jfxconsultorio/Pacientes/fxDetallePaciente.fxml", 
                    "Detalle de paciente", "pacientes.png", pl.xP.getPacienteFull());
        }
    }
    
    private class clsAPLSeleccionar extends clsAccionPacienteLazy
    {
        
        private clsAPLSeleccionar(ctrlPacienteLazy pl)
        {this.ConfigurarBoton(pl);}

        @Override
        public final void ConfigurarBoton(ctrlPacienteLazy pl) {
            pl.btnAccion.setText("Seleccionar");
        }

        @Override
        public void Accion(ctrlPacienteLazy pl) throws Exception {
            pl.Resultados.stream().forEach((xP) -> {
                xP.Deseleccionar();
            });
            pl.Seleccionar();
        }
    }
    
    public enum enAccion
    {
        VerDetalle,
        Seleccionar
    }

    @FXML private Label lblNombreCompleto;
    
    @FXML private AnchorPane ap;
    
    @FXML private Button btnAccion;
    
    private clsPacienteLazy xP;
    private clsAccionPacienteLazy Accion;
    private List<ctrlPacienteLazy> Resultados;
    private clsEstadoPacienteLazy Estado;
    private List<ctrlAddPaciente.iListenerPaciente> ListenersPaciente;
    private List<iReceptorError> ListenersError;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        xP = new clsPacienteLazy();
        Accion = new clsAPLVerDetalle(this);
        Estado = new clsEPLDeseleccionado();
        this.Resultados = new ArrayList<>();
        this.ListenersPaciente = new ArrayList<>();
        this.ListenersError = new ArrayList<>();
    }    
    
    private clsAccionPacienteLazy FabricarAccion(enAccion accion)
    {
        switch(accion)
        {
            case Seleccionar:
                return new clsAPLSeleccionar(this);
            case VerDetalle:
                return new clsAPLVerDetalle(this);
            default:
                return new clsAPLVerDetalle(this);
        }
    }
    
    public void setAccion(enAccion accion){Accion = FabricarAccion(accion);}
    
    public void setPacienteLazy(clsPacienteLazy paciente)
    {
        xP = paciente;
        this.lblNombreCompleto.setText(xP.toString());
    }
    
    public void setResultados(List<ctrlPacienteLazy> resultados)
    {this.Resultados = resultados;}
    
    private void Seleccionar() throws Exception
    {
        this.ap.setStyle("-fx-background-color: #d2ffbe;");
        this.btnAccion.setDisable(true);
        this.Estado = new clsEPLSeleccionado();
        this.NotificarSeleccion();
    }
    
    private void Deseleccionar()
    {
        this.ap.setStyle("-fx-background-color: transparent;");
        this.btnAccion.setDisable(false);
        this.Estado = new clsEPLDeseleccionado();        
    }
    
    @FXML
    private void btnAccion_Click(ActionEvent event)
    {
        try
        {this.Accion.Accion(this);}
        catch(Exception ex)
        {RaiseError(ex.getMessage());}        
    }
    
    public enEstadoPacienteLazy getEstado(){return this.Estado.getEstado();}
    
    public void NotificarSeleccion() throws Exception
    {
        for(ctrlAddPaciente.iListenerPaciente xLP : ListenersPaciente)
        {xLP.PacienteSeleccionado(this.xP.getPacienteFull());}
    }
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    public void RaiseError(String prError)
    {
        ListenersError.stream().forEach((xRE) -> {
            xRE.ErrorOcurred(prError);
        });
    }
    
    public void addListenerPaciente(ctrlAddPaciente.iListenerPaciente listener)
    {this.ListenersPaciente.add(listener);}
}
