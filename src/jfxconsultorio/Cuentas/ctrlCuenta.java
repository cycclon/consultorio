/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Cuentas;

import com.PRS.Framework.Cuentas.clsCuenta;
import com.PRS.Framework.Cuentas.clsMovimiento;
import com.PRS.Framework.FormulariosFX.iReceptorError;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author ARSPIDALIERI
 */
public class ctrlCuenta implements Initializable, iReceptorError {

    @FXML private Label lblSaldoInicial;
    @FXML private Label lblSaldoFinal;
    @FXML private VBox vbMovimientos;
    
    private List<iReceptorError> ListenersError;
    private clsCuenta Cuenta;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.ListenersError = new ArrayList<>();
    }
    
    public void addListenerError(iReceptorError listener)
    {this.ListenersError.add(listener);}
    
    private void RaiseError(String error)
    {
        ListenersError.stream().forEach((xRE) -> {
            xRE.ErrorOcurred(error);
        });
    }
    
    public void setCuenta(clsCuenta cuenta)
    {this.Cuenta = cuenta; this.MostrarCuenta();}
    
    private void MostrarCuenta()
    {
        try
        {
            this.lblSaldoInicial.setText(Cuenta.getSaldoInicial().pdValorString());
            this.lblSaldoFinal.setText(Cuenta.getSaldo().pdValorString());
            
            this.vbMovimientos.getChildren().clear();
            for(clsMovimiento xM : Cuenta.getMovimientos())
            {
                FXMLLoader xL = new FXMLLoader(getClass()
                        .getResource("/jfxconsultorio/Cuentas/fxMovimiento.fxml"));
                this.vbMovimientos.getChildren().add(xL.load());
                ctrlMovimiento xCM = xL.<ctrlMovimiento>getController();
                xCM.addListenerError(this);
                xCM.setMovimiento(xM);                
            }
            
            if(this.Cuenta.getMovimientos().isEmpty())
            {
                Label lblEmpty = new Label("Esta cuenta no tuvo movimientos");
                this.vbMovimientos.getChildren().add(lblEmpty);
            }
            
        }
        catch(Exception ex)
        {RaiseError(ex.getMessage());}
    }

    @Override
    public void ErrorOcurred(String prMensaje) {RaiseError(prMensaje);}
}
