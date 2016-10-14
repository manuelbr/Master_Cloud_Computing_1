package com.example.manuel.prototipo_personaemocion.Actividades.Listeners;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.manuel.prototipo_personaemocion.Actividades.Cuestionario;
import com.example.manuel.prototipo_personaemocion.Model.Respuesta;

/**
 * Created by Manuel on 24/03/2016.
 */
public class respuestaButtonClickListener implements View.OnClickListener {
        private Button imb = null;
        private Context contexto = null;
        private Respuesta r = null;
        private EditText entrada;
        private MediaPlayer[] mvp;

        public respuestaButtonClickListener(Button myimb, Context context, EditText ent){
            imb = myimb;
            contexto = context;
            entrada = ent;
        }

        public respuestaButtonClickListener(Button myimb, Context context) {
            imb = myimb;
            contexto = context;
        }

        public respuestaButtonClickListener(Button myimb,Respuesta r1, Context context) {
            imb = myimb;
            contexto = context;
            r = r1;
        }

        public respuestaButtonClickListener(Button myimb,Respuesta r1, MediaPlayer[] mvp2, Context context) {
            imb = myimb;
            contexto = context;
            r = r1;
            mvp = mvp2;
        }

        @Override
        public void onClick(View v) {
            //La pregunta no es de tipo 9
            if(r!=null){
                //En caso de que la pregunta incluya un player.
                if(mvp != null){
                    for(int  i = 0; i<mvp.length; i++){
                        mvp[i].stop();
                    }
                }

                //Se actualizan las emociones en las que influye la respuesta clicada.
                for (String key : r.getPorcentajes().keySet()) {
                    Cuestionario.addPercentage(key, r.getPorcentajes().get(key));
                }
            }else{
                Editable text = entrada.getText();
                String res = text.toString();
                Cuestionario.setPalabraClave(res);
            }

            //Pasamos a la siguiente pregunta
            Cuestionario.siguientePregunta(contexto);
        }

}


