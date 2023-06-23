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
import com.PRS.Framework.Monedas.clsValor;
import java.net.URL;
import java.util.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlAddCostoOS implements Initializable {

    @FXML private Label lblOS;
    
    @FXML private TextField txtCostoOS;
    @FXML private TextField txtCoseguro;
    
    @FXML private DatePicker dpVigencia;
    
    private clsOS OS;
    
    private clsValorPractica xVP;
    
    private List<iReceptorError> ListenersError;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.dpVigencia.setValue(LocalDate.now());
        this.xVP = new clsValorPractica();
        this.ListenersError = new ArrayList<>();
    }
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    public void RaiseError(String error)
    {
        ListenersError.stream().forEach((xRE) -> {
            xRE.ErrorOcurred(error);
        });
    }
    
    public void setOS(clsOS newVal)
    {
        OS = newVal;
        this.lblOS.setText(OS.getNombreConCodigo());              
        this.txtCostoOS.setDisable(OS.getID() == 0);
    }
    
    public void setValorPractica(clsValorPractica valor)
    {
        this.setOS(valor.getOS());
        this.xVP = valor;
        this.txtCoseguro.setText(String.valueOf(valor.getCoseguro().pdValor()));
        this.txtCostoOS.setText(String.valueOf(valor.getCostoOS().pdValor()));
        
        Instant instant = valor.getVigencia().toInstant();
        LocalDate xLD = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        this.dpVigencia.setValue(xLD);
        
    }
    
    public clsValorPractica getValorPractica() throws Exception
    {
        try
        {
            clsGestorValidacion.ValidarTextField(txtCostoOS, "Costo de obra social", true, enTipoTextField.Decimal);
            clsGestorValidacion.ValidarTextField(txtCoseguro, "Costo de coseguro", true, enTipoTextField.Decimal);
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
        if(this.xVP.getID() != 0){
            xVP.setCoseguro(new clsValor(Float.parseFloat(this.txtCoseguro.getText())));
            xVP.setCostoOS(new clsValor(Float.parseFloat(this.txtCostoOS.getText())));
            xVP.setVigencia(Date.from(this.dpVigencia.getValue().
                        atStartOfDay(ZoneId.systemDefault()).toInstant()));
            return xVP;
        }
        else
        {
            return new clsValorPractica(this.OS, Float.parseFloat(this.txtCostoOS.getText()), 
                    Float.parseFloat(this.txtCoseguro.getText()), 
                    Date.from(this.dpVigencia.getValue().
                            atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        
        
    }
    
    public void LimpiarFormulario()
    {
        this.txtCoseguro.setText("0");
        this.txtCostoOS.setText("0");
    }
}
