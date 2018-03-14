package com.matemeup.matemeup.entities.rendering;

import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

public class AvatarRemoteImageLoader extends RemoteImageLoader {

    public static final String URL = "http://192.168.0.100:8000/img/avatars";

    public static void load(ImageView view, String name) {
        load(view, URL, name);
    }
}