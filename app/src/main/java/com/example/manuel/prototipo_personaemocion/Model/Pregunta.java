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

import com.example.manuel.prototipo_personaemocion.Actividades.Cuestionario;
import com.example.manuel.prototipo_personaemocion.Actividades.FichaPregunta;
import com.example.manuel.prototipo_personaemocion.Actividades.MenuCuestionario;
import com.example.manuel.prototipo_personaemocion.checkAsyncTaskTimeout;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Manuel on 28/11/2015.
 */
public class Pregunta {
    private String emocion;
    private String tipo;
    private String textoPregunta;
    private String videoPregunta;
    private String enlaceImagen;
    private Bitmap imagenPregunta;
    private String sonidoPregunta;
    private Context contexto;
    private ArrayList<Respuesta> respuestas;
    private checkAsyncTaskTimeout taskCanceler;
    private Handler handler;
    private int idInTest;

    public Pregunta(String textPregunta){
        tipo = "tipo9";
        textoPregunta = textPregunta;
    }

    public Pregunta(Cursor c,ArrayList<Respuesta> respuestas2, Context context){
        contexto = context;
        respuestas = respuestas2;

        emocion = c.getString(6);
        tipo = c.getString(3);
        textoPregunta = c.getString(0);
        videoPregunta = c.getString(2);
        enlaceImagen = c.getString(1);
        sonidoPregunta = c.getString(7);
    }

    public ArrayList<Respuesta> getRespuestas(){
        return respuestas;
    }

    public String getEmocion(){
        return emocion;
    }

    public String getTipo(){
        return tipo;
    }

    public String getTextoPregunta(){
        return textoPregunta;
    }

    public String getVideoPregunta(){
        return videoPregunta;
    }

    public Bitmap getImagenPregunta(){
        return imagenPregunta;
    }

    public String getSonidoPregunta(){
        return sonidoPregunta;
    }

    public int getNumRespuestas(){
        return respuestas.size();
    }

    public Respuesta getRespuesta(int id){
        return respuestas.get(id);
    }

    public int getID(){
        return idInTest;
    }

    public void cargaPregunta(Context contexto,ProgressDialog progress, int id){
        ConnectivityManager connec = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo redesActivas = connec.getActiveNetworkInfo();
        idInTest = id;

        if(redesActivas != null) {
            cargaPreguntaConcreta task = new cargaPreguntaConcreta(contexto, progress);
            taskCanceler = new checkAsyncTaskTimeout(task, contexto);
            handler = new Handler();
            handler.postDelayed(taskCanceler, 25 * 1000);
            task.execute();
        }else{
            Toast.makeText(contexto, "No hay conexión a internet", Toast.LENGTH_LONG).show();
        }
    }

    public class cargaPreguntaConcreta extends AsyncTask<String,Void,Void> {
        Context contexto;
        ProgressDialog progressD;
        private NetworkInfo redesActivas;
        boolean conexionActiva;

        public cargaPreguntaConcreta(Context procedencia,ProgressDialog progress){
            contexto = procedencia;
            progressD = progress;
            progressD.setCancelable(true);
        }

        @Override
        public void onPreExecute() {
            ConnectivityManager connec = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
            redesActivas = connec.getActiveNetworkInfo();

            if(redesActivas != null) {
                progressD = ProgressDialog.show(contexto, "Procesando", "Cargando Pregunta...",true);
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
            //Se pasa a la actividad que recoge los datos de cada pregunta.
            Intent intentB = new Intent(contexto, FichaPregunta.class);
            //parámetros
            Bundle b = new Bundle();
            b.putInt("ID",idInTest);
            intentB.putExtras(b);

            contexto.startActivity(intentB);
        }

        @Override
        protected Void doInBackground(String... params){
            InputStream entrada = null;
            HttpURLConnection urlConnection = null;

            if(!tipo.equals("tipo9")) {
                if (conexionActiva) {
                    if (tipo.equals("tipo3") || tipo.equals("tipo4")) {
                        //Se descarga la imagen que acompaña a la pregunta
                        try {
                            URL conexion = new URL(enlaceImagen);
                            urlConnection = (HttpURLConnection) conexion.openConnection();
                            entrada = new BufferedInputStream(urlConnection.getInputStream());
                            imagenPregunta = BitmapFactory.decodeStream(entrada);
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
                    }

                    for (int i = 0; i < respuestas.size(); i++) {
                        if (respuestas.get(i).getTipoRespuesta().equals("tipo2")) {

                            String enlace = respuestas.get(i).getEnlaceImagen();

                            try {
                                URL conexion = new URL(enlace);
                                urlConnection = (HttpURLConnection) conexion.openConnection();
                                entrada = new BufferedInputStream(urlConnection.getInputStream());
                                respuestas.get(i).setImagenRespuesta(BitmapFactory.decodeStream(entrada));
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
            try {
                this.finalize();
                Intent intentB = new Intent(contexto, MenuCuestionario.class);
                intentB.putExtra("wallpaper", Cuestionario.getWallpaper());
                contexto.startActivity(intentB);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
