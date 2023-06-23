/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jfxconsultorio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.PRS.Framework.AccesoADatos.*;
import com.PRS.Framework.Archivos.clsGestorArchivos;
import com.PRS.Framework.FormulariosFX.clsGestorInterfaz;
import java.io.File;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.WindowEvent;

/**
 *
 * @author ARSpidalieri
 */
public class JfxConsultorio extends Application {
    
    public static Stage stage;
    
    @Override
    public void start(Stage primaryStage) {
        
        
        try
        {
            primaryStage.getIcons().add(new Image("file:icon.png"));
            String base;
            String path = new File(".").getCanonicalPath();
            String fullPath = path.replace("\\", "/") + "/";
            File file = new File(fullPath + "base.txt");
            Parent root;
            if(file.exists()){
               base = clsGestorArchivos.Instanciar().readFile(file);
               clsGestorBases xG = clsGestorBases.Instanciar();
               
               try
               {
                    xG.AgregarBaseSQLServer((byte)1, base + "\\SQLEXPRESS", 
                         "consultorio", "doctor", "SosaMagnano");

                    /*xG.EstablecerBaseActiva((byte)1);
                    xG.CrearComando(CommandType.Text, "SELECT * FROM practica");
                    ResultSet rs = xG.EjecutarConsulta();
                    CachedRowSetImpl crs = new CachedRowSetImpl();
                    crs.populate(rs);
                    int id = 1;
                    
                    while(crs.next())
                    {
                        String pathInforme = "\\\\" + clsConsultorio.Instanciar()
                                .getServerNameOrAddress() + "\\SomaShared\\InformesHTML\\";
                        File fileP = new File(pathInforme + "plantilla-" + crs.getInt("pra_codigo") + ".html");
                        if(fileP.exists())
                        {
                            xG.CrearComando(CommandType.Text, 
                                    "INSERT INTO informe VALUES(" + id + ", 'default', 'plantilla-" + crs.getInt("pra_codigo") + ".html'," + crs.getShort("pra_id") + ")");
                            xG.EjecutarComando();
                            id++;
                        }
                    }*/
                    
                    root = FXMLLoader.load(getClass()
                            .getResource("/jfxconsultorio/Acceso/fxLogin.fxml"));

                    
               }
               catch(Exception ex)
               {
                   root = FXMLLoader.load(getClass()
                           .getResource("/jfxconsultorio/Configuracion/fxBase.fxml"));
               }                
            }
            else
            {root = FXMLLoader.load(getClass()
                           .getResource("/jfxconsultorio/Configuracion/fxBase.fxml"));}
            
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);

            primaryStage.setTitle("Administración de consultorio Sosa Magnano");
            primaryStage.setOpacity(1);
            primaryStage.show();
            
            stage = primaryStage;

            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
            primaryStage.setWidth(bounds.getWidth());
            primaryStage.setHeight(bounds.getHeight());
            
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                      @Override
                      public void handle(WindowEvent we) {
                          clsGestorInterfaz.Instanciar().Dispose();
                      }
                  });   
            
        }
        catch(Exception ex){System.out.println(ex.getMessage());}
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
