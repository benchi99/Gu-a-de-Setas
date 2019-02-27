package com.rubenbermejo.fml.listapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    final public static String DIRECCION_REST_LOCAL = "http://localhost/ServicioREST";
    final public static String DIRECCION_REST_DATAUS = "http://datauschwitz-se/practicasRuben/ServicioREST";
    final public static String DIRECCION_REST_MARISMA = "http://dam2.ieslamarisma.net/2019/rubenbermejo";
    final public static String POST_GET_ALL = "/setas";
    final public static String GET_FAV = "/setas/favoritos";
    final public static String GET_PUT_DELETE_ID = "/setas/";
    final public static String IMG_LOCATION = "/img/";

    final public static String NORMAL = "normal";
    final public static String FAVORITOS = "favorito";

    final private static HttpClient httpClient = new DefaultHttpClient();

    public static ArrayList<ObjetoSetas> obtenerListaMasReciente(Context context, String param) throws IOException, JSONException {
        HttpGet getAll;

        if (param.equals(NORMAL)){
            getAll = new HttpGet(DIRECCION_REST_LOCAL + POST_GET_ALL);
        } else {
            getAll = new HttpGet(DIRECCION_REST_LOCAL + GET_FAV );
        }
        getAll.setHeader("content-type", "application/json");

        HttpResponse respuesta = httpClient.execute(getAll);
        String strRespuesta = EntityUtils.toString(respuesta.getEntity());

        JSONArray jsonArray = new JSONArray(strRespuesta);
        ArrayList<ObjetoSetas> listActual = new ArrayList<>();
        ObjetoSetas seta;

        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject JSONobj = jsonArray.getJSONObject(i);

            seta = new ObjetoSetas(JSONobj.getString("NOMBRE"), JSONobj.getString("NOMBRE"), JSONobj.getString("NOMBRE_COMUN"), JSONobj.getString("URL"), intToBool(JSONobj.getInt("COMESTIBLE")), IMG_LOCATION + JSONobj.getString("IMAGEN"));
            seta.setFavorito(intToBool(JSONobj.getInt("FAVORITO")));

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

    }

}
