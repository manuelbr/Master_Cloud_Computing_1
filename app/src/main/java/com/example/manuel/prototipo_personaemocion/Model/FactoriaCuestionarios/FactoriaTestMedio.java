package com.example.manuel.prototipo_personaemocion.Model.FactoriaCuestionarios;

import com.example.manuel.prototipo_personaemocion.Model.Pregunta;

import java.util.ArrayList;

/**
 * Created by Manuel on 28/11/2015.
 */
public class FactoriaTestMedio implements FactoriaTest{
    public Tests creaTest(ArrayList<Pregunta> preguntas){
        Tests medio = new TestsMedio(preguntas);
        return medio;
    }
}
