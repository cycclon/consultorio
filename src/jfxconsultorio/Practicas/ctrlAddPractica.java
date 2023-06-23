/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import com.PRS.Consultorio.CentrosCosto.clsCentroCosto;
import com.PRS.Consultorio.ObrasSociales.clsOS;
import com.PRS.Consultorio.Practicas.clsPractica;
import com.PRS.Framework.FormulariosFX.clsGestorCarga;
import com.PRS.Framework.FormulariosFX.clsGestorValidacion;
import com.PRS.Framework.FormulariosFX.enTipoTextField;
import com.PRS.Framework.FormulariosFX.iRegistryListener;
import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;


/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlAddPractica implements Initializable, iReceptorError  {
    
    @FXML private TextField txtNombre;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtHoras;
    @FXML private TextField txtMinutos;
    
    @FXML private VBox vbOSs;
    
    @FXML private ComboBox cmbCCs;
    
    @FXML private ScrollPane spValores;
    
    private List<iReceptorError> ListenersError;
    
    private List<iRegistryListener> ListenersMensajes;
    
    private List<ctrlAddCostoOS> Controllers;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try
        {
            this.ListenersError = new ArrayList<>();
            this.ListenersMensajes = new ArrayList<>();
            this.Controllers = new ArrayList<>();
            this.MostrarCentrosCosto();
            this.MostrarOSs();
        }
        catch(Exception ex){RaiseError(ex.getMessage());}
    }    

    @FXML
    private void btnAceptar_Click(ActionEvent event)
    {
        try
        {
            clsGestorValidacion.ValidarTextField(txtNombre, "Nombre", true, enTipoTextField.Texto);
            clsGestorValidacion.ValidarTextField(txtCodigo, "Código", true, enTipoTextField.Numerico);
            clsGestorValidacion.ValidarTextField(txtHoras, "Horas", true, enTipoTextField.Numerico);
            clsGestorValidacion.ValidarTextField(txtMinutos, "Minutos", true, enTipoTextField.Numerico);
            clsGestorValidacion.ValidarComboBox(cmbCCs, "Centro de costos", true);
            
            clsGestorCarga.getInstancia().startLoading("Registrando práctica...");
            
            clsPractica xP = new clsPractica(Integer.parseInt(txtCodigo.getText()), 
                    this.txtNombre.getText(), Integer.parseInt(txtHoras.getText()), 
                    Integer.parseInt(this.txtMinutos.getText()), 
                    (clsCentroCosto) cmbCCs.getSelectionModel().getSelectedItem());
            
            xP.Registrar();
            
            for(ctrlAddCostoOS xCOS : Controllers)
            {xP.AgregarCosto(xCOS.getValorPractica());}
            
            this.RaiseMensaje("Se registró correctamente la nueva práctica.");
            
            this.LimpiarFormulario();
            clsGestorCarga.getInstancia().endLoading();
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());clsGestorCarga.getInstancia().endLoading();}
    }
    
    private void LimpiarFormulario() throws Exception
    {
        this.txtCodigo.setText("");
        this.txtHoras.setText("");
        this.txtMinutos.setText("");
        this.txtNombre.setText("");
        this.MostrarCentrosCosto();
        this.MostrarOSs();
        
    }
    
    private void MostrarCentrosCosto() throws Exception
    {
        this.cmbCCs.getItems().clear();
        List<clsCentroCosto> xCCs = clsCentroCosto.Listar(false);
        
        for (clsCentroCosto xCC : xCCs) {
            this.cmbCCs.getItems().add(xCC);
        }
    }
    
    private void MostrarOSs() throws Exception
    {
        List<clsOS> xOSs = clsOS.Listar(true);
        xOSs.add(0, new clsOS());
        this.vbOSs.getChildren().clear();
        
        for (clsOS xOS : xOSs) {
            FXMLLoader xL = new FXMLLoader(getClass().
                    getResource("/jfxconsultorio/Practicas/fxAddCostoOS.fxml"));
            vbOSs.getChildren().add(xL.load());
            ctrlAddCostoOS xACOS = xL.<ctrlAddCostoOS>getController();
            xACOS.addListenerError(this);
            xACOS.setOS(xOS);
            Controllers.add(xACOS);
        }        
    }
    
    public void setSPWidth(double width){this.spValores.setPrefWidth(width);}
    
    void addListenerError(iReceptorError Listener)
    {this.ListenersError.add(Listener);}
    
    private void RaiseError(String prError)
    {
        for (iReceptorError ListenersError1 : this.ListenersError) {
            ListenersError1.ErrorOcurred(prError);
        }
    }
    
    void addListenerMensaje(iRegistryListener Listener)
    {ListenersMensajes.add(Listener);}
    
    private void RaiseMensaje(String prMensaje)
    {
        ListenersMensajes.stream().forEach((xM) -> {
            xM.RegistryUpdated(prMensaje);
        });
    }

    @Override
    public void ErrorOcurred(String prMensaje) {RaiseError(prMensaje);}
}
