package com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos;


import com.example.manuel.prototipo_personaemocion.Model.Pregunta;

import java.util.ArrayList;

/**
 * Created by Manuel on 28/11/2015.
 */
public abstract class Tipo {
    private Pregunta pregunta = null;
    protected static ArrayList<Pregunta> preguntasT;

    public void filtraPreguntas(String tipo,ArrayList<Pregunta> preguntas){
        preguntasT.clear();
        for(Pregunta p : preguntas){
            if(p.getTipo().equals(tipo)){
                preguntasT.add(p);
            }
        }
    }

    public Pregunta getPregunta(){
        int index = (int) (Math.random()*preguntasT.size());
        return preguntasT.get(index);
    }

}
