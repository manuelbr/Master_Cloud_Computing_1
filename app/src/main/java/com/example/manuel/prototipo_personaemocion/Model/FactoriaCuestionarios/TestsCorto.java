package com.example.manuel.prototipo_personaemocion.Model.FactoriaCuestionarios;

import com.example.manuel.prototipo_personaemocion.Model.Pregunta;

import java.util.ArrayList;
/**
 * Created by Manuel on 07/03/2016.
 */
public class TestsCorto extends Tests {
    //En los test cortos se medirán unicamente 4 tipos de emociones = tristeza, placer, disgusto, alegría.

    public TestsCorto(ArrayList<Pregunta> pregs) {
        creaCuestionario(pregs,"Corto",5);
    }

    @Override
    public Pregunta getPregunta(){
        Pregunta p = preguntasFinales.get(getNumeroPreguntaActual()-1);
        setNumeroPreguntaActual(getNumeroPreguntaActual()+1);

        return p;
    }
}
