package com.example.manuel.prototipo_personaemocion.Model;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.manuel.prototipo_personaemocion.Actividades.FichaPelicula;
import com.example.manuel.prototipo_personaemocion.R;
import com.example.manuel.prototipo_personaemocion.checkAsyncTaskTimeout;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
 * Created by Manuel on 03/12/2015.
 */
public class Pelicula {
    private String Titulo;
    private String Genero;
    private String enlaceCaratula;
    private String enlaceCartel;
    private Bitmap caratula;
    private Bitmap cartel;
    private ArrayList<Bitmap> imagenesAsociadas;
    private String sinopsis;
    private String pais;
    private String musica;
    private int duracion;
    private String enlaceImagenes;
    private Double valoracion;
    private int numValoraciones;
    private ArrayList<Integer> comentariosID;
    private HashMap<Object, Object> porcentajeEmociones;
    private String trailer;
    private String director;
    private int año,identificador;
    private String actores;
    private String frase;
    private Cursor fila;
    private enum Emocion{Ira, Tristeza, Temor, Placer, Amor, Sorpresa, Disgusto, Verguenza, Alegria}
    private ArrayList<String> emocionesPredominantes = new ArrayList<>();
    private cargaPeliculaConcreta task;
    private checkAsyncTaskTimeout taskCanceler;
    private Handler handler;
    private Context contexto;
    private ArrayList<String> palabrasClave;


    public Pelicula(Cursor c, int id, boolean local, Context context) throws IOException {
        fila = c;
        Titulo = c.getString(1);
        Genero = c.getString(15);
        identificador = id;
        enlaceCaratula = c.getString(9);
        contexto = context;

        if(!local) {
            try {
                URL conexion = new URL(enlaceCaratula);
                HttpURLConnection urlConnection = (HttpURLConnection) conexion.openConnection();
                InputStream entrada = new BufferedInputStream(urlConnection.getInputStream());
                caratula = BitmapFactory.decodeStream(entrada);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }else{
            caratula =  BitmapFactory.decodeResource(contexto.getResources(), R.drawable.caratulagenerica);
        }


        sinopsis = c.getString(2);
        valoracion = Double.parseDouble(c.getString(3));
        numValoraciones = Integer.parseInt(c.getString(4));

        //Parsear las emociones en "trozos y doubles"
        HashMap <String,Double> emocionesSinOrdenar = new HashMap<String,Double>();
        String emociones = c.getString(6);
        List<String> porcentajes = Arrays.asList(emociones.split(","));
        for(int i=0;i<porcentajes.size();i++){
            String emocion;
            switch(i){
                case 0: emocion = Emocion.Ira.toString();
                        break;
                case 1: emocion = Emocion.Tristeza.toString();
                    break;
                case 2: emocion = Emocion.Temor.toString();
                    break;
                case 3: emocion = Emocion.Placer.toString();
                    break;
                case 4: emocion = Emocion.Amor.toString();
                    break;
                case 5: emocion = Emocion.Sorpresa.toString();
                    break;
                case 6: emocion = Emocion.Disgusto.toString();
                    break;
                case 7: emocion = Emocion.Verguenza.toString();
                    break;
                case 8: emocion = Emocion.Alegria.toString();
                    break;
                default: emocion = Emocion.Ira.toString();
                        break;
            }
            emocionesSinOrdenar.put(emocion, Double.parseDouble(porcentajes.get(i)));
        }

        //Se almacenan los valores emocionales ordenadamente.
        List list = new LinkedList(emocionesSinOrdenar.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        porcentajeEmociones = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            porcentajeEmociones.put(entry.getKey(), entry.getValue());
        }
        /////////////////////////////////////////////////////////
        ///Detección de las principales emociones.
        Set set = porcentajeEmociones.entrySet();
        Iterator iterator = set.iterator();

        //Ordenadas de menor a mayor.
        for(int i=0;i<porcentajeEmociones.size();i++){
            Map.Entry me = (Map.Entry) iterator.next();
            emocionesPredominantes.add((String) me.getKey());
        }

        trailer = c.getString(8);
        pais = c.getString(18);
        musica = c.getString(19);
        duracion = Integer.parseInt(c.getString(17));
        director = c.getString(12);
        año = Integer.parseInt(c.getString(13));
        actores = c.getString(14);
        frase = c.getString(16);
        enlaceCartel = c.getString(10);
        enlaceImagenes = c.getString(11);

        String palabrasTemporal = c.getString(20);
        String[] pT = palabrasTemporal.split(",");
        palabrasClave = new ArrayList<String>();
        for(int i=0; i< pT.length; i++){
            palabrasClave.add(pT[i].toLowerCase());
        }
    }


    public void cargaPelicula(Context contexto,ProgressDialog progress){

        ConnectivityManager connec = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo redesActivas = connec.getActiveNetworkInfo();
        if(redesActivas != null) {
            cargaPeliculaConcreta task = new cargaPeliculaConcreta(contexto, progress);
            taskCanceler = new checkAsyncTaskTimeout(task, contexto);
            handler = new Handler();
            handler.postDelayed(taskCanceler, 25 * 1000);
            task.execute();
        }else{
            Toast.makeText(contexto, "No hay conexión a internet", Toast.LENGTH_LONG).show();
        }
    }

    public class cargaPeliculaConcreta extends AsyncTask<String,Void,Void> {
        Context contexto;
        ProgressDialog progressD;
        private NetworkInfo redesActivas;
        boolean conexionActiva;

        public cargaPeliculaConcreta(Context procedencia,ProgressDialog progress){
            contexto = procedencia;
            progressD = progress;
            progressD.setCancelable(true);
        }

        @Override
        public void onPreExecute() {
            ConnectivityManager connec = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
            redesActivas = connec.getActiveNetworkInfo();

            if(redesActivas != null) {
                progressD = ProgressDialog.show(contexto, "Procesando", "Cargando Película...",true);
                conexionActiva = true;
            }else {
                Toast.makeText(contexto, "No hay conexión a internet", Toast.LENGTH_LONG).show();
                this.cancel(true);
                conexionActiva = false;
            }

        }

        @Override
        protected void onPostExecute(Void unused) {
            if(taskCanceler != null && handler != null) {
                handler.removeCallbacks(taskCanceler);
            }
            progressD.dismiss();
            Intent intentB = new Intent(contexto, FichaPelicula.class);
            //parámetros
            Bundle b = new Bundle();
            b.putInt("ID",identificador);
            intentB.putExtras(b);

            contexto.startActivity(intentB);
        }

        @Override
        protected Void doInBackground(String... params){
            InputStream entrada = null;
            HttpURLConnection urlConnection = null;

            if(conexionActiva) {
                try {
                    URL conexion = new URL(enlaceCartel);
                    urlConnection = (HttpURLConnection) conexion.openConnection();
                    entrada = new BufferedInputStream(urlConnection.getInputStream());
                    cartel = BitmapFactory.decodeStream(entrada);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (entrada != null) {
                        try {
                            entrada.close();
                        } catch (IOException e) {
                        }
                    }
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

                imagenesAsociadas = new ArrayList<>();
                String imagenes = enlaceImagenes;
                List<String> imagenesSeparate = Arrays.asList(imagenes.split(","));
                for(String enlace: imagenesSeparate){
                    try {
                        URL conexion = new URL(enlace);
                        urlConnection = (HttpURLConnection) conexion.openConnection();
                        entrada = new BufferedInputStream(urlConnection.getInputStream());
                        imagenesAsociadas.add(BitmapFactory.decodeStream(entrada));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        if (entrada != null) {
                            try {
                                entrada.close();
                            } catch (IOException e) {
                            }
                        }
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }
                }
            }


            return null;
        }

        @Override
        protected void onCancelled() {
            if(taskCanceler != null && handler != null) {
                handler.removeCallbacks(taskCanceler);
            }
            progressD.dismiss();
        }
    }

    public String getGenero(){
        return Genero;
    }

    public String getTitulo(){
        return Titulo;
    }

    public Bitmap getCaratula(){
        return caratula;
    }

    public Bitmap getCartel(){ return cartel; }

    public ArrayList<Bitmap> getImagenesAsociadas(){ return imagenesAsociadas; }

    public String getSinopsis(){ return sinopsis; }

    public Double getValoracion(){ return valoracion; }

    public int getNumValoraciones(){ return numValoraciones; }

    public HashMap<Object, Object> getPorcentajeEmociones(){ return porcentajeEmociones; }

    public String getTrailer(){ return trailer; }

    public String getDirector(){ return director; }

    public int getAño(){ return año; }

    public String getActores(){ return actores; }

    public String getFrase(){ return frase; }

    public String getDuracion(){ return Integer.toString(duracion); }

    public String getPais(){ return pais; }

    public String getMusica(){ return musica; }

    public ArrayList<String> getEmocionesPredominantes(){ return emocionesPredominantes; }

    public ArrayList<String> getpC(){
        return palabrasClave;
    }
}
