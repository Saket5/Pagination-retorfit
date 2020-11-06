package com.example.payo;

import android.app.Activity;
import android.content.Context;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class jsonAdapter extends RecyclerView.Adapter<jsonAdapter.jsonViewHolder>{

    private ArrayList<JsonObjectAPI.Data> dataArrayList;
    private objectClickListener listener;
    private Activity activity;

    public jsonAdapter(Activity activity,ArrayList<JsonObjectAPI.Data> dataArrayList,objectClickListener listener)
    {
        this.activity=activity;
        this.dataArrayList=dataArrayList;
        this.listener=listener;
    }

    @NonNull
    @Override
    public jsonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.home_item, parent, false);
        return new jsonAdapter.jsonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull jsonViewHolder holder, int position) {
        JsonObjectAPI.Data data=dataArrayList.get(position);
        Glide.with(activity).load(data.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.roundedImageView);
        holder.firstName.setText(data.getFirstName());
        holder.lastName.setText(data.getLastName());
        holder.emailId.setText(data.getEmail());
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public interface objectClickListener{
        void onLongClick(int position);
    }

    public class jsonViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        RoundedImageView roundedImageView;
        TextView firstName;
        TextView lastName;
        TextView emailId;
        public jsonViewHolder(@NonNull View itemView) {
            super(itemView);
            roundedImageView=itemView.findViewById(R.id.avatarImage);
            firstName=itemView.findViewById(R.id.fullName);
            lastName=itemView.findViewById(R.id.lastName);
            emailId=itemView.findViewById(R.id.emailId);
            itemView.findViewById(R.id.cardViewHomeItem).setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onLongClick(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }
    }
}
