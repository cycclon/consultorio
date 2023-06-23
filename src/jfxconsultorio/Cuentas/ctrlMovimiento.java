/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Cuentas;

import com.PRS.Consultorio.Cuentas.iMovimientoConsultorio;
import com.PRS.Framework.Cuentas.clsMovimiento;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author ARSPIDALIERI
 */
public class ctrlMovimiento implements Initializable {

    @FXML private Label lblConcepto;
    @FXML private Label lblMonto;
    @FXML private Label lblFecha;
    
    @FXML private ImageView ivIcono;
    @FXML private Tooltip ttConcepto;

    private clsMovimiento Movimiento;
    private List<iReceptorError> ListenersError;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.ListenersError = new ArrayList<>();
    }    
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    private void RaiseError(String error)
    {
        ListenersError.stream().forEach((xRE) -> {
            xRE.ErrorOcurred(error);
        });
    }
    
    public void setMovimiento(clsMovimiento movimiento)
    {this.Movimiento = movimiento; this.MostrarMovimiento();}
    
    private void MostrarMovimiento()
    {
        try
        {
            this.lblConcepto.setText(this.Movimiento.getConcepetoMovimiento());
            this.ttConcepto.setText(this.Movimiento.getConcepetoMovimiento());
            this.lblMonto.setText(this.Movimiento.getValorMovimiento().pdValorString());
            this.lblFecha.setText(new SimpleDateFormat("dd/MM/yyyy")
                    .format(this.Movimiento.getFechaMovimiento()));
            
            String img = "/jfxconsultorio/Imagenes/Iconos/"
                + ((iMovimientoConsultorio)this.Movimiento).getIcono();
            Image image = new Image(img);
            this.ivIcono.setImage(image);
            this.lblMonto.setStyle("-fx-text-fill: " + ((iMovimientoConsultorio)this.Movimiento).getColorMonto());
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }
    
}
