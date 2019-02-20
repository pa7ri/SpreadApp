package com.ucm.informatica.spread.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnim;
import com.ucm.informatica.spread.Activities.MainTabActivity;
import com.ucm.informatica.spread.Model.Event;
import com.ucm.informatica.spread.R;

import java.util.List;
import java.util.Objects;

import static java.security.AccessController.getContext;

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {
    private List<Event> data;
    private LayoutInflater inflater;

    public CustomRecyclerAdapter(Context context, List<Event> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
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


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleText, dateTimeText, placeText;

        ViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleItemText);
            dateTimeText = itemView.findViewById(R.id.dateTimeItemText);
            placeText = itemView.findViewById(R.id.placerItemText);
            itemView.setOnClickListener(view -> {
                final AlertDialog dialogBuilder = new AlertDialog.Builder(view.getContext()).create();
                View dialogView = inflater.inflate(R.layout.dialog_alert_details, null);

                Button exitButton = dialogView.findViewById(R.id.exitButton);
                Button supportButton = dialogView.findViewById(R.id.supportButton);

                exitButton.setOnClickListener(v -> dialogBuilder.dismiss());
                supportButton.setOnClickListener(v -> Toast.makeText(view.getContext(), "Gracias por el apoyo", Toast.LENGTH_SHORT).show());

                dialogBuilder.setView(dialogView);
                dialogBuilder.show();
            });
        }
    }

}
