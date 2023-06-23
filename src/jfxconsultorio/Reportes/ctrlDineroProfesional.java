/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Reportes;

import com.PRS.Consultorio.Reportes.clsItemReporte;
import com.PRS.Consultorio.Reportes.clsRDineroProfesional;
import com.PRS.Consultorio.Reportes.enTipoCobro;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author cycclon
 */
public class ctrlDineroProfesional implements Initializable, iMensajeable {

    @FXML private PieChart pc;
    @FXML private Label lblMensaje;
    @FXML private ComboBox cbImporte;
    @FXML private ComboBox cbMes;
    @FXML private ComboBox cbAno;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.MostrarReporte();
        this.MostrarAnos();
        this.MostrarImportes();
        this.MostrarMeses();
    }    
    
    private void MostrarReporte()
    {
        try
        {
            clsRDineroProfesional rdp = new clsRDineroProfesional();
            if(!cbAno.getSelectionModel().isEmpty())if(cbAno.getSelectionModel()
                    .getSelectedIndex()!=0)rdp.setAno(Integer.valueOf(cbAno
                            .getSelectionModel().getSelectedItem().toString()));
            if(!cbMes.getSelectionModel().isEmpty())rdp
                    .setMes((byte)cbMes.getSelectionModel().getSelectedIndex());
            
            if(!cbImporte.getSelectionModel().isEmpty())rdp
                    .setTipoCobro(enTipoCobro.valueOf(cbImporte
                            .getSelectionModel().getSelectedItem().toString()));
            
            rdp.Generar();
            
            pc.getData().clear();
            for(clsItemReporte ir : rdp.getItems())this.pc.getData()
                    .add(new PieChart.Data(ir.getNombre(), ir.getValor()));
            
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public Label getCanvas() {return lblMensaje;}

    @Override
    public String getNombre() {return "Reporte dinero por profesional.";}
    
    private void MostrarImportes()
    {
        this.cbImporte.getItems().clear();
        for(enTipoCobro tc : enTipoCobro.values())
        {
            this.cbImporte.getItems().add(tc.toString());
        }
    }
    
    private void MostrarAnos()
    {Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        this.cbAno.getItems().clear();
        cbAno.getItems().add("Todos");
        for(int i = 2013; i<= cal.get(Calendar.YEAR); i++)cbAno.getItems().add(String.valueOf(i));
        
    }      
    
    private void MostrarMeses()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String[] monthNames = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", 
            "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", 
            "Diciembre"};
        this.cbMes.getItems().clear();
        cbMes.getItems().add("Todos");
        for(int i = 0; i< 12; i++)cbMes.getItems().add(monthNames[i]);        
    }
    
    @FXML private void cbImporte_SelectedItemChanged(Event event)
    {
        this.MostrarReporte();
    }
    
    @FXML private void cbMes_SelectedItemChanged(Event event)
    {
        this.MostrarReporte();
    }
            
    @FXML private void cbAno_SelectedItemChanged(Event event)
    {
        this.MostrarReporte();
    }
}
