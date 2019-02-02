package com.rubenbermejo.fml.listapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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

public class editarSeta extends AppCompatActivity {

    ImageView imgvw;
    EditText etNombre, etNombreComun, etDescripcion;
    Switch edibleSwitch;
    ObjetoSetas setaAModificar;

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

        imgvw.setImageBitmap(Utilidades.convertirBytesAImagen(setaAModificar.getImagen()));
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
                String[] params = { String.valueOf(setaAModificar.getId()) };
                
                ContentValues cvs = new ContentValues();
                cvs.put(Utilidades.NOMBRE_COLUMNA, etNombre.getText().toString());
                cvs.put(Utilidades.DESCRIPCION_COLUMNA, etDescripcion.getText().toString());
                cvs.put(Utilidades.NOMBRECOMUN_COLUMNA, etNombreComun.getText().toString());
                cvs.put(Utilidades.COMESTIBLE_COLUMNA, edibleSwitch.isChecked());
                Bitmap updImg = ((BitmapDrawable)imgvw.getDrawable()).getBitmap();
                cvs.put(Utilidades.IMG_COLUMNA, Utilidades.convertirImagenABytes(updImg));

                ContentResolver cr = getContentResolver();

                cr.update(Utilidades.CONTENT_URI, cvs, Utilidades.ID_COLUMNA + " = ?", params);
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
}
