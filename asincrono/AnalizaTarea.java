package net.proyecto.jose_antonio_sarria.agrisoft.asincrono;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;

import net.proyecto.jose_antonio_sarria.agrisoft.jon.JsonDetallesAnalisisLugar;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by choqu_000 on 06/03/2015.
 * Clase que analiza los formatos json de la clase de talle
 */
public class AnalizaTarea extends AsyncTask<String, Integer, HashMap<String,String>>{

    //Atributos
    JSONObject jObject;
    WebView DetalleLugarmWv;
    //Invoca este metodo para este objeto
    @Override
    protected HashMap<String, String> doInBackground(String... jsonData) {

        HashMap<String, String> hLugarDetalles = null;
        JsonDetallesAnalisisLugar placeDetailsJsonParser = new JsonDetallesAnalisisLugar();

        try{
            jObject = new JSONObject(jsonData[0]);

            // Anializamos el Json que llega de Google
            hLugarDetalles = placeDetailsJsonParser.parse(jObject);

        }catch(Exception e){
            Log.d("Exception", e.toString());
        }
        return hLugarDetalles;

    }

    //Ejecuta el metodo
    protected void onPostExecute(HashMap<String,String> hDetallesLugares){


        String name = hDetallesLugares.get("name");
        String icon = hDetallesLugares.get("icon");
        String vicinity = hDetallesLugares.get("vicinity");
        String lat = hDetallesLugares.get("lat");
        String lng = hDetallesLugares.get("lng");
        String formatted_address = hDetallesLugares.get("formatted_address");
        String formatted_phone = hDetallesLugares.get("formatted_phone");
        String website = hDetallesLugares.get("website");
        String rating = hDetallesLugares.get("rating");
        String international_phone_number = hDetallesLugares.get("international_phone_number");
        String url = hDetallesLugares.get("url");


        String mimeType = "text/html";
        String encoding = "utf-8";

        String data = 	"<html>"+
                "<body><img style='float:left' src="+icon+" /><h1><center>"+name+"</center></h1>" +
                "<br style='clear:both' />" +
                "<hr  />"+
                "<p>Vicinity : " + vicinity + "</p>" +
                "<p>Location : " + lat + "," + lng + "</p>" +
                "<p>Address : " + formatted_address + "</p>" +
                "<p>Phone : " + formatted_phone + "</p>" +
                "<p>Website : " + website + "</p>" +
                "<p>Rating : " + rating + "</p>" +
                "<p>International Phone  : " + international_phone_number + "</p>" +
                "<p>URL  : <a href='" + url + "'>" + url + "</p>" +
                "</body></html>";

        // Setting the data in WebView

        DetalleLugarmWv.loadDataWithBaseURL("", data, mimeType, encoding, "");
    }
}
