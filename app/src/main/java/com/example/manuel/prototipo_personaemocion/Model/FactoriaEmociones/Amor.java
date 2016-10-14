package com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones;

import com.example.manuel.prototipo_personaemocion.Model.Pregunta;

import java.util.ArrayList;

/**
 * Created by Manuel on 29/11/2015.
 */
public class Amor  extends Emocion{
    private static ArrayList<Pregunta> filtradas;
    private ArrayList<Pregunta> filtradasTipos;

    public Amor(String cuestionario,int actual,ArrayList<String> tiposYaIncluidos){
        tipoCuestionario = cuestionario;
        preguntaActual = actual;
        filtradasTipos  = filtraTipos(filtradas,cuestionario,tiposYaIncluidos);
    }

    public static void setAmor(ArrayList<Pregunta> filtr){
        filtradas = filtr;
    }

    public Pregunta getPregunta(){
        return super.getPregunta(filtradasTipos);
    }

    public void eliminaPregunta(Pregunta p){
        filtradas.remove(p);
    }
}
