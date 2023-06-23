/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxconsultorio.Gastos;

import com.PRS.Consultorio.Gastos.clsGasto;

/**
 *
 * @author ARSPIDALIERI
 */
public interface iGastos {
    public void GastoRegistrado(clsGasto gasto);
    
    public void GastoProgramadoEliminad(clsGasto gasto);
    
    public void GastoEliminado(clsGasto gasto);
}
