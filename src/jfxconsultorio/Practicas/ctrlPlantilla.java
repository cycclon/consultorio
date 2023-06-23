/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Informes.clsPlantilla;
import com.PRS.Consultorio.Practicas.clsPractica;
import com.PRS.Framework.Archivos.clsGestorArchivos;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import jfxconsultorio.Practicas.ctrlPlantillaPractica.iPlantillaListener;
import jfxconsultorio.clsConsultorio;

/**
 * FXML Controller class
 *
 * @author ARSPIDALIERI
 */
public class ctrlPlantilla implements Initializable, iPlantillaListener {

    @FXML private Label lblNombre;
    
    private clsPlantilla Plantilla;
    private clsPractica Practica;
    private List<iPlantillaListener> Listeners;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.Listeners = new ArrayList<>();
    }    
    
    void setPlantilla(clsPlantilla plantilla)
    {Plantilla = plantilla;this.MostrarPlantilla();}
    
    void setPractica(clsPractica practica)
    {Practica = practica;}
    
    private void MostrarPlantilla()
    {this.lblNombre.setText(Plantilla.getNombre());}
    
    @FXML private void btnEditar_Click(ActionEvent event)
    {
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Practicas/fxPlantillaPractica.fxml"));
            Parent root = xL.load();
        
            ctrlPlantillaPractica xPP = xL.<ctrlPlantillaPractica>getController();
            xPP.setPractica(Practica);
            xPP.setPlantilla(Plantilla);
            xPP.addPlantillaListener(this);
            Scene scene = new Scene(root);
            
            Stage st = new Stage();
            
            st.setScene(scene);
            st.setWidth(800);
            st.setHeight(800);
            st.setTitle("Editar Plantilla " + this.Plantilla.getNombre());
            st.show();
        }
        catch(Exception ex)
        {}
    }

    @Override
    public void PlantillaGuardada(clsPractica practica) {
        this.NotificarGrabacion(practica);
    }
    
    public void addPlantillaListener(iPlantillaListener listener)
    {this.Listeners.add(listener);}
    
    private void NotificarGrabacion(clsPractica practica)
    {
        for(iPlantillaListener xPL : Listeners)
        {xPL.PlantillaGuardada(practica);}
    }
    
    private void NotificarEliminacion()
    {
        for(iPlantillaListener xPL : Listeners)
        {xPL.PlantillaEliminada();}
    }
    
    @FXML private void btnEliminar_Click(ActionEvent event)
    {
        try
        {
            JOptionPane confirm = new JOptionPane("¿Está seguro que desea "
                    + "eliminar esta plantilla?", JOptionPane.QUESTION_MESSAGE, 
            JOptionPane.YES_NO_OPTION);
            JDialog dialog = confirm.createDialog("Eliminar plantilla");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            if((int)confirm.getValue() == JOptionPane.YES_OPTION)
            {
              
                String fullPath = "\\\\" + clsConsultorio.Instanciar()
                        .getServerNameOrAddress() + "\\SomaShared\\InformesHTML\\";
                File file = new File(fullPath + Plantilla.getArchivo());
                if(file.exists())
                {file.delete();}
                Plantilla.Eliminar();
                this.NotificarEliminacion();
            }
        }
        catch(Exception ex)
        {}
    }

    @Override
    public void PlantillaEliminada() {
        this.NotificarEliminacion();
    }
}
