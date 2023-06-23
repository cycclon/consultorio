/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Practicas.clsHojaTurnos;
import com.PRS.Consultorio.Practicas.clsProgramador;
import com.PRS.Consultorio.Practicas.clsTrabajoLazy;
import com.PRS.Consultorio.Practicas.enEstadoTrabajo;
import com.PRS.Framework.FormulariosFX.clsGestorCarga;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.print.PrinterJob;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Screen;

/**
 * FXML Controller class
 *
 * @author cycclon
 */
public class ctrlHojaTurnos implements Initializable, iMensajeable {

    @FXML private DatePicker dpDia;
    @FXML private CheckBox chSeleccionar;
    @FXML private Label lblMensaje;
    @FXML private VBox vbTurnos;
    @FXML private ScrollPane sp;
    
    private clsHojaTurnos HT;
    private List<ctrlTurnoParaFirmar> Ts;
    
    public void setHojaTurnos(clsHojaTurnos ht){HT = ht;}
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Ts = new ArrayList<>();
        this.dpDia.setValue(LocalDate.now());
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        this.sp.setPrefHeight(bounds.getHeight());
        this.sp.setPrefWidth(bounds.getWidth());
    }    

    @Override
    public Label getCanvas() {return this.lblMensaje;}

    @Override
    public String getNombre() {return "Hoja de turnos";}
    
    private void MostrarTrabajos() 
    {
        try
        {
            List<clsTrabajoLazy> ts = clsProgramador.Instanciar().getTurnos
                    (Date.from(this.dpDia.getValue().atStartOfDay()
                            .atZone(ZoneId.systemDefault()).toInstant()));
            this.vbTurnos.getChildren().clear();
            Ts.clear();
            for(clsTrabajoLazy t : ts)
            {
                FXMLLoader xL = new FXMLLoader(getClass()
                        .getResource("/jfxconsultorio/Practicas/fxTurnoParaFirmar.fxml"));
                this.vbTurnos.getChildren().add(xL.load());
                ctrlTurnoParaFirmar ctf = xL.<ctrlTurnoParaFirmar>getController();
                ctf.setTrabajo(t);
                this.Ts.add(ctf);
            }
            
            if(ts.isEmpty())vbTurnos.getChildren().add(new Label(
                    "No hay trabajos para el día seleccionado."));
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
    @FXML private void btnFirmar_Click(ActionEvent event)
    {
        try
        {
            clsGestorCarga.getInstancia().startLoading("Firmando trabajos...");
            int contador = 0;
            for(ctrlTurnoParaFirmar t : Ts)
            {
                if(t.isSelected())
                {
                    t.getTrabajoFull().Firmar(
                        Date.from(this.dpDia.getValue().atStartOfDay()
                                .atZone(ZoneId.systemDefault()).toInstant()))
                    ;contador++;
                }
            }
            this.MostrarTrabajos();
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                    "Se firmaron " + String.valueOf(contador) + " trabajos.");
            clsGestorCarga.getInstancia().endLoading();
        }
        catch(Exception ex)
        {
            clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());
            clsGestorCarga.getInstancia().endLoading();
        }
    }
    
    @FXML private void btnImprimir_Click(ActionEvent event)
    {
        try
        {
            HTMLEditor he = new HTMLEditor();
            String s = "<html dir=\"ltr\"><head></head><body "
                    + "contenteditable=\"true\"><p><font face=\"Segoe "
                    + "UI\">Hoja de informes del día: " + 
                    new SimpleDateFormat("dd/MM/yyyy").format(Date.from(
                            this.dpDia.getValue().atStartOfDay().atZone(
                                    ZoneId.systemDefault()).toInstant())) 
                    + "</font></p><p><hr><font face=\"Segoe UI\"></font></p>";
            
            for(ctrlTurnoParaFirmar t : Ts)
                if(t.getTrabajoLazy().getEstado() == enEstadoTrabajo.Firmado)
                s+= "<br/><br/>" + t.getTrabajoLazy().getPaciente() + " (" 
                        + t.getTrabajoLazy().getDocumentoPaciente() + ")<hr>";
            
            s+="</body></html>";
            he.setHtmlText(s);
            
            PrinterJob pj = PrinterJob.createPrinterJob();                        
            if(pj.showPrintDialog(null)){he.print(pj);}        
            pj.endJob();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
    @FXML private void chkSeleccionar_CheckedChanged(Event event)
    {
        for(ctrlTurnoParaFirmar t : Ts)t.setSelected(this.chSeleccionar.isSelected());
    }
    
    @FXML private void dpFecha_ValueChanged(ActionEvent event)
    {this.MostrarTrabajos();}
}
