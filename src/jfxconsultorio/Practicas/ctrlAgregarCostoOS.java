/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.ObrasSociales.clsOS;
import com.PRS.Consultorio.Practicas.clsValorPractica;
import com.PRS.Framework.FormulariosFX.clsGestorValidacion;
import com.PRS.Framework.FormulariosFX.enTipoTextField;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.net.URL;
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
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlAgregarCostoOS implements Initializable {

    @FXML private ComboBox cbOS;
    
    @FXML private TextField txtCostoOS;
    @FXML private TextField txtCoseguro;
    
    @FXML private DatePicker dpVigencia;
    
    private List<iReceptorError> ListenersError;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try
        {this.MostrarOSs();}
        catch(Exception ex)
        {}
        this.ListenersError = new ArrayList<>();
    }
    
    public void addListenerError(iReceptorError listener)
    {ListenersError.add(listener);}
    
    public void RaiseError(String error)
    {
        ListenersError.stream().forEach((xRE) -> {
            xRE.ErrorOcurred(error);
        });
    }
    
    private void MostrarOSs() throws Exception
    {
        List<clsOS> xOSs = clsOS.Listar(true);
        xOSs.add(0, new clsOS());
        this.cbOS.getItems().clear();
        xOSs.stream().forEach((xOS) -> {
            this.cbOS.getItems().add(xOS);
        });
    }
    
    public clsValorPractica getValorPractica()
    {
        try
        {
            clsGestorValidacion.ValidarComboBox(cbOS, "Obra social / particular", true);
            clsGestorValidacion.ValidarTextField(txtCostoOS, "Costo de obra social", 
                    true, enTipoTextField.Decimal);
            clsGestorValidacion.ValidarTextField(txtCoseguro, "Costo de coseguro", 
                    true, enTipoTextField.Decimal);
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
        return new clsValorPractica((clsOS)this.cbOS.getSelectionModel().
                getSelectedItem(), Float.parseFloat(this.txtCostoOS.getText()), 
                    Float.parseFloat(this.txtCoseguro.getText()), 
                    Date.from(this.dpVigencia.getValue().
                            atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
    
    @FXML
    private void cbOS_SelectedItemChanged(ActionEvent event)
    {
        this.txtCostoOS.setDisable(((clsOS)this.cbOS.getSelectionModel()
                .getSelectedItem()).getID() == 0);
        
    }
    
    public void LimpiarFormulario()
    {
        this.txtCoseguro.setText("0");
        this.txtCostoOS.setText("0");
    }
}
