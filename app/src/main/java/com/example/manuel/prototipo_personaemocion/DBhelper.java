package com.example.manuel.prototipo_personaemocion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Manuel on 08/03/2016.
 */
public class DBhelper  extends SQLiteOpenHelper {
    public static int VERSION_DB = 2;
    public static String NOMBRE_BD = "database";
    public static Context mContext= null;
    private SQLiteDatabase db;
    private static String fileName = "database.sqlite";

    public DBhelper(Context context, int version){
        super(context,NOMBRE_BD + Integer.toString(VERSION_DB+version) +".db", null, (VERSION_DB+version));
        mContext = context;

        VERSION_DB = VERSION_DB + version;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        db = database;
        //Creación y cargado de la base de datos
        StringBuilder sb = new StringBuilder();
        Scanner sc = null;

        try {
            sc = new Scanner(new File(mContext.getFilesDir(), fileName));
            while(sc.hasNextLine()) {
                sb.append(sc.nextLine());
                sb.append('\n');
                if (sb.indexOf(";") != -1) {
                    database.execSQL(sb.toString());
                    sb.delete(0, sb.capacity());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
