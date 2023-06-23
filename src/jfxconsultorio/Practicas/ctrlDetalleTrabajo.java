/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Cuentas.clsCobro;
import com.PRS.Consultorio.Cuentas.clsPago;
import com.PRS.Consultorio.Practicas.clsPractica;
import com.PRS.Consultorio.Practicas.clsPracticaLazy;
import com.PRS.Consultorio.Practicas.clsTrabajo;
import com.PRS.Consultorio.Practicas.clsValorCampo;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.clsGestorNavegacion;
import com.PRS.Framework.FormulariosFX.clsGestorValidacion;
import com.PRS.Framework.FormulariosFX.enTipoTextField;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import com.PRS.Framework.FormulariosFX.iParametrizable;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import jfxconsultorio.Cuentas.ctrlPago;
import jfxconsultorio.Practicas.ctrlBuscarPractica.iBusquedaPractica;

/**
 * FXML Controller class
 *
 * @author ARSPIDALIERI
 */
public class ctrlDetalleTrabajo implements Initializable, iMensajeable, 
        iParametrizable, iReceptorError, ctrlPagar.iListenerPago, 
        ctrlReprogramar.iListenerProgramacion, ctrlRealizar.iListenerRealizacion, 
        ctrlFirmar.iListenerFirma, ctrlCancelarTurno.iListenerCancelacion, iBusquedaPractica,
        iListenerPractica{
    
    @FXML private Label lblMensaje;
    @FXML private Label lblEstado;
    @FXML private Label lblPaciente;
    @FXML private Label lblOS;
    @FXML private Label lblDia;
    @FXML private Label lblHorario;
    @FXML private Label lblSolicitado;
    @FXML private Label lblRealizado;
    @FXML private Label lblFirmado;
    @FXML private Label lblCostoOS;
    @FXML private Label lblCostoCoseguro;
    @FXML private Label lblPagoCoseguro;
    @FXML private Label lblDocumento;
    @FXML private Label lblLeyendaOS;
    @FXML private Label lblDuracion;
    
    @FXML private Button btnPagar;
    @FXML private Button btnRealizar;
    @FXML private Button btnReprogramar;
    @FXML private Button btnFirmar;
    @FXML private Button btnCancelar;
    @FXML private Button btnInforme;
    
    @FXML private VBox vbOperar;
    @FXML private VBox vbAdicionales;
    @FXML private VBox vbPagos;
    @FXML private VBox vbBusquedaPracticas;
    
    @FXML private HBox hbBuscarPractica;
    
    @FXML private TitledPane tpPractica;    
    
    @FXML private TextField txtCostoCoseguro;
    
    @FXML private ScrollPane sp;
    
    List<ctrlPago> Pagos;
    
      
    private List<ctrlPracticaLazy> Practicas;
    
    private clsTrabajo Trabajo;
    private List<ctrlValorCampo> Valores;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try
        {
            Pagos = new ArrayList<>();
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            
            sp.setPrefWidth(bounds.getWidth()-50);
            this.Valores = new ArrayList<>();
            FXMLLoader xL2 = new FXMLLoader(getClass()
            .getResource("/jfxconsultorio/Practicas/fxBuscarPractica.fxml"));

            this.hbBuscarPractica.getChildren().clear();
            this.hbBuscarPractica.getChildren().add(xL2.load());
            ctrlBuscarPractica xBPra = xL2.<ctrlBuscarPractica>getController();
            xBPra.addListenerBusqueda(this);
            xBPra.addListenerError(this);
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
        
    }   
    
    @Override
    public String getNombre() {return "Detalle de trabajo";}

    @Override
    public Label getCanvas() {return this.lblMensaje;}

    @Override
    public void setParametro(Object parametro) 
    {this.Trabajo = (clsTrabajo)parametro; MostrarTrabajo();}
    
    private void MostrarTrabajo()
    {
        try
        {
            this.lblEstado.setText(this.Trabajo.getEstadoStr());            
            this.lblPaciente.setText(this.Trabajo.getPaciente().getNombreCompleto());
            this.lblOS.setText(this.Trabajo.getPaciente().getOS().toString());
            this.lblDia.setText(new SimpleDateFormat("dd/MM/yyyy")
                    .format(this.Trabajo.getProgramacion().getFecha()));
            this.lblHorario.setText(this.Trabajo.getProgramacion().getHorario().getHoraStr());
            this.lblSolicitado.setText(Trabajo.getSolicitud().getSolicitante().getNombreCompleto());
            this.lblCostoOS.setText(Trabajo.getCobroOS().getMonto().pdValorString());
            this.lblCostoCoseguro.setText(Trabajo.getCobroCoseguro().getMonto().pdValorString());
            this.lblPagoCoseguro.setText(Trabajo.getCobroCoseguro().getTotalPagado().pdValorString());
            this.lblRealizado.setText(Trabajo.getRealizacion().getRealizadorStr());
            this.lblFirmado.setText(Trabajo.getFirma().getFirmanteStr());
            this.lblDocumento.setText(Trabajo.getPaciente().getDocumento().toString());
            this.txtCostoCoseguro.setText(String.valueOf(Trabajo.getCobroCoseguro().getMonto().pdValor()));
            this.PracticaSeleccionada(Trabajo.getPractica());
            this.HabilitarBotones();
            this.MostrarCamposAdicionales();
            this.MostrarPagos();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
    private void MostrarPagos() throws Exception
    {
        this.vbPagos.getChildren().clear();
        Pagos.clear();
        
        for(clsPago p : Trabajo.getCobroCoseguro().getPagos())
        {
            FXMLLoader xL = new FXMLLoader(getClass().getResource("/jfxconsultorio/Cuentas/fxPago.fxml"));
            this.vbPagos.getChildren().add(xL.load());
            
            ctrlPago cp = xL.<ctrlPago>getController();
            cp.setPago(p);
            this.Pagos.add(cp);
        }
    }
    
    
    private void MostrarCamposAdicionales() throws Exception
    {
        this.vbAdicionales.getChildren().clear();
        this.Valores.clear();
        
        for(clsValorCampo xVC : Trabajo.getCampos())
        {
            FXMLLoader xL = new FXMLLoader(getClass().getResource("/jfxconsultorio/Practicas/fxValorCampo.fxml"));
            this.vbAdicionales.getChildren().add(xL.load());
            ctrlValorCampo xCVC = xL.<ctrlValorCampo>getController();
            xCVC.setValor(xVC);
            this.Valores.add(xCVC);            
        }
        
        if(Trabajo.getCampos().isEmpty()){this.vbAdicionales.getChildren()
                .add(new Label("No hay campos adicionales"));}
    }
    
    private void HabilitarBotones()
    {
        this.btnPagar.setVisible(this.Trabajo.isPagable());
        this.btnRealizar.setVisible(this.Trabajo.isTrabajable());
        this.btnReprogramar.setVisible(this.Trabajo.isProgramable());
        this.btnFirmar.setVisible(this.Trabajo.isFirmable());
        this.btnCancelar.setVisible(this.Trabajo.isCancelable());
        this.btnInforme.setVisible(this.Trabajo.isInformable());
    }   
    
    public void btnPagarPressed(ActionEvent event) {
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Practicas/fxPagar.fxml"));
            this.vbOperar.getChildren().clear();
            this.vbOperar.getChildren().add(xL.load());
            
            ctrlPagar xPagar = xL.<ctrlPagar>getController();
            xPagar.setTrabajo(Trabajo);
            xPagar.addListenerError(this);
            xPagar.addListenerPago(this);
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    public void btnReprogramarPressed(ActionEvent event) {
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Practicas/fxReprogramar.fxml"));
            this.vbOperar.getChildren().clear();
            this.vbOperar.getChildren().add(xL.load());
            
            ctrlReprogramar xReprogramar = xL.<ctrlReprogramar>getController();
            xReprogramar.setTrabajo(Trabajo);
            xReprogramar.addListenerError(this);
            xReprogramar.addListenerProgramacion(this);
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    public void btnRealizarPressed(ActionEvent event) {
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Practicas/fxRealizar.fxml"));
            this.vbOperar.getChildren().clear();
            this.vbOperar.getChildren().add(xL.load());
            
            ctrlRealizar xRealizar = xL.<ctrlRealizar>getController();
            xRealizar.setTrabajo(Trabajo);
            xRealizar.addListenerError(this);
            xRealizar.addListenerRealizacion(this);
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    public void btnFirmarPressed(ActionEvent event) {
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Practicas/fxFirmar.fxml"));
            this.vbOperar.getChildren().clear();
            this.vbOperar.getChildren().add(xL.load());
            
            ctrlFirmar xRealizar = xL.<ctrlFirmar>getController();
            xRealizar.setTrabajo(Trabajo);
            xRealizar.addListenerError(this);
            xRealizar.addListenerFirma(this);
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    public void btnCancelarPressed(ActionEvent event) {
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Practicas/fxCancelarTurno.fxml"));
            this.vbOperar.getChildren().clear();
            this.vbOperar.getChildren().add(xL.load());
            
            ctrlCancelarTurno xRealizar = xL.<ctrlCancelarTurno>getController();
            xRealizar.setTrabajo(Trabajo);
            xRealizar.addListenerError(this);
            xRealizar.addListenerCancelacion(this);
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void PagoEfectuado(clsTrabajo trabajo, clsCobro cobro, clsPago pago) {
        try
        {
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                    "Se registró el pago de " + trabajo.getPaciente().getNombreCompleto() + " por " + pago.getMonto().pdValorString() + " en concepto de " + trabajo.getPractica().getNombre());
            this.vbOperar.getChildren().clear();
            this.MostrarTrabajo();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void TrabajoReprogramado(clsTrabajo trabajo) {
        try
        {
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                "Se reprogramó el turno de " + trabajo.getPaciente()
                        .getNombreCompleto() + " para el día " + 
                        new SimpleDateFormat("dd/MM/yyyy").format(trabajo
                                .getProgramacion().getFecha()) + " a las " + 
                        trabajo.getProgramacion().getHorario().getHoraStr());
            this.vbOperar.getChildren().clear();
            this.MostrarTrabajo();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
        
    }

    @Override
    public void TrabajoRealizado(clsTrabajo trabajo) {
        try
        {
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
               trabajo.getPractica().getNombre() + " realizado para " + trabajo.getPaciente()
                        .getNombreCompleto() + " por " + trabajo.getRealizacion()
                                .getRealizador().getNombreCompleto());
            this.vbOperar.getChildren().clear();
            this.MostrarTrabajo();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void TrabajoFirmado(clsTrabajo trabajo) {
        try
        {
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
               trabajo.getPractica().getNombre() + " realizada por " + trabajo.getRealizacion()
                                .getRealizador().getNombreCompleto() + 
                       " y firmada por " + trabajo.getFirma().getFirmante()
                               .getNombreCompleto());
            
            this.vbOperar.getChildren().clear();
            this.MostrarTrabajo();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void TrabajoCancelado(clsTrabajo trabajo) {
        try
        {
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
               "Turno de " + trabajo.getPractica().getNombre() 
                       + " para " + trabajo.getPaciente().getNombreCompleto() + 
                       " cancelado correctamente");
            
            this.vbOperar.getChildren().clear();
            this.MostrarTrabajo();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}        
    }

    @Override
    public void ErrorOcurred(String prMensaje) {
        clsGestorMensajes.Instanciar().MostrarError(this, prMensaje);
    }
    
    public void btnVerInforme_Click(ActionEvent event)
    {
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Practicas/fxVerInforme.fxml"));
            Parent root = xL.load();
        
            ctrlVerInforme xPP = xL.<ctrlVerInforme>getController();
            xPP.setTrabajo(this.Trabajo);
            
            Scene scene = new Scene(root);
            
            Stage st = new Stage();
            
            st.setScene(scene);
            st.setWidth(800);
            st.setHeight(800);
            st.setTitle("Ver informe");
            st.show();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
    public void btnGuardarValores_Click(ActionEvent event)
    {
        try
        {
            clsGestorValidacion.ValidarTextField(txtCostoCoseguro, 
                    "Monto a pagar de coseguro", true, enTipoTextField.Decimal);
            
            for(ctrlValorCampo xVC : Valores)xVC.getValorCampo().Actualizar(); 
            
            for(ctrlPago p : Pagos)p.getPago().Modificar();
            
            Trabajo.getCobroCoseguro().Modificar(Float.parseFloat(this.txtCostoCoseguro.getText()));
          
            Trabajo.ModificarPractica();            
            
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                    "Se guardaron los cambios efectuados en este trabajo.");
            this.MostrarTrabajo();
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void VerResultadosPractica(List<clsPracticaLazy> resultados) {
        try
        {
            this.vbBusquedaPracticas.getChildren().clear();
            Practicas = new ArrayList<>();
            for(clsPracticaLazy xP : resultados)
            {
                FXMLLoader xL = new FXMLLoader(getClass()
                        .getResource("/jfxconsultorio/Practicas/fxPracticaLazy.fxml"));

                this.vbBusquedaPracticas.getChildren().add(xL.load());
                ctrlPracticaLazy xPL = xL.<ctrlPracticaLazy>getController();

                xPL.setPracticaLazy(xP);
                xPL.addListenerError(this);
                xPL.addListenerPractica(this);
                
                Practicas.add(xPL);
            }
            
            Practicas.stream().forEach((xP) -> {
                xP.setResultados(Practicas);
            });
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }

    @Override
    public void PracticaSeleccionada(clsPractica practica) {
        try
        {
            if(practica != null)
            {
                Trabajo.setPractica(practica);
                this.tpPractica.setText(practica.getNombreyCodigo());
                this.tpPractica.setVisible(true);
                this.tpPractica.setExpanded(true);  
                
                this.lblLeyendaOS.setText("Obra Social (" 
                        + lblOS.getText() + ")");
                this.lblCostoOS.setText(practica.getCosto()
                        .getValorVigente(Trabajo.getPaciente().getOS(), 
                                Trabajo.getProgramacion().getFecha())
                        .getCostoOS().pdValorString());
                this.lblCostoCoseguro.setText(practica.getCosto()
                        .getValorVigente(Trabajo.getPaciente().getOS(), 
                                Trabajo.getProgramacion().getFecha())
                        .getCoseguro().pdValorString());
                this.lblDuracion.setText(practica.getDuracion().getHoraStr() + " hs.");
            }
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}  
    }
    
    @FXML
    private void btnTurnos_Click(ActionEvent event)
    {
        try
        {
            clsGestorNavegacion.Instanciar()
                    .Abrir("/jfxconsultorio/Practicas/fxTurnos.fxml", 
                            "Turnos", "turnos.png", Trabajo.getProgramacion().getFecha());
        }
        catch(Exception ex)
        {
            clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());
        }
    }
    
    public void btnPaciente_Click(ActionEvent event)
    {}
}
