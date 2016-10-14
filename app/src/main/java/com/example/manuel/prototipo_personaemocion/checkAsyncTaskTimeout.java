package com.example.manuel.prototipo_personaemocion;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by Manuel on 17/12/2015.
 */
public class checkAsyncTaskTimeout implements Runnable {
        private AsyncTask<String, Void, Void> mAT;
        private Context contexto;

        public checkAsyncTaskTimeout(AsyncTask<String, Void, Void> at, Context context) {
            mAT = at;
            contexto = context;
        }

        @Override
        public void run() {
            if (mAT.getStatus() == AsyncTask.Status.RUNNING || mAT.getStatus() == AsyncTask.Status.PENDING) {
                Toast.makeText(contexto, "Agotado el tiempo de carga", Toast.LENGTH_LONG).show();
                mAT.cancel(true);
            }
        }
}


