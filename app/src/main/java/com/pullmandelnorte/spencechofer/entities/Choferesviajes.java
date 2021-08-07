package com.pullmandelnorte.spencechofer.entities;

import com.google.gson.annotations.Expose;
import com.orm.SugarRecord;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class Choferesviajes extends SugarRecord implements Serializable {
    public String idviaje;
    @Expose
    public String condicion_chofer_principal;
    @Expose
    public String choferes_principal;
    @Expose
    public String nombre_principal;

    public boolean realizado_principal;
    @Expose
    public String condicion_chofer_secundaria;
    @Expose
    public String choferes_secundaria;
    @Expose
    public String nombre_chofer_secundaria;

    public boolean realizado_secundario;
    @Expose
    public String condicion_auxiliar;
    @Expose
    public String auxiliar;
    @Expose
    public String nombre_auxiliar;

    public boolean realizado_auxiliar;
}
