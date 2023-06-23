/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Informes;

import com.PRS.Consultorio.Informes.clsGestorInformes;
import com.PRS.Consultorio.Informes.iComponenteInforme;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.net.URL;
import java.util.ArrayList;
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
public class ctrlComponenteInforme implements Initializable {
    
    public interface iListenerComponenteInforme
    {public void ComponenteSeleccionado(iComponenteInforme componente);}

    @FXML private ComboBox cbComponente;
    
    @FXML private TextField txtCodigo;
    
    @FXML private Label lblCodigo;
    
    private List<iReceptorError> ListenersError;
    private List<iListenerComponenteInforme> ListenersComponente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ListenersError = new ArrayList<>();
        ListenersComponente = new ArrayList<>();
        this.MostrarComponentes();
    }    
    
    private void MostrarComponentes()
    {
        try
        {
            List<iComponenteInforme> xCs = clsGestorInformes.Instanciar()
                    .getComponentesDisponibles();
            this.cbComponente.getItems().clear();
            
            xCs.stream().forEach((xCI) -> {
                this.cbComponente.getItems().add(xCI);
            });
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    public void addListenerCI(iListenerComponenteInforme listener)
    {this.ListenersComponente.add(listener);}
    
    private void RaiseError(String error)
    {
        ListenersError.stream().forEach((xRE) -> {
            xRE.ErrorOcurred(error);
        });
    }
    
    private void NotificarSeleccion(iComponenteInforme componente)
    {
        ListenersComponente.stream().forEach((xLC) -> {
            xLC.ComponenteSeleccionado(componente);
        });
    }
    
    @FXML
    private void cbComponente_SelectedItemChanged(ActionEvent event)
    {
        try
        {if(!cbComponente.getSelectionModel().isEmpty())
        {NotificarSeleccion((iComponenteInforme)this.cbComponente
                .getSelectionModel().getSelectedItem());
        this.txtCodigo.setText(((iComponenteInforme)this.cbComponente
                .getSelectionModel().getSelectedItem()).getCodigo());
        this.txtCodigo.setVisible(!cbComponente.getSelectionModel().isEmpty());
        this.lblCodigo.setVisible(!cbComponente.getSelectionModel().isEmpty());
        }
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}        
    }
}