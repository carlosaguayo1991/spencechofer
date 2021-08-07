package com.pullmandelnorte.spencechofer.entities;

import com.google.gson.annotations.Expose;
import com.orm.SugarRecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class Viaje extends SugarRecord {
    @Expose
    private String nroViaje;
    @Expose
    private String ruta;
    @Expose
    private String maquina;
    @Expose
    private String choferPrincipal;
    @Expose
    private String choferSecundario;
    @Expose
    private String auxiliar;
    @Expose
    private String cantidadAsientos;
    @Expose
    private String fechaPartida;
    @Expose
    private String fechaLlegada;
    @Expose
    private String horaPosturaChofer;
    @Expose
    private String horaPartida;
    @Expose
    private String horaLlegada;
    @Expose
    private String trayecto;
}
