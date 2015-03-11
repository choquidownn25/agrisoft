package net.proyecto.jose_antonio_sarria.agrisoft.jon;

/**
 * Created by choqu_000 on 26/02/2015.
 * Clase que trae el json
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonAnalisisdelLugar {

    /** Recibe el objeto Json y lo retorna a la lista */
    public List<HashMap<String,String>> parse(JSONObject jObject){

        JSONArray jLuares = null;
        try {
            /** Recibe todos los elementos lugares del array */
            jLuares = jObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoca objeto del lugar
         * Donde CAda objeto invoca lugar
         */
        return getLugares(jLuares);
    }

    /** Objeto lista que trae los parametros */

    private List<HashMap<String, String>> getLugares(JSONArray jLugares){
        int placesCount = jLugares.length();
        List<HashMap<String, String>> LugaresList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> place = null;

        /** Tomando cada lugar, analiza y añade a la lista de objetos */
        for(int i=0; i<placesCount;i++){
            try {
                /** Objeto Json Analiza lugar */
                place = getLugar((JSONObject)jLugares.get(i));
                LugaresList.add(place);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return LugaresList;
    }

    /** Analizando el Objeto Json */
    private HashMap<String, String> getLugar(JSONObject jLugar){

        HashMap<String, String> lugar = new HashMap<String, String>();
        String LugarName = "-NA-";
        String vecindad="-NA-";
        String latitude="";
        String longitude="";


        try {
            // Estrae wl nombre del lugar
            if(!jLugar.isNull("name")){
                LugarName = jLugar.getString("name");
            }

            // Extrae el la Vencidad
            if(!jLugar.isNull("vicinity")){
                vecindad = jLugar.getString("vicinity");
            }

            latitude = jLugar.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = jLugar.getJSONObject("geometry").getJSONObject("location").getString("lng");


            lugar.put("place_name", LugarName);
            lugar.put("vicinity", vecindad);
            lugar.put("lat", latitude);
            lugar.put("lng", longitude);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lugar;
    }



}
