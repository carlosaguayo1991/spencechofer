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
public class Respuesta extends SugarRecord implements Serializable {
    @Expose
    private String idviaje;
    @Expose
    private String qr;
    @Expose
    public String campo1;
    @Expose
    public String texto1;
    @Expose
    public String respuesta1;
    @Expose
    public String campo2;
    @Expose
    public String texto2;
    @Expose
    public String respuesta2;
    @Expose
    public String campo3;
    @Expose
    public String texto3;
    @Expose
    public String respuesta3;
    @Expose
    public String campo4;
    @Expose
    public String texto4;
    @Expose
    public String respuesta4;
    @Expose
    public String campo5;
    @Expose
    public String texto5;
    @Expose
    public String respuesta5;
    @Expose
    public String campo6;
    @Expose
    public String texto6;
    @Expose
    public String respuesta6;
    @Expose
    public String campo7;
    @Expose
    public String texto7;
    @Expose
    public String respuesta7;
    @Expose
    public String campo8;
    @Expose
    public String texto8;
    @Expose
    public String respuesta8;
    @Expose
    public String campo9;
    @Expose
    public String texto9;
    @Expose
    public String respuesta9;
    @Expose
    public String campo10;
    @Expose
    public String texto10;
    @Expose
    public String respuesta10;
}
