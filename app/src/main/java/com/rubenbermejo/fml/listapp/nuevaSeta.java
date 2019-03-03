package com.rubenbermejo.fml.listapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class nuevaSeta extends AppCompatActivity {

    ImageView imgvw;
    EditText etNombre, etNombreComun, etDescripcion;
    Switch edibleSwitch;
    HttpClient httpClient = new DefaultHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_seta);

        imgvw = findViewById(R.id.imageViewAdd);
        etNombre = findViewById(R.id.etNombre);
        etNombreComun = findViewById(R.id.edNombreComun);
        etDescripcion = findViewById(R.id.etDescripcion);
        edibleSwitch = findViewById(R.id.edibleSwitch);

        customizaActionBar();

        imgvw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent abrirImagen = new Intent(Intent.ACTION_GET_CONTENT);
                abrirImagen.setType("image/*");
                if (abrirImagen.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(abrirImagen, 3);
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3 && resultCode == Activity.RESULT_OK){

            if (data != null) {
                Uri uri = data.getData();

                imgvw.setImageURI(uri);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.aceptarCrearSeta:
                new InsertarSeta().execute(new ObjetoSetas(etNombre.getText().toString(), etDescripcion.getText().toString(), etNombreComun.getText().toString(), null, edibleSwitch.isEnabled(), "dano256px.png"));
                setResult(Activity.RESULT_OK);
                finish();
                break;
            case R.id.cancelarCrearSeta:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nueva_seta, menu);

        return true;
    }

    /**
     * Establece el título del Action Bar por "Añadir Seta" en
     * todos los idiomas a los que está traducida la aplicación.
     *
     */

    private void customizaActionBar() {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle(R.string.anadirSeta);
    }

    /**
     * Inserta una Seta en el servicio REST ubicado
     * en dam2.ieslamarisma.net/2019/rubenbermejo
     *
     */
    private class InsertarSeta extends AsyncTask<ObjetoSetas, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(ObjetoSetas... objetoSetas) {
            HttpPost postSeta = new HttpPost(Utilidades.DIRECCION_REST_MARISMA + Utilidades.POST_GET_ALL);
            postSeta.setHeader("content-type", "application/json");

            try {
                JSONObject setaAInsertar = new JSONObject();

                setaAInsertar.put("nombre", objetoSetas[0].getNombre());
                setaAInsertar.put("descripcion", objetoSetas[0].getDescripcion());
                setaAInsertar.put("nombre_comun", objetoSetas[0].getnombreComun());
                setaAInsertar.put("comestible", String.valueOf(Utilidades.boolToInt(objetoSetas[0].getComestible())));
                setaAInsertar.put("favorito", String.valueOf(Utilidades.boolToInt(objetoSetas[0].getFavorito())));
                if (objetoSetas[0].getURLlinea() != null) {
                    setaAInsertar.put("URL", objetoSetas[0].getURLlinea());
                }
                setaAInsertar.put("imagen", objetoSetas[0].getImagen());

                System.out.println(setaAInsertar.toString());

                StringEntity ent = new StringEntity(setaAInsertar.toString());
                postSeta.setEntity(ent);

                HttpResponse respuesta = httpClient.execute(postSeta);
                String respStr = EntityUtils.toString(respuesta.getEntity());

                System.out.println(respStr);

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
}
