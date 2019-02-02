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
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class nuevaSeta extends AppCompatActivity {

    ImageView imgvw;
    EditText etNombre, etNombreComun, etDescripcion;
    Switch edibleSwitch;

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
                ContentValues cv = new ContentValues();
                
                cv.put(Utilidades.NOMBRE_COLUMNA, etNombre.getText().toString());
                cv.put(Utilidades.DESCRIPCION_COLUMNA, etDescripcion.getText().toString());
                cv.put(Utilidades.NOMBRECOMUN_COLUMNA, etNombreComun.getText().toString());
                cv.put(Utilidades.URLLINEA_COLUMNA, "");
                cv.put(Utilidades.COMESTIBLE_COLUMNA, edibleSwitch.isChecked());
                cv.put(Utilidades.FAV_COLUMNA, false);
                Bitmap newImg = ((BitmapDrawable)imgvw.getDrawable()).getBitmap();
                cv.put(Utilidades.IMG_COLUMNA, Utilidades.convertirImagenABytes(newImg));

                ContentResolver cr = getContentResolver();
                cr.insert(Utilidades.CONTENT_URI, cv);

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
}
