/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Practicas.clsTrabajo;
import com.PRS.Consultorio.Practicas.clsTrabajoLazy;
import com.PRS.Consultorio.Practicas.enEstadoTrabajo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author cycclon
 */
public class ctrlTurnoParaFirmar implements Initializable {

    @FXML private Label lblHora;
    @FXML private Label lblEstadoFirma;
    @FXML private Label lblPracticaPaciente;
    @FXML private CheckBox chkSeleccionar;
    
    public void setSelected(boolean selected)
    {
        if(!chkSeleccionar.isDisabled())
        chkSeleccionar.setSelected(selected);
    }
    
    private clsTrabajoLazy TL;
    
    public void setTrabajo(clsTrabajoLazy tl){TL = tl; MostrarTrabajo();}
    public clsTrabajoLazy getTrabajoLazy(){return this.TL;}
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    private void MostrarTrabajo()
    {
        this.lblHora.setText(TL.getHorario());
        this.lblEstadoFirma.setText("(" + TL.getEstado().toString()+ ")");
        this.chkSeleccionar.setDisable(TL.getEstado() != enEstadoTrabajo.Realizado);
        this.lblPracticaPaciente.setText(TL.getNombrePractica() + " para " + TL.getPaciente());
    }
    
    public clsTrabajo getTrabajoFull()throws Exception {return this.TL.getTrabajoFull();}
    
    boolean isSelected()
    {return this.chkSeleccionar.isSelected();}
}
