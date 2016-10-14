package com.example.manuel.prototipo_personaemocion.Actividades;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manuel.prototipo_personaemocion.Actividades.Listeners.imageButtonClickListener_Sound;
import com.example.manuel.prototipo_personaemocion.Actividades.Listeners.respuestaButtonClickListener;
import com.example.manuel.prototipo_personaemocion.Config;
import com.example.manuel.prototipo_personaemocion.Model.Pregunta;
import com.example.manuel.prototipo_personaemocion.R;
import com.example.manuel.prototipo_personaemocion.checkAsyncTaskTimeout;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;

/**
 * Created by Manuel on 22/03/2016.
 */
public class FichaPregunta extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private Button[] respuestas;
    private ImageButton[] respuestasI;
    private YouTubePlayerView pregunta3;
    private Config configuracion;
    private static YouTubePlayer player;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private boolean fullscreen = false;
    private ImageButton[] play;
    private checkAsyncTaskTimeout taskCanceler;
    private Handler handler;
    private EditText entrada;
    private respuestaButtonClickListener[] rbk;
    private Pregunta p;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        int idPregunta = b.getInt("ID");

        p = Cuestionario.getPreguntas().get(idPregunta);


        ProgressDialog progress = new ProgressDialog(FichaPregunta.this);
        ArrayList<String> urls = new ArrayList<String>();

        respuestasI = new ImageButton[4];
        respuestas = new Button[4];
        play = new ImageButton[4];


        switch(p.getTipo()){
            case "tipo1": setContentView(R.layout.activity_tipo_pregunta1);
                inicializaRespuestas("tipo1");
                break;
            case "tipo2": setContentView(R.layout.activity_tipo_pregunta2);
                inicializaRespuestas("tipo2");
                break;
            case "tipo3": setContentView(R.layout.activity_tipo_pregunta3);

                ImageView imagenPregunta1 = (ImageView) findViewById(R.id.imagenPregunta);
                imagenPregunta1.setImageBitmap(p.getImagenPregunta());

                inicializaRespuestas("tipo3");
                break;
            case "tipo4": setContentView(R.layout.activity_tipo_pregunta4);
                ImageView imagenPregunta = (ImageView) findViewById(R.id.imagenPregunta);
                imagenPregunta.setImageBitmap(p.getImagenPregunta());

                inicializaRespuestas("tipo4");
                break;
            case "tipo5": setContentView(R.layout.activity_tipo_pregunta5);
                inicializaRespuestas("tipo5");

                configuracion = new Config(p.getVideoPregunta());
                pregunta3 = (YouTubePlayerView) findViewById(R.id.videoPregunta);
                pregunta3.initialize(configuracion.getDeveloperKey(), this);
                break;

            case "tipo6": setContentView(R.layout.activity_tipo_pregunta6);
                play[0] = (ImageButton) findViewById(R.id.play1);
                play[1] = (ImageButton) findViewById(R.id.play2);
                play[2] = (ImageButton) findViewById(R.id.play3);
                play[3] = (ImageButton) findViewById(R.id.play4);

                inicializaRespuestas("tipo6");

                for(int i=0;i<p.getNumRespuestas();i++) {
                    urls.add(p.getRespuesta(i).getSonidoRespuesta());
                }
                cargaPreguntaSonidos(this, progress, urls);
                break;

            case "tipo7": setContentView(R.layout.activity_tipo_pregunta7);
                play[0] = (ImageButton) findViewById(R.id.play2);
                inicializaRespuestas("tipo7");

                urls.add(p.getSonidoPregunta());
                cargaPreguntaSonidos(this, progress, urls);
                break;
            case "tipo8": setContentView(R.layout.activity_tipo_pregunta8);
                play[0] = (ImageButton) findViewById(R.id.play2);
                inicializaRespuestas("tipo8");

                urls.add(p.getSonidoPregunta());
                cargaPreguntaSonidos(this, progress, urls);
                break;
            case "tipo9": setContentView(R.layout.activity_tipo_pregunta9);
                respuestas[0] = (Button) findViewById(R.id.aceptar);
                TextView tituloPregunta7  = (TextView) findViewById(R.id.tituloPregunta);
                tituloPregunta7.setText(p.getTextoPregunta());
                rbk = new respuestaButtonClickListener[1];
                entrada = (EditText) findViewById(R.id.entrada);
                rbk[0] = new respuestaButtonClickListener(respuestas[0],this, entrada);
                respuestas[0].setOnClickListener(rbk[0]);
                Editable je = entrada.getText();
                String valor = je.toString();
                break;
        }
    }

    public void inicializaRespuestas(String tipo){
        TextView texto = (TextView) findViewById(R.id.tituloPregunta);
        texto.setText(p.getTextoPregunta());


        if(tipo == "tipo2" || tipo == "tipo4" || tipo == "tipo7"){
            respuestasI[0] = (ImageButton) findViewById(R.id.respuesta1);
            respuestasI[1] = (ImageButton) findViewById(R.id.respuesta2);
            respuestasI[2] = (ImageButton) findViewById(R.id.respuesta3);
            respuestasI[3] = (ImageButton) findViewById(R.id.respuesta4);

            rbk = new respuestaButtonClickListener[p.getNumRespuestas()];

            for(int i=0;i<p.getNumRespuestas();i++){
                respuestasI[i].setImageBitmap(p.getRespuesta(i).getImagenRespuesta());
                rbk[i] = new respuestaButtonClickListener(respuestas[i],p.getRespuesta(i),this);
                respuestasI[i].setOnClickListener(rbk[i]);
                respuestasI[i].setVisibility(View.VISIBLE);
            }
        }else{
            respuestas[0] = (Button) findViewById(R.id.respuesta1);
            respuestas[1] = (Button) findViewById(R.id.respuesta2);
            respuestas[2] = (Button) findViewById(R.id.respuesta3);
            respuestas[3] = (Button) findViewById(R.id.respuesta4);

            rbk = new respuestaButtonClickListener[p.getNumRespuestas()];

            for(int i=0;i<p.getNumRespuestas();i++){
                if(!tipo.equals("tipo6")){
                    respuestas[i].setText(p.getRespuesta(i).getTextoRespuesta());
                }
                if(!tipo.equals("tipo6") && !tipo.equals("tipo7") && !tipo.equals("tipo8")){
                    rbk[i] = new respuestaButtonClickListener(respuestas[i],p.getRespuesta(i),this);
                    respuestas[i].setOnClickListener(rbk[i]);
                }

                if(!tipo.equals("tipo5") && !tipo.equals("tipo6") && !tipo.equals("tipo7") && !tipo.equals("tipo8")) {
                    respuestas[i].setVisibility(View.VISIBLE);
                }
            }
        }

    }

    public void cargaPreguntaSonidos(Context contexto,ProgressDialog progress,ArrayList<String> urles){
        int tipo_pregunta_sonidos;

        if(urles.size() > 1){
            tipo_pregunta_sonidos = 0;
        }else
            tipo_pregunta_sonidos = 1;

        ConnectivityManager connec = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo redesActivas = connec.getActiveNetworkInfo();
        if(redesActivas != null) {
            cargaSonidos task = new cargaSonidos(contexto, progress,urles,tipo_pregunta_sonidos);
            taskCanceler = new checkAsyncTaskTimeout(task, contexto);
            handler = new Handler();
            handler.postDelayed(taskCanceler, 25 * 1000);
            task.execute();
        }else{
            Toast.makeText(contexto, "No hay conexión a internet", Toast.LENGTH_LONG).show();
        }
    }

    public class cargaSonidos extends AsyncTask<String,Void,Void> {
        private Context contexto;
        private ProgressDialog progressD;
        private ArrayList<String> urls;
        private imageButtonClickListener_Sound[] listenerButton;
        private int tipo_pregunta;

        public cargaSonidos (Context procedencia,ProgressDialog progress,ArrayList<String> urles, int tipo_pregunta_sonido){
            contexto = procedencia;
            progressD = progress;
            urls = urles;
            listenerButton = new imageButtonClickListener_Sound[4];
            progressD.setCancelable(true);
            tipo_pregunta = tipo_pregunta_sonido;
        }

        @Override
        public void onPreExecute() {
            progressD = ProgressDialog.show(contexto, "Procesando", "Cargando Pregunta...",true);
        }

        @Override
        protected void onPostExecute(Void unused) {
            if (taskCanceler != null && handler != null) {
                handler.removeCallbacks(taskCanceler);
            }

            for(int i = 0; i < urls.size();i++){
                play[i].setOnClickListener(listenerButton[i]);
                play[i].setVisibility(View.VISIBLE);
            }

            progressD.dismiss();

        }

        @Override
        protected Void doInBackground(String... params){
            Button[] influencias;

            //Hay que adaptar los tipos button e imagebutton, para las preguntas cuya respuesta sea imagen o texto.
            if(tipo_pregunta == 1){
                influencias = new Button[p.getNumRespuestas()];
            }else {
                influencias = new Button[1];
            }

            MediaPlayer[] mvps = new MediaPlayer[urls.size()];

            for (int i = 0; i < urls.size(); i++) {
                mvps[i] = creaMediaPlayer(urls.get(i));

                if(tipo_pregunta == 1){
                    for(int j = 0; j< p.getNumRespuestas(); j++){
                        influencias[j] = respuestas[j];
                    }
                }else {
                    influencias = new Button[1];
                    influencias[0] = respuestas[i];
                }
                listenerButton[i] = new imageButtonClickListener_Sound(play[i],mvps[i],influencias);
            }

            final MediaPlayer[] mvp = mvps;

            runOnUiThread(new Runnable() {
                public void run() {
                    for(int i = 0; i<p.getNumRespuestas(); i++){
                        rbk[i] = new respuestaButtonClickListener(respuestas[i],p.getRespuesta(i),mvp,contexto);
                        if(p.getRespuestas().get(i).getTipoRespuesta().equals("tipo2")){
                            respuestasI[i].setOnClickListener(rbk[i]);
                        }else
                            respuestas[i].setOnClickListener(rbk[i]);
                    }
                }
            });

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


    public MediaPlayer creaMediaPlayer(String url){
        MediaPlayer SoundPlayer = new MediaPlayer();

        try {
            SoundPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            SoundPlayer.setDataSource(url);
            SoundPlayer.prepare();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return SoundPlayer;
    }


    @Override
    public void onBackPressed()
    {
        if(player != null) {
            if (fullscreen) {
                player.pause();
                player.setFullscreen(false);
                fullscreen = false;
            } else
                super.onBackPressed();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,YouTubePlayer play, boolean wasRestored) {
        player = play;
        player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
        if (!wasRestored) {
            player.cueVideo(configuracion.getVideoCode());
        }
        MyPlayerStateChangeListener playerStateChangeListener = new MyPlayerStateChangeListener();
        player.setPlaybackEventListener(playbackEventListener);
        player.setPlayerStateChangeListener(playerStateChangeListener);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(configuracion.getDeveloperKey(), this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerFragment)getFragmentManager().findFragmentById(R.id.trailer);
    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {

        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
        }

        @Override
        public void onPlaying() {
            player.setFullscreen(true);
            fullscreen = true;
        }

        @Override
        public void onSeekTo(int arg0) {
        }

        @Override
        public void onStopped() {
        }

    };

    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {


        @Override
        public void onAdStarted() {
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
        }

        @Override
        public void onLoaded(String arg0) {
        }

        @Override
        public void onLoading() {

        }

        @Override
        public void onVideoEnded() {
            for(int i = 0; i<p.getNumRespuestas();i++){
                respuestas[i].setVisibility(View.VISIBLE);
            }
            player.setFullscreen(false);
            fullscreen = false;
        }

        @Override
        public void onVideoStarted() {
            player.setFullscreen(true);
            fullscreen = true;
        }
    };
}
