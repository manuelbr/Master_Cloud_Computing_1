package com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos;

import com.example.manuel.prototipo_personaemocion.Model.Pregunta;

import java.util.ArrayList;

/**
 * Created by Manuel on 07/03/2016.
 */
public class Tipo7 extends Tipo {
    public Tipo7(ArrayList<Pregunta> filtradas){
        preguntasT = new ArrayList<Pregunta>();
        filtraPreguntas("tipo7",filtradas);
    }

    public Pregunta getPregunta(){
        return super.getPregunta();
    }
}
