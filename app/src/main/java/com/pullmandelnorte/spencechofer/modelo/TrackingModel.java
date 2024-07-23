package com.pullmandelnorte.spencechofer.modelo;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TrackingModel {
    @Expose
    private String idViaje;
    @Expose
    private String latitud;
    @Expose
    private String longitud;
}
