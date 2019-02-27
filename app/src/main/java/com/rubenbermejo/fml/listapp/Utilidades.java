package com.rubenbermejo.fml.listapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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

    /**
     * Obtiene la lista de setas más reciente, realizando
     * un método GET al servicio REST ubicado en
     * dam2.ieslamarisma.net/2019/rubenbermejo
     *
     * @param param Parametro de obtención de setas.
     * @return Lista de setas más reciente.
     */

    public static ArrayList<ObjetoSetas> obtenerListaMasReciente(String param) {
        HttpGet getAll;

        try {
            if (param.equals(NORMAL)) {
                getAll = new HttpGet(DIRECCION_REST_MARISMA + POST_GET_ALL);
            } else {
                getAll = new HttpGet(DIRECCION_REST_MARISMA + GET_FAV);
            }
            getAll.setHeader("content-type", "application/json");

            HttpResponse respuesta = httpClient.execute(getAll);
            String strRespuesta = EntityUtils.toString(respuesta.getEntity());

            JSONArray jsonArray = new JSONArray(strRespuesta);
            ArrayList<ObjetoSetas> listActual = new ArrayList<>();
            ObjetoSetas seta;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject JSONobj = jsonArray.getJSONObject(i);

                seta = new ObjetoSetas(JSONobj.getString("NOMBRE"), JSONobj.getString("NOMBRE"), JSONobj.getString("NOMBRE_COMUN"), JSONobj.getString("URL"), intToBool(JSONobj.getInt("COMESTIBLE")), IMG_LOCATION + JSONobj.getString("IMAGEN"));
                seta.setId(JSONobj.getInt("ID"));
                seta.setFavorito(intToBool(JSONobj.getInt("FAVORITO")));

                listActual.add(seta);
            }
            return listActual;
        } catch (IOException ioe) {
            if (param.equals(NORMAL)){
                Log.e("REST API", "Error al realizar petición GET " + DIRECCION_REST_MARISMA + POST_GET_ALL + ".");
            } else {
                Log.e("REST API", "Error al realizar petición GET " + DIRECCION_REST_MARISMA + GET_FAV + ".");
            }
            ioe.printStackTrace();
            return null;
        } catch (JSONException jsone) {
            Log.e("REST API", "Error al leer JSON.");
            jsone.printStackTrace();
            return null;
        }
   }

    /**
     * Obtiene una seta del servicio REST ubicado
     * en dam2.ieslamarisma.net/2019/rubenbermejo
     * según identificador especificado.
     *
     * @param id Identificador de seta.
     * @return Seta coincidente con el identificador especificado, si existe.
     */

   public static ObjetoSetas obtenerSeta(int id) {
        HttpGet getSeta = new HttpGet(DIRECCION_REST_MARISMA + GET_PUT_DELETE_ID + String.valueOf(id));
        getSeta.setHeader("content-type", "application-json");

        try {
            HttpResponse respuesta = httpClient.execute(getSeta);
            String strResp = EntityUtils.toString(respuesta.getEntity());

            JSONObject json = new JSONObject(strResp);
            ObjetoSetas seta = new ObjetoSetas(json.getString("NOMBRE"), json.getString("DESCRIPCION"), json.getString("NOMBRE_COMUN"), json.getString("URL"), intToBool(json.getInt("COMESTIBLE")), IMG_LOCATION + json.getString("IMAGEN"));
            seta.setId(json.getInt("ID"));
            seta.setFavorito(intToBool(json.getInt("FAVORITO")));

            return seta;
        } catch (IOException ioe) {
            Log.e("REST API", "Error al realizar peticion GET " + DIRECCION_REST_MARISMA + GET_PUT_DELETE_ID + String.valueOf(id) + ".");
            ioe.printStackTrace();
            return null;
        } catch (JSONException jsone) {
            Log.e("REST API", "Error a la hora de leer JSON.");
            jsone.printStackTrace();
            return null;
        }
   }


   public static void insertarSeta(){

   }

   public static void modificarSeta() {

   }

   public static void delElement(Context context, int id){

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
}
