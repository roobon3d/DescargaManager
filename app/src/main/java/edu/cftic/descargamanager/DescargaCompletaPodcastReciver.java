package edu.cftic.descargamanager;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;





/*
hay que añadir esto al manifest.xml
 <receiver
            android:name=".DescargaCompletaPodcastReciver"
            android:enabled="true"
            android:exported="true" />


 */


public class DescargaCompletaPodcastReciver extends BroadcastReceiver {private long id_descarga;
    private Context context;




    public DescargaCompletaPodcastReciver(Context context) {
        this.context = context;
    }



    public void setId_descarga(long id_descarga) {
        this.id_descarga = id_descarga;
    }



    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d (getClass().getCanonicalName(), "Entrando en Onrecieve ");

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Query query = new Query();
        query.setFilterById(this.id_descarga);
        Cursor cursor = downloadManager.query(query);
        cursor.moveToFirst();
        int ref = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(ref);

        DescargaActivity da = (DescargaActivity)this.context;
        switch (status) {
            case DownloadManager.STATUS_SUCCESSFUL:

                da.actualizarVentanaTrasDescarga(true);


                break;

            case DownloadManager.STATUS_FAILED: //algo ha fallado

                da.actualizarVentanaTrasDescarga(false);

                Log.d(this.getClass().getCanonicalName(), "Ha habido un problema durante la descarga del archivo. Archivo no descargado");

                break;
        }

        /**
         * IMPORTANTÍSIMO DEREGISTRAR ESTA CLASE CON  context.unregisterReceiver(this);, DESASOCIAÁNDOLA DE LA ACTIVIDAD. SI NO, EN LA SIGUIENTE DESCARGA COMPLETA
         * ESTA CLASE SERÁ INVOCADA 2 VECES, PUDIENDO PRODUCIR INCOSISTENCIAS
         */

        context.unregisterReceiver(this);


    }

    public DescargaCompletaPodcastReciver() {
    }
}
