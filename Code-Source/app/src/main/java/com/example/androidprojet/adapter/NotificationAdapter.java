package com.example.androidprojet.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.*;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidprojet.R;
import com.example.androidprojet.model.Notification;
import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    Context context;
    private  final ArrayList notifications;

    public NotificationAdapter(Context context, ArrayList<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        Notification notification = (Notification) notifications.get(position);
        holder.sender.setText(notification.getSender());
        holder.messageNotification.setText(notification.getMessageNotification());
        holder.heureRecu.setText(notification.getHeureRecu());
        holder.senderImage.setImageResource(notification.getSenderImage());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder  {

        public final ImageView senderImage;
        public final TextView sender;
        private final TextView messageNotification;
        private final TextView heureRecu;
        private Notification notification;

        public NotificationViewHolder(final View itemView) {
            super(itemView);
            senderImage = itemView.findViewById(R.id.sender_image);
            sender = itemView.findViewById(R.id.sender);
            messageNotification = itemView.findViewById(R.id.messageNotification);
            heureRecu =  itemView.findViewById(R.id.heureRecu);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    notification = (Notification) notifications.get(getLayoutPosition());

                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle(notification.getMessageNotification())
                            .show();
                }
            });
        }
    }
}
