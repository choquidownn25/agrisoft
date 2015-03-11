package net.proyecto.jose_antonio_sarria.agrisoft.lista;

/**
 * Created by choqu_000 on 26/02/2015.
 * Clase para la propiedad de la listas
 */

public class Lista_entrada {

    private int idImagen;//imagen
    private String textoEncima; //Texto encabeadp
    private String textoDebajo;//documentacion

    //Poliformismo del constructor
    public Lista_entrada (int idImagen, String textoEncima, String textoDebajo) {
        this.idImagen = idImagen;
        this.textoEncima = textoEncima;
        this.textoDebajo = textoDebajo;
    }

    //Encapsulamiento y/o Propiedad
    public String get_textoEncima() {
        return textoEncima;
    }

    public String get_textoDebajo() {
        return textoDebajo;
    }

    public int get_idImagen() {
        return idImagen;
    }
}

