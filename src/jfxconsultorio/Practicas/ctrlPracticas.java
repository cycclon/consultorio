/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Practicas.clsPractica;
import com.PRS.Consultorio.Practicas.clsPracticaLazy;
import com.PRS.Framework.FormulariosFX.clsGestorCarga;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import com.PRS.Framework.FormulariosFX.iRegistryListener;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlPracticas implements Initializable, iMensajeable, iReceptorError, iRegistryListener {

    @FXML private Label lblMensaje;
    
    @FXML private VBox vbRegistrar;    
    @FXML private VBox vbPracticas;
    
    @FXML private CheckBox chkEliminadas;
    
    @FXML private ScrollPane spPracticas;    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try
        {                        
            FXMLLoader xL = new FXMLLoader(getClass().
                    getResource("/jfxconsultorio/Practicas/fxAddPractica.fxml"));
            vbRegistrar.getChildren().add(xL.load());
            ctrlAddPractica xAP = xL.<ctrlAddPractica>getController();
            xAP.addListenerError(this);
            xAP.addListenerMensaje(this);
            this.MostrarPracticas();
            xAP.setSPWidth(spPracticas.getPrefWidth());
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }   
    
    private void AnchoScroll()
    {
        
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        double max = (bounds.getWidth() - 50) /2;
        this.spPracticas.setPrefWidth(max);
    }
    
    @Override
    public String getNombre() {return "Prácticas";}

    @Override
    public Label getCanvas() {return this.lblMensaje;}

    @Override
    public void ErrorOcurred(String prMensaje) 
    {clsGestorMensajes.Instanciar().MostrarError(this, prMensaje);}

    @Override
    public void RegistryUpdated(String prMensaje) {
        try
        {
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, prMensaje);
            this.MostrarPracticas();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
        
    }

    @Override
    public short getIDListerner() {return 1;}
    
    public void MostrarPracticas() throws Exception
    {
        
        
          try {
                this.vbPracticas.getChildren().clear();
                List<clsPracticaLazy> xPs = clsPractica.ListarLazy(this.chkEliminadas.isSelected());

                for(clsPracticaLazy xP : xPs)
                {
                    FXMLLoader xL = new FXMLLoader(getClass().
                            getResource("/jfxconsultorio/Practicas/fxPractica.fxml"));
                    vbPracticas.getChildren().add(xL.load());
                    ctrlPractica xCP = xL.<ctrlPractica>getController();
                    xCP.addListenerError(this);
                    xCP.addListenerRegistro(this);
                    xCP.setPractica(xP);
                }

                if(xPs.isEmpty()){vbPracticas.getChildren()
                        .add(new Label("No hay prácticas registradas"));}
                        
               
                
                /*TableView table = new TableView();
                
                TableColumn tcNombrePractica = new TableColumn("Nombre");
                tcNombrePractica.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
                
                table.getItems().addAll(xPs);
                table.getColumns().add(tcNombrePractica);
                
                this.vbPracticas.getChildren().add(table);*/
                
                this.AnchoScroll();
                
          } catch (InterruptedException e) {
             
            clsGestorMensajes.Instanciar().MostrarError(this, e.getMessage());
          }
    }
    
    @FXML
    private void chkEliminadas_CHeckedChanged(ActionEvent event)
    {
        try
        {
            this.MostrarPracticas();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
}
