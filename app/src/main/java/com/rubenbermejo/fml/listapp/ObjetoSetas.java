package com.rubenbermejo.fml.listapp;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Random;

public class ObjetoSetas implements Serializable {

    private String nombre, descripcion, nombreComun, URLlinea;
    private boolean Comestible, favorito;
    private int id;
    private byte[] imagen;

    public ObjetoSetas(String nombre, String descripcion, String nombreComun, String URLlinea, boolean Comestible, byte[] imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nombreComun = nombreComun;
        this.URLlinea = URLlinea;
        this.Comestible = Comestible;
        this.imagen = imagen;
        this.favorito = false;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getnombreComun() {
        return nombreComun;
    }

    public void setnombreComun(String nombreComun) {
        this.nombreComun = nombreComun;
    }

    public String getURLlinea() {
        return URLlinea;
    }

    public void setURLlinea(String URLlinea) {
        this.URLlinea = URLlinea;
    }

    public boolean getComestible() {
        return Comestible;
    }

    public void setComestible(boolean Comestible) {
        this.Comestible = Comestible;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public boolean getFavorito() { return favorito; }

    public void setFavorito(boolean favorito) { this.favorito = favorito; }

    private String getToStringComestible(){

        if (getComestible()) {
            return "comestible";
        } else {
            return "no comestible";
        }

    }

    @Override
    public String toString() {
        return "¿Sabías que la " + this.nombre + ", comunmente conocida como " + this.nombreComun + " es " + getToStringComestible() + "? ¡Averigua más con mi aplicación de guía de Setas!";
    }
}
