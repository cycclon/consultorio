/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Bitacoras;

import com.PRS.Consultorio.Bitacoras.clsBitacora;
import com.PRS.Consultorio.Bitacoras.clsGestorBitacoras;
import com.PRS.Consultorio.Bitacoras.enTipoBitacora;
import com.PRS.Framework.FormulariosFX.clsGestorCarga;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.clsGestorValidacion;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * FXML Controller class
 *
 * @author cycclon
 */
public class ctrlBitacoras implements Initializable, iMensajeable {
    
    @FXML private Label lblMensaje;

    @FXML private ComboBox cbTiposBitacoras;
    @FXML private ComboBox cbUsuarios;
    
    @FXML private DatePicker dpDesde;
    @FXML private DatePicker dpHasta;
   
    @FXML private ScrollPane sp;
    @FXML private VBox vbBitacoras;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.MostrarTiposBitacoras();
        this.MostrarUsuarios();
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        
        sp.setPrefHeight(bounds.getHeight() - 150);
    }    
    
     private void MostrarUsuarios()
    {
        try
        {
            cbUsuarios.getItems().clear();
            cbUsuarios.getItems().add("Todos");
            List<String> xUs = clsBitacora.ListarUsuarios();

            xUs.stream().forEach((u) -> {
                this.cbUsuarios.getItems().add(u);
            });
            cbUsuarios.getSelectionModel().select(0);
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
        
    }
    
    private void MostrarTiposBitacoras()
    {
        try
        {
            this.cbTiposBitacoras.getItems().clear();
            this.cbTiposBitacoras.getItems().add("Todas");
            for(enTipoBitacora tb : enTipoBitacora.values())cbTiposBitacoras
                    .getItems().add(clsBitacora.getNombrePorTipo(tb));
            
            cbTiposBitacoras.getSelectionModel().select(0);
        }
        catch(Exception ex){clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}        
    }

    @Override
    public Label getCanvas() {return this.lblMensaje;}

    @Override
    public String getNombre() {return "Bitácoras";}
    
    private void MostrarBitacoras()
    {
        try
        {
            clsGestorCarga.getInstancia().startLoading("Cargando registros...");
            List<clsBitacora> xBs;
            if(this.cbTiposBitacoras.getSelectionModel().getSelectedIndex() > 0)
            {
                xBs = clsGestorBitacoras.Listar(Date.from(
                        this.dpDesde.getValue().atStartOfDay().atZone(ZoneId
                                .systemDefault()).toInstant()), Date.from(this
                                        .dpHasta.getValue().atStartOfDay()
                                        .atZone(ZoneId.systemDefault()).toInstant()),
                                this.cbUsuarios.getSelectionModel().getSelectedItem()
                                        .toString(), clsBitacora.getTipoPorNombre(String
                                                .valueOf(cbTiposBitacoras
                                                        .getSelectionModel()
                                                        .getSelectedItem())));
            }
            else
            {
                xBs = clsGestorBitacoras.Listar(Date.from(
                        this.dpDesde.getValue().atStartOfDay().atZone(ZoneId
                                .systemDefault()).toInstant()), Date.from(this
                                        .dpHasta.getValue().atStartOfDay()
                                        .atZone(ZoneId.systemDefault()).toInstant()),
                                this.cbUsuarios.getSelectionModel().getSelectedItem().toString());
            }
            
            this.vbBitacoras.getChildren().clear();
            for(clsBitacora b: xBs)
            {
                FXMLLoader xL = new FXMLLoader(getClass()
                        .getResource("/jfxconsultorio/Bitacoras/fxBitacora.fxml"));
                this.vbBitacoras.getChildren().add(xL.load());
                ctrlBitacora cb = xL.<ctrlBitacora>getController();
                cb.setBitacora(b);
            }
            clsGestorCarga.getInstancia().endLoading();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());
        clsGestorCarga.getInstancia().endLoading();}
    }
    
    @FXML
    private void btnVer_Click(ActionEvent event)
    {
        try
        {
            
            clsGestorValidacion.ValidarDatePicker(dpDesde, "Fecha desde", true);
            clsGestorValidacion.ValidarDatePicker(dpHasta, "Fecha hasta", true);
            this.MostrarBitacoras();
            
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
}
