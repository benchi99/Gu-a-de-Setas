package com.rubenbermejo.fml.listapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SetasSQLiteHelper extends SQLiteOpenHelper {

    String sql = "CREATE TABLE SETAS(nombre TEXT, descripcion TEXT, nombreComun TEXT, URLlinea TEXT, comestible BOOLEAN)";

    public SetasSQLiteHelper(Context contexto, String nombre, SQLiteDatabase.CursorFactory f, int version){
        super(contexto, nombre, f, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se crea la tabla en la primera instalación.
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Este método se ejecuta cada vez que la aplicación se actualice.

        db.execSQL("DROP TABLE IF EXISTS SETAS");

        db.execSQL(sql);
    }
}
