package demos.android.com.craneo.demoadklocaldatastorage.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.NumberFormat;

import demos.android.com.craneo.demoadklocaldatastorage.MainActivity;

public class Tour implements Parcelable{
	private long id;
	private String title;
	private String description;
	private double price;
	private String image;

	public Tour(){}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	@Override
	public String toString() {
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		return title + "\n(" + nf.format(price) + ")";
	}

	protected Tour(Parcel in) {
		Log.i(MainActivity.LOGTAG, "Parcel construtor" );
		id = in.readLong();
		title = in.readString();
		description = in.readString();
		price = in.readDouble();
		image = in.readString();
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeLong(id);
		parcel.writeString(title);
		parcel.writeString(description);
		parcel.writeDouble(price);
		parcel.writeString(image);
	}

	public static final Creator<Tour> CREATOR = new Creator<Tour>() {
		@Override
		public Tour createFromParcel(Parcel in) {
			Log.i(MainActivity.LOGTAG, "createFromParcel" );
			return new Tour(in);
		}

		@Override
		public Tour[] newArray(int size) {
			Log.i(MainActivity.LOGTAG, "newArray" );
			return new Tour[size];
		}
	};

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int describeContents() {
		return 0;
	}
}
