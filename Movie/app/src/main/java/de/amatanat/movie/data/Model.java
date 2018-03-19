package de.amatanat.movie.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by amatanat.
 */

public class Model implements Parcelable {

    private String name;
    private String content;

    public Model(String name, String content) {
        this.name = name;
        this.content = content;
    }

    private Model(Parcel in){
        this.name = in.readString();
        this.content = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(content);
    }

    public static final Parcelable.Creator<Model> CREATOR = new Parcelable.Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel parcel) {
            return new Model(parcel);
        }

        @Override
        public Model[] newArray(int i) {
            return new Model[i];
        }

    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
