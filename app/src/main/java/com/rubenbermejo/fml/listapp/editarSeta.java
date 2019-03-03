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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class editarSeta extends AppCompatActivity {

    ImageView imgvw;
    EditText etNombre, etNombreComun, etDescripcion;
    Switch edibleSwitch;
    ObjetoSetas setaAModificar;

    HttpClient httpClient = new DefaultHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_seta);

        imgvw = findViewById(R.id.imageViewAdd);
        etNombre = findViewById(R.id.etNombre);
        etNombreComun = findViewById(R.id.edNombreComun);
        etDescripcion = findViewById(R.id.etDescripcion);
        edibleSwitch = findViewById(R.id.edibleSwitch);

        Intent info = getIntent();
        setaAModificar = (ObjetoSetas) info.getSerializableExtra("setaMod");

        new BajarImagen().execute(setaAModificar.getImagen());
        etNombre.setText(setaAModificar.getNombre());
        etNombreComun.setText(setaAModificar.getnombreComun());
        etDescripcion.setText(setaAModificar.getDescripcion());
        edibleSwitch.setChecked(setaAModificar.getComestible());

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
    public void onBackPressed() {
        Intent intentDevolver = new Intent();
        System.out.println(setaAModificar.getId());
        intentDevolver.putExtra("id", setaAModificar.getId());
        setResult(Activity.RESULT_OK, intentDevolver);
        finish();
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

        String img;
        int id = item.getItemId();

        switch(id){
            case R.id.aceptarModifSeta:
                if (setaAModificar.getImagen() == null)
                    img = "dano256px.png";
                else
                    img = setaAModificar.getImagen();
                new ActualizarSeta().execute(new ObjetoSetas(etNombre.getText().toString(), etDescripcion.getText().toString(), etNombreComun.getText().toString(), setaAModificar.getURLlinea(), edibleSwitch.isChecked(), img));
                Intent intentDevolver = new Intent();
                intentDevolver.putExtra("id", setaAModificar.getId());
                setResult(Activity.RESULT_OK, intentDevolver);
                finish();
                break;
            case R.id.cancelarModifSeta:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editar_seta, menu);

        return true;
    }

    // TODO PROBAR
    /**
     * Actualiza una seta en el servicio REST ubicado en
     * dam2.ieslamarisma.net/2019/rubenbermejo
     */
    private class ActualizarSeta extends AsyncTask<ObjetoSetas, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(ObjetoSetas... objetoSetas) {
            HttpPut put = new HttpPut(Utilidades.DIRECCION_REST_MARISMA + Utilidades.GET_PUT_DELETE_ID + setaAModificar.getId());
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

                System.out.println(respStr);

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

    private class BajarImagen extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String imagen = Utilidades.DIRECCION_REST_MARISMA + strings[0];
            System.out.println(imagen);
            Bitmap imgAMostrar;
            try {
                InputStream ins = (InputStream) new URL(imagen).getContent();
                imgAMostrar = BitmapFactory.decodeStream(ins);
                ins.close();
            } catch (MalformedURLException badURLe) {
                Log.e("REST API - IMAGEN", "La URL que ha sido dada está mal formada.");
                imgAMostrar = null;
                badURLe.printStackTrace();
            } catch (IOException ioe) {
                Log.e("REST API - IMAGEN", "Hubo un error al descargar " + imagen);
                imgAMostrar = null;
                ioe.printStackTrace();
            }
            return imgAMostrar;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imgvw.setImageBitmap(bitmap);
        }
    }

}
