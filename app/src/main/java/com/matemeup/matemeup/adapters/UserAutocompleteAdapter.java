package com.matemeup.matemeup.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.matemeup.matemeup.R;
import com.matemeup.matemeup.entities.model.UserChat;
import com.matemeup.matemeup.entities.rendering.AvatarRemoteImageLoader;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class UserAutocompleteAdapter extends RemoteArrayAdapter<UserChat> {

    @Override
    public void setListFromResponse(JSONArray jsonUsers) {
        list.clear();
        try {
            for (int i = 0; i < jsonUsers.length(); i++) {
                list.add(new UserChat(jsonUsers.getJSONObject(i)));
            }
        } catch (JSONException e) {}
    }

    public UserAutocompleteAdapter(Context ctx, int textViewResourceId){
        super("users/chat/get", ctx, textViewResourceId, new ArrayList<UserChat>());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inflater.inflate(R.layout.username_autocomplete_dropdown_item, null, false);
        UserChat user = super.getItem(position);
        ((TextView)view.findViewById(R.id.username_autocomplete_container)).setText(user.name);
        ImageView img = view.findViewById(R.id.avatar_autocomplete_container);
        AvatarRemoteImageLoader.load(img, user.avatar);
        return view;
    }

    @Nullable
    @Override
    public UserChat getItem(final int position) {
        return new UserChat(super.getItem(position)) {
            @Override
            public String toString() {
                return name;
            }
        };
    }
}

