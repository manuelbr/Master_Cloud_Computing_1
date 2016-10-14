package com.example.manuel.prototipo_personaemocion.Actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;

import com.example.manuel.prototipo_personaemocion.R;

public class MenuCuestionario extends AppCompatActivity {
    private Button tipo1;
    private Button tipo2;
    private Button tipo3;
    private int wallpaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_cuestionario);

        Bundle b = getIntent().getExtras();
        wallpaper = b.getInt("wallpaper");
        final int wallpaper2 = wallpaper;

        tipo1 = (Button) findViewById(R.id.cuestionario1);
        tipo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cuestionario cue = new Cuestionario(MenuCuestionario.this,"Corto",wallpaper2);
            }
        });

        tipo2 = (Button) findViewById(R.id.cuestionario2);
        tipo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cuestionario cue = new Cuestionario(MenuCuestionario.this,"Medio",wallpaper2);
            }
        });


        tipo3 = (Button) findViewById(R.id.cuestionario3);
        tipo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cuestionario cue = new Cuestionario(MenuCuestionario.this,"Largo",wallpaper2);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(this, Menu.class);
        //Meter el cálculo del wallpaper
        myIntent.putExtra("wallpaper",wallpaper);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
        startActivity(myIntent);
        finish();
    }
}
