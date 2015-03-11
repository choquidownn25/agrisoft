package net.proyecto.jose_antonio_sarria.agrisoft.jon;

/**
 * Created by choqu_000 on 04/03/2015.
 * Clase para json del detalle del lugar
 *
 */

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonDetallesAnalisisLugar {

    /** Recibe el objeto Json y lo retorna a la lista */
    public HashMap<String,String> parse(JSONObject jObject){

        JSONObject jLuaresDetalles = null;
        try {
            /** Recibe todos los elementos lugares del array */
            jLuaresDetalles = jObject.getJSONObject("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoca objeto del lugar
         * Donde CAda objeto invoca lugar
         */
        return getLuaresDetalles(jLuaresDetalles);
    }

    /** Analizando el Objeto Json */
    private HashMap<String,String> getLuaresDetalles(JSONObject jLuaresDetalles) {

        //trae atributo de una tabla
        HashMap<String, String> hDetalleLugar = new HashMap<String, String>();
        //Atributos del servicio
        String NombreDetalleLugar = "-NA-";
        String Icono = "-NA-";
        String Vecindad ="-NA-";
        String latitude="";
        String longitude="";
        String formato_direccion="-NA-";
        String formato_telefono="-NA-";
        String website="-NA-";
        String clisificacion="-NA-";
        String numero_internacionl_telefno ="-NA-";
        String url="-NA-";

        try {

            // Estrae wl nombre del lugar
            if(!jLuaresDetalles.isNull("name")){
                NombreDetalleLugar = jLuaresDetalles.getString("name");
            }
            //Extrae icono del lugar
            if (!jLuaresDetalles.isNull("icon")){
                Icono = jLuaresDetalles.getString("icon");
            }

            // Extrae el la Vencidad
            if(!jLuaresDetalles.isNull("vicinity")){
                Vecindad = jLuaresDetalles.getString("vicinity");
            }

            //Extrae formato de direccion
            if(!jLuaresDetalles.isNull("formatted_address")){
                formato_direccion = jLuaresDetalles.getString("formatted_address");
            }

            // Extrae el formato de direccion
            if(!jLuaresDetalles.isNull("formatted_phone_number")){
                formato_telefono = jLuaresDetalles.getString("formatted_phone_number");
            }

            // Extrae website
            if(!jLuaresDetalles.isNull("website")){
                website = jLuaresDetalles.getString("website");
            }
            // Extrae numero internacional
            if(!jLuaresDetalles.isNull("international_phone_number")){
                numero_internacionl_telefno = jLuaresDetalles.getString("international_phone_number");
            }


            // Extrae clsificacion
            if(!jLuaresDetalles.isNull("rating")){
                clisificacion = jLuaresDetalles.getString("rating");
            }

            // Extrae url
            if(!jLuaresDetalles.isNull("url")){
                url = jLuaresDetalles.getString("url");
            }


                                        //Obtener el valor JSONObject asociado con una llave.
            latitude = jLuaresDetalles.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = jLuaresDetalles.getJSONObject("geometry").getJSONObject("location").getString("lng");

            //insertamos un valor en la clave
            hDetalleLugar.put("name", NombreDetalleLugar);
            hDetalleLugar.put("icon", Icono);
            hDetalleLugar.put("vicinity", Vecindad);
            hDetalleLugar.put("lat", latitude);
            hDetalleLugar.put("lng", longitude);
            hDetalleLugar.put("formatted_address", formato_direccion);
            hDetalleLugar.put("formatted_phone", formato_telefono);
            hDetalleLugar.put("website", website);
            hDetalleLugar.put("rating", clisificacion);
            hDetalleLugar.put("international_phone_number", numero_internacionl_telefno);
            hDetalleLugar.put("url", url);


        }catch (Exception e){
            //saca execion
            e.printStackTrace();
        }

        return hDetalleLugar;
    }

}
