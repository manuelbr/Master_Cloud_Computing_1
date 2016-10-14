package com.example.manuel.prototipo_personaemocion.Actividades;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.manuel.prototipo_personaemocion.Model.Pelicula;
import com.example.manuel.prototipo_personaemocion.Model.Pregunta;
import com.example.manuel.prototipo_personaemocion.Model.Respuesta;
import com.example.manuel.prototipo_personaemocion.asistenteDB;
import com.example.manuel.prototipo_personaemocion.R;
import com.example.manuel.prototipo_personaemocion.checkAsyncTaskTimeout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class splashScreen extends AppCompatActivity {
    private NetworkInfo redesActivas;
    private Handler handler = new Handler();
    private checkAsyncTaskTimeout taskCanceler;
    private ArrayList<Integer> splashes;
    private int wallpaper;
    private static ArrayList<Pelicula> peliculas;
    private static ArrayList<Pregunta> preguntas;
    private static HashMap<Integer,Respuesta> respuestas;
    private asistenteDB db;
    private static final String[] colPe = {"ID","Titulo","Sinopsis","Valoracion","NumValoraciones","LisComentarios","PorcentajesEmocionales","LisVideoopiniones","Trailer","Caratula","Cartel","ImagenesAsociadas","Director","Año","Actores","Genero","Frase","Duracion","Pais","Musica","Palabras","Version"};
    private static final String[] colPr = {"TextoPregunta","ImagenPregunta","EnlaceVideo","ArquitecturaPregunta","Respuestas","ID","Emocion","EnlaceSonido","Version"};
    private static final String[] colRe = {"ID","ArquitecturaRespuesta","ImagenRespuesta","TextoRespuesta","SonidoRespuesta","Valores","Version"};
    private static String fileName = "database.sqlite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash_screen);

        ConnectivityManager connec = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        redesActivas = connec.getActiveNetworkInfo();

        splashes = new ArrayList<Integer>();
        peliculas = new ArrayList<Pelicula>();
        preguntas = new ArrayList<Pregunta>();
        respuestas = new HashMap<Integer,Respuesta>();

        splashes.add(R.drawable.splash1);
        splashes.add(R.drawable.splash2);
        splashes.add(R.drawable.splash3);
        splashes.add(R.drawable.splash4);
        splashes.add(R.drawable.splash5);
        splashes.add(R.drawable.splash6);

        Mostrar task = new Mostrar(this);
        taskCanceler = new checkAsyncTaskTimeout(task, this);
        handler.postDelayed(taskCanceler, 20 * 1000);
        task.execute();
    }



    class Mostrar extends AsyncTask<String,Void,Void> {
        private Context contexto;

        public Mostrar(Context procedencia){
            contexto = procedencia;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if(taskCanceler != null && handler != null) {
                handler.removeCallbacks(taskCanceler);
            }


            Intent intentB = new Intent(splashScreen.this, Menu.class);
            intentB.putExtra("wallpaper",wallpaper);
            splashScreen.this.startActivity(intentB);
            ((splashScreen)contexto).finish();
        }

        @Override
        protected void onCancelled() {
            if(taskCanceler != null && handler != null) {
                handler.removeCallbacks(taskCanceler);
            }
            ((splashScreen)contexto).finish();
        }

        @Override
        protected Void doInBackground(String... params) {
            if (redesActivas != null && !isCancelled()) {
                try{
                    //creamos el objeto base de datos que descargará y almacenará los datos.
                    db = new asistenteDB(contexto,colPe,colPr,colRe,false);
                    int index = (int) (Math.random()*splashes.size());
                    creaObjeto("Pelicula",colPe,false);
                    creaObjeto("Respuestas", colRe, false);
                    creaObjeto("Pregunta", colPr, false);
                    Menu.setPeliculas(peliculas);
                    Cuestionario.setPreguntas(preguntas);
                    Cuestionario.setRespuestas(respuestas);
                    wallpaper = splashes.get(index);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else
                if(!isCancelled()){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getBaseContext(), "No hay conexión a internet", Toast.LENGTH_LONG).show();
                        }
                    });

                    File out = new File(contexto.getFilesDir(), fileName);
                    if(!out.exists()){
                        //Finaliza la actividad.
                        try {
                            this.finalize();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }else{
                        //Carga los datos de la versión local que ya hay almacenada.
                        try {
                            db = new asistenteDB(contexto,colPe,colPr,colRe,false);
                            int index = (int)Math.floor(Math.random()*(splashes.size()) + 1);
                            wallpaper = splashes.get(index);
                            creaObjeto("Pelicula", colPe, true);
                            creaObjeto("Respuestas", colRe, true);
                            creaObjeto("Pregunta",colPr,true);
                            Cuestionario.setPreguntas(preguntas);
                            Cuestionario.setRespuestas(respuestas);
                            Menu.setPeliculas(peliculas);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            return null;
        }
    }

    //Esta operación debe rediseñarse para que el objeto que se cree sea un cue de la Base de Datos local, no de los datos que se han
    //descargado. Devolverá el objeto de base de datos local.
    public void creaObjeto(String tabla,String[] columnas, boolean local) throws IOException {
        Cursor c = db.getDB().query(tabla, columnas, null, null, null, null, null);
        int identificador = 0;

        c.moveToFirst();
        do{
            switch(tabla){
                case "Pelicula": Pelicula peli = new Pelicula(c,identificador,local,this);
                                 peliculas.add(peli);
                                 break;
                case "Pregunta": Pregunta preg = new Pregunta(c,transRespuestas(c.getString(4)),this);
                                 preguntas.add(preg);
                                 break;
                case "Respuestas": Respuesta res = new Respuesta(c,this);
                                 respuestas.put(Integer.parseInt(c.getString(0)), res);
                                 break;
            }
            identificador++;
        }while(c.moveToNext());
        c.close();
    }

    public ArrayList<Respuesta> transRespuestas(String cadenaRespuestas){
        ArrayList<Respuesta> resultado  = new ArrayList<Respuesta>();

        String[] res = cadenaRespuestas.split(",");

        for(int i = 0; i<res.length; i++){
            resultado.add(respuestas.get(Integer.parseInt(res[i])));
        }

        return resultado;
    }
}
