package com.rubenbermejo.fml.listapp;

import android.app.Activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    RecyclerView lista;
    AdapterData adaptador;
    SetasSQLiteHelper con;
    SwipeRefreshLayout actualiza;
    boolean mostrarFavoritos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        con = new SetasSQLiteHelper(this, "Setas", null, Utilidades.VERSION);

        lista = findViewById(R.id.lista);
        actualiza = findViewById(R.id.actualiza);
        lista.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new AdapterData(Utilidades.obtenerListaMasReciente(con, con.NORMAL));

        actualiza.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mostrarFavoritos) {
                    adaptador.setListSetas(Utilidades.obtenerListaMasReciente(con, con.FAVORITOS));
                } else {
                    adaptador.setListSetas(Utilidades.obtenerListaMasReciente(con, con.NORMAL));
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
                    ArrayList<ObjetoSetas> nuevo = Utilidades.obtenerListaMasReciente(con, con.FAVORITOS);
                    adaptador.setListSetas(nuevo);
                    adaptador.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, R.string.hidingFavs, Toast.LENGTH_SHORT).show();
                    adaptador.setListSetas(Utilidades.obtenerListaMasReciente(con, con.NORMAL));
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
                    adaptador.setListSetas(Utilidades.obtenerListaMasReciente(con, con.NORMAL));
                    adaptador.notifyDataSetChanged();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(this, "Añadir seta cancelado.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
