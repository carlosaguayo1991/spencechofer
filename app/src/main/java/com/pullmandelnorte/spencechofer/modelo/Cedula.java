package com.pullmandelnorte.spencechofer.modelo;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Cedula {
    @Expose
    private String cedula;
}