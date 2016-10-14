package com.example.manuel.prototipo_personaemocion.Actividades;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.manuel.prototipo_personaemocion.Actividades.Listeners.imageButtonClickListener_Sinopsis;
import com.example.manuel.prototipo_personaemocion.Config;
import com.example.manuel.prototipo_personaemocion.Model.Pelicula;
import com.example.manuel.prototipo_personaemocion.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class FichaPelicula extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{
    private Pelicula pelicula;

    private TextView titulo, frase, director, año, pais, duracion, musica;
    private static TextView sinopsis, sinopsisTex, actoresTex, actores;
    private ImageView cartel, emocion;
    private static ImageButton imb,imb2, imb3, imb4;
    private YouTubePlayerView youTubeView;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private Config configuracion;
    private ArrayList<Bitmap> galeria;
    private ImageSwitcher imageSwitcher;
    private int position;
    private Animation left_in, left_out, right_in, right_out, actual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Película");

        //Recibo parámetros de invocación.
        Bundle b = getIntent().getExtras();
        int idPelicula = b.getInt("ID");
        pelicula = Menu.getPeliculas().get(idPelicula);

        setContentView(R.layout.activity_ficha_pelicula);

        //Se establecen los datos de la película en concreto
        titulo = (TextView) findViewById(R.id.titulo);
        titulo.setText(pelicula.getTitulo());

        cartel = (ImageView) findViewById(R.id.cartel);
        cartel.setImageBitmap(pelicula.getCartel());

        frase = (TextView) findViewById(R.id.frase);
        frase.setText(pelicula.getFrase());


        emocion = null;
        for(int i=0;i<3;i++){
            switch(i){
                case 0: emocion = (ImageView) findViewById(R.id.emocion1);
                        break;
                case 1: emocion = (ImageView) findViewById(R.id.emocion2);
                        break;
                case 2: emocion = (ImageView) findViewById(R.id.emocion3);
                        break;
            }

            switch(pelicula.getEmocionesPredominantes().get(i+(pelicula.getEmocionesPredominantes().size()-3))){
                case "Ira": emocion.setImageResource(R.drawable.ira);
                            break;
                case "Tristeza": emocion.setImageResource(R.drawable.triste);
                            break;
                case "Temor": emocion.setImageResource(R.drawable.miedo);
                            break;
                case "Placer": emocion.setImageResource(R.drawable.placer);
                            break;
                case "Amor": emocion.setImageResource(R.drawable.amor);
                            break;
                case "Sorpresa": emocion.setImageResource(R.drawable.sorpresa);
                            break;
                case "Disgusto": emocion.setImageResource(R.drawable.disgusto);
                            break;
                case "Verguenza": emocion.setImageResource(R.drawable.verguenza);
                            break;
                case "Alegria": emocion.setImageResource(R.drawable.alegria);
                    break;
            }
        }

        pais = (TextView) findViewById(R.id.pais);
        pais.setText(pelicula.getPais());

        duracion = (TextView) findViewById(R.id.duracion);
        duracion.setText(pelicula.getDuracion());

        musica =  (TextView) findViewById(R.id.musica);
        musica.setText(pelicula.getMusica());

        director = (TextView) findViewById(R.id.director);
        director.setText(pelicula.getDirector());

        actores = (TextView) findViewById(R.id.actor);
        actores.setText(pelicula.getActores());
        actores.setVisibility(View.GONE);
        imb2 = (ImageButton) findViewById(R.id.imageButton2);
        imb2.setImageResource(R.drawable.cerrado);
        imageButtonClickListener_Sinopsis listenerButton2 = new imageButtonClickListener_Sinopsis(imb2,actores);
        actoresTex = (TextView) findViewById(R.id.actortex);
        imb2.setOnClickListener(listenerButton2);
        actoresTex.setOnClickListener(listenerButton2);

        sinopsis = (TextView) findViewById(R.id.sinopsis);
        sinopsis.setText(pelicula.getSinopsis());
        sinopsis.setVisibility(View.GONE);

        año = (TextView) findViewById(R.id.año);
        año.setText(Integer.toString(pelicula.getAño()));

        imb = (ImageButton) findViewById(R.id.imageButton);
        imb.setImageResource(R.drawable.cerrado);
        imageButtonClickListener_Sinopsis listenerButton = new imageButtonClickListener_Sinopsis(imb,sinopsis);
        imb.setOnClickListener(listenerButton);

        //Para que también reaccione a la pulsación del texto de sinopsis.
        sinopsisTex = (TextView) findViewById(R.id.sinopsistex);
        sinopsisTex.setOnClickListener(listenerButton);

        galeria = pelicula.getImagenesAsociadas();

        position = 0;
        imageSwitcher = (ImageSwitcher)findViewById(R.id.galeria);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                ImageView imagen = new ImageView(getApplicationContext());
                imagen.setScaleType(ImageView.ScaleType.FIT_XY);
                imagen.setAdjustViewBounds(true);
                imagen.setLayoutParams(new ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams.MATCH_PARENT, ImageSwitcher.LayoutParams.WRAP_CONTENT));
                return imagen;
            }
        });

        Bitmap bit = galeria.get(0);
        BitmapDrawable drawable = new BitmapDrawable(getResources(),bit);
        imageSwitcher.setImageDrawable(drawable);


        left_in = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        left_out = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        right_in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        right_out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);

        actual = left_in;
        imageSwitcher.setInAnimation(left_in);
        imageSwitcher.setOutAnimation(left_out);

        imb3 = (ImageButton) findViewById(R.id.left);
        imb3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(actual !=  left_in){
                    actual = left_in;
                    imageSwitcher.setInAnimation(left_in);
                    imageSwitcher.setOutAnimation(left_out);
                }
                position--;
                Bitmap bit = galeria.get(abs(position) % galeria.size());
                BitmapDrawable drawable = new BitmapDrawable(getResources(),bit);
                imageSwitcher.setImageDrawable(drawable);
            }
        });

        imb4 = (ImageButton) findViewById(R.id.right);
        imb4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(actual !=  right_in){
                    actual = right_in;
                    imageSwitcher.setInAnimation(right_in);
                    imageSwitcher.setOutAnimation(right_out);
                }
                position++;
                Bitmap bit = galeria.get(abs(position) % galeria.size());
                BitmapDrawable drawable = new BitmapDrawable(getResources(),bit);
                imageSwitcher.setImageDrawable(drawable);
            }
        });

        configuracion = new Config(pelicula.getTrailer());
        youTubeView = (YouTubePlayerView) findViewById(R.id.trailer);
        youTubeView.initialize(configuracion.getDeveloperKey(), this);

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    public void onInitializationSuccess(YouTubePlayer.Provider provider,YouTubePlayer player, boolean wasRestored) {
        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);
        player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        if (!wasRestored) {
            player.cueVideo(configuracion.getVideoCode());
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
        }

        @Override
        public void onSeekTo(int arg0) {
        }

        @Override
        public void onStopped() {
        }

    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {

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
        }

        @Override
        public void onVideoStarted() {
        }
    };

}
