/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Colegio;

import com.PRS.Consultorio.Cuentas.clsCobroOS;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

/**
 * FXML Controller class
 *
 * @author cycclon
 */
public class ctrlCobroPendiente implements Initializable {

    @FXML private CheckBox chSeleccion;
    @FXML private Label lblConcepto;
    @FXML private Label lblMonto;
    @FXML private Tooltip ttConcepto;
    
    private clsCobroOS Cobro;
    
    public void setCobro(clsCobroOS cobro){this.Cobro = cobro; this.MostrarCobro();}
    public clsCobroOS getCobro(){return this.Cobro;}
    
    private void MostrarCobro()
    {
        this.lblConcepto.setText(Cobro.getConceptoStr());
        this.lblMonto.setText(Cobro.getRestante().pdValorString());
        this.ttConcepto.setText(Cobro.getConceptoStr());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public boolean isSeleccionado(){return this.chSeleccion.isSelected();}
    
    public void ToogleSelection(boolean toogle)
    {this.chSeleccion.setSelected(toogle);}
}
