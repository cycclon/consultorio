/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Practicas.clsPractica;
import com.PRS.Consultorio.Practicas.clsPracticaLazy;
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
public class ctrlPracticaLazy implements Initializable {
    
    public enum enEstadoPracticaLazy
    {
        Seleccionada,
        Deseleccionada
    }
    
    private abstract class clsEstadoPracticaLazy
    {public abstract enEstadoPracticaLazy getEstado();}
    
    private class clsEPLSeleccionada extends clsEstadoPracticaLazy
    {

        @Override
        public enEstadoPracticaLazy getEstado() 
        {return enEstadoPracticaLazy.Seleccionada;}
    }
    
    private class clsEPLDeseleccionada extends clsEstadoPracticaLazy
    {

        @Override
        public enEstadoPracticaLazy getEstado() 
        {return enEstadoPracticaLazy.Deseleccionada;}
    }

    @FXML private Label lblNombre;
    @FXML private Button btnAccion;
    @FXML private AnchorPane ap;
    
    
    private clsPracticaLazy Practica;
    private List<iReceptorError> ListenersError;
    private List<iListenerPractica> ListenersPractica;
    private List<ctrlPracticaLazy> Resultados;
    private clsEstadoPracticaLazy Estado;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.ListenersError = new ArrayList<>();
        this.ListenersPractica = new ArrayList<>();
    }    
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    public void addListenerPractica(iListenerPractica listener)
    {this.ListenersPractica.add(listener);}
    
    public void setPracticaLazy(clsPracticaLazy practica)
    {
        Practica = practica;
        this.lblNombre.setText(Practica.toString());
    }
    
    public void setResultados(List<ctrlPracticaLazy> resultados)
    {this.Resultados = resultados;}
    
    private void Seleccionar() throws Exception
    {
        this.ap.setStyle("-fx-background-color: #d2ffbe;");
        this.btnAccion.setDisable(true);
        this.Estado = new clsEPLSeleccionada();
        this.RaisePracticaSeleccionada(Practica.getFullPractica());
    }
    
    private void Deseleccionar()
    {
        this.ap.setStyle("-fx-background-color: transparent;");
        this.btnAccion.setDisable(false);
        this.Estado = new clsEPLDeseleccionada();        
    }
    
    private void RaisePracticaSeleccionada(clsPractica practica)
    {
        for(iListenerPractica xLP : ListenersPractica)
            xLP.PracticaSeleccionada(practica);
    }
    
    @FXML
    private void btnAccion_Click(ActionEvent event)
    {
        try
        {
            for(ctrlPracticaLazy xP : Resultados)
            {xP.Deseleccionar();}
            this.Seleccionar();
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }
    
    public void RaiseError(String error)
    {
        for(iReceptorError xRE : ListenersError)
        {xRE.ErrorOcurred(error);}
    }
            
}
