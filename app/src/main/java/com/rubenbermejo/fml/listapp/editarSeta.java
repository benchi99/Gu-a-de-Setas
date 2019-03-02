package com.rubenbermejo.fml.listapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class editarSeta extends AppCompatActivity {

    ImageView imgvw;
    EditText etNombre, etNombreComun, etDescripcion;
    Switch edibleSwitch;
    ObjetoSetas setaAModificar;
    Bitmap img;
    Uri imagenSeleccionada;
    String direccionImagen;

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

        imgvw.setImageBitmap(setaAModificar.getImg());
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

        switch(id){
            case R.id.aceptarModifSeta:
                new ActualizarSeta().execute(new ObjetoSetas(etNombre.getText().toString(), etDescripcion.getText().toString(), etNombreComun.getText().toString(), setaAModificar.getURLlinea(), edibleSwitch.isChecked(), obtenNombreArchivoImagen(imagenSeleccionada)));
                setResult(Activity.RESULT_OK);
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

    private String obtenNombreArchivoImagen(Uri uri){
        // TODO Obtener direccion y nombre de la imagen seleccionada del telefono.
        return null;
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

}
