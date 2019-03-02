package com.rubenbermejo.fml.listapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Utilidades {

    Context context;
    ArrayList<ObjetoSetas> datos;

    public Utilidades (Context context) {
        this.context = context;
    }

    //Atributos
    final public static String NOMBRE_COLUMNA = "nombre";
    final public static String DESCRIPCION_COLUMNA = "descripcion";
    final public static String NOMBRECOMUN_COLUMNA = "nombre_comun";
    final public static String URLLINEA_COLUMNA = "url";
    final public static String COMESTIBLE_COLUMNA = "comestible";
    final public static String ID_COLUMNA = "id";
    final public static String FAV_COLUMNA = "favorito";
    final public static String IMG_COLUMNA = "imagen";
    final public static String DIRECCION_REST_LOCAL = "https://localhost/ServicioREST";
    final public static String DIRECCION_REST_DATAUS = "https://datauschwitz-se/practicasRuben/ServicioREST";
    final public static String DIRECCION_REST_MARISMA = "https://dam2.ieslamarisma.net/2019/rubenbermejo";
    final public static String POST_GET_ALL = "/setas";
    final public static String GET_FAV = "/setas/favoritos";
    final public static String PUT_FAV = "/setas/favorito/";
    final public static String GET_PUT_DELETE_ID = "/setas/";
    final public static String IMG_LOCATION = "/img/";

    final public static String NORMAL = "normal";
    final public static String FAVORITOS = "favorito";


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

   public static boolean intToBool(int val) {

       if (val == 0) {
           return false;
       } else {
           return true;
       }
   }

   public static int boolToInt(boolean bool){
       if (bool) {
           return 1;
       } else {
           return 0;
       }
   }

   private class SubirImagenAlServidor extends AsyncTask<Void, Void, String> {
       @Override
       protected String doInBackground(Void... voids) {
           return null;
       }
   }
}
