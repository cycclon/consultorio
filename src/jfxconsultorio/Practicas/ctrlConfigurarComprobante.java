/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Bitacoras.Comprobantes.bComprobanteEditado;
import com.PRS.Framework.Archivos.clsGestorArchivos;
import com.PRS.Framework.FormulariosFX.clsGestorMensajes;
import com.PRS.Framework.FormulariosFX.iMensajeable;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.HBox;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jfxconsultorio.clsConsultorio;

/**
 * FXML Controller class
 *
 * @author ARSPIDALIERI
 */
public class ctrlConfigurarComprobante implements Initializable, iMensajeable {

    @FXML private HTMLEditor heComprobante;
    
    @FXML private HBox hbComponentes;
    
    @FXML private Label lblMensaje;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try
        {
            FXMLLoader xL = new FXMLLoader(getClass()
                    .getResource("/jfxconsultorio/Informes/fxComponenteInforme.fxml"));
            this.hbComponentes.getChildren().add(xL.load());
            this.CargarComprobante();
            this.AgregarBotonImagen(heComprobante);
            this.AgregarBotonTabla(heComprobante);
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }    
    
    @Override
    public String getNombre() {return "Configurar Comprobante";}

    @Override
    public Label getCanvas() {return this.lblMensaje;}
    
    private void CargarComprobante()
    {
        try
        {
            String fullPath = "\\\\" + clsConsultorio.Instanciar()
                    .getServerNameOrAddress() + "\\SomaShared\\comprobante.html";
            File file = new File(fullPath);
            if(file.exists())
            {this.heComprobante.setHtmlText(clsGestorArchivos.Instanciar().readFile(file));}
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
    }
    
    @FXML
    private void btnGuardar_Click(ActionEvent event)
    {
        try
        {
            String path = new File(".").getCanonicalPath();
            String fullPath = "\\\\" + clsConsultorio.Instanciar()
                    .getServerNameOrAddress() + "\\SomaShared\\comprobante.html";
            File file = new File(fullPath);
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(this.heComprobante.getHtmlText());
            }
            
            new bComprobanteEditado().Registrar();
            
            clsGestorMensajes.Instanciar().MostrarMensaje(lblMensaje, 
                    "Se guardó el comprobante de turno");            
        }
        catch(Exception ex)
        {clsGestorMensajes.Instanciar().MostrarError(this, ex.getMessage());}
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
                String img = "";
                    /* "<img src=\"data:Image/jpeg;base64,"+ javax.xml.bind.DatatypeConverter.printBase64Binary(bos.toByteArray()) +"\" />";*/
                    
                    
                
                
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
