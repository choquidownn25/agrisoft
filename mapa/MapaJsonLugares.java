package net.proyecto.jose_antonio_sarria.agrisoft.mapa;

/**
 * Created by choqu_000 on 26/02/2015.json
 * Clase vista del mapa para el servicio
 *
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
//
import net.proyecto.jose_antonio_sarria.agrisoft.R;
import net.proyecto.jose_antonio_sarria.agrisoft.jon.JsonAnalisisdelLugar;


public class MapaJsonLugares extends FragmentActivity implements LocationListener{

    //Atibutos
    GoogleMap mGoogleMap;
    Spinner mSprLugarType;

    String[] mPlaceType=null;
    String[] mPlaceTypeName=null;

    double mLatitude=0;
    double mLongitude=0;

    protected void onCreate(Bundle saveInstanceSaved) {
        super.onCreate(saveInstanceSaved);
        //vista del mapa
        setContentView(R.layout.mapalugaranalisisjson);


        // Array tipos de lugares
        mPlaceType = getResources().getStringArray(R.array.place_type);

        // Array tipos de nombres
        mPlaceTypeName = getResources().getStringArray(R.array.place_type_name);

        // Creación de un adaptador de matriz con una matriz de tipos Place
        // Para poblar el spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mPlaceTypeName);

        // referencia al Spinner
        mSprLugarType = (Spinner) findViewById(R.id.spr_place_type);

        // adaptador en Spinner para establecer tipos de lugar
        mSprLugarType.setAdapter(adapter);

        Button btnFind;

        // Casting al boton
        btnFind = (Button) findViewById(R.id.btn_find);


        // Obtiene los servicios de google
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());


        if (status != ConnectionResult.SUCCESS) { // si el servicio no esta viable

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else { // el serviio google esta viable

            // obtine la referencia del map
            SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            // Obtiene Google maps
            mGoogleMap = fragment.getMap();

            // Habilita la localizacion Google Map
            mGoogleMap.setMyLocationEnabled(true);


            // Obtiene un casting en el servicio de localizacion
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creación de un objeto de criterios para recuperar proveedor
            Criteria criteria = new Criteria();

            // Casting ´para el provedor
            String provider = locationManager.getBestProvider(criteria, true);

            // obtiene el recorrido del GPS
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                onLocationChanged(location);
            }
            //Registrar el detector con el Administrador de ubicaciones para recibir actualizaciones de ubicación
                locationManager.requestLocationUpdates(provider, 20000, 0, this);

            // Obtiene el click del botom
            btnFind.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    //Seleccione a poscion
                    int selectedPosition = mSprLugarType.getSelectedItemPosition();
                    String type = mPlaceType[selectedPosition];

                    //Servicio en la nube
                    StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                    sb.append("location=" + mLatitude + "," + mLongitude);
                    sb.append("&radius=5000");
                    sb.append("&types=" + type);
                    sb.append("&sensor=true");
                    sb.append("&key=YOUR_API_KEY");


                    // Creación de una nueva tarea subproceso no ui para descargar Google datos lugar json
                    PlacesTask placesTask = new PlacesTask();

                    // invoca el  "doInBackground()" -mrtodo de la clase PlaceTask
                    placesTask.execute(sb.toString());


                }
            });

        }
    }

    /** MEtodo de descarga json para a url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);


            // Creación de una conexión HTTP para comunicarse con url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Conectandola a una url
            urlConnection.connect();

            // Lectura de  url
            iStream = urlConnection.getInputStream();
            //instancia de clase para el buffer
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));//clase que covierte el binario en dato
            //calse que lo adiciona a la pila del buffer
            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
           // Log.d("Excepcion al descargar url", e.toString());
            Log.d("", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }

    /** Clase para la descarga del los lugares */
    private class PlacesTask extends AsyncTask<String, Integer, String>{

        String data = null;

        // Ejecucion completo del metodo doInBackground()
        @Override
        protected void onPostExecute(String result){
            ParserTask parserTask = new ParserTask();
            // inicia el analisis
            parserTask.execute(result);
        }

        // Invoca metodo
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

    }

    /** Una clase para analizar los sitios de Google en formato JSON */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        // objeto que invoca el dato json
        @Override
        protected List<HashMap<String,String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> lugares = null;
            JsonAnalisisdelLugar LugarJsonParser = new JsonAnalisisdelLugar();

            try{
                jObject = new JSONObject(jsonData[0]);

                /** obtiene dato en el constructor */
                lugares = LugarJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return lugares;
        }

        // el metodo se completa despues doInBackground()
        @Override
        protected void onPostExecute(List<HashMap<String,String>> list){

            // Borra los marcodes exitentes en el mapa
            mGoogleMap.clear();

            for(int i=0;i<list.size();i++){

                // Crea marcador
                MarkerOptions markerOptions = new MarkerOptions();

                // obtiene el lugar de la lista
                HashMap<String, String> hmLugar = list.get(i);

                // Obtiene lalitud
                double lat = Double.parseDouble(hmLugar.get("lat"));

                // longitud del lugar
                double lng = Double.parseDouble(hmLugar.get("lng"));

                // nombre
                String name = hmLugar.get("place_name");

                // vencidad
                String vicinity = hmLugar.get("vicinity");

                LatLng latLng = new LatLng(lat, lng);

                // posiscion del marcador
                markerOptions.position(latLng);

                // despliega titulo del marcador
                markerOptions.title(name + " : " + vicinity);

                // lugar donde esta el marcador
                mGoogleMap.addMarker(markerOptions);

            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //Interface
    @Override
    public void onLocationChanged(Location location) {

        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(mLatitude, mLongitude);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));//acercamiento a la camara

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
