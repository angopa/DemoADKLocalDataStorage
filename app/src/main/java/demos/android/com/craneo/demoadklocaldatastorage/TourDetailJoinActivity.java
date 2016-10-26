package demos.android.com.craneo.demoadklocaldatastorage;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;

import demos.android.com.craneo.demoadklocaldatastorage.db.ToursDataSource;
import demos.android.com.craneo.demoadklocaldatastorage.model.Tour;

public class TourDetailJoinActivity extends AppCompatActivity {

    private static final String LOGTAG = "EXPLORECA";
    Tour tour;
    ToursDataSource dataSource;
    boolean isMyTours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail_join);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        tour = b.getParcelable(".model.Tour");
        isMyTours = b.getBoolean("isMyTours");

        refresDisplay();

        dataSource = new ToursDataSource(this);

    }

    private void refresDisplay() {
        TextView tv = (TextView) findViewById(R.id.titleText);
        tv.setText(tour.getTitle());

        NumberFormat nf = NumberFormat.getCurrencyInstance();
        tv = (TextView) findViewById(R.id.priceText);
        tv.setText(nf.format(tour.getPrice()));

        tv = (TextView) findViewById(R.id.descText);
        tv.setText(tour.getDescription());

        ImageView iv = (ImageView) findViewById(R.id.imageView1);
        int imageResource = getResources().getIdentifier(
                tour.getImage(), "drawable", getPackageName());
        if (imageResource != 0) {
            iv.setImageResource(imageResource);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tour_detail, menu);

        //Show delete menu item if we came from myTours
        menu.findItem(R.id.menu_delete).setVisible(isMyTours);

        //Show delete menu item if we came from myTours
        menu.findItem(R.id.menu_add).setVisible(!isMyTours);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_add:
                if (dataSource.addToMyTours(tour)){
                    Log.d(LOGTAG, "Tour added");
                }else{
                    Log.d(LOGTAG, "Tour not added");
                }
            case R.id.menu_delete:
                if(dataSource.removeFromMyTours(tour)){
                    setResult(-1);;
                    finish();
                }
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
}
