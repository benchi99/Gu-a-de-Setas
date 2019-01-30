package com.rubenbermejo.fml.listapp;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.MenuRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class contenidoInformacion extends AppCompatActivity {

    TextView tvNombre, tvComestibilidad, tvDescripcion, tvNombreComun;
    ImageView imageView;
    ObjetoSetas setaRecibida;
    SetasSQLiteHelper con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenido_informacion);

        tvNombre = findViewById(R.id.tvNombre);
        tvComestibilidad = findViewById(R.id.tvComestibilidad);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        tvNombreComun = findViewById(R.id.tvNombreComun);
        imageView = findViewById(R.id.imageView);

        con = new SetasSQLiteHelper(this, "Setas", null, Utilidades.VERSION);

        Bundle informacionRecibida = getIntent().getExtras();

        setaRecibida = (ObjetoSetas) informacionRecibida.getSerializable("seta");

        if (informacionRecibida != null){
            tvNombre.setText(setaRecibida.getNombre());
            tvDescripcion.setText(setaRecibida.getDescripcion());
            tvNombreComun.setText(setaRecibida.getnombreComun());
            if (setaRecibida.getComestible()) {
                tvComestibilidad.setText(R.string.edible);
                tvComestibilidad.setTextColor(Color.GREEN);
            } else {
                tvComestibilidad.setText(R.string.notEdible);
                tvComestibilidad.setTextColor(Color.RED);
            }
            imageView.setImageBitmap(Utilidades.convertirBytesAImagen(setaRecibida.getImagen()));
        }

        customizaActionBar(setaRecibida.getNombre());

    }

    @Override
    public void onBackPressed() {
        Intent intentDevolver = new Intent();
        Bundle bundleDevuelve = new Bundle();
        bundleDevuelve.putSerializable("devolverSeta", setaRecibida);
        intentDevolver.putExtras(bundleDevuelve);
        setResult(Activity.RESULT_OK, intentDevolver);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contenidoinformacion_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.togglerFav:
                SQLiteDatabase db = con.getWritableDatabase();
                String[] param = { String.valueOf(setaRecibida.getId()) };
                ContentValues cv = new ContentValues();
                if (!setaRecibida.getFavorito()) {
                    setaRecibida.setFavorito(true);
                    cv.put(Utilidades.FAV_COLUMNA, true);
                    Toast.makeText(this, "Añadido a favoritos.", Toast.LENGTH_SHORT).show();
                } else if (setaRecibida.getFavorito()){
                    setaRecibida.setFavorito(false);
                    cv.put(Utilidades.FAV_COLUMNA, false);
                    Toast.makeText(this, "Eliminado de favoritos.", Toast.LENGTH_SHORT).show();
                }

                try {
                    db.update(Utilidades.NOMBRE_TABLA, cv, Utilidades.ID_COLUMNA + " = ?", param);
                } catch (Exception e) {
                    Toast.makeText(this, "No se ha podido actualizar la tabla...", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT ,setaRecibida.toString());
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
                break;
            case R.id.verWeb:
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(setaRecibida.getURLlinea()));
                if (webIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(webIntent);
                }
                break;
            case R.id.enviarCorreo:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, setaRecibida.getNombre());
                emailIntent.putExtra(Intent.EXTRA_TEXT, setaRecibida.getDescripcion());
                if(emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(emailIntent);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Establece el título de la seta escogida en el Action Bar.
     *
     * @param tituloActividad
     */

    private void customizaActionBar(String tituloActividad) {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle(tituloActividad);
    }
}
