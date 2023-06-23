/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Practicas.clsTrabajo;
import com.PRS.Consultorio.Profesionales.clsFirmante;
import com.PRS.Consultorio.Profesionales.clsProfesional;
import com.PRS.Consultorio.Profesionales.clsProfesionalLazy;
import com.PRS.Consultorio.Profesionales.enTipoProfesional;
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

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlFirmar implements Initializable {

    public interface iListenerFirma
    {public void TrabajoFirmado(clsTrabajo trabajo);}

    @FXML private Label lblTrabajo;
    @FXML private Label lblPaciente;
    @FXML private Label lblDia;
    @FXML private Label lblHorario;  
    @FXML private Label lblRealizador;
   
    @FXML private ComboBox cbFirmante;
    
    private List<iListenerFirma> ListenersFirma;
    private List<iReceptorError> ListenersError;
    private clsTrabajo Trabajo;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.ListenersError = new ArrayList<>();
        this.ListenersFirma = new ArrayList<>();
        try{this.MostrarFirmantes();}catch(Exception ex)
        {RaiseError(ex.getMessage());}
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
        this.lblRealizador.setText(Trabajo.getRealizacion().getRealizador()
                .getNombreCompleto());
        if(Trabajo.getFirma().getID() > 0)
        {
            for(Object xPL : this.cbFirmante.getItems())
            {
                if(Trabajo.getFirma().getFirmante().getID() ==
                        ((clsProfesionalLazy)xPL).getID())
                {this.cbFirmante.getSelectionModel().select(xPL);}
            }
            
        }
    }   
    
    public void MostrarFirmantes() throws Exception
    {
        this.cbFirmante.getItems().clear();
        List<clsProfesionalLazy> xPs = clsProfesional.Listar(true, 
                enTipoProfesional.Firmante);
        for(clsProfesionalLazy xP : xPs)
        {this.cbFirmante.getItems().add(xP);}
    }
    
    public void addListenerFirma(iListenerFirma listener)
    {this.ListenersFirma.add(listener);}
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    private void RaiseError(String error)
    {
        for(iReceptorError xRE : ListenersError)
        {xRE.ErrorOcurred(error);}
    }
    
    private void NotificarFirma(clsTrabajo trabajo)
    {
        for(iListenerFirma xLF : ListenersFirma)
        {xLF.TrabajoFirmado(trabajo);}
    }
    
    @FXML
    private void btnFirmar_Click(ActionEvent event)
    {
        try
        {
            clsGestorValidacion.ValidarComboBox(cbFirmante, "Firmante", true);
            Trabajo.Firmar(new Date(), (clsFirmante)((clsProfesionalLazy)this
                    .cbFirmante.getSelectionModel().getSelectedItem())
                    .getFullProfesional());
            this.NotificarFirma(Trabajo);
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }
}
