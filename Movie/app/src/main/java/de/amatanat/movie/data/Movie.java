package de.amatanat.movie.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by amatanat.
 */

public class Movie implements Parcelable {

    private int id;
    private String title;
    private String image;
    private String releaseDate;
    private double rating;
    private String description;

    public Movie(int id, String title, String image, String releaseDate, double rating, String description) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.description = description;
    }

    private Movie(Parcel in){
        this.title = in.readString();
        this.image = in.readString();
        this.id = in.readInt();
        this.releaseDate = in.readString();
        this.rating = in.readDouble();
        this.description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() { return id + "--" + title + "--" + releaseDate
                                + "--" + rating + "--" + description + "--" + image; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(image);
        dest.writeInt(id);
        dest.writeString(releaseDate);
        dest.writeDouble(rating);
        dest.writeString(description);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }

    };
}
