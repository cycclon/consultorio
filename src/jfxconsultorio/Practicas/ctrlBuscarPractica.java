/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Practicas;

import com.PRS.Consultorio.Practicas.clsBuscadorPracticas;
import com.PRS.Consultorio.Practicas.clsPracticaLazy;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ARSpidalieri
 */
public class ctrlBuscarPractica implements Initializable {
    
    public interface iBusquedaPractica
    {public void VerResultadosPractica(List<clsPracticaLazy> resultados);}

    @FXML private RadioButton rdCodigo;
    @FXML private RadioButton rdNombre;
    
    @FXML private TextField txtBusqueda;
    
    private List<iReceptorError> ListenersError;    
    private List<iBusquedaPractica> ListenersBusqueda;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.ListenersError = new ArrayList<>();
        this.ListenersBusqueda = new ArrayList<>();
        this.rdCodigo.setUserData(clsBuscadorPracticas.enParametroBusquedaPractica.Codigo);
        txtBusqueda.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                try
                {
                    List<clsPracticaLazy> xPs = new ArrayList<>();

                    if(txtBusqueda.getText().length() >=3)
                    {
                        rdCodigo_CheckedChanged();
                        xPs = clsBuscadorPracticas.Instanciar().Buscar(
                                (clsBuscadorPracticas.enParametroBusquedaPractica)rdCodigo.getUserData(), 
                                txtBusqueda.getText());
                    }
                    MostrarResultados(xPs);
                }
                catch(Exception ex)
                {RaiseError(ex.getMessage());}
            }
        });
    }    
    
    @FXML
    private void rdCodigo_CheckedChanged()
    {
        if(rdCodigo.isSelected())
        {this.rdCodigo.setUserData(clsBuscadorPracticas.enParametroBusquedaPractica.Codigo);}
        else
        {this.rdCodigo.setUserData(clsBuscadorPracticas.enParametroBusquedaPractica.Nombre);}
    }
    
    public void MostrarResultados(List<clsPracticaLazy> resultados)
    {
        ListenersBusqueda.stream().forEach((xBP) -> {
            xBP.VerResultadosPractica(resultados);
        });
    }
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    public void addListenerBusqueda(iBusquedaPractica listener)
    {this.ListenersBusqueda.add(listener);}
    
    public void RaiseError(String error)
    {
        ListenersError.stream().forEach((xRE) -> {
            xRE.ErrorOcurred(error);
        });
    }
    
}
