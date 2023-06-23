/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jfxconsultorio.Controles;

import com.PRS.Consultorio.Alertas.clsGestorAlertas;
import com.PRS.Consultorio.Usuarios.clsGestorUsuariosConsultorio;
import com.PRS.Framework.Acceso.clsGestorAcceso;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlMenu implements Initializable {
    
    @FXML VBox vbMenu;
    
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try
        {
            
            MostrarMenu();

        }
        catch(Exception ex){System.out.println(ex.getMessage());}
        
    }
    
    private void MostrarMenu() throws Exception
    {
        HBox hb1 = new HBox();
        HBox hb2 = new HBox();
        HBox hb3 = new HBox();
        hb1.setSpacing(15);
        hb2.setSpacing(15);
        hb3.setSpacing(15);
        
        //OBRAS SOCIALES
        if(clsGestorAcceso.Instanciar().ComprobarPermisos
        (clsGestorUsuariosConsultorio.Instanciar().ObtenerAccesor(), (short)1))
        {
            FXMLLoader xL1 = new FXMLLoader(getClass().
                    getResource("/jfxconsultorio/Controles/fxCtrlOpcion.fxml"));
            hb1.getChildren().add(xL1.load());
            ctrlOpcion xO1 = xL1.<ctrlOpcion>getController();
            xO1.setIcono("os.png");
            xO1.setNombre("Obras Sociales");
            xO1.setNotificaciones((byte)0);
            xO1.setFormulario("/jfxconsultorio/ObrasSociales/fxObrasSociales.fxml");
        }  
        //CENTROS DE COSTOS
        if(clsGestorAcceso.Instanciar().ComprobarPermisos
        (clsGestorUsuariosConsultorio.Instanciar().ObtenerAccesor(), (short)2))
        {
            FXMLLoader xL2 = new FXMLLoader(getClass().
                    getResource("/jfxconsultorio/Controles/fxCtrlOpcion.fxml"));
            hb1.getChildren().add(xL2.load());
            ctrlOpcion xO2 = xL2.<ctrlOpcion>getController();
            xO2.setIcono("costos.png");
            xO2.setNombre("Centros de costos");
            xO2.setNotificaciones((byte)0);
            xO2.setFormulario("/jfxconsultorio/CentrosCosto/fxCentrosCosto.fxml");
        }
        
        //COLEGIO
        if(clsGestorAcceso.Instanciar().ComprobarPermisos
        (clsGestorUsuariosConsultorio.Instanciar().ObtenerAccesor(), (short)3))
        {
            FXMLLoader xL3 = new FXMLLoader(getClass().
                    getResource("/jfxconsultorio/Controles/fxCtrlOpcion.fxml"));
            hb1.getChildren().add(xL3.load());
            ctrlOpcion xO3 = xL3.<ctrlOpcion>getController();
            xO3.setIcono("colegio.png");
            xO3.setNombre("Colegio");
            xO3.setNotificaciones((byte)0);
            xO3.setFormulario("/jfxconsultorio/Colegio/fxColegio.fxml");
        }
        
        //GASTOS
        if(clsGestorAcceso.Instanciar().ComprobarPermisos
        (clsGestorUsuariosConsultorio.Instanciar().ObtenerAccesor(), (short)4))
        {
            FXMLLoader xL4 = new FXMLLoader(getClass().
                    getResource("/jfxconsultorio/Controles/fxCtrlOpcion.fxml"));
            hb1.getChildren().add(xL4.load());
            ctrlOpcion xO4 = xL4.<ctrlOpcion>getController();
            xO4.setIcono("gastos.png");
            xO4.setNombre("Gastos");
            xO4.setNotificaciones((byte)0);
            xO4.setFormulario("/jfxconsultorio/Gastos/fxGastos.fxml");
        }
        //REPORTES
        if(clsGestorAcceso.Instanciar().ComprobarPermisos
        (clsGestorUsuariosConsultorio.Instanciar().ObtenerAccesor(), (short)5))
        {
            FXMLLoader xL5 = new FXMLLoader(getClass().
                    getResource("/jfxconsultorio/Controles/fxCtrlOpcion.fxml"));
            hb1.getChildren().add(xL5.load());
            ctrlOpcion xO5 = xL5.<ctrlOpcion>getController();
            xO5.setIcono("reportes.png");
            xO5.setNombre("Reportes");
            xO5.setNotificaciones((byte)0);
            xO5.setFormulario("/jfxconsultorio/Reportes/fxReportes.fxml");
        }
        //PRACTICAS
        if(clsGestorAcceso.Instanciar().ComprobarPermisos
        (clsGestorUsuariosConsultorio.Instanciar().ObtenerAccesor(), (short)6))
        {
            FXMLLoader xL6 = new FXMLLoader(getClass().
                    getResource("/jfxconsultorio/Controles/fxCtrlOpcion.fxml"));
            hb2.getChildren().add(xL6.load());
            ctrlOpcion xO6 = xL6.<ctrlOpcion>getController();
            xO6.setIcono("practicas.png");
            xO6.setNombre("Prácticas");
            xO6.setNotificaciones((byte)0);
            xO6.setFormulario("/jfxconsultorio/Practicas/fxPracticas.fxml");
        }
        //PROFESIONALES
        if(clsGestorAcceso.Instanciar().ComprobarPermisos
        (clsGestorUsuariosConsultorio.Instanciar().ObtenerAccesor(), (short)8))
        {
            FXMLLoader xL7 = new FXMLLoader(getClass().
                    getResource("/jfxconsultorio/Controles/fxCtrlOpcion.fxml"));
            hb2.getChildren().add(xL7.load());
            ctrlOpcion xO7 = xL7.<ctrlOpcion>getController();
            xO7.setIcono("profesionales.png");
            xO7.setNombre("Profesionales");
            xO7.setNotificaciones((byte)0);
            xO7.setFormulario("/jfxconsultorio/Profesionales/fxProfesionales.fxml");
        }
        //TURNOS
        if(clsGestorAcceso.Instanciar().ComprobarPermisos
        (clsGestorUsuariosConsultorio.Instanciar().ObtenerAccesor(), (short)10))
        {
            FXMLLoader xL8 = new FXMLLoader(getClass().
                    getResource("/jfxconsultorio/Controles/fxCtrlOpcion.fxml"));
            hb2.getChildren().add(xL8.load());
            ctrlOpcion xO8 = xL8.<ctrlOpcion>getController();
            xO8.setIcono("turnos.png");
            xO8.setNombre("Turnos");         
            xO8.setNotificaciones((byte)0);
            /*xO8.setNotificaciones((byte)clsGestorAlertas.Instanciar()
                    .ObtenerAlertasTrabajo().size());*/
            xO8.setFormulario("/jfxconsultorio/Practicas/fxTurnos.fxml");
            xO8.setParametro(new Date());
        }
        //BALANCES
        if(clsGestorAcceso.Instanciar().ComprobarPermisos
        (clsGestorUsuariosConsultorio.Instanciar().ObtenerAccesor(), (short)11))
        {
            FXMLLoader xL9 = new FXMLLoader(getClass().
                    getResource("/jfxconsultorio/Controles/fxCtrlOpcion.fxml"));
            hb2.getChildren().add(xL9.load());
            ctrlOpcion xO9 = xL9.<ctrlOpcion>getController();
            xO9.setIcono("balances.png");
            xO9.setNombre("Balances");
            xO9.setNotificaciones((byte)0);
            xO9.setFormulario("/jfxconsultorio/Balances/fxBalances.fxml");
        }
        //PACIENTES
        if(clsGestorAcceso.Instanciar().ComprobarPermisos
        (clsGestorUsuariosConsultorio.Instanciar().ObtenerAccesor(), (short)12))
        {
        FXMLLoader xL10 = new FXMLLoader(getClass().
                getResource("/jfxconsultorio/Controles/fxCtrlOpcion.fxml"));
        hb2.getChildren().add(xL10.load());
        ctrlOpcion xO10 = xL10.<ctrlOpcion>getController();
        xO10.setIcono("pacientes.png");
        xO10.setNombre("Pacientes");
        xO10.setNotificaciones((byte)0);
        xO10.setFormulario("/jfxconsultorio/Pacientes/fxPacientes.fxml");
        }
        //INFORMES
        if(clsGestorAcceso.Instanciar().ComprobarPermisos
        (clsGestorUsuariosConsultorio.Instanciar().ObtenerAccesor(), (short)14))
        {
            FXMLLoader xL11 = new FXMLLoader(getClass().
                    getResource("/jfxconsultorio/Controles/fxCtrlOpcion.fxml"));
            hb3.getChildren().add(xL11.load());
            ctrlOpcion xO11 = xL11.<ctrlOpcion>getController();
            xO11.setIcono("informes.png");
            xO11.setNombre("Informes");
            xO11.setNotificaciones((byte)0);
            xO11.setFormulario("/jfxconsultorio/Informes/fxConfigurarInforme.fxml");
        }
        //CAMPOS
        if(clsGestorAcceso.Instanciar().ComprobarPermisos
        (clsGestorUsuariosConsultorio.Instanciar().ObtenerAccesor(), (short)15))
        {
            FXMLLoader xL12 = new FXMLLoader(getClass().
                    getResource("/jfxconsultorio/Controles/fxCtrlOpcion.fxml"));
            hb3.getChildren().add(xL12.load());
            ctrlOpcion xO12 = xL12.<ctrlOpcion>getController();
            xO12.setIcono("campo.png");
            xO12.setNombre("Campos");
            xO12.setNotificaciones((byte)0);
            xO12.setFormulario("/jfxconsultorio/Practicas/fxCamposTrabajo.fxml");
        }
        //COMPROBANTE TURNO
        if(clsGestorAcceso.Instanciar().ComprobarPermisos
        (clsGestorUsuariosConsultorio.Instanciar().ObtenerAccesor(), (short)16))
        {
            FXMLLoader xL13 = new FXMLLoader(getClass().
                    getResource("/jfxconsultorio/Controles/fxCtrlOpcion.fxml"));
            hb3.getChildren().add(xL13.load());
            ctrlOpcion xO13 = xL13.<ctrlOpcion>getController();
            xO13.setIcono("comprobante.png");
            xO13.setNombre("Comprobante de Turno");
            xO13.setNotificaciones((byte)0);
            xO13.setFormulario("/jfxconsultorio/Practicas/fxConfigurarComprobante.fxml");
        }
        
        //Hoja de turnos
        if(clsGestorAcceso.Instanciar().ComprobarPermisos
        (clsGestorUsuariosConsultorio.Instanciar().ObtenerAccesor(), (short)16))
        {
            FXMLLoader xL13 = new FXMLLoader(getClass().
                    getResource("/jfxconsultorio/Controles/fxCtrlOpcion.fxml"));
            hb3.getChildren().add(xL13.load());
            ctrlOpcion xO13 = xL13.<ctrlOpcion>getController();
            xO13.setIcono("hojaturnos.png");
            xO13.setNombre("Hoja de Turnos");
            xO13.setNotificaciones((byte)0);
            xO13.setFormulario("/jfxconsultorio/Practicas/fxHojaTurnos.fxml");
        }
        
        
        //REGISTRO DE ACTIVIDAD
        if(clsGestorAcceso.Instanciar().ComprobarPermisos
        (clsGestorUsuariosConsultorio.Instanciar().ObtenerAccesor(), (short)29))
        {
            FXMLLoader xL14 = new FXMLLoader(getClass().
                    getResource("/jfxconsultorio/Controles/fxCtrlOpcion.fxml"));
            hb3.getChildren().add(xL14.load());
            ctrlOpcion xO14 = xL14.<ctrlOpcion>getController();
            xO14.setIcono("registro.png");
            xO14.setNombre("Registro de actividad");
            xO14.setNotificaciones((byte)0);
            xO14.setFormulario("/jfxconsultorio/Bitacoras/fxBitacoras.fxml");
        }
        hb1.setSpacing(10);
        this.vbMenu.getChildren().add(hb1);
        this.vbMenu.getChildren().add(hb2);
        this.vbMenu.getChildren().add(hb3);
        
        
    }    
    
}
