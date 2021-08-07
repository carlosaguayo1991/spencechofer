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
public class Preguntas extends SugarRecord  implements Serializable {
    @Expose
    public String campo;
    @Expose
    public String texto;
    @Expose
    public String tipodato;
    @Expose
    public String columna;
    @Expose
    public String respuesta;

}
