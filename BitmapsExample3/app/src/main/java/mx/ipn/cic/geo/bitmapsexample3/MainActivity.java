package mx.ipn.cic.geo.bitmapsexample3;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button buttonDrawBitmap;
    ImageView imageViewBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.buttonDrawBitmap = findViewById(R.id.buttonDrawBitmap);
        this.imageViewBitmap = this.findViewById(R.id.imageViewBitmap);
        this.buttonDrawBitmap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*
                BitmapWorkerTask task = new BitmapWorkerTask(imageViewBitmap);
                task.execute(R.drawable.forest_restoration_map);
                */
                if (cancelPotentialWork(R.drawable.forest_restoration_map, imageViewBitmap)) {
                    Log.i("onOptiomsItemSelected", "Creando objeto tarea asíncrona");
                    final BitmapWorkerTask task = new BitmapWorkerTask(imageViewBitmap,
                            getResources(), imageViewBitmap.getWidth(), imageViewBitmap.getHeight());
                    final AsyncDrawable asyncDrawable =  new AsyncDrawable(getResources(), null, task);
                    imageViewBitmap.setImageDrawable(asyncDrawable);

                    final TypedArray imgs = getResources().obtainTypedArray(R.array.imgDarkSouls);
                    final Random rand = new Random();
                    final int rndInt = rand.nextInt(imgs.length());
                    final int resID = imgs.getResourceId(rndInt, 0);
                    task.execute(resID);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.itemMenuLoadBitmap:
                Log.i("onOptiomsItemSelected", "Creando objeto tarea asíncrona");
                BitmapWorkerTask task = new BitmapWorkerTask(imageViewBitmap, getResources(),
                        imageViewBitmap.getWidth(), imageViewBitmap.getHeight());
                task.execute(R.drawable.forest_restoration_map);
                return true;
            case R.id.itemMenuClearBitmap:
                imageViewBitmap.setImageResource(android.R.color.transparent);
                return true;
            case R.id.itemMenuExitApp:
                this.finish();
                System.exit(RESULT_OK);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean cancelPotentialWork(int data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            Log.i("cancelPotentialWork", "Buscando procesos previos...");
            final int bitmapData = bitmapWorkerTask.getBitmapData();
            if (bitmapData != data) {
                // Cancel previous task
                Log.i("cancelPotencialWork", "Cancelando tarea asíncrona...");
                bitmapWorkerTask.cancel(true);
                return true;
            } else {
                // The same work is already in progress
                return false;
            }
        }
        Log.i("cancelPotentialWork", "No hay hilos previos o ya se finalizaron");
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = this.imageViewBitmap.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }
}