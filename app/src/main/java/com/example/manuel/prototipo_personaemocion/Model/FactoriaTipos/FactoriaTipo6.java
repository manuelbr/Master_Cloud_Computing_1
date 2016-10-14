package com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos;

import com.example.manuel.prototipo_personaemocion.Model.Pregunta;

import java.util.ArrayList;

/**
 * Created by Manuel on 07/03/2016.
 */
public class FactoriaTipo6 implements FactoriaTipo {
    @Override
    public Tipo6 crearPregunta(ArrayList<Pregunta> filtradas) {
        return new Tipo6(filtradas);
    }
}
