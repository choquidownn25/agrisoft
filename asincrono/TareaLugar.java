package net.proyecto.jose_antonio_sarria.agrisoft.asincrono;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import net.proyecto.jose_antonio_sarria.agrisoft.serviciogoogle.DetalleLugarActivity;

/**
 * Created by choqu_000 on 06/03/2015.
 * Clase que realiza la descarga del mapa google maps
 *
 *
 */
public class TareaLugar   extends AsyncTask<String, Integer, String> {

    //Atributos
    String data = null;
    WebView DetalleLugarmWv;
    //invacamoe el metodo ejecuta en este proceso doInBackground
    protected String doInBackground(String... url){

        try {

            data = downloadUrl(url[0]);
        }catch (Exception e){

            Log.d("Background Task", e.toString());
        }


        return data;
    }

    /* Metodo que descarga el Json*/
    private String downloadUrl(String strUrl) throws IOException{

        //Atributos
        String data = "";

        InputStream iStream = null;
        HttpURLConnection urlConnection = null;

        try{
            URL url = new URL(strUrl);


            // Creación de una conexión HTTP para comunicarse con url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Conecta la URL
            urlConnection.connect();

            // Lee la direccion url
            iStream = urlConnection.getInputStream();
            //Clase que permire leer los caracteres de un texto
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            //Instancia de clase que retorna  la cadena de caracteres invertida.
            StringBuffer sb  = new StringBuffer();

            String line = "";
            //repita hasta que la linea de cada caracter se diferente a null
            while( ( line = br.readLine())  != null){
                sb.append(line); //almacena
            }
            //guarda cadena
            data = sb.toString();
            br.close(); //cierra

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close(); //realiza cierre
            urlConnection.disconnect(); //Desconecta
        }

        return data;

    }

    //completa la ejecucion del metodo doInBackground()
    @Override
    protected void onPostExecute(String result){
        AnalizaTarea parserTask = new AnalizaTarea();
        // Comienza analizando los detalles del lugar de Google en formato JSON
        // invoca al método de la clase Analizar tareas "doInBackground ()"
        parserTask.execute(result);
    }



}
