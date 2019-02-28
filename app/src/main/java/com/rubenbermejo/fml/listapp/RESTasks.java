package com.rubenbermejo.fml.listapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class RESTasks {
    final private static HttpClient httpClient = new DefaultHttpClient();
    ArrayList<ObjetoSetas> listaActual;
    ObjetoSetas setaDevolver;



    /**
     * Obtiene una seta del servicio REST ubicado
     * en dam2.ieslamarisma.net/2019/rubenbermejo
     * según identificador especificado.
     *
     */

    private class ObtenSeta extends AsyncTask<Integer, Integer, ObjetoSetas>{
        @Override
        protected ObjetoSetas doInBackground(Integer... integers) {
            HttpGet getSeta = new HttpGet(Utilidades.DIRECCION_REST_MARISMA + Utilidades.GET_PUT_DELETE_ID + String.valueOf(integers[0]));
            getSeta.setHeader("content-type", "application-json");

            try {
                HttpResponse respuesta = httpClient.execute(getSeta);
                String strResp = EntityUtils.toString(respuesta.getEntity());

                JSONObject json = new JSONObject(strResp);
                ObjetoSetas seta = new ObjetoSetas(json.getString("NOMBRE"), json.getString("DESCRIPCION"), json.getString("NOMBRE_COMUN"), json.getString("URL"), Utilidades.intToBool(json.getInt("COMESTIBLE")), Utilidades.IMG_LOCATION + json.getString("IMAGEN"));
                seta.setId(json.getInt("ID"));
                seta.setFavorito(Utilidades.intToBool(json.getInt("FAVORITO")));

                return seta;
            } catch (IOException ioe) {
                Log.e("REST API", "Error al realizar peticion GET " + Utilidades.DIRECCION_REST_MARISMA + Utilidades.GET_PUT_DELETE_ID + String.valueOf(integers[0]) + ".");
                ioe.printStackTrace();
                return null;
            } catch (JSONException jsone) {
                Log.e("REST API", "Error a la hora de leer JSON.");
                jsone.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ObjetoSetas objetoSetas) {
            super.onPostExecute(objetoSetas);
            setaDevolver = objetoSetas;
        }
    }

    /**
     * Inserta una Seta en el servicio REST ubicado
     * en dam2.ieslamarisma.net/2019/rubenbermejo
     *
     */
    private class InsertarSeta extends AsyncTask<ObjetoSetas, Integer, Boolean>{
        @Override
        protected Boolean doInBackground(ObjetoSetas... objetoSetas) {
            HttpPost postSeta = new HttpPost(Utilidades.DIRECCION_REST_MARISMA + Utilidades.POST_GET_ALL);
            postSeta.setHeader("content-type", "application/json");

            try {
                JSONObject setaAInsertar = new JSONObject();

                setaAInsertar.put("nombre", objetoSetas[0].getNombre());
                setaAInsertar.put("descripcion", objetoSetas[0].getDescripcion());
                setaAInsertar.put("nombre_comun", objetoSetas[0].getnombreComun());
                setaAInsertar.put("comestible", Utilidades.boolToInt(objetoSetas[0].getComestible()));
                setaAInsertar.put("favorito", Utilidades.boolToInt(objetoSetas[0].getFavorito()));
                if (objetoSetas[0].getURLlinea() != null) {
                    setaAInsertar.put("URL", objetoSetas[0].getURLlinea());
                }
                setaAInsertar.put("imagen", objetoSetas[0].getImagen());

                StringEntity ent = new StringEntity(setaAInsertar.toString());
                postSeta.setEntity(ent);

                HttpResponse respuesta = httpClient.execute(postSeta);
                String respStr = EntityUtils.toString(respuesta.getEntity());

                if (!respStr.equals("true")){
                    return false;
                }
            } catch (IOException ioe) {
                Log.e("REST API", "Error al realizar método POST! " + ioe.getMessage());
                return false;
            } catch (JSONException jsone) {
                Log.e("REST API", "Error al formar JSON. " + jsone.getMessage());
                return false;
            }

            return true;
        }
    }

    /**
     * Elimina una seta en el servicio REST ubicado en
     * dam2.ieslamarisma.net/2019/rubenbermejo
     *
     */
    private class EliminarSeta extends AsyncTask<Integer, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... integers) {
            HttpDelete del = new HttpDelete(Utilidades.DIRECCION_REST_MARISMA + Utilidades.GET_PUT_DELETE_ID + String.valueOf(integers[0]));

            del.setHeader("content-type", "application/json");

            try {
                HttpResponse respuesta = httpClient.execute(del);
                String respStr = EntityUtils.toString(respuesta.getEntity());

                if (!respStr.equals("true")){
                    return false;
                }
            } catch (IOException ioe) {
                Log.e("REST API", "Error al realizar petición DELETE! " + ioe.getMessage());
                return false;
            }
            return true;
        }
    }

    /**
     * Actualiza una seta en el servicio REST ubicado en
     * dam2.ieslamarisma.net/2019/rubenbermejo
     */
    private class ActualizarSeta extends AsyncTask<ObjetoSetas, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(ObjetoSetas... objetoSetas) {
            HttpPut put = new HttpPut(Utilidades.DIRECCION_REST_MARISMA + Utilidades.GET_PUT_DELETE_ID + String.valueOf(objetoSetas[0].getId()));
            put.setHeader("content-type", "application/json");

            try {
                JSONObject setaActualizar = new JSONObject();
                setaActualizar.put("nombre", objetoSetas[0].getNombre());
                setaActualizar.put("descripcion", objetoSetas[0].getDescripcion());
                setaActualizar.put("nombre_comun", objetoSetas[0].getnombreComun());
                setaActualizar.put("comestible", Utilidades.boolToInt(objetoSetas[0].getComestible()));
                setaActualizar.put("favorito", Utilidades.boolToInt(objetoSetas[0].getFavorito()));
                if (objetoSetas[0].getURLlinea() != null) {
                    setaActualizar.put("URL", objetoSetas[0].getURLlinea());
                }
                setaActualizar.put("imagen", objetoSetas[0].getImagen());

                StringEntity ent = new StringEntity(setaActualizar.toString());
                put.setEntity(ent);

                HttpResponse resp = httpClient.execute(put);
                String respStr = EntityUtils.toString(resp.getEntity());

                if (!respStr.equals("true")){
                    return false;
                }
            } catch (IOException ioe) {
                return false;
            } catch (JSONException jsone) {
                return false;
            }

            return true;
        }
    }



    public ObjetoSetas obtenerSeta(int id) {
        ObtenSeta obSeta = new ObtenSeta();
        obSeta.execute(id);
        return setaDevolver;
    }

    public void insertarSeta(ObjetoSetas seta){
        InsertarSeta ins = new InsertarSeta();
        ins.execute(seta);
    }

    public void modificarSeta(ObjetoSetas seta){
        ActualizarSeta upd = new ActualizarSeta();
        upd.execute(seta);
    }

    public void eliminarSeta(int id){
        EliminarSeta del = new EliminarSeta();
        del.execute(id);
    }
}



