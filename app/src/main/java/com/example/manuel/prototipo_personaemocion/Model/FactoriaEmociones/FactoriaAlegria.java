package com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones;

import java.util.ArrayList;

/**
 * Created by Manuel on 15/03/2016.
 */
public class FactoriaAlegria implements FactoriaEmocion {
    protected String tipoCuestionario;
    protected int preguntaActual;

    public FactoriaAlegria(String Cuestionario, int pregActual){
        tipoCuestionario = Cuestionario;
        preguntaActual = pregActual;
    }

    public Emocion creaObjeto(ArrayList<String> tiposYaIncluidos){
        return new Alegria(tipoCuestionario,preguntaActual,tiposYaIncluidos);
    }
}
