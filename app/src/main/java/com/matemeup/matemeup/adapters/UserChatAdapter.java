package com.matemeup.matemeup.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.matemeup.matemeup.R;

import com.matemeup.matemeup.entities.model.UserChat;
import com.matemeup.matemeup.entities.rendering.AvatarRemoteImageLoader;

import java.util.List;

public class UserChatAdapter extends RecyclerView.Adapter<UserChatAdapter.ViewHolder> {
    private Context context;
    private List<UserChat> users;
    private int textViewResourceId;
    private final OnItemClickListener listener;

    public UserChatAdapter(Context ctx, int textViewResourceId, List<UserChat> u){
        this.textViewResourceId = textViewResourceId;
        this.listener = null;
        context = ctx;
        users = u;
    }


    public UserChatAdapter(Context ctx, int textViewResourceId, List<UserChat> u,  OnItemClickListener listener){
        this.textViewResourceId = textViewResourceId;
        this.listener = listener;
        context = ctx;
        users = u;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    @Override
    public UserChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(textViewResourceId, parent, false);
        return new UserChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserChat chat = users.get(position);
        holder.bind(chat, listener);
        holder.display(chat);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private UserChat currentUser;

        public ViewHolder(final View itemView) {
            super(itemView);
        }

        public void bind(final UserChat item, final OnItemClickListener listener) {
            if (listener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        listener.onItemClick(item);
                    }
                });
            }
        }

        public void display(UserChat user) {
            currentUser = user;
            View view = itemView;
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (view == null)
                view = inflater.inflate(R.layout.item_chat_user, null, false);
            ((TextView)view.findViewById(R.id.username_container)).setText(user.name);
            ImageView img = view.findViewById(R.id.avatar_container);
            img.setClipToOutline(true);
            AvatarRemoteImageLoader.load(img, user.avatar);
        }
    }


    public void setList(List<UserChat> u) {
        users = u;
        notifyDataSetChanged();
    }}

