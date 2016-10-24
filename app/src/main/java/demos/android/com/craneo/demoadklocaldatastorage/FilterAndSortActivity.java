package demos.android.com.craneo.demoadklocaldatastorage;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.List;

import demos.android.com.craneo.demoadklocaldatastorage.db.ToursDataSource;
import demos.android.com.craneo.demoadklocaldatastorage.model.Tour;
import demos.android.com.craneo.demoadklocaldatastorage.xml.ToursPullParser;

public class FilterAndSortActivity extends ListActivity implements View.OnClickListener{

    public static final String LOGTAG="EXPLORECA";
    public static final String USERNAME="pref_username";
    public static final String VIEWIMAGE="pref_viewimages";

    private SharedPreferences settings;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    ToursDataSource dataSource;

    private List<Tour> tours;

    Button buttonAll;
    Button buttonCheap;
    Button buttonFancy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_and_sort);

        buttonAll = (Button) findViewById(R.id.show_all);
        buttonCheap = (Button) findViewById(R.id.show_cheap);
        buttonFancy = (Button) findViewById(R.id.show_fancy);

        buttonAll.setOnClickListener(this);
        buttonCheap.setOnClickListener(this);
        buttonFancy.setOnClickListener(this);

        settings = PreferenceManager.getDefaultSharedPreferences(this);

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                  String key) {
                FilterAndSortActivity.this.refreshDisplay();
            }
        };
        settings.registerOnSharedPreferenceChangeListener(listener);

        dataSource = new ToursDataSource(this);
        dataSource.open();

        tours = dataSource.findAll();
        if (tours.size() == 0){
            createData();
            tours = dataSource.findAll();
        }
        refreshDisplay();
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
        ArrayAdapter<Tour> adapter = new ArrayAdapter<Tour>(this,
                android.R.layout.simple_list_item_1, tours);
        setListAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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

    private void createData() {
        ToursPullParser parser = new ToursPullParser();
        List<Tour> tours = parser.parseXML(this);

        for (Tour tour : tours) {
            dataSource.create(tour);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_all:
                tours = dataSource.findAll();
                refreshDisplay();
                break;

            case R.id.show_cheap:
                tours = dataSource.findFilter("price <= 300", "price ASC");
                refreshDisplay();
                break;

            case R.id.show_fancy:
                tours = dataSource.findFilter("price >= 300", "price DESC");
                refreshDisplay();
                break;

            default:
                break;
        }
    }
}
