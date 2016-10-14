package com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones;

import java.util.ArrayList;

/**
 * Created by Manuel on 05/04/2016.
 */
public class FactoriaTemor implements FactoriaEmocion{
    protected String tipoCuestionario;
    protected int preguntaActual;

    public FactoriaTemor(String Cuestionario, int pregActual){
        tipoCuestionario = Cuestionario;
        preguntaActual = pregActual;
    }

    public Emocion creaObjeto(ArrayList<String> tiposYaIncluidos){
        return new Temor(tipoCuestionario,preguntaActual,tiposYaIncluidos);
    }
}
