package com.example.graspandbloom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class recyclerv extends RecyclerView.Adapter<recyclerv.ViewHolder> {
    List<PodcastModel> eventList;
    onItemClickListener clickListener;




    public recyclerv(List<PodcastModel> eventList, onItemClickListener clickListener) {
        this.eventList = eventList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public recyclerv.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =null;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview, parent, false);
            return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerv.ViewHolder holder, int position) {

        PodcastModel m = eventList.get(position);
        holder.topic.setText(m.getTopic());
        Picasso.get().load(m.getImageUrl()).fit().placeholder(R.drawable.decib_loading).into(holder.Image);



    }



    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView topic;

        ImageView Image;
        onItemClickListener clickListener;

        public ViewHolder(@NonNull View itemView, final onItemClickListener clickListener) {
            super(itemView);
            Image = itemView.findViewById(R.id.eventImg);
            topic = itemView.findViewById(R.id.podcastTopicId);
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
