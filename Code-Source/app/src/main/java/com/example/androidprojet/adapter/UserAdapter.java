package com.example.androidprojet.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidprojet.databinding.ItemContainerUserBinding;
import com.example.androidprojet.model.FireBaseUser;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private ItemClickListener myitemClickListener;
    private final List<FireBaseUser> fireBaseUsers;

    public UserAdapter(List<FireBaseUser> fireBaseUsers, ItemClickListener itemClickListener) {
        this.fireBaseUsers = fireBaseUsers;
        this.myitemClickListener = itemClickListener;
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        ItemContainerUserBinding binding;
        UserViewHolder(ItemContainerUserBinding itemContainerUserBinding){
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;
        }

        void setUserData(FireBaseUser fireBaseUser) {
            binding.textName.setText(fireBaseUser.name);
            binding.textEmail.setText(fireBaseUser.email);
            binding.imageProfile.setImageResource(fireBaseUser.image);
        }


    }

    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(fireBaseUsers.get(position));
        holder.itemView.setOnClickListener(view -> {
            myitemClickListener.onItemClick(fireBaseUsers.get(position)); // this will get the position of our item in RecyclerView
        });
    }

    @Override
    public int getItemCount() {
        return fireBaseUsers.size();
    }

    public interface ItemClickListener {
        void onItemClick(FireBaseUser fireBaseUser);
    }
}
