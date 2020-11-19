package com.example.graspandbloom;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class recyclerv extends RecyclerView.Adapter<recyclerv.ViewHolder> {
    List<model> eventList;
    onItemClickListener clickListener;




    public recyclerv(List<model> eventList, onItemClickListener clickListener) {
        this.eventList = eventList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public recyclerv.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerv.ViewHolder holder, int position) {
        model event = eventList.get(position);
//        holder.SpeakerName.setText(event.getSpeakerName());
//        holder.eventName.setText(event.getEventName());
        holder.date.setText(event.getDate());
        holder.time.setText(event.getTime());

        holder.eventTitle.setText(event.getEventName());


        Picasso.get().load(event.getImageUrl()).fit().into(holder.eventImage);

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, SpeakerName, time, date, eventTitle;

        ImageView eventImage;
        onItemClickListener clickListener;
        public ViewHolder(@NonNull View itemView, final onItemClickListener clickListener) {
            super(itemView);
            date = itemView.findViewById(R.id.enteredDate);
            time = itemView.findViewById(R.id.enteredTiming);
            eventImage = itemView.findViewById(R.id.eventImg);
            eventTitle = itemView.findViewById(R.id.EventTitle);
            this.clickListener = clickListener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.itemClick(getAdapterPosition());
                }
            });
        }
    }
    public interface onItemClickListener{
        void itemClick(int position);
    }
}
