package com.rubenbermejo.fml.listapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AdapterData extends RecyclerView.Adapter<AdapterData.ViewHolderDatos> implements View.OnClickListener{

    ArrayList<ObjetoSetas> listSetas;
    private View.OnClickListener listener;
    Bitmap bmp = null;

    public AdapterData(ArrayList<ObjetoSetas> listDatos) {
        this.listSetas = listDatos;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lista, null, false);
        view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos viewHolderDatos, int i) {
        viewHolderDatos.nombre.setText(listSetas.get(i).getNombre());
        viewHolderDatos.informacion.setText(listSetas.get(i).getnombreComun());
        listSetas.get(i).setImg(bajarImagen(listSetas.get(i).getImagen()));
        viewHolderDatos.imagen.setImageBitmap(listSetas.get(i).getImg());
        if (listSetas.get(i).getComestible()) {
            viewHolderDatos.comestibilidad.setText(R.string.edible);
            viewHolderDatos.comestibilidad.setTextColor(Color.GREEN);
        } else {
            viewHolderDatos.comestibilidad.setText(R.string.notEdible);
            viewHolderDatos.comestibilidad.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return listSetas.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public void setListSetas (ArrayList<ObjetoSetas> listSetas) {
        this.listSetas = listSetas;
    }

    public ArrayList<ObjetoSetas> getListSetas() {
        return this.listSetas;
    }

    private Bitmap bajarImagen(String url) {
        new DescargarImagen().execute(Utilidades.DIRECCION_REST_MARISMA + Utilidades.IMG_LOCATION + url);
        return bmp;
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView nombre, informacion, comestibilidad;
        ImageView imagen;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.idNombre);
            informacion = itemView.findViewById(R.id.idInfo);
            imagen = itemView.findViewById(R.id.idImagen);
            comestibilidad = itemView.findViewById(R.id.idComest);
        }

    }

    private class DescargarImagen extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            String imagen = strings[0];
            Bitmap imgAMostrar = null;
            try {
                InputStream ins = new URL(imagen).openStream();
                imgAMostrar = BitmapFactory.decodeStream(ins);
            } catch (MalformedURLException badURLe) {
                Log.e("REST API", "La URL que ha sido dada está mal formada.");
                badURLe.printStackTrace();
            } catch (IOException ioe) {
                Log.e("REST API", "Hubo un error al descargar " + imagen);
                ioe.printStackTrace();
            }
            return imgAMostrar;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            bmp = bitmap;
        }
    }

}
