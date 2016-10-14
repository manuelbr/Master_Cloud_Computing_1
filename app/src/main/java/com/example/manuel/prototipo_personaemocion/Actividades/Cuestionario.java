package com.example.manuel.prototipo_personaemocion.Actividades;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaCuestionarios.FactoriaTest;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaCuestionarios.FactoriaTestCorto;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaCuestionarios.FactoriaTestLargo;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaCuestionarios.FactoriaTestMedio;
import com.example.manuel.prototipo_personaemocion.Model.FactoriaCuestionarios.Tests;
import com.example.manuel.prototipo_personaemocion.Model.Pelicula;
import com.example.manuel.prototipo_personaemocion.Model.Pregunta;
import com.example.manuel.prototipo_personaemocion.Model.Respuesta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Manuel on 01/03/2016.
 */
public class Cuestionario{
    private static ArrayList<Pregunta> preguntas;
    private static ArrayList<Pelicula> peliculasSugeridas;
    private static HashMap<Integer,Respuesta> respuestas;
    private FactoriaTest fabricatest;
    private static Tests test;
    private Context contexto;
    private static HashMap<String,Double> porcentajes;
    private static ArrayList<String> emocionesOrdenadas;
    private static String palabraClave;
    private static int wallpaper;

    public Cuestionario(Context context, String tipoCuestionario, int wall){
        contexto = context;

        //Limpiamos los residuos de anteriores ejecuciones.
        porcentajes = null;
        test = null;
        wallpaper = wall;

        //Inicializamos los porcentajes.
        porcentajes = new HashMap<String,Double>();
        porcentajes.put("Ira",1.0);
        porcentajes.put("Tristeza",1.0);
        porcentajes.put("Temor",1.0);
        porcentajes.put("Placer",1.0);
        porcentajes.put("Amor",1.0);
        porcentajes.put("Sorpresa",1.0);
        porcentajes.put("Disgusto",1.0);
        porcentajes.put("Verguenza",1.0);
        porcentajes.put("Alegria",1.0);


        switch (tipoCuestionario){
            case "Corto": fabricatest = new FactoriaTestCorto();
                test = fabricatest.creaTest(preguntas);
                Tests.setLimitePreguntas(5);
                break;
            case "Medio": fabricatest = new FactoriaTestMedio();
                test = fabricatest.creaTest(preguntas);
                Tests.setLimitePreguntas(10);
                break;
            case "Largo": fabricatest = new FactoriaTestLargo();
                test = fabricatest.creaTest(preguntas);
                Tests.setLimitePreguntas(15);
                break;
        }
        test.setNumeroPreguntaActual(1);
        siguientePregunta(contexto);
    }

    public static void siguientePregunta(Context contexto){
        if(test.getNumeroPreguntaActual() <= test.getLimitePreguntas()){
            Pregunta p  = test.getPregunta();
            ProgressDialog progress = new ProgressDialog(contexto);
            p.cargaPregunta(contexto, progress, (test.getNumeroPreguntaActual()-2));
        }else{
            //Adaptamos los resultados para que sean medibles.
            if(porcentajes.containsValue(1.0)){
                Set<String> keys = porcentajes.keySet();
                String[] llaves = new String[keys.size()];
                keys.toArray(llaves);
                for(int i = 0;i<porcentajes.size(); i++){
                    if(porcentajes.get(llaves[i]).equals(1.0)){
                        porcentajes.remove(llaves[i]);
                        porcentajes.put(llaves[i],0.0);
                    }
                }
            }
            ArrayList<Pelicula> p = analizaPorcentajes(porcentajes);
            ListaPeliculas.setPelis(p);
            Intent intentB = new Intent(contexto, ListaPeliculas.class);
            intentB.putExtra("sugiriendo",true);
            intentB.putExtra("wallpaper",wallpaper);
            contexto.startActivity(intentB);
        }
    }

    public static int getWallpaper(){
        return wallpaper;
    }

    //Obtiene una lista ordenada por prioridades de las películas más adecuadas a los porcentajes pasados como parámetro.
    public static ArrayList<Pelicula> analizaPorcentajes(HashMap<String,Double> emocionesSinOrdenar){
        emocionesOrdenadas = null;
        emocionesOrdenadas = new ArrayList<String>();
        HashMap<Pelicula,Integer> sugeridas = new HashMap<Pelicula,Integer>();
        peliculasSugeridas = new ArrayList<Pelicula>();

        List list = new LinkedList(emocionesSinOrdenar.entrySet());
        Iterator it = ordenaHash(list);

        //Ordenadas de menor a mayor.
        for(int i=0;i<emocionesSinOrdenar.size();i++){
            Map.Entry me = (Map.Entry) it.next();
            emocionesOrdenadas.add((String) me.getKey());
        }

        boolean terminado = false;
        int contador = 0; //Contador del número de películas encontradas.

        for(int i = 0; i < Menu.getPeliculas().size() && (!terminado);i++){
            Pelicula p = Menu.getPeliculas().get(i);

            for(int j = 0; j< p.getEmocionesPredominantes().size();j++){
                Log.i(p.getTitulo(),p.getEmocionesPredominantes().get(j));
            }

            if(emocionesOrdenadas.equals(p.getEmocionesPredominantes())){
                if(p.getpC().contains(palabraClave)){
                    sugeridas.put(p,0); //Prioridad 0, la mayor de las prioridades, es una película perfecta para el sujeto.
                }else{
                    sugeridas.put(p,1); //Prioridad 1, la segunda mayor prioridad, es una película muy adecuada para el sujeto: cumple sus mayores emociones actuales.
                }

            }else{
                //Medimos cómo de cercana es la película en cuanto a las emociones demostradas.
                List<String> lit = emocionesOrdenadas.subList(emocionesOrdenadas.size()-3,emocionesOrdenadas.size());
                List<String> lit2 = p.getEmocionesPredominantes().subList(p.getEmocionesPredominantes().size() - 3, p.getEmocionesPredominantes().size());

                if(lit.equals(lit2)){
                    if(p.getpC().contains(palabraClave)){
                        sugeridas.put(p,2); //Prioridad 2, Cumple con las mayores emociones del sujeto y además tiene relación con el concepto que le ha llamado la atención.
                    }else{
                        sugeridas.put(p,3); //Prioridad 3, Cumple con las mayores  emociones del sujeto
                    }
                }else{
                    //La siguiente opción que queda es que darle una película con la emoción predominante del sujeto.
                    if(lit.get(lit.size()-1).equals(lit2.get(lit2.size()-1))){
                        if(p.getpC().contains(palabraClave)){
                            sugeridas.put(p,4); //Prioridad 4, se cumple con la emoción predominante del sujeto y además tiene relación con el concepto que le ha llamado la atención.
                        }else{
                            sugeridas.put(p,5); //Prioridad 5, se cumple con la emoción predominante del sujeto
                        }

                    }else{
                        //El último caso: NO HAY CONCORDANCIA EMOCIONAL NINGUNA CON EL SUJETO, la única forma de empatía
                        //con él, es mediante un acercamiento al concepto que le llama la atención.
                        if(p.getpC().contains(palabraClave)){
                            sugeridas.put(p,6); //Prioridad 6, la más baja de las prioridades, cumple sólo con el  concepto que le ha llamado la atención.
                        }
                    }
                }
            }
            terminado = isTerminado(sugeridas);
        }

        //Ahora crearemos un arrayList de películas sugeridas en función de los valores del hash.
        List lista = new LinkedList(sugeridas.entrySet());
        Iterator iterator = ordenaHash(lista);

        //Ordenadas de menor a mayor.
        for(int i=0;i<sugeridas.size();i++){
            Map.Entry me = (Map.Entry) iterator.next();
            peliculasSugeridas.add((Pelicula) me.getKey());
        }

        return peliculasSugeridas;
    }

    public static void setPalabraClave(String pC){
        palabraClave = pC.toLowerCase();
    }


    public static Iterator ordenaHash(List list){
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        LinkedHashMap linkedOrdenado = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            linkedOrdenado.put(entry.getKey(), entry.getValue());
        }
        /////////////////////////////////////////////////////////
        ///Ordenación de valores (menor a mayor)
        Set set = linkedOrdenado.entrySet();
        Iterator iterator = set.iterator();

        return iterator;
    }

    public static boolean isTerminado(HashMap<Pelicula,Integer> sugeridas){
        boolean terminado;

        if( ( ( sugeridas.containsValue(0) || sugeridas.containsValue(1)  ) && sugeridas.size()>5 ) || (( sugeridas.containsValue(2) || sugeridas.containsValue(3)) && sugeridas.size()>10 ) ){
            terminado = true;
        }else{
            terminado = false;
        }
            return terminado;
    }

    public static void addPercentage(String emocion, double porcentaje){
        double panterior = porcentajes.get(emocion);
        double resultado = panterior;

        if(panterior > porcentaje){
            resultado = (panterior - ((panterior - porcentaje)*panterior))/100;
        }else {
            resultado = (panterior + ((porcentaje - panterior) * panterior))/100;
        }
        porcentajes.remove(emocion);
        porcentajes.put(emocion,resultado);
    }

    public static void setPreguntas(ArrayList<Pregunta> pregs){
        preguntas = pregs;
    }

    public static void setRespuestas(HashMap<Integer,Respuesta> res){
        respuestas = res;
    }

    public static ArrayList<Pregunta> getPreguntas(){
        return test.getPreguntasFinales();
    }
}
