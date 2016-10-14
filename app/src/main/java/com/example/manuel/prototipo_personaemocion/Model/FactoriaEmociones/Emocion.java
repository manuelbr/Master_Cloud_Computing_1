package com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones;


import com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos.FactoriaTipo;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos.FactoriaTipo1;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos.FactoriaTipo2;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos.FactoriaTipo3;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos.FactoriaTipo4;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos.FactoriaTipo5;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos.FactoriaTipo6;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos.FactoriaTipo7;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos.FactoriaTipo8;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos.Tipo;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos.Tipo1;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos.Tipo2;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos.Tipo3;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos.Tipo4;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos.Tipo5;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos.Tipo6;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos.Tipo7;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaTipos.Tipo8;
import com.example.manuel.prototipo_personaemocion.Model.Pregunta;

import java.util.ArrayList;

/**
 * Created by Manuel on 29/11/2015.
 */
public abstract class Emocion {
    protected FactoriaTipo tipoPregunta;
    protected Tipo preguntaTipo;
    protected String tipoCuestionario;
    protected int preguntaActual;

    public Pregunta getPregunta(ArrayList<Pregunta> filtradas){
        Pregunta pregunta = null;

        String tipoActual  = "";
        tipoActual = getTipo(filtradas);
        //Tipos de preguntas comunes a los tres cuestionarios.
        switch(tipoActual){
            case "tipo1":tipoPregunta = new FactoriaTipo1();
                preguntaTipo = tipoPregunta.crearPregunta(filtradas);
                pregunta = ((Tipo1) preguntaTipo).getPregunta();
                break;
            case "tipo2": tipoPregunta = new FactoriaTipo2();
                preguntaTipo = tipoPregunta.crearPregunta(filtradas);
                pregunta = ((Tipo2) preguntaTipo).getPregunta();
                break;
            case "tipo3": tipoPregunta = new FactoriaTipo3();
                preguntaTipo = tipoPregunta.crearPregunta(filtradas);
                pregunta = ((Tipo3) preguntaTipo).getPregunta();
                break;
            case "tipo4": tipoPregunta = new FactoriaTipo4();
                preguntaTipo = tipoPregunta.crearPregunta(filtradas);
                pregunta = ((Tipo4) preguntaTipo).getPregunta();
                break;
            case "tipo5": tipoPregunta = new FactoriaTipo5();
                preguntaTipo = tipoPregunta.crearPregunta(filtradas);
                pregunta = ((Tipo5) preguntaTipo).getPregunta();
                break;
            case "tipo6": tipoPregunta = new FactoriaTipo6();
                preguntaTipo = tipoPregunta.crearPregunta(filtradas);
                pregunta = ((Tipo6) preguntaTipo).getPregunta();
                break;
            case "tipo7": tipoPregunta = new FactoriaTipo7();
                preguntaTipo = tipoPregunta.crearPregunta(filtradas);
                pregunta = ((Tipo7) preguntaTipo).getPregunta();
                break;
            case "tipo8": tipoPregunta = new FactoriaTipo8();
                preguntaTipo = tipoPregunta.crearPregunta(filtradas);
                pregunta = ((Tipo8) preguntaTipo).getPregunta();
                break;
        }

        return pregunta;
    }

    public ArrayList<Pregunta> filtraTipos(ArrayList<Pregunta> sinFiltrar,String Cuestionario,ArrayList<String>tiposYaIncluidos){
        ArrayList<Pregunta> p = new ArrayList<Pregunta>();

        switch(Cuestionario){
            case "Corto":
                while(p.isEmpty()){//SOLUCION PROVISIONAL. BORRAR CUANDO HAYA 8 PREGUNTAS DE CADA EMOCION
                    for (int i = 0; i < sinFiltrar.size(); i++) {
                        if ((sinFiltrar.get(i).getTipo().equals("tipo1") || sinFiltrar.get(i).getTipo().equals("tipo2") || sinFiltrar.get(i).getTipo().equals("tipo3") || sinFiltrar.get(i).getTipo().equals("tipo4")) && !tiposYaIncluidos.contains(sinFiltrar.get(i).getTipo())) {
                            p.add(sinFiltrar.get(i));
                        }
                    }

                    if(p.isEmpty()){//SOLUCION PROVISIONAL. BORRAR CUANDO HAYA 8 PREGUNTAS DE CADA EMOCION
                        tiposYaIncluidos.clear();
                    }
                }
                break;
            case "Medio":
                while(p.isEmpty()) {//SOLUCION PROVISIONAL. BORRAR CUANDO HAYA 8 PREGUNTAS DE CADA EMOCION
                    for (int i = 0; i < sinFiltrar.size(); i++) {
                        if ((sinFiltrar.get(i).getTipo().equals("tipo1") || sinFiltrar.get(i).getTipo().equals("tipo2") || sinFiltrar.get(i).getTipo().equals("tipo3") || sinFiltrar.get(i).getTipo().equals("tipo4") || sinFiltrar.get(i).getTipo().equals("tipo5")) && !tiposYaIncluidos.contains(sinFiltrar.get(i).getTipo())) {
                            p.add(sinFiltrar.get(i));
                        }
                    }

                    if(p.isEmpty()){//SOLUCION PROVISIONAL. BORRAR CUANDO HAYA 8 PREGUNTAS DE CADA EMOCION
                        tiposYaIncluidos.clear();
                    }
                }
                break;
            case "Largo":
                while(p.isEmpty()) {//SOLUCION PROVISIONAL. BORRAR CUANDO HAYA 8 PREGUNTAS DE CADA EMOCION
                    for (int i = 0; i < sinFiltrar.size(); i++) {
                        if (!tiposYaIncluidos.contains(sinFiltrar.get(i).getTipo())) {
                            p.add(sinFiltrar.get(i));
                        }
                    }

                    if(p.isEmpty()){//SOLUCION PROVISIONAL. BORRAR CUANDO HAYA 8 PREGUNTAS DE CADA EMOCION
                        tiposYaIncluidos.clear();
                    }
                }
                break;
        }

        return p;
    }

    public String getTipo(ArrayList<Pregunta> filtradas){
        int numAleatorio=(int)Math.floor(Math.random()*(filtradas.size()));
        return filtradas.get(numAleatorio).getTipo();
    }
}
