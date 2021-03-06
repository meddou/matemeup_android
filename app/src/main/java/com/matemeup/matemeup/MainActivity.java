package com.matemeup.matemeup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.gson.JsonObject;
import com.matemeup.matemeup.entities.Callback;
import com.matemeup.matemeup.entities.ConnectedUser;
import com.matemeup.matemeup.entities.Request;
import com.matemeup.matemeup.entities.JWT;
import com.matemeup.matemeup.entities.IntentManager;
import com.matemeup.matemeup.entities.websocket.MMUWebSocket;
import com.matemeup.matemeup.entities.websocket.WebSocket;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private void checkJWT()
    {
        String jwt = JWT.getAPI(this);
        Callback cb = new Callback()
        {
            @Override
            public void success(Object data)
            {
                try {
                    ConnectedUser.set(((JSONObject)data).getJSONObject("user"));
                } catch (JSONException e) {}
                goToHome();
            }

            @Override
            public void fail(String error)
            {
                final View loader = findViewById(R.id.loader_progressbar);

                loader.animate()
                        .alpha(0.0f)
                        .setDuration(750)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                loader.setVisibility(View.GONE);
                                findViewById(R.id.connection_buttons).setVisibility(View.VISIBLE);
                            }
                        });
            }
        };

        Request req = Request.getInstance();
        if (jwt != null && !jwt.equals(""))
        {
            req.addQueryString("token", jwt);
            req.send(this, "me", "GET", null, null, cb);
        }
        else {
            cb.fail("noJWT");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.checkJWT();
    }

    public void goToRegister(View android)
    {
        IntentManager.goTo(MainActivity.this, RegisterActivity.class);
    }

    public void goToLogin(View android)
    {
        IntentManager.goTo(MainActivity.this, LoginActivity.class);
    }

    public void goToHome()
    {
        IntentManager.replace(MainActivity.this, HomeActivity.class);
    }
}
