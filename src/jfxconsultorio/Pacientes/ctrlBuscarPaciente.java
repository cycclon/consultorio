/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Pacientes;

import com.PRS.Consultorio.Pacientes.clsBuscadorPacientes;
import com.PRS.Consultorio.Pacientes.clsPacienteLazy;
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
public class ctrlBuscarPaciente implements Initializable {
    
    public interface iBusquedaPaciente
    {public void VerResultados(List<clsPacienteLazy> resultados);}

    @FXML RadioButton rdApellido;
        
    @FXML TextField txtBusqueda;
    
    private List<iReceptorError> ListenersError;
    
    private List<iBusquedaPaciente> ListenersBusqueda;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.ListenersError = new ArrayList<>();
        this.ListenersBusqueda = new ArrayList<>();
        this.rdApellido.setUserData(clsBuscadorPacientes.enParametroBusquedaPaciente.Apellido);
        
        txtBusqueda.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                try
                {
                    List<clsPacienteLazy> xPs = new ArrayList<>();

                    if(txtBusqueda.getText().length() >= 2)
                    {
                        rdApellido_CheckChanged();
                        xPs = clsBuscadorPacientes.Instanciar().Buscar(
                                (clsBuscadorPacientes.enParametroBusquedaPaciente)rdApellido.getUserData(), 
                                txtBusqueda.getText());
                        MostrarResultados(xPs);
                    }
                    
                }
                catch(Exception ex)
                {RaiseError(ex.getMessage());}
            }
        });
        
    }
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    public void addListenerBusqueda(iBusquedaPaciente listener)
    {this.ListenersBusqueda.add(listener);}
    
    private void MostrarResultados(List<clsPacienteLazy> resultados)
    {
        ListenersBusqueda.stream().forEach((xB) -> {
            xB.VerResultados(resultados);
        });
    }
    
    private void RaiseError(String prError)
    {
        ListenersError.stream().forEach((xR) -> {
            xR.ErrorOcurred(prError);
        });
    }
    
    private void rdApellido_CheckChanged()
    {
        if(rdApellido.isSelected())
        {this.rdApellido.setUserData(clsBuscadorPacientes.enParametroBusquedaPaciente.Apellido);}
        else
        {this.rdApellido.setUserData(clsBuscadorPacientes.enParametroBusquedaPaciente.Documento);}
    }
}
