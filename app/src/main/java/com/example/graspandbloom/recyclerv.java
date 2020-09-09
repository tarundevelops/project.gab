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
    Context context;




    public recyclerv(List<model> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public recyclerv.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerv.ViewHolder holder, int position) {
        model event = eventList.get(position);
//        holder.SpeakerName.setText(event.getSpeakerName());
//        holder.eventName.setText(event.getEventName());
        holder.date.setText(event.getDate());
        holder.time.setText(event.getTime());


        Picasso.get().load(event.getImageUrl()).fit().into(holder.eventImage);

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, SpeakerName, time, date;
        Button knowMore, register;
        ImageView eventImage;
        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);



//            eventName = itemView.findViewById(R.id.enteredTitle);
//            SpeakerName = itemView.findViewById(R.id.enteredSpeaker);
            date = itemView.findViewById(R.id.enteredDate);
            time = itemView.findViewById(R.id.enteredTiming);
            knowMore = itemView.findViewById(R.id.KnowMoreButton);
            register = itemView.findViewById(R.id.registerButton);
            eventImage = itemView.findViewById(R.id.eventImg);
        }
    }
}
