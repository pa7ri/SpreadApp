package com.ucm.informatica.spread.Utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnim;
import com.ramotion.foldingcell.FoldingCell;
import com.ucm.informatica.spread.Activities.MainTabActivity;
import com.ucm.informatica.spread.Model.Event;
import com.ucm.informatica.spread.Model.Poster;
import com.ucm.informatica.spread.R;

import java.util.List;


public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {
    private List<Alert> data;
    private LayoutInflater inflater;

    public CustomRecyclerAdapter(Context context, List<Alert> data) {
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
        holder.titleItemText.setText(data.get(position).getTitle());
        holder.dateItemText.setText(data.get(position).getDateTimeFormat().substring(0,
                                        data.get(position).getDateTimeFormat().indexOf(" ")));
        holder.iconItemImage.setImageDrawable(inflater.getContext().getResources().getDrawable(R.drawable.ic_location));


        //holder.iconContentItemImage.setImageBitmap(BitmapFactory.decodeByteArray(
          //      data.get(position).getImage(), 0, data.get(position).getImage().length));
        holder.titleContentItemText.setText(data.get(position).getTitle());
        holder.descriptionContentItemText.setText(data.get(position).getDescription());
        Address address = data.get(position).getLocation();
        String addressLine= address.getThoroughfare() + ", " + address.getSubThoroughfare() + "\n"
                + address.getLocality() + "\n"
                + address.getPostalCode() + " - " + address.getSubAdminArea() + "\n"
                + address.getCountryName();

        holder.placeDescriptionContentItemText.setText(addressLine);
        holder.dateContentItemText.setText(data.get(position).getDateTimeFormat());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleItemText, dateItemText, titleContentItemText, descriptionContentItemText,
                placeDescriptionContentItemText, dateContentItemText;
        ImageView iconItemImage, iconContentItemImage;
        ImageButton locationMapButton;


        ViewHolder(View itemView) {
            super(itemView);

            FoldingCell foldingCell = itemView.findViewById(R.id.folding_cell);

            //cell title
            titleItemText = itemView.findViewById(R.id.titleItemText);
            dateItemText = itemView.findViewById(R.id.dateItemText);
            iconItemImage = itemView.findViewById(R.id.iconItemImage);

            //cell content
            titleContentItemText = itemView.findViewById(R.id.titleContentItemText);
            iconContentItemImage = itemView.findViewById(R.id.iconContentItemImage);
            descriptionContentItemText = itemView.findViewById(R.id.descriptionContentItemText);
            placeDescriptionContentItemText = itemView.findViewById(R.id.placeDescriptionContentItemText);
            dateContentItemText = itemView.findViewById(R.id.dateContentItemText);
            locationMapButton = itemView.findViewById(R.id.locationMapButton);

            foldingCell.setOnClickListener(v -> foldingCell.toggle(false));


            locationMapButton.setOnClickListener(view ->
                    Toast.makeText(view.getContext(), "Llévame al mapa", Toast.LENGTH_SHORT).show());

        }
    }

}
