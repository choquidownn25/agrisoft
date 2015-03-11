package net.proyecto.jose_antonio_sarria.agrisoft.mapa;

/**
 * Created by choqu_000 on 06/03/2015.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.proyecto.jose_antonio_sarria.agrisoft.R;
import net.proyecto.jose_antonio_sarria.agrisoft.jon.JsonAnalisisdelLugar;
import net.proyecto.jose_antonio_sarria.agrisoft.serviciogoogle.DetalleLugarActivity;
import net.proyecto.jose_antonio_sarria.agrisoft.asincrono.TareaLugar;
import net.proyecto.jose_antonio_sarria.agrisoft.asincrono.AnalizaTarea;
import net.proyecto.jose_antonio_sarria.agrisoft.jon.JsonDetallesAnalisisLugar;



public class MapaDetalleLugarAnalisisJson extends FragmentActivity implements LocationListener{

    GoogleMap mGoogleMap;
    Spinner mSprPlaceType;

    String[] mPlaceType=null;
    String[] mPlaceTypeName=null;

    double mLatitude=0;
    double mLongitude=0;

    HashMap<String, String> mMarkerPlaceLink = new HashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.mpadetallelugaranalizisjson);

        // Array para lugares
        mPlaceType = getResources().getStringArray(R.array.place_type);

        // Array lugares con nombres
        mPlaceTypeName = getResources().getStringArray(R.array.place_type_name);

        // Creación de un adaptador de matriz con una matriz de tipos Place para poblar el spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mPlaceTypeName);

        // Conseguir referencia al Spinner
        mSprPlaceType = (Spinner) findViewById(R.id.spr_place_type);

        // Configuración de adaptador en Spinner para establecer tipos de lugar
        mSprPlaceType.setAdapter(adapter);

        Button btnFind;

        // Conseguir referencia al botón Buscar
        btnFind = ( Button ) findViewById(R.id.btn_find);


        // Google Play estado de disponibilidad
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());


        if(status!=ConnectionResult.SUCCESS){ // Google Play Servicio no disponible
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }else { // Google Play servicio disponible

            //  Conseguir referencia al SupportMapFragment
            SupportMapFragment fragment = ( SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            // GObtengo el mapa
            mGoogleMap = fragment.getMap();

            // Encuentra localizacion
            mGoogleMap.setMyLocationEnabled(true);



            //  Sistema de Servicio LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creación de un objeto de criterios para recuperar proveedor
            Criteria criteria = new Criteria();

            // Obtener el nombre de los mejores proveedores
            String provider = locationManager.getBestProvider(criteria, true);

            // Ubicación actual En GPS
            Location location = locationManager.getLastKnownLocation(provider);

            if(location!=null){
                onLocationChanged(location);
            }

            locationManager.requestLocationUpdates(provider, 20000, 0, this);

            mGoogleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker arg0) {
                    Intent intent = new Intent(getBaseContext(), DetalleLugarActivity.class);
                    String reference = mMarkerPlaceLink.get(arg0.getId());
                    intent.putExtra("reference", reference);

                    // Inicio de la Actividad
                    startActivity(intent);
                }
            });



            //Ajuste clic lister evento para el botón Buscar
            btnFind.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {


                    int selectedPosition = mSprPlaceType.getSelectedItemPosition();
                    String type = mPlaceType[selectedPosition];


                    StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                    //StringBuilder sb = new StringBuilder("http://maps.google.com/maps/api/geocode/json?components=route:Annegatan|administrative_area:Helsinki|country:Finland&sensor=false");
                    sb.append("location="+mLatitude+","+mLongitude);
                    sb.append("&radius=5000");
                    sb.append("&types="+type);
                    sb.append("&sensor=true");
                    sb.append("&key=AIzaSyDD5eCwdsn-o-9ERcs2EfugiLVHep_gvY0");


                    // Creación de una nueva tarea subproceso no ui para descargar Google datos lugar json
                    PlacesTask placesTask = new PlacesTask();

                    // Invoca la clase para ejecutar el metodo "doInBackground()"
                    placesTask.execute(sb.toString());


                }
            });

        }

    }

    /** Un método para descargar datos JSON de url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);


            // Creación de una conexión HTTP para comunicarse con url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Conexión a url
            urlConnection.connect();

            // Lectura de datos de url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }


    /** Una clase, para descargar Google */
    private class PlacesTask extends AsyncTask<String, Integer, String>{

        String data = null;

        // Invoca metodo para ejecutar
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Ejecutado después de la ejecución completa de doInBackground ()
        @Override
        protected void onPostExecute(String result){
            ParserTask parserTask = new ParserTask();

            // Inicia analizar los lugares de Google en formato JSON invoca al método de la clase ParseTask "doInBackground ()"
            parserTask.execute(result);
        }

    }

    /** Una clase para analizar los sitios de Google en formato JSON */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        // Se invoca por ejecutar
        @Override
        protected List<HashMap<String,String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            JsonAnalisisdelLugar placeJsonParser = new JsonAnalisisdelLugar();

            try{
                jObject = new JSONObject(jsonData[0]);

                /** Obtener los datos analizados como un constructo Lista */
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        // Ejecutado antes de la ejecución completa de doInBackground()
        @Override
        protected void onPostExecute(List<HashMap<String,String>> list){

            // Borra todos los marcadores existentes
            mGoogleMap.clear();

            for(int i=0;i<list.size();i++){

                // Creación de un marcador
                MarkerOptions markerOptions = new MarkerOptions();

                // Conseguir un lugar en la lista de lugares
                HashMap<String, String> hmPlace = list.get(i);

                // Conseguir latitud del lugar
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Conseguir longitud del lugar
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Obtener nombre
                String name = hmPlace.get("place_name");

                // Obtener vecindad
                String vicinity = hmPlace.get("vicinity");

                LatLng latLng = new LatLng(lat, lng);

                // Ajuste de la posición del marcador
                markerOptions.position(latLng);

                // Ajuste del título para el marcador. Esto se mostrará en grabar el marcador
                markerOptions.title(name + " : " + vicinity);

                // Colocar un marcador en la posición tocada
                Marker m = mGoogleMap.addMarker(markerOptions);

                // Identificación Vinculación Marker y el lugar de referencia
                mMarkerPlaceLink.put(m.getId(), hmPlace.get("reference"));


            }

        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(mLatitude, mLongitude);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
}
