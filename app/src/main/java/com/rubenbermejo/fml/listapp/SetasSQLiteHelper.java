package com.rubenbermejo.fml.listapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SetasSQLiteHelper extends SQLiteOpenHelper {

    /*
    TABLA SETAS:
    COLUMNAS:
    0. ID - INTEGER <- PRIMARY KEY AUTOINCREMENTADO
    1. NOMBRE - TEXT
    2. DESCRIPCION - TEXT
    3. NOMBE COMÚN - TEXT
    4. URL - TEXT
    5. COMESTIBLE - BOOLEAN
    6. FAVORITO - BOOLEAN
    7. IMAGEN - BLOB <- NULL
     */

    final String NORMAL = "normal";
    final String FAVORITOS = "favorito";

    Context contexto;

    String sql = "CREATE TABLE " + Utilidades.NOMBRE_TABLA + "( " + Utilidades.ID_COLUMNA +"  INTEGER PRIMARY KEY AUTOINCREMENT, " + Utilidades.NOMBRE_COLUMNA + " TEXT, " + Utilidades.DESCRIPCION_COLUMNA + " TEXT, " + Utilidades.NOMBRECOMUN_COLUMNA + " TEXT, " + Utilidades.URLLINEA_COLUMNA + " TEXT, " + Utilidades.COMESTIBLE_COLUMNA + " BOOLEAN, " + Utilidades.FAV_COLUMNA + " BOOLEAN, " + Utilidades.IMG_COLUMNA + " BLOB)";

    public SetasSQLiteHelper(Context contexto, String nombre, SQLiteDatabase.CursorFactory f, int version){
        super(contexto, nombre, f, version);
        this.contexto = contexto;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se crea la tabla en la primera instalación.
        db.execSQL(sql);
        Utilidades ut = new Utilidades(contexto);
        ut.rellenaBaseDeDatos(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Este método se ejecuta cada vez que la aplicación se actualice.
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.NOMBRE_TABLA);

        onCreate(db);
    }
}
