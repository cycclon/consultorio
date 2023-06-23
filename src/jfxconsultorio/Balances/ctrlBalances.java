/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Balances;

import com.PRS.Consultorio.Balances.clsBalance;
import com.PRS.Consultorio.Balances.clsPeriodoAnual;
import com.PRS.Consultorio.Balances.clsPeriodoDiario;
import com.PRS.Consultorio.Balances.clsPeriodoMensual;
import com.PRS.Consultorio.Balances.clsPeriodoSemanal;
import com.PRS.Consultorio.Balances.iEgreso;
import com.PRS.Consultorio.Balances.iIngreso;
import com.PRS.Framework.FormulariosFX.clsGestorCarga;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.clsGestorValidacion;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * FXML Controller class
 *
 * @author cycclon
 */
public class ctrlBalances implements Initializable, iMensajeable {

    @FXML private Label lblMensaje;
    @FXML private Label lblPeriodo;
    @FXML private Label lblBalance;
    @FXML private Label lblIngresos;
    @FXML private Label lblEgresos;
    @FXML private Label lblNombrePeriodo;
    @FXML private Label lblAno;
        
    @FXML private RadioButton rdDiario;
    @FXML private RadioButton rdMensual;
    @FXML private RadioButton rdSemanal;
    @FXML private RadioButton rdAnual;
    
    @FXML private ComboBox cbAnos;
    @FXML private ComboBox cbPeriodo;
    
    @FXML private VBox vbIngresos;
    @FXML private VBox vbEgresos;    
    
    @FXML private ScrollPane spIngresos;
    @FXML private ScrollPane spEgresos;
    
    @FXML private DatePicker dp;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.MostrarAños();
        this.MostrarMeses();
        this.MostrarPeriodo();
        
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        
        this.spEgresos.setPrefWidth((bounds.getWidth() - 50) / 2);
        this.spIngresos.setPrefWidth((bounds.getWidth() - 50) / 2);
    }    

    @Override
    public Label getCanvas() {return this.lblMensaje;}

    @Override
    public String getNombre() {return "Balances";}
    
    private void MostrarAños()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        this.cbAnos.getItems().clear();
        for(int i = 2013; i<= cal.get(Calendar.YEAR); i++)cbAnos.getItems().add(String.valueOf(i));
        cbAnos.getSelectionModel().select(String.valueOf(cal.get(Calendar.YEAR)));

        if(Integer.parseInt(cbAnos.getSelectionModel().getSelectedItem()
                .toString()) != cal.get(Calendar.YEAR))cbPeriodo.getSelectionModel().select(0);
    }
    
    private void MostrarMeses()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String[] monthNames = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", 
            "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", 
            "Diciembre"};
        this.cbPeriodo.getItems().clear();
        for(int i = 0; i< 12; i++)cbPeriodo.getItems().add(monthNames[i]);
        cbPeriodo.getSelectionModel().select(cal.get(Calendar.MONTH));
        if(Integer.parseInt(cbAnos.getSelectionModel().getSelectedItem()
                .toString()) != cal.get(Calendar.YEAR))cbPeriodo.getSelectionModel().select(0);
    }
    
    private void MostrarSemanas() throws Exception
    {
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d = sdf.parse("27/12/" + this.cbAnos.getSelectionModel().getSelectedItem().toString());
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(d);
        cbPeriodo.getItems().clear();
        for(int i = 1; i<=cal.get(Calendar.WEEK_OF_YEAR);i++)cbPeriodo.getItems().add(i);
        cal.setTime(new Date());
        
    }
    
    @FXML
    private void rdDiario_CheckedChanged(Event event)
    {
        this.MostrarPeriodo();        
    }
    
    
    @FXML
    private void rdMensual_CheckedChanged(Event event)
    {
        this.MostrarPeriodo();
        if(rdMensual.isSelected())
        {
            this.lblPeriodo.setText("Mes:");
            this.MostrarMeses();
        }
    }
    
    @FXML
    private void rdSemanal_CheckedChanged(Event event)
    {
        try
        {
            this.MostrarPeriodo();
            if(rdSemanal.isSelected())
            {
                this.lblPeriodo.setText("Semana:");
                this.MostrarSemanas();
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                this.cbPeriodo.getSelectionModel().select(cal.get(Calendar.WEEK_OF_YEAR)-1);
            }
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}        
    }
    
    @FXML
    private void cbAños_SelectedIndexChanged(Event event)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        if(Integer.parseInt(cbAnos.getSelectionModel().getSelectedItem()
                .toString()) != cal.get(Calendar.YEAR));
        cbPeriodo.getSelectionModel().select(0);
         if(Integer.parseInt(cbAnos.getSelectionModel().getSelectedItem()
                .toString()) != cal.get(Calendar.YEAR))cbPeriodo.getSelectionModel().select(0);
    }
    
    @FXML
    private void rdAnual_CheckedChanged(Event event)
    {
        this.MostrarPeriodo();
    }
    
    private void MostrarPeriodo()
    {
        this.lblPeriodo.setVisible(!rdAnual.isSelected() && !rdDiario.isSelected());
        this.cbPeriodo.setVisible(!rdAnual.isSelected() && !rdDiario.isSelected());
        this.lblAno.setVisible(!rdDiario.isSelected());
        this.cbAnos.setVisible(!rdDiario.isSelected());
        this.dp.setVisible(rdDiario.isSelected());
    }
    
    @FXML
    private void btnVer_Click(ActionEvent event)
    {
        try
        {
            clsBalance b = null;
            if(this.rdMensual.isSelected())
            {
                
                clsGestorValidacion.ValidarComboBox(cbPeriodo, "Mes", true);
                clsGestorCarga.getInstancia().startLoading("Generando balance...");
                b = new clsBalance(new clsPeriodoMensual((byte)(this
                        .cbPeriodo.getSelectionModel().getSelectedIndex()+1), 
                        Short.parseShort(this.cbAnos.getSelectionModel()
                                .getSelectedItem().toString())));
            }
            if(this.rdSemanal.isSelected())
            {
                clsGestorValidacion.ValidarComboBox(cbPeriodo, "Semana", true);
                clsGestorCarga.getInstancia().startLoading("Generando balance...");
                b = new clsBalance(new clsPeriodoSemanal((byte)(cbPeriodo.getSelectionModel()
                        .getSelectedIndex() + 1), Short.parseShort(this.cbAnos
                                .getSelectionModel().getSelectedItem().toString())));
            }
            if(this.rdAnual.isSelected())
            {
                clsGestorCarga.getInstancia().startLoading("Generando balance...");
                b = new clsBalance(new clsPeriodoAnual(Short.parseShort(this.cbAnos
                                .getSelectionModel().getSelectedItem().toString())));
            }
            
            if(this.rdDiario.isSelected())
            {
                clsGestorCarga.getInstancia().startLoading("Generando balance...");
                b = new clsBalance(new clsPeriodoDiario(Date.from(dp.getValue()
                        .atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())));
            }
            
            this.MostrarBalance(b);
            clsGestorCarga.getInstancia().endLoading();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());
        clsGestorCarga.getInstancia().endLoading();}
    }
    
    private void MostrarBalance(clsBalance b) throws Exception
    {
        this.lblNombrePeriodo.setText(b.getNombre());
        //INGRESOS
        this.vbIngresos.getChildren().clear();
        for(iIngreso i : b.getIngresos())
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Balances/fxItemBalance.fxml"));
            vbIngresos.getChildren().add(xL.load());
            ctrlItemBalance xCI = xL.<ctrlItemBalance>getController();
            xCI.setItemBalance(i);
        }
        
        if(b.getIngresos().isEmpty())this.vbIngresos.getChildren()
                .add(new Label("No hay ingresos para este período"));
        
        //EGRESOS
        this.vbEgresos.getChildren().clear();
        for(iEgreso e : b.getEgresos())
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Balances/fxItemBalance.fxml"));
            vbEgresos.getChildren().add(xL.load());
            ctrlItemBalance xCI = xL.<ctrlItemBalance>getController();
            xCI.setItemBalance(e);
        }
        
        if(b.getEgresos().isEmpty())this.vbEgresos.getChildren()
                .add(new Label("No hay egresos para este período"));
        
        //RESUMEN
        this.lblBalance.setText(b.getBalance().pdValorString());
        this.lblEgresos.setText(b.getTotalEgresos().pdValorString());
        this.lblIngresos.setText(b.getTotalIngresos().pdValorString());
    }
}
