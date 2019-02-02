package com.rubenbermejo.fml.listapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Utilidades {

    Context context;
    ArrayList<ObjetoSetas> datos;

    public Utilidades (Context context) {
        this.context = context;
    }


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
    final private static String AUTHORITY = "com.rubenbermejo.fml.contentprovidersetas";
    final public static String uRI = "content://" + AUTHORITY + "/" + NOMBRE_TABLA;
    final public static Uri CONTENT_URI = Uri.parse(uRI);
    final public static String NORMAL = "normal";
    final public static String FAVORITOS = "favorito";


    public static ArrayList<ObjetoSetas> obtenerListaMasReciente(Context context, String param) {

        String[] params = { "1" };
        String[] cols = { "*" };

        ContentResolver cr = context.getContentResolver();
        Cursor c;


        if (param.equals(NORMAL)){
            c = cr.query(CONTENT_URI, cols, null, null, null);
        } else {
            c = cr.query(CONTENT_URI, cols, Utilidades.FAV_COLUMNA + " = ?", params, null);
        }

        ArrayList<ObjetoSetas> listActual = new ArrayList<>();
        ObjetoSetas seta;

        while (c.moveToNext()) {
            seta = new ObjetoSetas(c.getString(1), c.getString(2), c.getString(3), c.getString(4), intToBool(Integer.parseInt(c.getString(5))), c.getBlob(7));
            seta.setId(c.getInt(0));
            listActual.add(seta);
        }
        return listActual;
   }

    public static byte[] convertirImagenABytes(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(175000);
        bmp.compress(Bitmap.CompressFormat.PNG, 0, baos);
        byte[] blob = baos.toByteArray();
        return blob;
    }

    public static Bitmap convertirBytesAImagen(byte[] blob) {
        Bitmap bmp;
        ByteArrayInputStream bais = new ByteArrayInputStream(blob);
        bmp = BitmapFactory.decodeStream(bais);
        return bmp;
    }

    private static boolean intToBool(int val) {

        if (val == 0) {
            return false;
        } else {
            return true;
        }

    }

    public static void delElement(Context context, int id){
        String[] param = { String.valueOf(id) };

        ContentResolver cr = context.getContentResolver();

        cr.delete(CONTENT_URI, Utilidades.ID_COLUMNA + " = ?", param);
    }

}
