/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jfxconsultorio.Controles;

import com.PRS.Framework.FormulariosFX.clsGestorCarga;
import com.PRS.Framework.FormulariosFX.clsGestorNavegacion;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlOpcion implements Initializable {

    @FXML private ImageView imgIcono;
    
    @FXML private Label lblNombre;
    @FXML private Label lblNotificaciones;
    
    @FXML private Tooltip tlNombre;
    @FXML private Tooltip tlNotificaciones;
    @FXML private Tooltip tlNombrebtn;
    
    private String Formulario;
    private Object Parametro;
    private String archivoImagen;
    
    public void setIcono(String prFileName) throws IOException
    {        
        String img = "/jfxconsultorio/Imagenes/Iconos/"
            + prFileName;
        Image image = new Image(img);
        this.imgIcono.setImage(image);
        this.archivoImagen = prFileName;
        
    }
    public void setNombre(String Nombre)
    {this.lblNombre.setText(Nombre);this.tlNombre.setText(Nombre);
    this.tlNombrebtn.setText(Nombre);}
    
    public void setNotificaciones(byte Cantidad)
    {
        byte xC;
        if(Cantidad > 99){xC = 99;}
        else{xC = Cantidad;}
        
        this.lblNotificaciones.setText(String.valueOf(xC));
        this.tlNotificaciones.setText("Tiene " + String.valueOf(xC) + 
                " notificaciones sin revisar.");
        this.lblNotificaciones.setVisible(xC > 0);
    }
    
    public void setFormulario(String prFormulario){Formulario = prFormulario;}
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.Formulario = "/jfxconsultorio/fxInicio.fxml";
        this.archivoImagen = "";
    }
    
    private void Abrir() throws Exception
    {
        clsGestorCarga.getInstancia().startLoading("Abriendo: " + this.lblNombre.getText());
        if(Parametro == null){
            clsGestorNavegacion.Instanciar().Abrir(this.Formulario, 
                            this.lblNombre.getText(), this.archivoImagen);
        }
        else
        {
            clsGestorNavegacion.Instanciar().Abrir(this.Formulario, 
                            this.lblNombre.getText(), this.archivoImagen, Parametro);
        }
        clsGestorCarga.getInstancia().endLoading();
    }
    
    void setParametro(Object parametro)
    {Parametro = parametro;}
    
    @FXML
    private void btnOpcion_Click(ActionEvent event)
    {
        try
        {                        
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    try
                    {
                        Abrir();                        
                    }
                    catch(Exception ex)
                    {}                    
                }
            });            
        }
        catch(Exception ex)
        {System.out.println(ex.getMessage());}
        
    }
    
}
