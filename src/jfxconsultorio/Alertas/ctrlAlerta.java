/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Alertas;

import com.PRS.Consultorio.Alertas.clsAlerta;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

/**
 * FXML Controller class
 *
 * @author ARSPIDALIERI
 */
public class ctrlAlerta implements Initializable {

    @FXML private Label lblMsjAlerta;
    @FXML private Tooltip ttAlerta;
    
    private clsAlerta Alerta;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setAlerta(clsAlerta alerta)
    {Alerta = alerta; this.MostrarAlerta();}
    
    private void MostrarAlerta()
    {this.lblMsjAlerta.setText(Alerta.getAlertaStr());
    this.ttAlerta.setText(Alerta.getAlertaStr());}
    
}
