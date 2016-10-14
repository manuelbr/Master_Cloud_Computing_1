package com.example.manuel.prototipo_personaemocion.Actividades.Listeners;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.manuel.prototipo_personaemocion.R;

/**
 * Created by Manuel on 16/12/2015.
 */

public class imageButtonClickListener_Sinopsis implements View.OnClickListener {
    private  ImageButton imb = null;
    private  int estadoSinopsis;
    private  TextView afectado = null;

    public imageButtonClickListener_Sinopsis(ImageButton myimb, TextView aAfectar) {
        imb = myimb;
        afectado = aAfectar;
        estadoSinopsis = 0;
    }

    @Override
    public void onClick(View v)
    {
        if (estadoSinopsis == 0) {
            imb.setImageResource(R.drawable.abierto);
            estadoSinopsis = 1;
            afectado.setVisibility(View.VISIBLE);
        } else
        if (estadoSinopsis == 1) {
            imb.setImageResource(R.drawable.cerrado);
            estadoSinopsis = 0;
            afectado.setVisibility(View.GONE);
        }
    }

}


