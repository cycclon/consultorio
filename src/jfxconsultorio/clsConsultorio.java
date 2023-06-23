/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio;

import com.PRS.Framework.Archivos.clsGestorArchivos;
import java.io.File;

/**
 *
 * @author cycclon
 */
public class clsConsultorio {
    
    private static clsConsultorio Instancia;
    private String Server;
    
    private clsConsultorio() throws Exception
    {
        Server = "";
        UpdateServer();
    }
    
    public static clsConsultorio Instanciar() throws Exception
    {
        if(Instancia == null)Instancia = new clsConsultorio();
        return Instancia;
    }
    
    public String getServerNameOrAddress(){return Server;}
    
    public void UpdateServer() throws Exception
    {

        String path = new File(".").getCanonicalPath();
        String fullPath = path.replace("\\", "/") + "/";
        File file = new File(fullPath + "base.txt");

        if(file.exists()){Server = clsGestorArchivos.Instanciar().readFile(file);}
    }
}
