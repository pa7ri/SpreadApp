package com.ucm.informatica.spread.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ucm.informatica.spread.R;

import java.util.List;

public class TelegramRecyclerAdapter extends RecyclerView.Adapter<TelegramRecyclerAdapter.ViewHolder> {
    private List<Pair<String,String>> telegramGroupList;
    private LayoutInflater inflater;

    public TelegramRecyclerAdapter(Context context, List<Pair<String,String>> data) {
        this.inflater = LayoutInflater.from(context);
        this.telegramGroupList = data;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_telegram_recycler_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TelegramRecyclerAdapter.ViewHolder holder, int position) {
        loadItemData(holder, telegramGroupList.get(position).first);
    }

    private void loadItemData(@NonNull TelegramRecyclerAdapter.ViewHolder holder, String title){
        holder.titleItemText.setText(title);
    }

    @Override
    public int getItemCount() {
        return telegramGroupList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleItemText;

        ViewHolder(View itemView) {
            super(itemView);
            titleItemText = itemView.findViewById(R.id.titleItemText);
        }
    }

}
