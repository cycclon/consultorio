/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Imagenes;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
/**
 *
 * @author ARSPIDALIERI
 */
public class clsGestorImagenes {
    private class clsNamedImage
    {
        private String Nombre;
        private Image Imagen;
        
        public clsNamedImage(String nombre, Image image)
        {
            Nombre = nombre;
            Imagen = image;
        }
        
        private Image getImage(){return Imagen;}
        
        private String getNombre(){return Nombre;}
    }
    
    private static clsGestorImagenes Instancia;
    
    private List<clsNamedImage> Imagenes;   
    
    private clsGestorImagenes()
    {
        Imagenes = new ArrayList<>();
        
        String path;
        Image img;
        
        path = "/jfxconsultorio/Imagenes/Iconos/pago.png";
        img = new Image(path);        
        Imagenes.add(new clsNamedImage("pago", img));
        
        path = "/jfxconsultorio/Imagenes/Iconos/turnos_sm.png";
        img = new Image(path);        
        Imagenes.add(new clsNamedImage("calendario", img));
        
        path = "/jfxconsultorio/Imagenes/Iconos/practicante.png";
        img = new Image(path);        
        Imagenes.add(new clsNamedImage("realizar", img));
        
        path = "/jfxconsultorio/Imagenes/Iconos/firmante_sm.png";
        img = new Image(path);        
        Imagenes.add(new clsNamedImage("firmar", img));
        
        path = "/jfxconsultorio/Imagenes/Iconos/informes_sm.png";
        img = new Image(path);        
        Imagenes.add(new clsNamedImage("imprimir", img));
        
        path = "/jfxconsultorio/Imagenes/Iconos/remove.png";
        img = new Image(path);        
        Imagenes.add(new clsNamedImage("cancelar", img));
        
        path = "/jfxconsultorio/Imagenes/Iconos/copy.png";
        img = new Image(path);        
        Imagenes.add(new clsNamedImage("duplicar", img));
    }
    
    public static clsGestorImagenes getInstancia()
    {if(Instancia == null)Instancia = new clsGestorImagenes();return Instancia;}
    
    public Image getImage(String nombre)
    {
        Image img = null;
        for(int i = 0; i < this.Imagenes.size(); i++)
        {
            if(Imagenes.get(i).getNombre().equalsIgnoreCase(nombre))
            {
                img = Imagenes.get(i).getImage();
                i = this.Imagenes.size();
            }
        }
        
        return img;
    }
}
