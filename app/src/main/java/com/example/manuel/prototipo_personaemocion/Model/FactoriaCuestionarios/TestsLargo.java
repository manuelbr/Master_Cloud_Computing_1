package com.example.manuel.prototipo_personaemocion.Model.FactoriaCuestionarios;

import com.example.manuel.prototipo_personaemocion.Model.Pregunta;

import java.util.ArrayList;

/**
 * Created by Manuel on 07/03/2016.
 */
public class TestsLargo extends Tests {

    //Nuevo Objetivo!!
    public TestsLargo(ArrayList<Pregunta> pregs){
        creaCuestionario(pregs,"Largo",15);
    }

    @Override
    public Pregunta getPregunta(){
        Pregunta p = preguntasFinales.get(getNumeroPreguntaActual()-1);
        setNumeroPreguntaActual(getNumeroPreguntaActual()+1);

        return p;
    }
}
