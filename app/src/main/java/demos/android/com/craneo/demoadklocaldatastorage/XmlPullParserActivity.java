package demos.android.com.craneo.demoadklocaldatastorage;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import demos.android.com.craneo.demoadklocaldatastorage.bean.Tour;
import demos.android.com.craneo.demoadklocaldatastorage.utils.UIHelper;

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
