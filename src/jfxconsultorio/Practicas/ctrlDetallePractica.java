/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Informes.clsPlantilla;
import com.PRS.Consultorio.Practicas.clsPractica;
import com.PRS.Consultorio.Practicas.clsValorPractica;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.clsGestorNavegacion;
import com.PRS.Framework.FormulariosFX.clsGestorValidacion;
import com.PRS.Framework.FormulariosFX.enTipoTextField;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlDetallePractica implements Initializable, iMensajeable, 
        ctrlPlantillaPractica.iPlantillaListener, iReceptorError {

    @FXML private Label lblMensaje;
    @FXML private Label lblEstado;
    @FXML private Label lblNombre;
    @FXML private Label lblCodigo;
    @FXML private Label lblCC;
    
    @FXML private ImageView ivBoton;    
    @FXML private Tooltip ttAyudaBoton;
    
    @FXML private VBox vbOSs;
    @FXML private VBox vbAdd;
    @FXML private VBox vbPlantillas;
    
    @FXML private TextField txtHoras;
    @FXML private TextField txtMinutos;
    
    @FXML private HBox hbEstadO;
    
    @FXML private DatePicker dpVigencia;
    
    @FXML private ScrollPane spValores;
    
    private clsPractica xP;
    private List<ctrlAddCostoOS> xACOSs;
    private ctrlAgregarCostoOS xAgregar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        
        spValores.setPrefWidth(bounds.getWidth() - 50);
        xACOSs = new ArrayList<>();
        this.dpVigencia.setValue(LocalDate.now());
        FXMLLoader xL = new FXMLLoader(getClass().getResource(
                "/jfxconsultorio/Practicas/fxAgregarCostoOS.fxml"));
        try {
            this.vbAdd.getChildren().add(xL.load());
            xAgregar = xL.<ctrlAgregarCostoOS>getController();
            xAgregar.addListenerError(this);
        } catch (IOException ex) {
            clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());
        }
        
    }    
    
    private void MostrarPlantillas()
    {
        try
        {
            this.vbPlantillas.getChildren().clear();
            List<clsPlantilla> ps = clsPlantilla.getPlantillas(this.xP.getID());
            
            for(clsPlantilla p : ps)
            {
                FXMLLoader xL = new FXMLLoader(getClass().getResource(
                        "/jfxconsultorio/Practicas/fxPlantilla.fxml"));
                this.vbPlantillas.getChildren().add(xL.load());
                ctrlPlantilla cp = xL.<ctrlPlantilla>getController();
                cp.setPlantilla(p);
                cp.setPractica(xP);
                cp.addPlantillaListener(this);
            }
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
    @Override
    public String getNombre() {return "Detalle de práctica";}
    
    public void setPractica(clsPractica practica) throws Exception
    {
        this.xP = practica;
        this.MostrarPractica();
    }
    
    private void MostrarPractica() throws Exception
    {
        this.lblCodigo.setText("("+ xP.getCodigo() + ")");
        this.lblEstado.setText("Estado: " + this.xP.getNombreEstado());
        this.lblNombre.setText(xP.getNombre());
        this.ttAyudaBoton.setText(xP.getAyudaBoton());
        
        String img = "/jfxconsultorio/Imagenes/Iconos/"
            + this.xP.getIconoBoton();
        Image image2 = new Image(img);
        this.ivBoton.setImage(image2);
        this.lblCC.setText("Centro de costos: " + this.xP.getCC().getNombreConCodigo());
        this.MostrarOSs();
        this.txtHoras.setText(String.valueOf(this.xP.getDuracion().getHoras()));
        this.txtMinutos.setText(String.valueOf(this.xP.getDuracion().getMinutos()));
        this.hbEstadO.setStyle("-fx-background-color: " + xP.getColorEstado() + ";");
        this.MostrarPlantillas();
    }

    @Override
    public Label getCanvas() {return this.lblMensaje;}
    
    private void MostrarOSs() throws Exception
    {
        List<clsValorPractica> xVPs = xP.getCosto().getValoresVigentes(
                Date.from(dpVigencia.getValue().
                        atStartOfDay(ZoneId.systemDefault()).toInstant()));
        this.vbOSs.getChildren().clear();
        this.xACOSs.clear();
        for(clsValorPractica xVP : xVPs){
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Practicas/fxAddCostoOS.fxml"));
            this.vbOSs.getChildren().add(xL.load());
            ctrlAddCostoOS xACOS = xL.<ctrlAddCostoOS>getController();
            
            xACOS.setValorPractica(xVP);            
            
            xACOSs.add(xACOS);
        }    
        
        
    }
    
    @FXML
    private void dpVigencia_SelectedIndexChanged(ActionEvent event)
    {
        try
        {
            this.MostrarOSs();
            clsGestorMensajes.Instanciar().MostrarMensaje(this.lblMensaje, "Viendo valores vigentes para el día " + this.dpVigencia.getValue().toString());
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
    @FXML
    private void btnGuardar_Click(ActionEvent event)
    {
        try
        {
            clsGestorValidacion.ValidarTextField(txtHoras, "Horas", true, enTipoTextField.Numerico);
            clsGestorValidacion.ValidarTextField(txtMinutos, "Minutos", true, enTipoTextField.Numerico);

            JOptionPane confirm = new JOptionPane("¿Está seguro que desea guardar los cambios efectuados a esta práctica?", JOptionPane.QUESTION_MESSAGE, 
            JOptionPane.YES_NO_OPTION);
            JDialog dialog = confirm.createDialog("Guardar");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            if((int)confirm.getValue() == JOptionPane.YES_OPTION){
                dialog.dispose();
                this.xP.getDuracion().setMinutosTotales((short)(Float.parseFloat(
                    this.txtHoras.getText())*60 + Float.parseFloat(
                            this.txtMinutos.getText())));
                xP.Actualizar();
                for(ctrlAddCostoOS xACOS : xACOSs)
                {
                    clsValorPractica xVP = xACOS.getValorPractica();
                    if(xVP.getCoseguro().pdValor() != 0.0 || xVP.getCostoOS().pdValor() != 0.0)
                    {xP.AgregarCosto(xVP);}
                }
                
                clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje,
                        "Se actualizaron los datos de la práctica correctamente.");
            }  
            
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
    @FXML
    private void btnAccion_Click(ActionEvent event)
    {
        try
        {
            JOptionPane confirm = new JOptionPane(this.xP.getPregunta(), JOptionPane.QUESTION_MESSAGE, 
            JOptionPane.YES_NO_OPTION);
            JDialog dialog = confirm.createDialog(this.xP.getTituloConfirmacion());
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            if((int)confirm.getValue() == JOptionPane.YES_OPTION){
                dialog.dispose();
                this.xP.CambiarEstado();
                clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, xP.getConfirmacion());
                this.MostrarPractica();
            }  
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
    @FXML
    private void btnAgregar_Click(ActionEvent event)
    {
            JOptionPane confirm = new JOptionPane("¿Está seguro que desea agregar este valor para la práctica?", JOptionPane.QUESTION_MESSAGE, 
            JOptionPane.YES_NO_OPTION);
            JDialog dialog = confirm.createDialog("Agregar valor");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            if((int)confirm.getValue() == JOptionPane.YES_OPTION){
                dialog.dispose();
            try {
                this.xP.AgregarCosto(this.xAgregar.getValorPractica());
                this.MostrarOSs();
                clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                        "Se agregó un nuevo valor de obra social. No es "
                                + "necesario presionar el botón Guardar Cambios.");
                xAgregar.LimpiarFormulario();
            } catch (Exception ex) 
            {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
        }
    }       

    @Override
    public void PlantillaGuardada(clsPractica practica) {
        clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                "Se editó la plantilla del informe para esta práctica");
        this.MostrarPlantillas();
    }

    @Override
    public void ErrorOcurred(String prMensaje) 
    {clsGestorMensajes.Instanciar().MostrarError(this, prMensaje);}
    
    @FXML 
    private void btnPracticas_Click(ActionEvent event)
    {
        try
        {
            clsGestorNavegacion.Instanciar().Abrir(
                    "/jfxconsultorio/Practicas/fxPracticas.fxml", "Prácticas", 
                    "practicas.png");
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}        
    }

    @Override
    public void PlantillaEliminada() {
        clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                "Se eliminó la plantilla de esta práctica");
        this.MostrarPlantillas();
    }
    
    @FXML private void btnRegistrarPlantilla_Click(ActionEvent event)
    {
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Practicas/fxPlantillaPractica.fxml"));
            Parent root = xL.load();
        
            ctrlPlantillaPractica xPP = xL.<ctrlPlantillaPractica>getController();
            xPP.setPractica(xP);
            xPP.setPlantilla(new clsPlantilla());
            xPP.addPlantillaListener(this);
            Scene scene = new Scene(root);
            
            Stage st = new Stage();
            
            st.setScene(scene);
            st.setWidth(800);
            st.setHeight(800);
            st.setTitle("Registrar Plantilla para " + this.xP.getNombre());
            st.show();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
}
