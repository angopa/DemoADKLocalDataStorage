package demos.android.com.craneo.demoadklocaldatastorage;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;

import demos.android.com.craneo.demoadklocaldatastorage.db.ToursDBOpenHelper;
import demos.android.com.craneo.demoadklocaldatastorage.db.ToursDataSource;
import demos.android.com.craneo.demoadklocaldatastorage.model.Tour;
import demos.android.com.craneo.demoadklocaldatastorage.xml.ToursJDOMParser;

public class SqliteOpenHelperActivity extends ListActivity {

    public static final String LOGTAG="EXPLORECA";
    public static final String USERNAME="pref_username";
    public static final String VIEWIMAGE="pref_viewimages";

    private SharedPreferences settings;

    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    ToursDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_open_helper);

        settings = PreferenceManager.getDefaultSharedPreferences(this);

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                  String key) {
                SqliteOpenHelperActivity.this.refreshDisplay();
            }
        };
        settings.registerOnSharedPreferenceChangeListener(listener);

        dataSource = new ToursDataSource(this);
        dataSource.open();

        List<Tour> tours = dataSource.findAll();
        if (tours.size() == 0){
            createData();
            tours = dataSource.findAll();
        }

        ArrayAdapter<Tour> adapter = new ArrayAdapter<Tour>(this,
                android.R.layout.simple_list_item_1, tours);
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void setPreference(View v) {
        Log.i(LOGTAG, "Clicked set");
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    public void refreshDisplay() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }

    private void createData(){
        Tour tour = new Tour();
        tour.setTitle("Salton Sea");
        tour.setDescription("A tour to saulton sea");
        tour.setPrice(600);
        tour.setImage("salton_image");
        tour = dataSource.create(tour);
        Log.i(LOGTAG, "Tour created with id " + tour.getId());

        tour = new Tour();
        tour.setTitle("Death Valley");
        tour.setDescription("A tour to Death Valley");
        tour.setPrice(900);
        tour.setImage("death_valley");
        tour = dataSource.create(tour);
        Log.i(LOGTAG, "Tour created with id " + tour.getId());

        tour = new Tour();
        tour.setTitle("San Francisco");
        tour.setDescription("A tour to San Francisco");
        tour.setPrice(1200);
        tour.setImage("san_francisco");
        tour = dataSource.create(tour);
        Log.i(LOGTAG, "Tour created with id " + tour.getId());
    }
}
