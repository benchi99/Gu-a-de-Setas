package com.rubenbermejo.fml.listapp;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    HttpClient httpClient = new DefaultHttpClient();
    RecyclerView lista;
    AdapterData adaptador;
    ArrayList<ObjetoSetas> listaSetas = new ArrayList<>();
    SwipeRefreshLayout actualiza;
    boolean mostrarFavoritos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista = findViewById(R.id.lista);
        actualiza = findViewById(R.id.actualiza);
        lista.setLayoutManager(new LinearLayoutManager(this));
        listaSetas.add(new ObjetoSetas("Cargando...", null, "Espere, si no, recargue manualmente...", null, false, null));
        adaptador = new AdapterData(listaSetas);

        actualiza.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mostrarFavoritos) {
                    obtenListaMasReciente(Utilidades.FAVORITOS);
                    adaptador.setListSetas(listaSetas);
                } else {
                    obtenListaMasReciente(Utilidades.NORMAL);
                    adaptador.setListSetas(listaSetas);
                }
                adaptador.notifyDataSetChanged();
                actualiza.setRefreshing(false);
            }
        });
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, contenidoInformacion.class);
                ObjetoSetas seta = adaptador.getListSetas().get(lista.getChildAdapterPosition(v));

                Bundle informacion = new Bundle();
                informacion.putSerializable("seta", seta);
                intent.putExtras(informacion);

                startActivityForResult(intent, 2);
            }
        });

        lista.setAdapter(adaptador);
        obtenListaMasReciente(Utilidades.NORMAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.anadirSeta:
                Toast.makeText(this, R.string.anadirSeta, Toast.LENGTH_SHORT).show();
                Intent crear = new Intent(MainActivity.this, nuevaSeta.class);
                startActivityForResult(crear, 2);
                break;
            case R.id.filter:
                Toast.makeText(this, R.string.filter, Toast.LENGTH_SHORT).show();
                break;
            case R.id.toggleFav:
                if (!mostrarFavoritos) {
                    Toast.makeText(this, R.string.showingFavs, Toast.LENGTH_SHORT).show();
                    obtenListaMasReciente(Utilidades.FAVORITOS);
                    adaptador.setListSetas(listaSetas);
                    adaptador.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, R.string.hidingFavs, Toast.LENGTH_SHORT).show();
                    obtenListaMasReciente(Utilidades.NORMAL);
                    adaptador.setListSetas(listaSetas);
                    adaptador.notifyDataSetChanged();
                }
                mostrarFavoritos = !mostrarFavoritos;
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch(requestCode) {
            case 2:     //Actualiza la lista.
                if (resultCode == Activity.RESULT_OK) {
                    obtenListaMasReciente(Utilidades.NORMAL);
                    adaptador.setListSetas(listaSetas);
                    adaptador.notifyDataSetChanged();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(this, "Añadir seta cancelado.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void obtenListaMasReciente(String param){
        TareaGETALLSetas getallSetas = new TareaGETALLSetas(this);
        getallSetas.execute(param);
    }

    /**
     * AsyncTask que obtiene realizando un método GET una
     * lista de Setas desde dam2.ieslamarisma.net/2019/rubenbermejo
     *
     */
    private class TareaGETALLSetas extends AsyncTask<String, Integer, ArrayList<ObjetoSetas>> {

        ProgressDialog pdialog;
        Context context;

        public TareaGETALLSetas(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pdialog = new ProgressDialog(context);
            pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pdialog.setMessage("Obteniendo del servicio...");
            pdialog.setMax(100);
            pdialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pdialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<ObjetoSetas> objetoSetas) {
            super.onPostExecute(objetoSetas);
            listaSetas = objetoSetas;
            adaptador.setListSetas(listaSetas);
            adaptador.notifyDataSetChanged();
            pdialog.dismiss();
        }

        @Override
        protected ArrayList<ObjetoSetas> doInBackground(String... strings) {
            HttpGet getAll;

            try {
                if (strings[0].equals(Utilidades.NORMAL)) {
                    getAll = new HttpGet(Utilidades.DIRECCION_REST_MARISMA + Utilidades.POST_GET_ALL);
                } else {
                    getAll = new HttpGet(Utilidades.DIRECCION_REST_MARISMA + Utilidades.GET_FAV);
                }
                getAll.setHeader("content-type", "application/json");

                publishProgress(20);

                HttpResponse respuesta = httpClient.execute(getAll);
                String strRespuesta = EntityUtils.toString(respuesta.getEntity());

                publishProgress(40);

                JSONArray jsonArray = new JSONArray(strRespuesta);
                ArrayList<ObjetoSetas> listActual = new ArrayList<>();
                ObjetoSetas seta;

                publishProgress(60);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject JSONobj = jsonArray.getJSONObject(i);

                    seta = new ObjetoSetas(JSONobj.getString("NOMBRE"), JSONobj.getString("DESCRIPCION"), JSONobj.getString("NOMBRE_COMUN"), JSONobj.getString("URL"), Utilidades.intToBool(JSONobj.getInt("COMESTIBLE")), JSONobj.getString("IMAGEN"));
                    seta.setId(JSONobj.getInt("ID"));
                    seta.setFavorito(Utilidades.intToBool(JSONobj.getInt("FAVORITO")));

                    listActual.add(seta);
                }

                publishProgress(100);

                return listActual;
            } catch (IOException ioe) {
                if (strings[0].equals(Utilidades.NORMAL)){
                    Log.e("REST API", "Error al realizar petición GET " + Utilidades.DIRECCION_REST_MARISMA + Utilidades.POST_GET_ALL + ".");
                } else {
                    Log.e("REST API", "Error al realizar petición GET " + Utilidades.DIRECCION_REST_MARISMA + Utilidades.GET_FAV + ".");
                }
                ioe.printStackTrace();
                return null;
            } catch (JSONException jsone) {
                Log.e("REST API", "PETICIÓN GET ALL - Error al leer JSON.");
                jsone.printStackTrace();
                return null;
            }
        }
    }
}
