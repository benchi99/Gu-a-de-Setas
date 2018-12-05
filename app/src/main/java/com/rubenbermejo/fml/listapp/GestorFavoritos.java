package com.rubenbermejo.fml.listapp;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

public class GestorFavoritos {

    //Aquí almaceno todas las setas marcadas como favoritos.
    static ArrayList<ObjetoSetas> listaFavoritos = new ArrayList<>();

    /**
     *
     * Este método se encarga de añadir a favoritos la seta escogida.
     *
     * @param seta
     * @param context
     */

    public static void anadirAFavoritos(ObjetoSetas seta, Context context) {

        boolean anadido = false;

        for (int i = 0; i < listaFavoritos.size(); i++ ) {
            if (listaFavoritos.get(i).getNombre() == seta.getNombre()){
                anadido = true;
                break;
            }
        }

        if (!anadido) {
            seta.setFavorito(true);
            listaFavoritos.add(seta);
            Toast.makeText(context, R.string.addFav, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.errorAddFav, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     * Este método se encarga de eliminar una seta especificada de favoritos.
     *
     * @param seta
     * @param context
     */

    public static void eliminarDeFavoritos(ObjetoSetas seta, Context context) {

        boolean encontrado = false;

        for (int i = 0; i < listaFavoritos.size(); i++ ) {
            if (listaFavoritos.get(i).getNombre() == seta.getNombre()){
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            seta.setFavorito(false);
            listaFavoritos.remove(seta);
            Toast.makeText(context, R.string.removeFav, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.errorRemoveFav, Toast.LENGTH_SHORT).show();
        }

    }

}