/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Practicas.clsTrabajo;
import com.PRS.Consultorio.Profesionales.clsProfesional;
import com.PRS.Consultorio.Profesionales.clsProfesionalLazy;
import com.PRS.Framework.FormulariosFX.clsGestorValidacion;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlRealizar implements Initializable {
    
    public interface iListenerRealizacion
    {public void TrabajoRealizado(clsTrabajo trabajo);}

    @FXML private Label lblTrabajo;
    @FXML private Label lblPaciente;
    @FXML private Label lblDia;
    @FXML private Label lblHorario;
    @FXML private Label lblAclaracion;
    
    @FXML private TextField txtObservaciones;
    
    @FXML private ComboBox cbRealizador;
    
    private List<iListenerRealizacion> ListenersRealizacion;
    private List<iReceptorError> ListenersError;
    private clsTrabajo Trabajo;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.ListenersError = new ArrayList<>();
        this.ListenersRealizacion = new ArrayList<>();
        try{this.MostrarRealizadores();}catch(Exception ex){RaiseError(ex.getMessage());}
    }    
    
    public void setTrabajo(clsTrabajo trabajo)
    {this.Trabajo = trabajo; this.MostrarTrabajo();}
    
    private void MostrarTrabajo()
    {
        this.lblTrabajo.setText(Trabajo.getPractica().getNombreyCodigo());
        this.lblPaciente.setText(Trabajo.getPaciente().getNombreCompleto());
        this.lblDia.setText(new SimpleDateFormat("dd/MM/yyyy")
                .format(Trabajo.getProgramacion().getFecha()));
        this.lblHorario.setText(Trabajo.getProgramacion().getHorario().getHoraStr());
        if(Trabajo.getRealizacion().getID() > 0 )
        {
            if(Trabajo.getRealizacion().getRealizador().getActivo())
            {
                for(Object xPL : this.cbRealizador.getItems())
                {
                    if(((clsProfesionalLazy)xPL).getID() == this.Trabajo
                            .getRealizacion().getRealizador().getID())
                    {this.cbRealizador.getSelectionModel().select(xPL);}
                }
            
            }
            else{this.lblAclaracion.setText("La persona que llevó a cabo este "
                    + "trabajo, fue eliminada del sistema");}
        }
    }
    
    private void MostrarRealizadores() throws Exception
    {
        this.cbRealizador.getItems().clear();
        List<clsProfesionalLazy> xRs = clsProfesional.ListarEncargados(true);
        
        for(clsProfesionalLazy xPL : xRs)
        {this.cbRealizador.getItems().add(xPL);}
    }
    
    public void addListenerRealizacion(iListenerRealizacion listener)
    {this.ListenersRealizacion.add(listener);}
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    private void RaiseError(String error)
    {
        for(iReceptorError xRE : ListenersError)
        {xRE.ErrorOcurred(error);}
    }
    
    private void NotificarRealizacion(clsTrabajo trabajo)
    {
        for(iListenerRealizacion xLR : ListenersRealizacion)
        {xLR.TrabajoRealizado(trabajo);}
    }
    
    @FXML
    private void btnRealizar_Click(ActionEvent event)
    {
        try
        {
            clsGestorValidacion.ValidarComboBox(cbRealizador, "Realizador", true);
            Trabajo.Realizar(((clsProfesionalLazy)this.cbRealizador
                    .getSelectionModel().getSelectedItem()).getFullProfesional(),
                    new Date(), this.txtObservaciones.getText());
            NotificarRealizacion(Trabajo);            
        }
        catch(Exception ex){RaiseError(ex.getMessage());}
    }
}
