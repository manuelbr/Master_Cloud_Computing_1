package com.example.manuel.prototipo_personaemocion.Model.FactoriaCuestionarios;


import com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones.Alegria;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones.Amor;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones.Disgusto;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones.Emocion;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones.FactoriaAlegria;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones.FactoriaAmor;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones.FactoriaDisgusto;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones.FactoriaEmocion;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones.FactoriaIra;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones.FactoriaPlacer;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones.FactoriaTemor;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones.FactoriaTristeza;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones.FactoriaVergüenza;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones.Ira;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones.Placer;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones.Temor;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones.Tristeza;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaEmociones.Vergüenza;
import com.example.manuel.prototipo_personaemocion.Model.Pregunta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Manuel on 07/03/2016.
 */
public abstract class Tests {
    private int numeroPreguntaActual;
    private HashMap<String,Integer> porcentajes;
    private static int limitePreguntas = 0;
    protected FactoriaEmocion factoriaE;
    protected ArrayList<String> emociones;
    protected Emocion emocion;
    protected static ArrayList<Pregunta> preguntas;
    protected static ArrayList<Pregunta> preguntasFinales;
    protected static HashMap<String,ArrayList<Pregunta> > preguntasEmociones;

    public abstract Pregunta getPregunta();

    public void setNumeroPreguntaActual(int numeroPreguntaActual2){
        numeroPreguntaActual = numeroPreguntaActual2;
    }

    public int getNumeroPreguntaActual(){
        return numeroPreguntaActual;
    }

    public void modificaPorcentaje(int pos,int valor){}

    //Devuelve el porcentaje asociado a una emoción concreta
    public int getPorcentaje(String emocion){
        return porcentajes.get(emocion);
    }

    //Filtra las preguntas totales por emociones
    public void filtraPreguntas(String emocion){
        ArrayList<Pregunta> provisional = new ArrayList<Pregunta>();
        for(Pregunta p: preguntas){
            if(p.getEmocion().equals(emocion)){
                provisional.add(p);
            }
        }
        preguntasEmociones.put(emocion,provisional);
    }

    public void creaCuestionario(ArrayList<Pregunta> pregs, String tipoCuestionario, int limite) {

        emociones = new ArrayList<String>(4);
        inicializaEmociones();
        preguntas = pregs;
        preguntasFinales = new ArrayList<Pregunta>();

        preguntasEmociones = new HashMap<String, ArrayList<Pregunta>>();

        //Se filtran las preguntas por emociones, para crear las factorias adecuadas de cada una.
        filtraPreguntas("Tristeza");
        filtraPreguntas("Placer");
        filtraPreguntas("Disgusto");
        filtraPreguntas("Alegria");
        filtraPreguntas("Ira");
        filtraPreguntas("Temor");
        filtraPreguntas("Amor");
        filtraPreguntas("Verguenza");
        Tristeza.setTristeza(getPreguntasFiltradas("Tristeza"));
        Placer.setPlacer(getPreguntasFiltradas("Placer"));
        Disgusto.setDisgusto(getPreguntasFiltradas("Disgusto"));
        Alegria.setAlegria(getPreguntasFiltradas("Alegria"));
        Ira.setIra(getPreguntasFiltradas("Ira"));
        Temor.setTemor(getPreguntasFiltradas("Temor"));
        Amor.setAmor(getPreguntasFiltradas("Amor"));
        Vergüenza.setVerguenza(getPreguntasFiltradas("Verguenza"));


        //Se establece el número de pregunta actual al comenzar el cuestionario.
        setNumeroPreguntaActual(1);
        Pregunta p = null;

        ArrayList<String> tiposYaIncluidos = new ArrayList<String>();

        for (int i = 0; i < limite; i++) {

            //Comprueba que ya se hayan comprobado todas las emociones
            if(emociones.isEmpty()){
                inicializaEmociones();
                //No puede volver a aparecer una pregunta de tipo no emocion: SOLO UNA VEZ
                emociones.remove(4);
            }
            //Comprueba que ya se hayan probado todos los tipos en función del cuestionario.
            //Si es así, se reinicia para poder seguir mostrando preguntas, porque ya se habrán mostrado todos los arquetipos
            //principales del cuestionario en cuestión "valga la redundancia".
            if((tiposYaIncluidos.size() == 6 && tipoCuestionario.equals("Medio") ) || (tiposYaIncluidos.size() == 9 && tipoCuestionario.equals("Largo") ) || (tiposYaIncluidos.size() == 5 && tipoCuestionario.equals("Corto") ) ){
                tiposYaIncluidos.clear();
            }


            int rango = emociones.size();
            int numAleatorio = (int) Math.floor(Math.random() * rango);


            switch (emociones.get(numAleatorio)) {
                case "Tristeza":
                    factoriaE = new FactoriaTristeza(tipoCuestionario, (i + 1));
                    emocion = factoriaE.creaObjeto(tiposYaIncluidos);
                    p = ((Tristeza) emocion).getPregunta();
                    preguntasFinales.add(p);
                    //((Tristeza) emocion).eliminaPregunta(p);
                    break;
                case "Placer":
                    factoriaE = new FactoriaPlacer(tipoCuestionario, (i + 1));
                    emocion = factoriaE.creaObjeto(tiposYaIncluidos);
                    p = ((Placer) emocion).getPregunta();
                    preguntasFinales.add(p);
                    //((Placer) emocion).eliminaPregunta(p);
                    break;
                case "Disgusto":
                    factoriaE = new FactoriaDisgusto(tipoCuestionario, (i + 1));
                    emocion = factoriaE.creaObjeto(tiposYaIncluidos);
                    p = ((Disgusto) emocion).getPregunta();
                    preguntasFinales.add(p);
                    //((Disgusto) emocion).eliminaPregunta(p);
                    break;
                case "Alegria":
                    factoriaE = new FactoriaAlegria(tipoCuestionario, (i + 1));
                    emocion = factoriaE.creaObjeto(tiposYaIncluidos);
                    p = ((Alegria) emocion).getPregunta();
                    preguntasFinales.add(p);
                    //((Alegria) emocion).eliminaPregunta(p);
                    break;
                case "Ira":
                    factoriaE = new FactoriaIra(tipoCuestionario, (i + 1));
                    emocion = factoriaE.creaObjeto(tiposYaIncluidos);
                    p = ((Ira) emocion).getPregunta();
                    preguntasFinales.add(p);
                    //((Ira) emocion).eliminaPregunta(p);
                    break;
                case "Temor":
                    factoriaE = new FactoriaTemor(tipoCuestionario, (i + 1));
                    emocion = factoriaE.creaObjeto(tiposYaIncluidos);
                    p = ((Temor) emocion).getPregunta();
                    preguntasFinales.add(p);
                    //((Temor) emocion).eliminaPregunta(p);
                    break;
                case "Amor":
                    factoriaE = new FactoriaAmor(tipoCuestionario, (i + 1));
                    emocion = factoriaE.creaObjeto(tiposYaIncluidos);
                    p = ((Amor) emocion).getPregunta();
                    preguntasFinales.add(p);
                    //((Amor) emocion).eliminaPregunta(p);
                    break;
                case "Verguenza":
                    factoriaE = new FactoriaVergüenza(tipoCuestionario, (i + 1));
                    emocion = factoriaE.creaObjeto(tiposYaIncluidos);
                    p = ((Vergüenza) emocion).getPregunta();
                    preguntasFinales.add(p);
                    //((Vergüenza) emocion).eliminaPregunta(p);
                    break;
                case "Otro":
                    //Se crea la pregunta de tipo 9, que no está asociada a ninguna emoción.
                    p = new Pregunta("Describe con una palabra el último concepto que llamó tu atención");
                    preguntasFinales.add(p);
                    break;
            }
            //Eliminamos la emoción de la lista de preguntas restantes.
            emociones.remove(numAleatorio);
            tiposYaIncluidos.add(p.getTipo());
        }
    }

    public void inicializaEmociones(){
        emociones.add("Tristeza");
        emociones.add("Placer");
        emociones.add("Disgusto");
        emociones.add("Alegria");
        emociones.add("Otro");
        emociones.add("Ira");
        emociones.add("Temor");
        emociones.add("Amor");
        emociones.add("Verguenza");
    }

    public ArrayList<Pregunta> getPreguntasFiltradas (String emocion){
        return preguntasEmociones.get(emocion);
    }

    //Devuelve un arraylist de las mayores emociones ordenadas de menor a mayor
    public ArrayList<String> getPorcentajeMax(){
        int i = 0;
        ArrayList<String> porcentajesMax = new ArrayList<String>(3);

        for (Map.Entry<String, Integer> entry  : entriesSortedByValues(porcentajes)) {
            if(i >= (porcentajes.size()-3) ){
                porcentajesMax.add(entry.getKey());
            }
        }
        return porcentajesMax;
    }

    //Comparador para ordenar los porcentajes emocionales.
    static <String,Integer extends Comparable<? super Integer>>
    SortedSet<Map.Entry<String,Integer>> entriesSortedByValues(Map<String,Integer> map) {
        SortedSet<Map.Entry<String,Integer>> sortedEntries = new TreeSet<Map.Entry<String,Integer>>(
                new Comparator<Map.Entry<String,Integer>>() {
                    @Override public int compare(Map.Entry<String,Integer> e1, Map.Entry<String,Integer> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res : 1;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    public static void setLimitePreguntas(int limitePreguntas2){
        limitePreguntas = limitePreguntas2;
    }

    public static int getLimitePreguntas(){
        return limitePreguntas;
    }

    public static ArrayList<Pregunta> getPreguntasFinales(){
        return preguntasFinales;
    }



}
