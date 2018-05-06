package com.ma.traveldroid.ui;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ma.traveldroid.R;
import com.ma.traveldroid.data.CountryContract;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    public interface ListItemClickListener {
        void onListItemClick(int clickedPosition);
    }

    private static final String TAG = MyRecyclerAdapter.class.getName();

    private CursorAdapter mCursorAdapter;
    private Context mContext;
    private ViewHolder holder;
    private ListItemClickListener mOnClickListener;


    public MyRecyclerAdapter(Context context, Cursor c, ListItemClickListener listener){
        mOnClickListener = listener;
        mContext = context;
        mCursorAdapter = new CursorAdapter(mContext, c, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                // Inflate the view here
                return LayoutInflater.from(context)
                        .inflate(R.layout.main_list_item, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                // Binding operations
                String countryName = cursor.getString
                        (cursor.getColumnIndexOrThrow(CountryContract.CountryEntry.COLUMN_COUNTRY_NAME));
                holder.countryName.setText(countryName);
            }

        };
    }

    public void changeCursor(Cursor cursor){
        mCursorAdapter.changeCursor(cursor);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Passing the inflater job to the cursor-adapter
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Passing the binding operation to cursor loader
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView countryName;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            countryName =  itemView.findViewById(R.id.country_name_textview);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition + 1);
        }
    }
}
