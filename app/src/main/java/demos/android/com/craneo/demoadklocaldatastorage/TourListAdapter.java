package demos.android.com.craneo.demoadklocaldatastorage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import demos.android.com.craneo.demoadklocaldatastorage.model.Tour;

/**
 * Created by crane on 10/23/2016.
 */

public class TourListAdapter extends ArrayAdapter<Tour> {
    Context context;
    List<Tour> tours;

    public TourListAdapter(Context context, List<Tour> tours){
        super(context, android.R.id.content, tours);
        this.context = context;
        this.tours = tours;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = vi.inflate(R.layout.list_item_tour, null);

        Tour tour = tours.get(position);

        TextView tv = (TextView) view.findViewById(R.id.titleText);
        tv.setText(tour.getTitle());

        tv = (TextView) view.findViewById(R.id.priceText);
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        tv.setText(nf.format(tour.getPrice()));

        ImageView iv = (ImageView) view.findViewById(R.id.imageView1);
        int imageResource = context.getResources().getIdentifier(
                tour.getImage(), "drawable", context.getPackageName());
        if(imageResource != 0){
            iv.setImageResource(imageResource);
        }
        return view;
    }
}
