package com.rubenbermejo.fml.listapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class Ralentizador extends AsyncTask<Void, Integer, Boolean> {

    private ProgressDialog prog;
    private Context contexto;

    public Ralentizador(Context contexto) {
        this.contexto = contexto;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        for (int i = 0; i < 3; i++) {
            tareaLarga();
        }

        return true;
    }

    @Override
    protected void onPreExecute() {
        prog = new ProgressDialog(contexto);
        prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prog.setMessage("Consultando base de datos...");
        prog.setMax(100);
        prog.show();

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        prog.dismiss();
    }

    private void tareaLarga() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException iee){
            iee.printStackTrace();
        }
    }

}
