package com.rubenbermejo.fml.listapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.rubenbermejo.fml.listapp.CamaraDatos.listDatos;

public class Utilidades {

    //VERSIÓN
    final public static int VERSION = 1;

    //Tablas
    final public static String NOMBRE_TABLA = "SETAS";

    //Atributos
    final public static String NOMBRE_COLUMNA = "nombre";
    final public static String DESCRIPCION_COLUMNA = "descripcion";
    final public static String NOMBRECOMUN_COLUMNA = "nombreComun";
    final public static String URLLINEA_COLUMNA = "URLlinea";
    final public static String COMESTIBLE_COLUMNA = "comestible";
    final public static String ID_COLUMNA = "id";
    final public static String FAV_COLUMNA = "favorito";
    final public static String IMG_COLUMNA = "imagen";

    public static void rellenaBaseDeDatos(SQLiteDatabase bd) {
        ContentValues cvs = null;
        for (int i = 0; i < listDatos.size(); i++) {
            cvs = new ContentValues();
            cvs.put(NOMBRE_COLUMNA, listDatos.get(i).getNombre());
            cvs.put(DESCRIPCION_COLUMNA, listDatos.get(i).getDescripcion());
            cvs.put(NOMBRECOMUN_COLUMNA, listDatos.get(i).getnombreComun());
            cvs.put(URLLINEA_COLUMNA, listDatos.get(i).getURLlinea());
            cvs.put(COMESTIBLE_COLUMNA, listDatos.get(i).getComestible());
            cvs.put(FAV_COLUMNA, listDatos.get(i).getFavorito());
            cvs.put(IMG_COLUMNA, convertirImagenABytes(MainActivity.getBmpFromList(i)));

            bd.insert(NOMBRE_TABLA, ID_COLUMNA, cvs);
        }
    }

    public ArrayList<ObjetoSetas> obtenerListaMasReciente(SetasSQLiteHelper con) {

        SQLiteDatabase db = con.getReadableDatabase();

        ObjetoSetas seta = null;
        Cursor c = db.rawQuery("SELECT * FROM " + NOMBRE_TABLA, null);

        while (c.moveToNext()) {
            //seta = new ObjetoSetas();


        }


        return null;
    }

    public static byte[] convertirImagenABytes(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(175000);
        bmp.compress(Bitmap.CompressFormat.PNG, 0, baos);
        byte[] blob = baos.toByteArray();
        return blob;
    }

    public static Bitmap convertirBytesAImagen(byte[] blob) {
        Bitmap bmp = null;
        ByteArrayInputStream bais = new ByteArrayInputStream(blob);
        bmp = BitmapFactory.decodeStream(bais);
        return bmp;
    }

}
