/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Gastos;

import com.PRS.Consultorio.Gastos.clsGasto;
import com.PRS.Consultorio.Usuarios.clsGestorUsuariosConsultorio;
import com.PRS.Framework.Acceso.clsGestorAcceso;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author ARSPIDALIERI
 */
public class ctrlGasto implements Initializable {

    @FXML private DatePicker dpFecha;
    @FXML private Label lblConcepto;
    @FXML private TextField txtMonto;
    @FXML private Tooltip ttConcepto;
    @FXML private Button btnRegistrar;
    @FXML private Button btnDelteAll;
    @FXML private Button btnDelete;
    @FXML private Label lblFrecuencia;
    
    private clsGasto Gasto;    
    private List<iReceptorError> ListenersError;
    private List<iGastos> ListenersGasto;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.ListenersError = new ArrayList<>();
        this.ListenersGasto = new ArrayList<>();
    }    
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    public void addListenerGastos(iGastos listener)
    {this.ListenersGasto.add(listener);}
    
    private void RaiseError(String error)
    {
        ListenersError.stream().forEach((xRE) -> {
            xRE.ErrorOcurred(error);
        });
    }
    
    private void NotificarRegistro()
    {
        ListenersGasto.stream().forEach((xG) -> {
            xG.GastoRegistrado(Gasto);
        });
    }
    
    public void btnRegistrar_Click(ActionEvent event)
    {
        try
        {
            JOptionPane confirm = new JOptionPane("¿Está seguro que desea registrar este gasto?", JOptionPane.QUESTION_MESSAGE, 
            JOptionPane.YES_NO_OPTION);
            JDialog dialog = confirm.createDialog("Evectivizar gasto");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            if((int)confirm.getValue() == JOptionPane.YES_OPTION) {
                dialog.dispose();
                this.Gasto.Efectivizar(Date.from(this.dpFecha.getValue().atStartOfDay()
                    .atZone(ZoneId.systemDefault()).toInstant()), 
                    Float.valueOf(this.txtMonto.getText()));
                this.NotificarRegistro();
            }           
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }
    
    public void setGasto(clsGasto gasto)
    {Gasto = gasto; this.MostrarGasto();}
    
    private void MostrarGasto()
    {
        try
        {
           this.dpFecha.setValue(Gasto.getFecha().toInstant().atZone(ZoneId
                   .systemDefault()).toLocalDate());
           this.lblConcepto.setText(Gasto.getConcepto());
           this.txtMonto.setText(String.valueOf(Gasto.getMonto().pdValor()));
           this.ttConcepto.setText(Gasto.getConcepto());
           this.lblFrecuencia.setText(Gasto.getFrecuenciaStr());

           this.dpFecha.setDisable(Gasto.isEfectivo());
           this.txtMonto.setDisable(Gasto.isEfectivo());
           
           this.btnRegistrar.setVisible(!Gasto.isEfectivo()&&clsGestorAcceso.Instanciar().ComprobarPermisos
                   (clsGestorUsuariosConsultorio.Instanciar().ObtenerAccesor(), (short)25));
           this.btnDelteAll.setVisible(Gasto.isProgramado()&&clsGestorAcceso.Instanciar().ComprobarPermisos
                   (clsGestorUsuariosConsultorio.Instanciar().ObtenerAccesor(), (short)24));
           this.btnDelete.setVisible(!Gasto.isPlantilla()&&clsGestorAcceso.Instanciar().ComprobarPermisos
                   (clsGestorUsuariosConsultorio.Instanciar().ObtenerAccesor(), (short)24)); 
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
              
    }
    
    public void btnDelete_Click(ActionEvent event)
    {
        try
        {
            JOptionPane confirm = new JOptionPane("¿Está seguro que desea eliminar este gasto?", JOptionPane.QUESTION_MESSAGE, 
            JOptionPane.YES_NO_OPTION);
            JDialog dialog = confirm.createDialog("Eliminar gasto");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            if((int)confirm.getValue() == JOptionPane.YES_OPTION) {
                dialog.dispose();                
                Gasto.Delete();
                RaiseGastoEliminado();
            }
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }
    
    public void btnDeleteAll_Click(ActionEvent event)
    {
        try
        {
            JOptionPane confirm = new JOptionPane("¿Está seguro que desea eliminar este gasto y todas las "
                            + "ocurrencias programadas?", JOptionPane.QUESTION_MESSAGE, 
            JOptionPane.YES_NO_OPTION);
            JDialog dialog = confirm.createDialog("Eliminar gasto programado");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            if((int)confirm.getValue() == JOptionPane.YES_OPTION) {
                dialog.dispose();
                Gasto.DeleteAll();
                RaiseGastoProgramadoEliminado();
            }
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }
    
    private void RaiseGastoEliminado()
    {
        ListenersGasto.stream().forEach((xG) -> {
            xG.GastoEliminado(Gasto);
        });
    }
    
    private void RaiseGastoProgramadoEliminado()
    {
        ListenersGasto.stream().forEach((xG) -> {
            xG.GastoProgramadoEliminad(Gasto);
        });
    }
}
