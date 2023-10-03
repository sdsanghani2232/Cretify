package com.sd.certify.rv_adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sd.certify.R;
import com.sd.certify.data_class.Event_Details;

import java.util.List;

public class User_Home_Rv_Adapter extends RecyclerView.Adapter<User_Home_Rv_Adapter.EventHolder> {

    Context context;
    String name,date,url;
    List<Event_Details> evList;
    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new User_Home_Rv_Adapter.EventHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_event_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {
        name = evList.get(position).getEvent_name();
        date = evList.get(position).getDate();
        url = evList.get(position).getImg();
        holder.evenImg.setImageURI(Uri.parse(url));
        holder.eventName.setText(name);
        holder.eventDate.setText(date);
    }

    @Override
    public int getItemCount() {
        return evList != null ? evList.size() : 0;
    }

    public void setEventList(List<Event_Details> evList) {
        this.evList = evList;
    }

    class EventHolder extends RecyclerView.ViewHolder {

        ImageView evenImg;
        TextView eventName,eventDate;
        public EventHolder(@NonNull View itemView) {
            super(itemView);

            evenImg = itemView.findViewById(R.id.event_img);
            eventName = itemView.findViewById(R.id.event_name);
            eventDate = itemView.findViewById(R.id.event_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, Event_Details.class);
                    i.putExtra("name",name);
                    i.putExtra("img",url);
                    i.putExtra("date",date);
                    startActivity(context,i,null);

                }
            });
        }
    }
}
