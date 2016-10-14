package com.example.manuel.prototipo_personaemocion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Manuel on 03/12/2015.
 */
public class asistenteDB{
    private HttpURLConnection urlConnection;
    private InputStream entrada;
    private DBhelper database;
    private SQLiteDatabase db;
    private static int versionActualPeliculas;
    private static int versionActualPreguntas;
    private static int versionActualRespuestas;
    private OutputStream databaseOutputStream;
    private OutputStreamWriter outStreamWriter;
    private InputStream databaseInputStream;
    private InputStreamReader inStreamReader;
    private static HashMap<String,Integer> versiones;
    private Context context;
    private static String fileName = "database.sqlite";
    private static String[] colPe = null;
    private static String[] colPr = null;
    private static String[] colRe = null;
    private int bdAct = 0;
    private File out;
    private ArrayList<String> lineasVersionLocal;
    private HashMap<String,Integer> modificacionesIndex;

    public asistenteDB (Context contexto, String[] columnasPe, String[] columnasPr, String[] columnasRe, boolean SinConexion){
        colPe = columnasPe;
        colPr = columnasPr;
        colRe = columnasRe;
        context = contexto;
        versiones = new HashMap<String,Integer>();
        //Creación de la referencia del archivo local de base de datos.
        out = new File(contexto.getFilesDir(), fileName);

        //No existe el fichero local: PRIMERA VEZ INICIANDO
        if(!out.exists()){
            iniciaBDlocal();
        }else{
            try {
                databaseInputStream  = new FileInputStream(out);
                inStreamReader = new InputStreamReader(databaseInputStream);
                //Consulta a la base de datos local, para inicializar el atributo versionDB.
                //Comprobamos versión de la bd local de Peliculas

                modificacionesIndex = new HashMap<String,Integer>();
                BufferedReader bufferedReader = new BufferedReader(inStreamReader);
                String receiveString = "";
                lineasVersionLocal = new ArrayList<String>();

                int i = 0;
                while((receiveString = bufferedReader.readLine()) != null){
                    lineasVersionLocal.add(receiveString);

                    String[] parts = receiveString.split(" ");
                    //Debe ser [2]

                    if (!parts[0].equals("CREATE") && !versiones.containsKey(parts[2])) {
                        String[] partes = receiveString.split(",");
                        String[] subVersion = partes[partes.length - 1].split("\\)");
                        versiones.put(parts[2].replaceAll("'", ""), Integer.parseInt(subVersion[0].replaceAll("'", "")));
                        modificacionesIndex.put(parts[2].replaceAll("'", ""),i);
                    }

                    i++;
                }

                inStreamReader.close();
                databaseInputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(!SinConexion) {
            try {
                conexion("consulta.php?tabla=Pelicula", "Pelicula");
                conexion("consulta.php?tabla=Pregunta", "Pregunta");
                conexion("consulta.php?tabla=Respuestas", "Respuestas");
            } catch (IOException e){
                e.printStackTrace();
            }

            if (bdAct > 0){
                int version = maxVersion();
                database = new DBhelper(contexto, version);
            } else {
                database = new DBhelper(contexto, 0);
            }
        }else
            database = new DBhelper(contexto, 0);

        db = database.getWritableDatabase();
    }

    public int maxVersion(){
        int resultado = -10;
        ArrayList<Integer> v = new ArrayList<Integer>();
        v.add(versiones.get("Pelicula"));
        v.add(versiones.get("Pregunta"));
        v.add(versiones.get("Respuestas"));

        for(int i = 0; i<v.size();i++){
            if(resultado < 5){
                resultado = v.get(i);
            }
        }
        return resultado;
    }

    public void iniciaBDlocal(){
        if(out.exists()){
            out.delete();
        }

        try {
            versiones.put("Pelicula", 0);
            versiones.put("Pregunta", 0);
            versiones.put("Respuestas", 0);
            out.createNewFile();
            databaseOutputStream  = new FileOutputStream(out,true);
            outStreamWriter = new OutputStreamWriter(databaseOutputStream);
            outStreamWriter.append("CREATE TABLE Pelicula (ID int PRIMARY KEY, Titulo varchar not null, Sinopsis text not null,Valoracion double null, NumValoraciones int null, LisComentarios varchar null, PorcentajesEmocionales varchar not null,  LisVideoopiniones text null, Trailer varchar not null, Caratula varchar not null, Cartel varchar not null, ImagenesAsociadas text null, Director varchar not null, Año int not null, Actores text not null, Genero varchar not null, Frase text not null, Duracion int not null, Pais varchar not null, Musica varchar not null, Palabras varchar not null, Version int null);"+"\n");
            outStreamWriter.append("CREATE TABLE Pregunta (TextoPregunta varchar NOT NULL, ImagenPregunta varchar NOT NULL, EnlaceVideo varchar NOT NULL, ArquitecturaPregunta int NOT NULL, Respuestas varchar NOT NULL,ID int PRIMARY KEY, Emocion varchar NOT NULL, EnlaceSonido varchar NOT NULL,Version int null);"+"\n");
            outStreamWriter.append("CREATE TABLE Respuestas (ID int PRIMARY KEY, ArquitecturaRespuesta varchar NOT NULL, ImagenRespuesta varchar NULL, TextoRespuesta varchar NULL, Valores varchar NOT NULL, SonidoRespuesta varchar NULL, Version int NULL);"+"\n");
            outStreamWriter.flush();
            outStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SQLiteDatabase getDB(){
        return db;
    }


    public void copyDataBase(JSONArray items, String nombre,String[] cols, int version, int nuevaVersion) throws IOException {
        //Cuando se ha creado un nuevo archivo porque la versión era anterior deberia seguir con append
        //está con write porque
        if(version != 0 && nuevaVersion > version){
            out.delete();
            out.createNewFile();
            databaseOutputStream  = new FileOutputStream(out,true);
            outStreamWriter = new OutputStreamWriter(databaseOutputStream);

            //Modificamos la versión actual por la deseada
            String[] lineasTotales = new String[lineasVersionLocal.size()];

            for(int i=0;i<lineasVersionLocal.size();i++){

                if(i == modificacionesIndex.get(nombre)){
                    lineasTotales[i] = lineasVersionLocal.get(modificacionesIndex.get(nombre)).replaceAll("'"+Integer.toString(versiones.get(nombre))+ "'" + "\\)", "'" + Integer.toString(nuevaVersion) + "'" + "\\)");
                }else{
                    lineasTotales[i] = lineasVersionLocal.get(i);
                }
                outStreamWriter.append(lineasTotales[i]+"\n");
            }
        }else{
            databaseOutputStream  = new FileOutputStream(out,true);
            outStreamWriter = new OutputStreamWriter(databaseOutputStream);
        }


        //Terminamos de copiar las nuevas filas de la base de datos externa.
        for(int i = 0; i<items.length();i++){
            try {
                JSONObject fila = items.getJSONObject(i);
                //Obtenemos string con la orden sql, parseada desde los datos obtenidos en la bd externa.
                String insercion = JSONparseString(fila, nombre,cols);
                //Escribimos en la Base de datos.
                outStreamWriter.append(insercion+"\n");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        outStreamWriter.flush();
        outStreamWriter.close();
        databaseOutputStream.flush();
        databaseOutputStream.close();
    }

    public String JSONparseString(JSONObject obj, String table, String[] cols) throws JSONException {

        String resultado ="";
        for(int i=0;i<cols.length;i++){
            if(i!=(cols.length-1))
                resultado = resultado + "'" +obj.get(cols[i]).toString()+ "'" + ",";
            else
                resultado = resultado + "'" +obj.get(cols[i]).toString()+ "'";
        }

        String resultadoFinal = "INSERT INTO "+ table +" VALUES("+resultado+");";

        return resultadoFinal;
    }

    public void conexion(String operacion, String nombreTabla) throws IOException{
        int versionBuscada = 0;
        String[] columnas = null;

        switch(nombreTabla){
            case "Pelicula" : columnas = colPe;
                            break;
            case "Pregunta" : columnas = colPr;
                            break;
            case "Respuestas" : columnas = colRe;
                break;
        }

        URL conexionComprobacion = new URL("http://personaemocion.esy.es/"+operacion+"&version=-1");
        urlConnection = (HttpURLConnection) conexionComprobacion.openConnection();
        entrada = new BufferedInputStream(urlConnection.getInputStream());

        if(!bdActualizada(nombreTabla)) {
            bdAct++;

            switch (nombreTabla){
                case "Pelicula" : versionBuscada = versionActualPeliculas;
                    break;
                case "Pregunta" : versionBuscada = versionActualPreguntas;
                    break;
                case "Respuestas" : versionBuscada = versionActualRespuestas;
                    break;
            }

            if(versionBuscada < versiones.get(nombreTabla)){
                //reiniciar el archivo local de base de datos.
                iniciaBDlocal();
            }

            urlConnection = null;
            URL conexion = new URL("http://personaemocion.esy.es/" + operacion + "&version="+versionBuscada + "&versionActual=" + versiones.get(nombreTabla));
            urlConnection = (HttpURLConnection) conexion.openConnection();
            entrada = new BufferedInputStream(urlConnection.getInputStream());

            String datos = (String) getString();

            if (!datos.equalsIgnoreCase("")) {
                JSONObject json; //objeto json donde se guardará los resultados de la consulta
                try {
                    json = new JSONObject(datos); //Se crea el objeto json con los datos recibidos.
                    JSONArray items = json.optJSONArray(nombreTabla);

                    //Creación de la nueva base de datos de preguntas local.
                    copyDataBase(items,nombreTabla,columnas,versiones.get(nombreTabla),versionBuscada);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public Object getString() throws IOException {
        String datos = convierteAstring(entrada);
        return datos;
    }

    public Bitmap getImagen(){
        Bitmap wall = BitmapFactory.decodeStream(entrada);
        return wall;
    }

    public String convierteAstring(InputStream is)
            throws IOException {
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }
    
    public boolean bdActualizada(String tabla) throws IOException {
        String datos = (String) getString();
        boolean resultado = true;

        if(!datos.equalsIgnoreCase("")) {
            JSONObject json; //objeto json donde se guardará los resultados de la consulta
            try {
                json = new JSONObject(datos); //Se crea el objeto json con los datos recibidos.
                JSONArray items = json.optJSONArray(tabla); //Se organiza un array por las filas de la tabla que se quiere consultar.
                JSONObject fila = items.getJSONObject(0);

                if(Integer.parseInt(fila.optString("Version")) != versiones.get(tabla)){
                    switch(tabla){
                        case "Pelicula": versionActualPeliculas = Integer.parseInt(fila.optString("Version"));
                                        break;
                        case "Pregunta": versionActualPreguntas = Integer.parseInt(fila.optString("Version"));
                                        break;
                        case "Respuestas": versionActualRespuestas = Integer.parseInt(fila.optString("Version"));
                            break;
                    }
                    resultado = false;
                }else{
                    resultado = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return resultado;
    }

}
