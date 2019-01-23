package com.rubenbermejo.fml.listapp;

import android.app.Activity;
import android.content.Intent;
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
                abrirImagen.setType("*/*");  //Lo suyo es que fuera "image/*", pero no parece querer aceptar ninguna imagen si pongo eso.
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

        Intent intentDevolver = new Intent();
        Bundle bundleDevolver = new Bundle();

        int id = item.getItemId();

        switch (id) {

            case R.id.aceptarCrearSeta:
                //La imagen realmente no se pone, da igual qué selecciones ya que no he conseguido
                //poder realmente pasarle la imagen a mi objeto.
                bundleDevolver.putSerializable("nuevaSeta", new ObjetoSetas(etNombre.getText().toString(), etDescripcion.getText().toString(), etNombreComun.getText().toString(), null, edibleSwitch.isChecked(), R.drawable.dano128px));
                intentDevolver.putExtras(bundleDevolver);
                setResult(Activity.RESULT_OK, intentDevolver);
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
