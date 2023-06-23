/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Practicas.clsCancelacion;
import com.PRS.Consultorio.Practicas.clsTrabajo;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javax.swing.JDialog;
import javax.swing.JOptionPane;


/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlCancelarTurno implements Initializable {
    
    public interface iListenerCancelacion
    {public void TrabajoCancelado(clsTrabajo trabajo);}

    @FXML private Label lblTrabajo;
    @FXML private Label lblPaciente;
    @FXML private Label lblDia;
    @FXML private Label lblHorario;  
    
    @FXML private TextField txtMotivo;
    
    @FXML private ListView lstMotivos;
    
    private List<iListenerCancelacion> ListenersCancelacion;
    private List<iReceptorError> ListenersError;
    private clsTrabajo Trabajo;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.ListenersError = new ArrayList<>();
        this.ListenersCancelacion = new ArrayList<>();
        this.MostrarMotivos();
    }    
    
    public void setTrabajo(clsTrabajo trabajo)
    {this.Trabajo = trabajo; this.MostrarTrabajo();}
    
    private void MostrarTrabajo()
    {
        this.lblTrabajo.setText(Trabajo.getPractica().getNombreyCodigo());
        this.lblPaciente.setText(Trabajo.getPaciente().getNombreCompleto());
        this.lblDia.setText(new SimpleDateFormat("dd/MM/yyyy")
                .format(Trabajo.getProgramacion().getFecha()));
        this.lblHorario.setText(Trabajo.getProgramacion().getHorario()
                .getHoraStr());
        
    }   
    
    public void addListenerCancelacion(iListenerCancelacion listener)
    {this.ListenersCancelacion.add(listener);}
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    private void RaiseError(String error)
    {
        ListenersError.stream().forEach((xRE) -> {
            xRE.ErrorOcurred(error);
        });
    }
    
    private void NotificarCancelacion(clsTrabajo trabajo)
    {
        ListenersCancelacion.stream().forEach((xLF) -> {
            xLF.TrabajoCancelado(trabajo);
        });
    }
    
    private void MostrarMotivos()
    {
        try
        {
            List<String> xMotivos = clsCancelacion.ListarMotivos();
            lstMotivos.getItems().clear();
            xMotivos.stream().forEach((s) -> {
                this.lstMotivos.getItems().add(s);
            });
            if(xMotivos.isEmpty()){this.lstMotivos.getItems().add("Sin motivo");}
        }
        
        catch(Exception ex){RaiseError(ex.getMessage());}
    }
    
    @FXML
    private void lstMotivos_SeletedItemChanged(Event mouseEvent)
    {
        if(!lstMotivos.getSelectionModel().isEmpty())
        {
            this.txtMotivo.setText((String)this.lstMotivos.getSelectionModel()
                .getSelectedItem());
        }
    }
    
    @FXML
    private void btnCancelar_Click(ActionEvent event)
    {
        try
        {
            JOptionPane confirm = new JOptionPane("¿Está seguro que desea cancelar este turno?", JOptionPane.QUESTION_MESSAGE, 
            JOptionPane.YES_NO_OPTION);
            JDialog dialog = confirm.createDialog("Cancelar turno");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            if((int)confirm.getValue() == JOptionPane.YES_OPTION){
                dialog.dispose();
                Trabajo.Cancelar(new Date(), this.txtMotivo.getText());
                this.NotificarCancelacion(Trabajo);
            }
            
        }
        catch(Exception ex){RaiseError(ex.getMessage());}
    }
}
