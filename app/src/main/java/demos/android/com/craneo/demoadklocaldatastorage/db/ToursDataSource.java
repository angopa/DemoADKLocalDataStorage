package demos.android.com.craneo.demoadklocaldatastorage.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import demos.android.com.craneo.demoadklocaldatastorage.model.Tour;

/**
 * Created by crane on 10/22/2016.
 */

public class ToursDataSource {

    public static final String LOGTAG="EXPLORECA";
    SQLiteOpenHelper dbHelper;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            ToursDBOpenHelper.COLUMN_ID,
            ToursDBOpenHelper.COLUMN_TITLE,
            ToursDBOpenHelper.COLUMN_DESC,
            ToursDBOpenHelper.COLUMN_PRICE,
            ToursDBOpenHelper.COLUMN_IMAGE};

    public ToursDataSource(Context context) {
        dbHelper = new ToursDBOpenHelper(context);
    }

    public void open(){
        Log.i(LOGTAG, "Database opened");
        database = dbHelper.getWritableDatabase();

    }

    public void close(){
        Log.i(LOGTAG, "Database closed");
    }

    public Tour create(Tour tour){
        ContentValues values = new ContentValues();
        values.put(ToursDBOpenHelper.COLUMN_TITLE, tour.getTitle());
        values.put(ToursDBOpenHelper.COLUMN_DESC, tour.getDescription());
        values.put(ToursDBOpenHelper.COLUMN_PRICE, tour.getPrice());
        values.put(ToursDBOpenHelper.COLUMN_IMAGE, tour.getImage());
        long insertId = database.insert(ToursDBOpenHelper.TABLE_TOURS, null, values);
        tour.setId(insertId);
        return tour;
    }

    public List<Tour> findAll(){
        Cursor cursor = database.query(ToursDBOpenHelper.TABLE_TOURS, allColumns,
                null, null, null, null, null);
        Log.i(LOGTAG, "Returned "+ cursor.getCount() + " rows");
        List<Tour> tours = cursorToList(cursor);
        return tours;

    }

    public List<Tour> findFilter(String selection, String orderBy){
        Cursor cursor = database.query(ToursDBOpenHelper.TABLE_TOURS, allColumns,
                selection, null, null, null, orderBy);
        Log.i(LOGTAG, "Returned "+ cursor.getCount() + " rows");
        List<Tour> tours = cursorToList(cursor);
        return tours;
    }

    public List<Tour> cursorToList(Cursor cursor) {
        List<Tour> tours = new ArrayList<>();
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                Tour tour = new Tour();
                tour.setId(cursor.getLong(cursor.getColumnIndex(ToursDBOpenHelper.COLUMN_ID)));
                tour.setTitle(cursor.getString(cursor.getColumnIndex(ToursDBOpenHelper.COLUMN_TITLE)));
                tour.setDescription(cursor.getString(cursor.getColumnIndex(ToursDBOpenHelper.COLUMN_DESC)));
                tour.setImage(cursor.getString(cursor.getColumnIndex(ToursDBOpenHelper.COLUMN_IMAGE)));
                tour.setPrice(cursor.getDouble(cursor.getColumnIndex(ToursDBOpenHelper.COLUMN_PRICE)));
                tours.add(tour);
            }
        }
        return tours;
    }

    public boolean addToMyTours(Tour tour){
        ContentValues values = new ContentValues();
        values.put(ToursDBOpenHelper.COLUMN_ID, tour.getId());
        Long result = database.insert(ToursDBOpenHelper.TABLE_MYTOURS, null, values);
        return (result != -1);
    }

    public boolean removeFromMyTours(Tour tour){
        String where = ToursDBOpenHelper.COLUMN_ID+"="+tour.getId();
        int result = database.delete(ToursDBOpenHelper.TABLE_MYTOURS, where, null);
        return (result == 1);
    }

    public List<Tour> findMyTours(){
        String query = "SELECT tours.* FROM "+
                "tours JOIN mytours ON "+
                "tours.tourId = mytours.tourId";
        Cursor cursor = database.rawQuery(query, null);
        Log.d(LOGTAG, "Returned "+cursor.getCount()+" columns");
        List<Tour> tours = cursorToList(cursor);
        return tours;
    }


}
