package demos.android.com.craneo.demoadklocaldatastorage;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import demos.android.com.craneo.demoadklocaldatastorage.utils.UIHelper;

public class PreferencesJavaActivity extends AppCompatActivity {

    public static final String LOGTAG= "EXPLORECA";
    public static final String USERNAME = "username";

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences_java);

        settings = getPreferences(MODE_PRIVATE);
    }

    public void setPreference(View view){
        Log.i(LOGTAG, "Clicked set");

        SharedPreferences.Editor editor = settings.edit();
        String prefValue = UIHelper.getText(this, R.id.editText1);
        editor.putString(USERNAME, prefValue);
        editor.commit();
        UIHelper.displayText(this, R.id.textView1, "Preference saved");
    }

    public void refreshDisplay(View view){
        Log.i(LOGTAG, "Clicked show");

        String prefValue = settings.getString(USERNAME, "Not found");
        UIHelper.displayText(this, R.id.textView1, prefValue);
    }
}
