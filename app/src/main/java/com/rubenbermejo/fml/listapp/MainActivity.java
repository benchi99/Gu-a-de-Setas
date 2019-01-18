package com.rubenbermejo.fml.listapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import static com.rubenbermejo.fml.listapp.CamaraDatos.*;
import static com.rubenbermejo.fml.listapp.CamaraDatos.inicializarDatos;

public class MainActivity extends AppCompatActivity {

    static ArrayList<Bitmap> bmps;

    RecyclerView lista;
    AdapterData adaptador;
    boolean mostrarFavoritos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista = findViewById(R.id.lista);
        lista.setLayoutManager(new LinearLayoutManager(this));
        CamaraDatos.inicializarDatos();
        adaptador = new AdapterData(CamaraDatos.listDatos);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, contenidoInformacion.class);
                ObjetoSetas seta = adaptador.getListSetas().get(lista.getChildAdapterPosition(v));

                Bundle informacion = new Bundle();
                informacion.putSerializable("seta", seta);
                intent.putExtras(informacion);

                startActivityForResult(intent, 1);
            }
        });

        SetasSQLiteHelper con = new SetasSQLiteHelper(this, "Setas", null, 1);

        lista.setAdapter(adaptador);
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
                    ArrayList<ObjetoSetas> nuevo = new ArrayList<>();
                    adaptador.setListSetas(nuevo);
                    nuevo.addAll(GestorFavoritos.listaFavoritos);
                    adaptador.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, R.string.hidingFavs, Toast.LENGTH_SHORT).show();
                    adaptador.setListSetas(CamaraDatos.listDatos);
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

            /*
             * NO ACTUALIZA BIEN EL OBJETO EN EL ARRAYLIST PRINCIPAL.
             */

            case 1:     //Devuelve objeto de Seta para ver si se ha añadido a favoritos o no.
                if (resultCode == Activity.RESULT_OK) {
                    ObjetoSetas setaActualizada = (ObjetoSetas) data.getSerializableExtra("devolverSeta");
                    for (int i = 0; i < CamaraDatos.listDatos.size(); i++) {
                        if (CamaraDatos.listDatos.get(i).getNombre() == setaActualizada.getNombre()){
                            CamaraDatos.listDatos.remove(i);
                            CamaraDatos.listDatos.add(i, setaActualizada);
                            adaptador.notifyItemInserted(i);
                        }
                    }
                }
                break;
            case 2:     //Devuelve objeto de Seta para añadirlo a la lista.
                if (resultCode == Activity.RESULT_OK) {

                    ObjetoSetas nuevaSeta = (ObjetoSetas) data.getSerializableExtra("nuevaSeta");
                    for (int i = 0; i < CamaraDatos.listDatos.size(); i++) {
                        if (CamaraDatos.listDatos.get(i).getNombre() == nuevaSeta.getNombre()){
                            CamaraDatos.listDatos.remove(i);
                        }
                    }
                    CamaraDatos.listDatos.add(nuevaSeta);
                    adaptador.notifyDataSetChanged();

                    Toast.makeText(this, "Seta añadida", Toast.LENGTH_SHORT).show();

                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(this, "Añadir seta cancelado.", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void addBmpsToList(){
        for (int i = 0; i < CamaraDatos.listDatos.size(); i++){
            bmps.add(BitmapFactory.decodeResource(this.getApplicationContext().getResources(), i));
        }
    }

    public static Bitmap getBmpFromList(int i){
        return bmps.get(i);
    }


}
