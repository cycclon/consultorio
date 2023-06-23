/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jfxconsultorio;

import com.PRS.Framework.FormulariosFX.clsGestorCarga;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlInicio implements Initializable, iMensajeable {
    
    @FXML Label lblmensaje;
    
    @FXML HBox hb;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try
        {
            clsGestorCarga.getInstancia().startLoading("Cargando menú principal...");
            FXMLLoader xL1 = new FXMLLoader(getClass()
             .getResource("/jfxconsultorio/Controles/fxMenu.fxml"));
            hb.getChildren().add(xL1.load());  
            clsGestorCarga.getInstancia().endLoading();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());clsGestorCarga.getInstancia().endLoading();}
    }    

    @Override
    public Label getCanvas() {
        return this.lblmensaje;
    }

    @Override
    public String getNombre() {return "Menú principal";}
    
}
