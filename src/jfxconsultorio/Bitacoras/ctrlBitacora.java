/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Bitacoras;

import com.PRS.Consultorio.Bitacoras.clsBitacora;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author cycclon
 */
public class ctrlBitacora implements Initializable {


    private clsBitacora Bitacora;
    
    @FXML private Label lblFecha;
    @FXML private Label lblAccion;
    @FXML private Label lblUsuario;
    @FXML private Label lblIP;
    @FXML private Label lblWorkCenter;
    @FXML private Label lblTipo;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setBitacora(clsBitacora bitacora)
    {this.Bitacora = bitacora; this.MostrarBitacora();}
    
    private void MostrarBitacora()
    {
        this.lblAccion.setText(Bitacora.getAccion());
        this.lblFecha.setText(new SimpleDateFormat("dd/MM/yyyy hh:mm")
                .format(Bitacora.getFecha()));
        this.lblUsuario.setText(Bitacora.getUsuario());
        this.lblIP.setText(Bitacora.getIP());
        this.lblWorkCenter.setText(Bitacora.getWorkCenter());
        this.lblTipo.setText(Bitacora.getNombre());
    }
   
}
