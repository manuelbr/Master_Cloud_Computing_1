package com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos;

import com.example.manuel.prototipo_personaemocion.Model.Pregunta;

import java.util.ArrayList;

/**
 * Created by Manuel on 28/11/2015.
 */
public interface FactoriaTipo {
    public Tipo crearPregunta(ArrayList<Pregunta> filtradas);
}
