package com.example.mychat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mychat.Model.Chat;
import com.example.mychat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import me.jagar.chatvoiceplayerlibrary.VoicePlayerView;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int MSG_TEXT_LEFT = 0;
    public static final int MSG_TEXT_RIGHT = 1;
    public static final int MSG_RECORD_LEFT = 2;
    public static final int MSG_RECORD_RIGHT = 3;

    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;

    FirebaseUser firebaseUser;


    public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl) {
        this.mContext = mContext;
        this.mChat = mChat;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TEXT_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else if(viewType == MSG_TEXT_LEFT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else if(viewType == MSG_RECORD_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_audio_item_right, parent, false);
            return new MessageAdapter.ViewHolder2(view);
        } else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_audio_item_left, parent, false);
            return new MessageAdapter.ViewHolder2(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == MSG_TEXT_LEFT || holder.getItemViewType() == MSG_TEXT_RIGHT){
            ViewHolder viewHolder = (ViewHolder) holder;
            Chat chat = mChat.get(position);
            viewHolder.show_message.setText(chat.getMessage());

            if (imageurl.equals("default")) {
                viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
            } else {
                Glide.with(mContext).load(imageurl).into(viewHolder.profile_image);
            }

            if (position == mChat.size() - 1) {
                if (chat.isIsseen()) {
                    viewHolder.txt_seen.setText("Seen");
                } else {
                    viewHolder.txt_seen.setText("Delivered");
                }
            } else {
                viewHolder.txt_seen.setVisibility(View.GONE);
            }
        } else {
            ViewHolder2 viewHolder2 = (ViewHolder2) holder;
            Chat chat = mChat.get(position);
            viewHolder2.voicePlayerView.setAudio(chat.getMessage());

            if (imageurl.equals("default")) {
                viewHolder2.profile_image.setImageResource(R.mipmap.ic_launcher);
            } else {
                Glide.with(mContext).load(imageurl).into(viewHolder2.profile_image);
            }

            if (position == mChat.size() - 1) {
                if (chat.isIsseen()) {
                    viewHolder2.txt_seen.setText("Seen");
                } else {
                    viewHolder2.txt_seen.setText("Delivered");
                }
            } else {
                viewHolder2.txt_seen.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView show_message;
        public ImageView profile_image;
        public TextView txt_seen;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {

        public ImageView profile_image;
        public TextView txt_seen;
        public VoicePlayerView voicePlayerView;


        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
            voicePlayerView = itemView.findViewById(R.id.voicePlayerView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid())) {
            if (mChat.get(position).getType().equals("text"))
                return MSG_TEXT_RIGHT;
            else return MSG_RECORD_RIGHT;
        } else {
            if (mChat.get(position).getType().equals("text"))
                return MSG_TEXT_LEFT;
            else return MSG_RECORD_LEFT;
        }
    }
}
