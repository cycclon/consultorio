/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Practicas.clsTrabajo;
import com.PRS.Framework.FormulariosFX.clsGestorValidacion;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import com.PRS.Framework.Horarios.clsHorario;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlReprogramar implements Initializable {
    
    public interface iListenerProgramacion
    {public void TrabajoReprogramado(clsTrabajo trabajo);}

    @FXML private ComboBox cbHora;
    @FXML private ComboBox cbMinutos;
    
    @FXML private Label lblPractica;
    @FXML private Label lblPaciente;
    
    @FXML private DatePicker dpFecha;
    
    private List<iReceptorError> ListenersError;
    private List<iListenerProgramacion> ListenersProgramacion;
    
    private clsTrabajo Trabajo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.MostrarHora();
        this.MostrarMinutos();
        this.ListenersError = new ArrayList<>();
        this.ListenersProgramacion = new ArrayList<>();
    }    
    
    public void setTrabajo(clsTrabajo trabajo)
    {Trabajo = trabajo;this.MostrarTrabajo();}
    
    private void MostrarHora()
    {
        this.cbHora.getItems().clear();
        for(int i = 0; i<=23; i++)
        {this.cbHora.getItems().add(String.format("%02d", i));}
    }
    
    private void MostrarTrabajo()
    {
        this.lblPractica.setText(Trabajo.getPractica().getNombreyCodigo());
        this.lblPaciente.setText(Trabajo.getPaciente().getNombreCompleto());
        this.dpFecha.setValue(Trabajo.getProgramacion().getFecha().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate());
        
    }
    
    private void MostrarMinutos()
    {
        this.cbMinutos.getItems().clear();
        for(int i = 0; i<=60; i += 5)
        {this.cbMinutos.getItems().add(String.format("%02d", i));}
    }
    
    @FXML
    private void btnReprogramar_Click(ActionEvent event)
    {
        try
        {
            clsGestorValidacion.ValidarComboBox(cbHora, "Hora de inicio", true);
            clsGestorValidacion.ValidarComboBox(cbMinutos, "Minutos", true);
        
            JOptionPane confirm = new JOptionPane("¿Está seguro que desea reprogramar este trabajo para el día " 
                            + new SimpleDateFormat("dd/MM/yyyy")
                                    .format(Date.from(dpFecha.getValue().atStartOfDay()
                    .atZone(ZoneId.systemDefault()).toInstant())) + " a las " 
                            + new clsHorario(Integer.parseInt(this.cbHora
                                    .getSelectionModel().getSelectedItem().toString()), 
                            Integer.parseInt(this.cbMinutos.getSelectionModel()
                                    .getSelectedItem().toString())).toString(), JOptionPane.QUESTION_MESSAGE, 
            JOptionPane.YES_NO_OPTION);
            JDialog dialog = confirm.createDialog("Reprogramar trabajo");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            if((int)confirm.getValue() == JOptionPane.YES_OPTION){
                dialog.dispose();
                this.Trabajo.Programar(Date.from(dpFecha.getValue().atStartOfDay()
                    .atZone(ZoneId.systemDefault()).toInstant()), new clsHorario(Integer.parseInt(this.cbHora
                            .getSelectionModel().getSelectedItem().toString()), 
                            Integer.parseInt(this.cbMinutos.getSelectionModel()
                                    .getSelectedItem().toString())));
            this.NotificarReprogramacion(Trabajo);
            }
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    public void RaiseError(String error)
    {
        for(iReceptorError xRE : ListenersError)
        {xRE.ErrorOcurred(error);}
    }
    
    public void addListenerProgramacion(iListenerProgramacion listener)
    {this.ListenersProgramacion.add(listener);}
    
    private void NotificarReprogramacion(clsTrabajo trabajo)
    {
        for(iListenerProgramacion xLP : ListenersProgramacion)
        {xLP.TrabajoReprogramado(trabajo);}
    }
}
