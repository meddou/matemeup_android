package com.matemeup.matemeup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.matemeup.matemeup.entities.JWT;
import com.matemeup.matemeup.entities.Request;
import com.matemeup.matemeup.entities.Validator;
import com.matemeup.matemeup.entities.containers.Quad;
import com.matemeup.matemeup.entities.model.UserChat;
import com.matemeup.matemeup.entities.rendering.RemoteImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AccountModifierLayout {

    @SuppressWarnings("unchecked")
    public void submitModif(View view) {
        List<Quad<Integer, ValueGetter, RegisterActivity.ValueValidation, String>> fieldsId = new ArrayList();
        JSONObject obj;

        fieldsId.add(new Quad(R.id.firstname_input, new ValueGetter() {
            public Object get(View _view) {
                //return "toto";
                return getFromString(_view);
            }
        }, new RegisterActivity.ValueValidation() {
            public Boolean validate(Object value, HashMap<String, Object> map) {
                return Validator.validateString((String) value);
            }
        }, "firstname"));
        fieldsId.add(new Quad(R.id.lastname_input, new ValueGetter() {
            public Object get(View _view) {
                //return "toto";
                return getFromString(_view);
            }
        }, new RegisterActivity.ValueValidation() {
            public Boolean validate(Object value, HashMap<String, Object> map) {
                return Validator.validateString((String) value);
            }
        }, "lastname"));
        fieldsId.add(new Quad(R.id.password_input, new ValueGetter() {
            public Object get(View _view) {
                return getFromString(_view);
            }
        }, new RegisterActivity.ValueValidation() {
            public Boolean validate(Object value, HashMap<String, Object> map) {
                return value.toString().length() == 0 || Validator.validateString((String) value);
            }
        }, "password"));
        fieldsId.add(new Quad(R.id.conf_password_input, new ValueGetter() {
            public Object get(View _view) {
                return getFromString(_view);
            }
        }, new RegisterActivity.ValueValidation() {
            public Boolean validate(Object value, HashMap<String, Object> map) {
                return value.toString().length() == 0 || Validator.validateString((String) value);
            }
        }, "password_confirmation"));
        fieldsId.add(new Quad(R.id.location_input, new ValueGetter() {
            public Object get(View _view) {
                //return "Paris, France";
                return placeAddress;
            }
        }, new ValueValidation() {
            public Boolean validate(Object value, HashMap<String, Object> map) {
                return Validator.validateString((String) value);
            }
        }, "location"));
        fieldsId.add(new Quad(null, new ValueGetter() {
            public Object get(View _view) {
                Calendar cal = Calendar.getInstance();

                cal.setTime(birthdate);
                //return 1990;
                return cal.get(Calendar.YEAR);
            }
        }, new RegisterActivity.ValueValidation() {
            public Boolean validate(Object value, HashMap<String, Object> map) {
                return true;
            }
        }, "bdyear"));
        fieldsId.add(new Quad(null, new ValueGetter() {
            public Object get(View _view) {
                Calendar cal = Calendar.getInstance();

                cal.setTime(birthdate);
                //return 2;
                return cal.get(Calendar.MONTH) + 1;
            }
        }, new RegisterActivity.ValueValidation() {
            public Boolean validate(Object value, HashMap<String, Object> map) {
                return true;
            }
        }, "bdmonth"));
        fieldsId.add(new Quad(null, new ValueGetter() {
            public Object get(View _view) {
                Calendar cal = Calendar.getInstance();

                cal.setTime(birthdate);
                //return 1;
                return cal.get(Calendar.DAY_OF_MONTH);
            }
        }, new RegisterActivity.ValueValidation() {
            public Boolean validate(Object value, HashMap<String, Object> map) {
                return true;
            }
        }, "bdday"));
        fieldsId.add(new Quad(R.id.gender_spinner, new ValueGetter() {
            public Object get(View _view) {
                //return 1;
                return getFromGender(_view);
            }
        }, new RegisterActivity.ValueValidation() {
            public Boolean validate(Object value, HashMap<String, Object> map) {
                return true;
            }
        }, "gender"));
        fieldsId.add(new Quad(R.id.chat_visibility_input, new ValueGetter() {
            public Object get(View _view) {
                //return 1;
                return getFromBoolean(_view);
            }
        }, new RegisterActivity.ValueValidation() {
            public Boolean validate(Object value, HashMap<String, Object> map) {
                return true;
            }
        }, "open_chat_disabled"));

        if ((obj = validateFields(fieldsId)) == null) {
            System.out.println("Error validating form");
        }
        else {
            System.out.println("I SEND REQUEST");
            System.out.println(obj);
            Request req = (new Request()
            {
                @Override
                public void success(JSONObject data)
                {
                    System.out.println("Hello");
                }

                @Override
                public void fail(String error)
                {
                    System.out.println("Dans le fail");
                    System.out.println(error);
                }
            });

            req.send(this, "update", "POST", null, obj);
        }

    }

    private void updateLayoutFromUser(JSONObject user)
    {
        System.out.println(user);

        try {
            ((EditText) findViewById(R.id.firstname_input)).setText(user.getString("firstname"));
            ((EditText) findViewById(R.id.lastname_input)).setText(user.getString("lastname"));
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                birthdate = format.parse(user.getString("birthdate"));
                DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
                ((TextView) findViewById(R.id.birthdate_input)).setText(df.format(birthdate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ((Switch) findViewById(R.id.chat_visibility_input)).setChecked(user.getInt("open_chat_disabled") == 1);

            ((TextView)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setText(user.getString("location"));
            placeAddress = user.getString("location");
            autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button).setVisibility(View.GONE);
            ((TextView)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(16.0f);
            ((TextView) findViewById(R.id.username_container)).setText(user.getString("name"));
            ((Spinner) findViewById(R.id.gender_spinner)).setSelection(user.getInt("gender"));
            findViewById(R.id.avatar_container).setClipToOutline(true);
            RemoteImageLoader.load((ImageView)findViewById(R.id.avatar_container), user.getString("avatar"));
        } catch (JSONException e) {}
    }

    private void getUser() {
        Request req = new Request() {
            public void success(JSONObject result) {
                try {
                    updateLayoutFromUser(result.getJSONObject("user"));
                } catch (JSONException e) {}
            }

            public void fail(String res) {
                System.out.println("Ca foire " + res);
            }
        };

        req.send(this, "me", "GET", null, null);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_back);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_profile);
        initToolbar();
        getUser();
    }
}
