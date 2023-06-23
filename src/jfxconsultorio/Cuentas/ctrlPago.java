/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Cuentas;

import com.PRS.Consultorio.Cuentas.clsPago;
import com.PRS.Framework.Monedas.clsValor;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ARSPIDALIERI
 */
public class ctrlPago implements Initializable {

    @FXML private Label lblFecha;
    @FXML private TextField txtMonto;
    
    private clsPago Pago;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setPago(clsPago pago)
    {Pago = pago; MostrarPago();}
    
    private void MostrarPago()
    {
        this.lblFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(Pago.getFecha()));
        this.txtMonto.setText(String.valueOf(Pago.getMonto().pdValor()));
    }
    
    public clsPago getPago()
    {
        Pago.setMonto(new clsValor(Float.parseFloat(this.txtMonto.getText())));
        return Pago;
    }
}
