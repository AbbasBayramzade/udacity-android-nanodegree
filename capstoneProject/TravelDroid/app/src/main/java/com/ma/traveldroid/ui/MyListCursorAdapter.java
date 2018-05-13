package com.ma.traveldroid.ui;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ma.traveldroid.R;
import com.ma.traveldroid.data.CountryContract;

public class MyListCursorAdapter  extends CursorRecyclerAdapter<MyListCursorAdapter.ViewHolder> {

    public MyListCursorAdapter(Cursor cursor) {
        super(cursor);
    }

    @Override
    public void onBindViewHolderCursor(ViewHolder holder, Cursor cursor) {
        String countryName = cursor.getString
                (cursor.getColumnIndexOrThrow(CountryContract.CountryEntry.COLUMN_COUNTRY_NAME));
        holder.countryName.setText(countryName);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_list_item, parent, false);
        return new ViewHolder(itemView);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView countryName;

        public ViewHolder(View itemView) {
            super(itemView);
            countryName =  itemView.findViewById(R.id.country_name_textview);
        }
    }
}
