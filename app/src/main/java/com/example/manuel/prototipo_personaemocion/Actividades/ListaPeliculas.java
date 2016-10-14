package com.example.manuel.prototipo_personaemocion.Actividades;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.manuel.prototipo_personaemocion.Actividades.Adaptadores.expandableList;
import com.example.manuel.prototipo_personaemocion.Model.Pelicula;
import com.example.manuel.prototipo_personaemocion.R;

import java.util.ArrayList;

public class ListaPeliculas extends AppCompatActivity {
    private expandableList adaptadorExp;
    private ExpandableListView listView;
    private NetworkInfo redesActivas;
    private static Bundle estadoGuardado;
    private static ArrayList<Pelicula> pelis;
    private boolean sugiriendo;
    private int wallpaper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Películas");
        setContentView(R.layout.activitylistapeliculas);

        listView = (ExpandableListView) findViewById(R.id.listView);

        //Comprueba si lo que queremos crear es una lista de las películas de la biblioteca o de películas sugeridas:
        Bundle b = getIntent().getExtras();
        sugiriendo = b.getBoolean("sugiriendo");
        wallpaper = b.getInt("wallpaper");

        //Comrprueba si hay conexión a internet
        ConnectivityManager connec = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        redesActivas = connec.getActiveNetworkInfo();
        final Context contexto = this.getBaseContext();

        //Aquí se llamará a la actividad de película seleccionada, donde se mostrará la ficha de la película en cuestión.
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView arg0, View arg1, int groupPosition, int childPosition, long arg4) {
                if (redesActivas != null) {
                    //Carga las imagenes más pesadas de la película en concreto.
                    Pelicula p = adaptadorExp.getChild(groupPosition, childPosition);
                    ProgressDialog progress = new ProgressDialog(ListaPeliculas.this);
                    p.cargaPelicula(ListaPeliculas.this, progress);
                    return false;
                } else {
                    Toast.makeText(getBaseContext(), "No hay conexión a internet", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

        //Se abre la lista de películas del género seleccionado.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                //Toast.makeText(getBaseContext(), "", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        if(!sugiriendo) {
            adaptadorExp = new expandableList(this, new ArrayList<ArrayList<Pelicula>>(), new ArrayList<String>()) {
                @Override
                public boolean areAllItemsEnabled() {
                    return true;
                }
            };
        }else{
            adaptadorExp = new expandableList(this, new ArrayList<Pelicula>()) {
                @Override
                public boolean areAllItemsEnabled() {
                    return true;
                }
            };
        }
        mostrarDatos();
    }

    public static void setPelis(ArrayList<Pelicula> p){
        pelis = p;
    }


    @Override
    protected void onSaveInstanceState(Bundle estadoGuardado){
        super.onSaveInstanceState(estadoGuardado);
        this.estadoGuardado = estadoGuardado;
    }

    @Override
    protected void onRestoreInstanceState(Bundle unused){
        super.onRestoreInstanceState(this.estadoGuardado);
    }

    protected void mostrarDatos(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                inicializaBiblioteca();
                listView.setAdapter(adaptadorExp);
                listView.expandGroup(0);
            }
        });
    }

    private void inicializaBiblioteca() {
        for (int i=0; i < pelis.size(); i++) {
            adaptadorExp.addItem(pelis.get(i));
        }
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
