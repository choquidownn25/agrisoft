package net.proyecto.jose_antonio_sarria.agrisoft.serviciogoogle;

import android.app.Activity;

/**
 * Created by choqu_000 on 06/03/2015.
 * Clase para el servicio Google
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import net.proyecto.jose_antonio_sarria.agrisoft.R;
import net.proyecto.jose_antonio_sarria.agrisoft.asincrono.TareaLugar;
import net.proyecto.jose_antonio_sarria.agrisoft.serviciogoogle.DetalleLugarActivity;
public class DetalleLugarActivity extends Activity{
    //Atributos
    WebView DetalleLugarmWv;

    //creamos la actividad
    protected void onCreate(Bundle savedInstancestate){
        //inicializa nuestra antividad
        super.onCreate(savedInstancestate);
        //la publicamos
        setContentView(R.layout.actividad_detalles_lugar);
        // referencia a  WebView a nuestra actividad
        //Encuentra una vista que fue identificado por el atributo id del XML que se procesó en onCreate (Bundle).
        DetalleLugarmWv = (WebView) findViewById(R.id.wv_place_details); //casting con WebView
        //usa la ventana del navegador como falsa
        DetalleLugarmWv.getSettings().setUseWideViewPort(false);

        // referencia del mapa
        String reference = getIntent().getStringExtra("reference");


        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        sb.append("reference="+reference);
        sb.append("&sensor=true");
        sb.append("&key=YOUR_BROWSER_API_KEY_FOR_PLACES");


        // Intancia de clase para el sub proceso de descarga
        TareaLugar lugardetalle = new TareaLugar();

        // invoca "doInBackground()" esta clase la ejecuta
        lugardetalle.execute(sb.toString());

    }


}
