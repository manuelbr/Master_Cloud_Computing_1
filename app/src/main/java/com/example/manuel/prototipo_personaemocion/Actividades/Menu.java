package com.example.manuel.prototipo_personaemocion.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.manuel.prototipo_personaemocion.Model.Pelicula;
import com.example.manuel.prototipo_personaemocion.R;

import java.util.ArrayList;

/**
 * Created by Manuel on 07/12/2015.
 */
public class Menu extends AppCompatActivity {
    private Button bibliotecaPeliculas;
    private Button cuestionarios;
    private static ArrayList<Pelicula> peliculas = new ArrayList<Pelicula>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Bundle b = getIntent().getExtras();
        final int wallpaper = b.getInt("wallpaper");

        getWindow().setBackgroundDrawableResource(wallpaper);

        bibliotecaPeliculas = (Button) findViewById(R.id.button1);
        bibliotecaPeliculas.setText("Biblioteca de Películas");
        bibliotecaPeliculas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListaPeliculas.setPelis(getPeliculas());
                Intent intentB = new Intent(Menu.this, ListaPeliculas.class);
                intentB.putExtra("sugiriendo",false);
                intentB.putExtra("wallpaper",wallpaper);
                Menu.this.startActivity(intentB);
            }
        });

        cuestionarios = (Button) findViewById(R.id.button2);
        cuestionarios.setText("Sugerencia Emocional");
        cuestionarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentB = new Intent(Menu.this, MenuCuestionario.class);
                intentB.putExtra("wallpaper",wallpaper);
                Menu.this.startActivity(intentB);
            }
        });
    }

    public static void setPeliculas(ArrayList<Pelicula> p){
        peliculas = p;
    }

    public static ArrayList<Pelicula> getPeliculas(){
        return peliculas;
    }
}
