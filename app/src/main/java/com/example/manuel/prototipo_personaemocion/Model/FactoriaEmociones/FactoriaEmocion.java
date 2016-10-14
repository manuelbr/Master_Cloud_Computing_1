package com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones;


import java.util.ArrayList;

/**
 * Created by Manuel on 29/11/2015.
 */
public interface FactoriaEmocion {
    public Emocion creaObjeto(ArrayList<String> tiposYaIncluidos);
}
