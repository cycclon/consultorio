/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Informes;

import com.PRS.Consultorio.Bitacoras.Informes.bEncabezadoYPie;
import com.PRS.Consultorio.Informes.iComponenteInforme;
import com.PRS.Framework.Archivos.clsGestorArchivos;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jfxconsultorio.clsConsultorio;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlConfigurarInforme implements Initializable, 
        ctrlComponenteInforme.iListenerComponenteInforme, iReceptorError, iMensajeable {

    @FXML private Label lblMensaje;
    @FXML private HTMLEditor heEncabezado;
    @FXML private HTMLEditor hePie;
    
    
    @FXML private VBox vbComponente;
    
    private HTMLEditor heFocused;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
      
       
        this.CargarArchivos();
        
        heFocused = heEncabezado;
        
        heEncabezado.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if(oldPropertyValue)
                {heFocused = heEncabezado;}
                else
                {heFocused = hePie;}
            }
        });
        
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Informes/fxComponenteInforme.fxml"));
            this.vbComponente.getChildren().add(xL.load());
            ctrlComponenteInforme xCI = xL.<ctrlComponenteInforme>getController();
            xCI.addListenerCI(this);
            xCI.addListenerError(this);
            this.AgregarBotonImagen(heEncabezado);
            this.AgregarBotonImagen(hePie);
            this.AgregarBotonTabla(heEncabezado);
            this.AgregarBotonTabla(hePie);
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
    @Override
    public String getNombre() {return "Configurar informe";}
    
    private void CargarArchivos()
    {
        try
        {
            String path = new File(".").getCanonicalPath();
            String fullPath = "\\\\" + clsConsultorio.Instanciar().getServerNameOrAddress() + "\\SomaShared\\InformesHTML\\";
            File file = new File(fullPath + "encabezado.html");
            if(file.exists()){
                this.heEncabezado.setHtmlText(clsGestorArchivos.Instanciar().readFile(file));
            }
            
            File file2 = new File(fullPath + "pie.html");
            if(file2.exists()){
                this.hePie.setHtmlText(clsGestorArchivos.Instanciar().readFile(file2));
            }
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
    
    
    @FXML
    private void btnGuardarCambios_Click(ActionEvent event)
    {
        try {
            
            String path = new File(".").getCanonicalPath();
            String fullPath = "\\\\" + clsConsultorio.Instanciar().getServerNameOrAddress() + "\\SomaShared\\InformesHTML\\";
                    
            File file = new File(fullPath + "encabezado.html");
            File file2 = new File(fullPath + "pie.html");
            
            FileWriter fileWriter =  new FileWriter(file);
            fileWriter.write(this.heEncabezado.getHtmlText());
            fileWriter.close();
            
            fileWriter = new FileWriter(file2);
            fileWriter.write(this.hePie.getHtmlText());
            fileWriter.close();
            
            new bEncabezadoYPie().Registrar();
            
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                    "Se guardaron los cambios en el encabezado y pie de página "
                            + "de los informes.");
        } catch (Exception ex) {
            clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());
        }
    }

    @Override
    public void ComponenteSeleccionado(iComponenteInforme componente) {
        try
        {
           
        
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
        
    }

    @Override
    public void ErrorOcurred(String prMensaje) {
        clsGestorMensajes.Instanciar().MostrarError(this, prMensaje);
    }

    @Override
    public Label getCanvas() {return this.lblMensaje;}
    
    @FXML
    private void heEncabezado_FocusChanged(Event event)
    {
        if(heEncabezado.isFocused())
        {heFocused = heEncabezado;}
        else
        {heFocused = hePie;}
    }
    
    private void AgregarBotonTabla(HTMLEditor htmlEditor) throws Exception
    {
        Node node = htmlEditor.lookup(".top-toolbar");
        if (node instanceof ToolBar) {
          ToolBar bar = (ToolBar) node;
          ImageView graphic = new ImageView(new Image("/jfxconsultorio/Imagenes/Iconos/table.png", 20, 20, true, true));
          Button smurfButton = new Button("", graphic);
          bar.getItems().add(smurfButton);
          Node webNode = htmlEditor.lookup(".web-view");
          
          WebView webView = (WebView) webNode;
          
            WebEngine engine = webView.getEngine();
            
          smurfButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent arg0)  {
                String table = "<table><tr><td>1</td><td>2</td></tr></table>";
                
                String jsCodeInsertHtml = "function insertHtmlAtCursor(html) {\n" +
                    "    var range, node;\n" +
                    "    if (window.getSelection && window.getSelection().getRangeAt) {\n" +
                    "        range = window.getSelection().getRangeAt(0);\n" +
                    "        node = range.createContextualFragment(html);\n" +
                    "        range.insertNode(node);\n" +
                    "    } else if (document.selection && document.selection.createRange) {\n" +
                    "        document.selection.createRange().pasteHTML(html);\n" +
                    "    }\n" +
                    "}insertHtmlAtCursor('####html####')";
            engine.executeScript(jsCodeInsertHtml.
                            replace("####html####",
                                    escapeJavaStyleString(table, true, true)));
                    
            } 
            });
          
        }
    }
    
    private void AgregarBotonImagen(HTMLEditor htmlEditor) throws Exception
    {
        Node node = htmlEditor.lookup(".top-toolbar");
        if (node instanceof ToolBar) {
          ToolBar bar = (ToolBar) node;
          ImageView graphic = new ImageView(new Image("/jfxconsultorio/Imagenes/Iconos/image.png", 20, 20, true, true));
          Button smurfButton = new Button("", graphic);
          bar.getItems().add(smurfButton);
          Node webNode = htmlEditor.lookup(".web-view");
          
          WebView webView = (WebView) webNode;
          
            WebEngine engine = webView.getEngine();
            
          smurfButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent arg0)  {
                try
                {
                    FileChooser fs = new FileChooser();
                    fs.setTitle("Seleccionar imagen");
                    File file = fs.showOpenDialog(new Stage());
                    
                    if(file != null)
                    {
                    FileInputStream fis = new FileInputStream(file);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    try {
                        for (int readNum; (readNum = fis.read(buf)) != -1;) {
                            //Writes to this byte array output stream
                            bos.write(buf, 0, readNum); 
                            System.out.println("read " + readNum + " bytes,");
                        }
                    } catch (IOException ex) {

                }
/*                 String img =
                   "<img src=\"data:Image/jpeg;base64,"+ javax.xml.bind.DatatypeConverter.printBase64Binary(bos.toByteArray()) +"\" />";*/
                   String img = "";
                    
                
                
            //http://stackoverflow.com/questions/2213376/how-to-find-cursor-position-in-a-contenteditable-div
            String jsCodeInsertHtml = "function insertHtmlAtCursor(html) {\n" +
                    "    var range, node;\n" +
                    "    if (window.getSelection && window.getSelection().getRangeAt) {\n" +
                    "        range = window.getSelection().getRangeAt(0);\n" +
                    "        node = range.createContextualFragment(html);\n" +
                    "        range.insertNode(node);\n" +
                    "    } else if (document.selection && document.selection.createRange) {\n" +
                    "        document.selection.createRange().pasteHTML(html);\n" +
                    "    }\n" +
                    "}insertHtmlAtCursor('####html####')";
            engine.executeScript(jsCodeInsertHtml.
                            replace("####html####",
                                    escapeJavaStyleString(img, true, true)));
                    }
            }
                catch(Exception ex){}
            }
            
          });
          
        }
    }
private static String escapeJavaStyleString(String str,
            boolean escapeSingleQuote, boolean escapeForwardSlash) {
        StringBuilder out = new StringBuilder("");
        if (str == null) {
            return null;
        }
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);

            // handle unicode
            if (ch > 0xfff) {
                out.append("\\u").append(hex(ch));
            } else if (ch > 0xff) {
                out.append("\\u0").append(hex(ch));
            } else if (ch > 0x7f) {
                out.append("\\u00").append(hex(ch));
            } else if (ch < 32) {
                switch (ch) {
                    case '\b':
                        out.append('\\');
                        out.append('b');
                        break;
                    case '\n':
                        out.append('\\');
                        out.append('n');
                        break;
                    case '\t':
                        out.append('\\');
                        out.append('t');
                        break;
                    case '\f':
                        out.append('\\');
                        out.append('f');
                        break;
                    case '\r':
                        out.append('\\');
                        out.append('r');
                        break;
                        default:
                        if (ch > 0xf) {
                            out.append("\\u00").append(hex(ch));
                        } else {
                            out.append("\\u000").append(hex(ch));
                        }
                        break;
                }
            } else {
                switch (ch) {
                    case '\'':
                        if (escapeSingleQuote) {
                            out.append('\\');
                        }
                        out.append('\'');
                        break;
                    case '"':
                        out.append('\\');
                        out.append('"');
                        break;
                    case '\\':
                        out.append('\\');
                        out.append('\\');
                        break;
                    case '/':
                        if (escapeForwardSlash) {
                            out.append('\\');
                        }
                        out.append('/');
                        break;
                    default:
                        out.append(ch);
                        break;
                }
            }
        }
        return out.toString();
    }
    
    private static String hex(int i) {
        return Integer.toHexString(i);
    }
    
}
