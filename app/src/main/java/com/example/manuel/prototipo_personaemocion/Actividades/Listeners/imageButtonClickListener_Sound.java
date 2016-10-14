package com.example.manuel.prototipo_personaemocion.Actividades.Listeners;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.manuel.prototipo_personaemocion.R;

/**
 * Created by Manuel on 05/03/2016.
 */
public class imageButtonClickListener_Sound implements MediaPlayer.OnCompletionListener,View.OnClickListener {
        private  ImageButton imb = null;
        private  int estadoReproduccion;
        private MediaPlayer player;
        private Button[] asociado;

        //Hay que adaptar el tipo de boton que se le pasa, en función de si se trata de imagen o texto.
        public imageButtonClickListener_Sound(ImageButton myimb, MediaPlayer play, Button[] asociad) {
            imb = myimb;
            player = play;
            estadoReproduccion = 0;
            asociado = asociad;
            player.setOnCompletionListener(this);
        }

        @Override
        public void onClick(View v)
        {
            if (estadoReproduccion == 0) {
                imb.setImageResource(R.drawable.pause);
                estadoReproduccion = 1;
                player.start();
            } else
            if (estadoReproduccion == 1) {
                imb.setImageResource(R.drawable.play);
                estadoReproduccion = 0;
                player.pause();
            }
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            imb.setImageResource(R.drawable.play);
            for(int i = 0; i < asociado.length; i++ ){
                asociado[i].setVisibility(View.VISIBLE);
            }
            estadoReproduccion = 0;
        }
}
