package com.example.clientapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class EditProfile extends AppCompatActivity {
    private SessionManagement sessionManagement;
    private User user;
    private ImageView avatar;
    private TextView uname;
    private EditText email, firstname, lastname, birthdate;
    private RadioButton maleGender, femaleGender;
    private Button changePassword, update;
    private Bundle defaultValues;
    private String editURL = "https://e2019cc107grouptwo.000webhostapp.com/API/updateProfile.php";

    Tool TOOL = new Tool(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Profile");
        actionBar.setDisplayHomeAsUpEnabled(true);

        sessionManagement = new SessionManagement(this);
        user = sessionManagement.getSession();

        avatar = findViewById(R.id.epAvatar);
        uname = findViewById(R.id.epUname);
        email = findViewById(R.id.epEmail);
        firstname = findViewById(R.id.aaTitle);
        lastname = findViewById(R.id.epLname);
        birthdate =  findViewById(R.id.epBdate);
        maleGender = findViewById(R.id.epMale);
        femaleGender = findViewById(R.id.csFemale);
        changePassword = findViewById(R.id.epChangePass);
        update = findViewById(R.id.epUpdate);
        defaultValues = user.getBundleInformation();

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                disableEnableButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                disableEnableButton();
            }
        };

        email.addTextChangedListener(watcher);
        firstname.addTextChangedListener(watcher);
        lastname.addTextChangedListener(watcher);
        birthdate.addTextChangedListener(watcher);

        maleGender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                disableEnableButton();
            }
        });

        femaleGender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                disableEnableButton();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goEditProfile(getUpdatedValues(false));
            }
        });

        updateValues();
        disableEnableButton();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void disableEnableButton() {
        boolean status = true;
        Bundle updated = getUpdatedValues(true);
        String _email = TOOL.getValueFromInput(email);
        String _firstname = TOOL.getValueFromInput(firstname);
        String _lastname = TOOL.getValueFromInput(lastname);
        String _birthdate = TOOL.getValueFromInput(birthdate);
        Boolean isMale = maleGender.isChecked();
        Boolean isFemale = femaleGender.isChecked();

        if (_email.length() == 0) {
            status = false;
        }

        if (_firstname.length() == 0) {
            status = false;
        }

        if (_lastname.length() == 0) {
            status = false;
        }

        if (_birthdate.length() == 0) {
            status = false;
        }

        if (!TOOL.validateJavaDate(_birthdate)) {
            status = false;
        }

        if(!isMale && !isFemale) {
            status = false;
        }

        if (TOOL.equalBundles(defaultValues, updated)) {
            status = false;
        }

        update.setEnabled(status);
    }

    private Bundle getUpdatedValues(Boolean theRest) {
        Bundle updated = theRest ? user.getBundleInformation() : new Bundle();
        String _email = TOOL.getValueFromInput(email);
        String _firstname = TOOL.getValueFromInput(firstname);
        String _lastname = TOOL.getValueFromInput(lastname);
        String _birthdate = TOOL.getValueFromInput(birthdate);
        Boolean isMale = maleGender.isChecked();
        Boolean isFemale = femaleGender.isChecked();

        updated.putString("email", _email);
        updated.putString("firstname", _firstname);
        updated.putString("lastname", _lastname);
        updated.putString("birthdate", _birthdate);
        updated.putString("gender", isMale ? "Male" : "Female");

        return updated;
    }

    private void updateValues() {
        RadioButton button = user.getGender().equals("Male") ? maleGender : femaleGender;
        uname.setText(user.getFirstname() + ", " + user.getLastname());
        email.setText(user.getEmail());
        firstname.setText(user.getFirstname());
        lastname.setText(user.getLastname());
        birthdate.setText(user.getBirthdate());
        button.setChecked(true);
    }

    private void goEditProfile(Bundle values) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", "" + user.getId());
        Set<String> ks = values.keySet();
        Iterator<String> iterator = ks.iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            params.put(key, values.getString(key));
        }

        if (TOOL.isInternetConnectionAvailable()) {
            TOOL.Ajax(editURL, Request.Method.POST, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);

                        TOOL.ToastText(obj.getString("message"));

                        if (obj.getBoolean("success")) {
                            sessionManagement.removeSession();
                            TOOL.createSession(obj.getJSONObject("object"));
                        }

                    } catch (Exception e) {
                        TOOL.ToastText(e.getMessage().toString());
                    }
                }
            });
        }
    }
}