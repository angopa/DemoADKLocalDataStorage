package demos.android.com.craneo.demoadklocaldatastorage;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;

import demos.android.com.craneo.demoadklocaldatastorage.model.Tour;
import demos.android.com.craneo.demoadklocaldatastorage.xml.ToursPullParser;

public class XmlPullParserActivity extends ListActivity {

    public static final String LOGTAG="EXPLORECA";
    private SharedPreferences settings;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml_pull_parser);

        settings = PreferenceManager.getDefaultSharedPreferences(this);

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                  String key) {
                XmlPullParserActivity.this.refreshDisplay(null);
            }
        };
        settings.registerOnSharedPreferenceChangeListener(listener);

        ToursPullParser parser = new ToursPullParser();
        List<Tour> tours = parser.parseXML(this);

        ArrayAdapter<Tour> adapter = new ArrayAdapter<Tour>(this, android.R.layout.simple_list_item_1, tours);
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

    public void refreshDisplay(View v) {
        Log.i(LOGTAG, "Clicked show");

    }
}
