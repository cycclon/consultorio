/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Informes.clsInforme;
import com.PRS.Consultorio.Practicas.clsTrabajo;
import com.PRS.Framework.Archivos.clsGestorArchivos;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.control.Label;
import javafx.scene.web.HTMLEditor;
import jfxconsultorio.clsConsultorio;


/**
 * FXML Controller class
 *
 * @author ARSPIDALIERI
 */
public class ctrlImprimirComprobante implements Initializable, iMensajeable {

    @FXML private HTMLEditor wvComprobante;
    
    @FXML private Label lblMensaje;
    
    private clsTrabajo Trabajo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO        
        
    }   
    
    @Override
    public String getNombre() {return "Imprimir comprobante";}

    @Override
    public Label getCanvas() {return this.lblMensaje;}
    
    public void setTrabajo(clsTrabajo trabajo)
    {this.Trabajo = trabajo; this.CargarComprobante();}
    
    private void CargarComprobante()
    {
        try
        {
             String fullPath = "\\\\" + clsConsultorio.Instanciar()
                    .getServerNameOrAddress() + "\\SomaShared\\comprobante.html";
            File file = new File(fullPath);
            if(file.exists())
            {
                clsInforme xI = new clsInforme(clsGestorArchivos.Instanciar()
                        .readFile(file), this.Trabajo);
                
                this.wvComprobante.setHtmlText(xI.getTextoHTML());
                this.wvComprobante.setDisable(true);
            }
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
    @FXML
    private void btnImprimir_Click(ActionEvent event)
    {
        try
        {            
            PrinterJob pj = PrinterJob.createPrinterJob();                        
            if(pj.showPrintDialog(null)){wvComprobante.print(pj);}        
            pj.endJob();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
}
