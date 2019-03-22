package com.ucm.informatica.spread.Utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ramotion.foldingcell.FoldingCell;
import com.ucm.informatica.spread.Model.Alert;
import com.ucm.informatica.spread.Model.Poster;
import com.ucm.informatica.spread.R;

import java.util.List;


public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {
    private List<Alert> dataAlert;
    private List<Poster> dataPoster;
    private LayoutInflater inflater;
    private CustomLocationManager locationManager;

    public CustomRecyclerAdapter(Context context, List<Alert> dataAlert, List<Poster> dataPoster) {
        locationManager = new CustomLocationManager(context);
        this.inflater = LayoutInflater.from(context);
        this.dataAlert = dataAlert;
        this.dataPoster = dataPoster;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_recycler_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomRecyclerAdapter.ViewHolder holder, int position) {
        if (position<dataAlert.size()){
            Alert singleAlert = dataAlert.get(position);
            loadItemData(holder, singleAlert.getTitle(),singleAlert.getDescription(),
                singleAlert.getDateTimeFormat(),
                locationManager.getFormatAddress(singleAlert.getLatitude(), singleAlert.getLongitude()),
                inflater.getContext().getResources().getDrawable(R.drawable.ic_location), null);
        } else {
            Poster singlePoster = dataPoster.get(position-dataAlert.size());
            loadItemData(holder, singlePoster.getTitle(),singlePoster.getDescription(),
                singlePoster.getDateTimeFormat(),
                locationManager.getFormatAddress(singlePoster.getLatitude(), singlePoster.getLongitude()),
                inflater.getContext().getResources().getDrawable(R.drawable.ic_pic),
                singlePoster.getImage());

        }

    }

    private void loadItemData(@NonNull CustomRecyclerAdapter.ViewHolder holder, String title,
                              String description, String datetime, String address,
                              Drawable icon, byte[] image){
        //header
        holder.titleItemText.setText(title);
        holder.dateItemText.setText(description);
        holder.iconItemImage.setImageDrawable(icon);
        //body
        holder.titleContentItemText.setText(title);
        holder.descriptionContentItemText.setText(description);
        holder.placeDescriptionContentItemText.setText(address);
        holder.dateContentItemText.setText(datetime);
        if(image!=null) {
            holder.iconContentItemImage.setVisibility(View.VISIBLE);
            holder.iconContentItemImage.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
        }
    }

    @Override
    public int getItemCount() {
        return dataAlert.size() + dataPoster.size();
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
