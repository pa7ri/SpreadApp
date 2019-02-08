package com.ucm.informatica.spread.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ucm.informatica.spread.Model.Event;
import com.ucm.informatica.spread.R;

import java.util.List;

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {
    private List<Event> data;
    private LayoutInflater inflater;

    public CustomRecyclerAdapter(Context context, List<Event> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView titleText, dateTimeText, placeText;

        ViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleItemText);
            dateTimeText = itemView.findViewById(R.id.dateTimeItemText);
            placeText = itemView.findViewById(R.id.placerItemText);
        }

        @Override
        public void onClick(View view) {
            // getAdapterPosition());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_recycler_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomRecyclerAdapter.ViewHolder holder, int position) {
        holder.titleText.setText(data.get(position).getTitle());
        holder.dateTimeText.setText(data.get(position).getDateTimeFormat());
        holder.placeText.setText(data.get(position).getPlace());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
