package demos.android.com.craneo.demoadklocaldatastorage;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import demos.android.com.craneo.demoadklocaldatastorage.db.ToursDataSource;
import demos.android.com.craneo.demoadklocaldatastorage.model.Tour;
import demos.android.com.craneo.demoadklocaldatastorage.xml.ToursPullParser;

public class ParcelableInterfaceActivity extends AppCompatActivity{

    public static final String LOGTAG="EXPLORECA";
    public static final String USERNAME="pref_username";
    public static final String VIEWIMAGE="pref_viewimages";
    private static final int TOUR_DETAIL_ACTIVITY = 1009;

    private SharedPreferences settings;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    private List<Tour> tours;

    ToursDataSource datasource;

    ListView listView;
    boolean isMyTours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcelable_interface);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.lisView);

        settings = PreferenceManager.getDefaultSharedPreferences(this);

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                  String key) {
                ParcelableInterfaceActivity.this.refreshDisplay();
            }
        };
        settings.registerOnSharedPreferenceChangeListener(listener);

        datasource = new ToursDataSource(this);
        datasource.open();

        tours = datasource.findAll();
        if (tours.size() == 0) {
            createData();
            tours = datasource.findAll();
        }
        isMyTours = false;
        refreshDisplay();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_all:
                tours = datasource.findAll();
                refreshDisplay();
                isMyTours = false;
                break;

            case R.id.menu_cheap:
                tours = datasource.findFilter("price <= 300", "price ASC");
                refreshDisplay();
                isMyTours = false;
                break;

            case R.id.menu_fancy:
                tours = datasource.findFilter("price >= 1000", "price DESC");
                refreshDisplay();
                isMyTours = false;
                break;

            case R.id.menu_mytours:
                tours = datasource.findMyTours();
                refreshDisplay();
                isMyTours = true;
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshDisplay() {

        boolean viewImages = settings.getBoolean(VIEWIMAGE, false);

        if (viewImages) {
            ArrayAdapter<Tour> adapter = new TourListAdapter(this, tours);
            listView.setAdapter(adapter);
        } else {
            ArrayAdapter<Tour> adapter = new ArrayAdapter<Tour>(this,
                    android.R.layout.simple_list_item_1, tours);
            listView.setAdapter(adapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Tour tour = tours.get(position);

                Intent intent = new Intent(ParcelableInterfaceActivity.this, TourDetailJoinActivity.class);

                intent.putExtra(".model.Tour", tour);
                intent.putExtra("isMyTours", isMyTours);
                
                startActivityForResult(intent, TOUR_DETAIL_ACTIVITY);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        datasource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        datasource.close();
    }

    private void createData() {
        ToursPullParser parser = new ToursPullParser();
        List<Tour> tours = parser.parseXML(this);

        for (Tour tour : tours) {
            datasource.create(tour);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TOUR_DETAIL_ACTIVITY && resultCode == -1){
            datasource.open();
            tours = datasource.findMyTours();
            refreshDisplay();
            isMyTours = true;
        }
    }
}
