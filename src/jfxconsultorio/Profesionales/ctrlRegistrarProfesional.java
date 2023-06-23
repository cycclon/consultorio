/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jfxconsultorio.Profesionales;

import com.PRS.Consultorio.Profesionales.clsProfesional;
import com.PRS.Consultorio.Profesionales.enTipoProfesional;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.clsGestorValidacion;
import com.PRS.Framework.FormulariosFX.enTipoTextField;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import com.PRS.Framework.FormulariosFX.iRegistryListener;
import com.PRS.Framework.Identificacion.clsIdentificacion;
import com.PRS.Framework.Identificacion.enTipoIdentificacion;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxconsultorio.Usuarios.ctrlRegistrarUsuario;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlRegistrarProfesional implements Initializable, iMensajeable, iReceptorError {

    @Override
    public void ErrorOcurred(String prMensaje) 
    {clsGestorMensajes.Instanciar().MostrarError(this, prMensaje);}
    
    public interface iListenerProfesional
    {public void ProfesionalRegistrado(clsProfesional xP);}
    
    @FXML private ComboBox cmbTipoPro;
    @FXML private ComboBox cmbTipoDocumento;
    
    @FXML private TextField txtTitulo;
    @FXML private TextField txtAbreviatura;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtNroDocumento;
    
    @FXML private Label lblMensaje;
    
    @FXML private Tooltip ttTipoProfesional;
    @FXML private Tooltip ttTipoDocumento;
    
    @FXML private VBox vbUsuario;
    
    private FXMLLoader xL;
    private ctrlRegistrarUsuario xRU;
    private boolean CerrarAlRegistrar;
    private List<iListenerProfesional> ListenersProfesional;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.RegistryListeners = new ArrayList<>();
        this.MostrarTiposDocumento();
        this.MostrarTiposProfesionales();
        this.MostrarRegistroUsuario();
        CerrarAlRegistrar = false;
        this.ListenersProfesional = new ArrayList<>();          
    }    
    
    @Override
    public String getNombre() {return "Registrar profesional";}
    
    private void MostrarRegistroUsuario()
    {
        vbUsuario.getChildren().clear();
        xL = new FXMLLoader(getClass().
                getResource("/jfxconsultorio/Usuarios/fxRegistrarUsuario.fxml"));
        
        try
        {
            this.vbUsuario.getChildren().add(xL.load());
            xRU = xL.<ctrlRegistrarUsuario>getController();
            xRU.addListenerError(this);
        }
        catch(IOException ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}      
    }
    
    private void MostrarTiposDocumento()
    {
        this.cmbTipoDocumento.getItems().clear();
        this.ttTipoDocumento.setText("Seleccionar un tipo de documento: \r");
        for(enTipoIdentificacion xTD : enTipoIdentificacion.values())
        {
            this.cmbTipoDocumento.getItems().add(xTD.toString());
            this.ttTipoDocumento.setText(this.ttTipoDocumento.getText() + 
                    " " + xTD.toString() + ";");
        }
        this.cmbTipoDocumento.getSelectionModel().select("DNI");
    }
    
    private void MostrarTiposProfesionales()
    {
        this.cmbTipoPro.getItems().clear();
        this.ttTipoProfesional.setText("Seleccionar un tipo de profesional: \r");
        
        for(enTipoProfesional xTP : enTipoProfesional.values())
        {
            this.cmbTipoPro.getItems().add(xTP.toString());
            this.ttTipoProfesional.setText(this.ttTipoProfesional.getText() + 
                    " " + xTP.toString() + ";");
        }        
    }
    
    @FXML
    private void btnAceptar_Click(ActionEvent event)
    {
        try
        {
            clsGestorValidacion.ValidarComboBox(cmbTipoPro, "Tipo de profesional", true);
            clsGestorValidacion.ValidarTextField(txtTitulo, "Título", true, enTipoTextField.Texto);
            clsGestorValidacion.ValidarTextField(txtAbreviatura, "Abreviatura", true, enTipoTextField.Texto);
            clsGestorValidacion.ValidarTextField(txtNombres, "Nombres", true, enTipoTextField.Texto);
            clsGestorValidacion.ValidarTextField(txtApellidos, "Apellidos", true, enTipoTextField.Texto);
            
            if(enTipoProfesional.valueOf(
                cmbTipoPro.getSelectionModel().getSelectedItem()
                        .toString())!= enTipoProfesional.Solicitante)
            {
                clsGestorValidacion.ValidarComboBox(cmbTipoDocumento, 
                        "Tipo de documento", true);
                clsGestorValidacion.ValidarTextField(txtNroDocumento, 
                        "Número de documento", true, enTipoTextField.Numerico);
            }
            clsProfesional xP = clsProfesional.Fabricar(
                    enTipoProfesional.valueOf(
                            this.cmbTipoPro.getSelectionModel().
                                    getSelectedItem().toString()));
            
            xP.setAbreviatura(this.txtAbreviatura.getText());
            xP.setApellido(this.txtApellidos.getText());
            xP.setNombre(this.txtNombres.getText());
            xP.setTitulo(this.txtTitulo.getText());
            
            if(enTipoProfesional.valueOf(
                cmbTipoPro.getSelectionModel().getSelectedItem()
                        .toString())!= enTipoProfesional.Solicitante)
            {
                xP.setDocumento(clsIdentificacion.Instanciar(
                    enTipoIdentificacion.valueOf(this.cmbTipoDocumento.
                            getSelectionModel().getSelectedItem().toString()), 
                    Long.parseLong( this.txtNroDocumento.getText())));
                ctrlRegistrarUsuario xCRU = xL.<ctrlRegistrarUsuario>getController();
                xP.setUsuario(xCRU.getUsuario());
            }     
            
            
            
            xP.Registrar();
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                    "Se registró el nuevo profesional: " + xP.getNombreCompleto());
            
            this.NotificarCambio("Se registró un nuevo profesional");
            this.NotificarRegistro(xP);
            
            if(this.CerrarAlRegistrar)
            {
                Stage stage = (Stage) this.txtApellidos.getScene().getWindow();
                stage.close();
            }
            else
            {this.LimpiarFormulario();}
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
    private void LimpiarFormulario()
    {
        this.txtAbreviatura.setText("");
        this.txtApellidos.setText("");
        this.txtNombres.setText("");
        this.txtNroDocumento.setText("");
        this.txtTitulo.setText("");
        this.MostrarTiposDocumento();
        
        xRU.LimpiarFormulario();
    }

    @Override
    public Label getCanvas() {return this.lblMensaje;}
    
    private List<iRegistryListener> RegistryListeners;
    
    public void addRegistryListerner(iRegistryListener prListener)
    {
        if(!this.checkUpdateListener(prListener))
        {this.RegistryListeners.add(prListener);}
    }
    
    private boolean checkUpdateListener(iRegistryListener prListener)
    {
        boolean xF = false;
        for(int i= 0; i<this.RegistryListeners.size(); i++)
        {
            if(RegistryListeners.get(i).getIDListerner() == 
                    prListener.getIDListerner())
            {xF = true; break;}
        }
        
        return xF;
    }
    
    private void NotificarCambio(String prMensaje)
    {
        for (iRegistryListener RegistryListener : this.RegistryListeners) {
            RegistryListener.RegistryUpdated(prMensaje);
        }
    }
    
    public void setCerrarAlRegistrar(boolean activo)
    {this.CerrarAlRegistrar = activo;}
    
    public void setTipo(enTipoProfesional tipo)
    {
        this.cmbTipoPro.getSelectionModel().select(tipo.toString());
        this.cmbTipoPro.setDisable(tipo == enTipoProfesional.Solicitante);
    }
    
    public void addListenerProfesional(iListenerProfesional listener)
    {this.ListenersProfesional.add(listener);}
    
    private void NotificarRegistro(clsProfesional profesional)
    {
        for(iListenerProfesional xLP : ListenersProfesional)
        {xLP.ProfesionalRegistrado(profesional);}
    }
    
    @FXML
    private void cmbTipoPro_SelectedItemChanged(ActionEvent event)
    {
        this.txtNroDocumento.setText("");
        this.txtNroDocumento.setVisible(enTipoProfesional.valueOf(
                cmbTipoPro.getSelectionModel().getSelectedItem()
                        .toString())!= enTipoProfesional.Solicitante);
        
        this.cmbTipoDocumento.setVisible(enTipoProfesional.valueOf(
                cmbTipoPro.getSelectionModel().getSelectedItem()
                        .toString())!= enTipoProfesional.Solicitante);
        if(enTipoProfesional.valueOf(
                cmbTipoPro.getSelectionModel().getSelectedItem()
                        .toString())== enTipoProfesional.Solicitante)
        {this.vbUsuario.getChildren().clear();}
        else
        {this.MostrarRegistroUsuario();}
    }
}
