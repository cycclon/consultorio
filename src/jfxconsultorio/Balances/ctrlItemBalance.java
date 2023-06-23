/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Balances;

import com.PRS.Consultorio.Balances.iItemBalance;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

/**
 * FXML Controller class
 *
 * @author cycclon
 */
public class ctrlItemBalance implements Initializable {

    @FXML private Label lblFecha;
    @FXML private Label lblMonto;
    @FXML private Label lblConcepto;
    @FXML private Label lblConceptoAbajo;
    
    @FXML private Tooltip ttConcepto;
    
    private iItemBalance Item;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setItemBalance(iItemBalance item){Item = item; this.MostrarItem();}
    
    private void MostrarItem()
    {
        this.lblConcepto.setText(Item.getConceptoBalance());
        this.lblFecha.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm")
                .format(Item.getFechaBalance()));
        this.lblMonto.setText(Item.getMontoBalance().pdValorString());
        this.ttConcepto.setText(Item.getConceptoBalance());
        this.lblConceptoAbajo.setText(Item.getConceptoAbajo());
    }
    
}
