/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Practicas.clsValorCampo;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author ARSPIDALIERI
 */
public class ctrlValorCampo implements Initializable {
    
    @FXML private Label lblNombre;
    @FXML private TextField txtValor;
    @FXML private ImageView ivIcono;
    @FXML private Tooltip ttIcono;
    
    private clsValorCampo Valor;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setValor(clsValorCampo valor)
    {this.Valor = valor;MostrarValorCampo();}
    
    private void MostrarValorCampo()
    {
        try
        {
            this.lblNombre.setText(Valor.getCampo().getNombre() + ":");
            this.ttIcono.setText(this.Valor.getEstadoStr());
            this.ivIcono.setVisible(this.Valor.getCampo().isObligatorio());
            
            String img = "/jfxconsultorio/Imagenes/Iconos/" + this.Valor.getIcono();         
            this.ivIcono.setImage(new Image(img));
            this.txtValor.setText(this.Valor.getValor());
        }
        catch(Exception ex){}
    }
    
    public clsValorCampo getValorCampo()
    {this.Valor.setValor(this.txtValor.getText()); return this.Valor;}
}
