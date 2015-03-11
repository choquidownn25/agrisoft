package net.proyecto.jose_antonio_sarria.agrisoft.notificacionesparces;

/**
 * Created by choqu_000 on 08/03/2015.
 */

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseUser;

import android.app.Application;

public class ToDoListApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Inicializa reporte.
        ParseCrashReporting.enable(
                this);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Adiciona codigo
        //Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);

        Parse.initialize(this, "0YxdMTxubuoJiBCAwkCnenL5D5ZfX79magrxfBsS", "KmjuN3TFg0SwPTCFWkV82fpwoOee5I6p22HrkAeG");
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        //// Opcionalmente permitir el acceso de lectura pública.
        ParseACL.setDefaultACL(defaultACL, true);
    }
}