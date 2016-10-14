package com.example.manuel.prototipo_personaemocion.Model;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;

import java.util.HashMap;


/**
 * Created by Manuel on 22/03/2016.
 */
public class Respuesta {
    private int ID;
    private String tipoRespuesta;
    private String enlaceImagen;
    private Bitmap imagenRespuesta;
    private String textoRespuesta;
    private String sonidoRespuesta;
    private HashMap<String,Integer> valores;

    public Respuesta(Cursor c, Context context){
        ID = Integer.parseInt(c.getString(0));
        tipoRespuesta = c.getString(1);
        enlaceImagen = c.getString(2);
        textoRespuesta = c.getString(3);

        valores = transPorcentajes(c.getString(4));
        sonidoRespuesta = c.getString(5);
    }

    public String getTipoRespuesta(){
        return tipoRespuesta;
    }

    public String getEnlaceImagen(){
        return enlaceImagen;
    }

    public String getTextoRespuesta(){
        return textoRespuesta;
    }

    public String getSonidoRespuesta(){
        return sonidoRespuesta;
    }

    public HashMap<String,Integer> transPorcentajes(String valores){
        HashMap<String,Integer> porcentajes = new HashMap<String,Integer>();
        String[] p = valores.split(" ");
        for(int i = 0; i<p.length;i++){
            String[] kv = p[i].split("\\-");
            porcentajes.put(kv[0],Integer.parseInt(kv[1]));
        }

        return porcentajes;
    }

    public HashMap<String,Integer> getPorcentajes(){
        return valores;
    }

    public Bitmap getImagenRespuesta(){
        return imagenRespuesta;
    }

    public void setImagenRespuesta(Bitmap imagen){
        imagenRespuesta = imagen;
    }
}
