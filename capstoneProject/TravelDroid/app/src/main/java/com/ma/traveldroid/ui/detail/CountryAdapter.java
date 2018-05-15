package com.ma.traveldroid.ui.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ma.traveldroid.R;
import com.ma.traveldroid.roomDb.CountryEntry;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {


    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;

    private List<CountryEntry> mCountryEntries;
    private Context mContext;

    public CountryAdapter(Context context, ItemClickListener itemClickListener){
        mContext = context;
        mItemClickListener = itemClickListener;
    }


    @Override
    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.main_list_item, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CountryViewHolder holder, int position) {
        CountryEntry countryEntry = mCountryEntries.get(position);
        String countryName = countryEntry.getCountryName();

        holder.countryName.setText(countryName);

    }

    @Override
    public int getItemCount() {
        if(mCountryEntries == null)
            return 0;
        return mCountryEntries.size();
    }

    public List<CountryEntry> getCountryEntries(){
        return mCountryEntries;
    }

    public void setCountries(List<CountryEntry> countryEntries) {
        mCountryEntries = countryEntries;
        notifyDataSetChanged();
    }

    class CountryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView countryName;
        public CountryViewHolder(View itemView) {
            super(itemView);

            countryName = itemView.findViewById(R.id.country_name_textview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int elementId = mCountryEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }
}
