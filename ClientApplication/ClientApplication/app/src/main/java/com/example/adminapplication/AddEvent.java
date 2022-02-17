package com.example.adminapplication;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AddEvent extends AppCompatActivity {
    private Tool TOOL;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private LinearLayout defColor, addPeople, addAttachments;
    private EditText title, location, description;
    private TextView fromDate, toDate, fromTime, toTime, defaultColorText;
    private EditText fromDateInput, toDateInput, fromTimeInput, toTimeInput, addDefaultColorInput;
    private CardView aaDefaultColorPreview,saveBtn,backBtn;
    private String insertEventURL = "https://e2019cc107grouptwo.000webhostapp.com/API/insertEvent.php";
    private RequestQueue reqQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        TOOL = new Tool(this);
        backBtn = findViewById(R.id.aaBackBtn);
        saveBtn = findViewById(R.id.aaSaveBtn);
        fromDate = findViewById(R.id.aaFromDate);
        toDate = findViewById(R.id.aaToDate);
        fromTime = findViewById(R.id.aaFromTime);
        toTime = findViewById(R.id.aaToTime);
        fromDateInput = findViewById(R.id.aaFromDateInput);
        toDateInput = findViewById(R.id.aaToDateInput);
        fromTimeInput = findViewById(R.id.aafromTimeInput);
        toTimeInput = findViewById(R.id.aaToTimeInput);
        defaultColorText = findViewById(R.id.aaDefaultColorText);
        addDefaultColorInput = findViewById(R.id.aaAddDefaultColorInput);
        aaDefaultColorPreview = findViewById(R.id.aaDefaultColorPreview);
        location = findViewById(R.id.aaAddLocation);
        title = findViewById(R.id.aaaTitle);
        description = findViewById(R.id.aaDescription);
        defColor = findViewById(R.id.aaAddDefaultColor);
        addPeople = findViewById(R.id.aaAddPeople);
        addAttachments = findViewById(R.id.aaAddAttachment);
        reqQueue = Volley.newRequestQueue(getApplicationContext());

        clickCalendarUpdateListener(fromDate, fromDateInput);
        clickCalendarUpdateListener(toDate, toDateInput);
        clickTimeUpdateListener(fromTime, fromTimeInput);
        clickTimeUpdateListener(toTime, toTimeInput);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewEvent();
            }
        });

        addAttachments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadAttachments(view);
            }
        });

        defColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addDefaultColorInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        defaultColorText.setText(addDefaultColorInput.getText().toString());
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
                createColorRingPicker(addDefaultColorInput, aaDefaultColorPreview);
            }
        });
    }



    public void clickCalendarUpdateListener(TextView text, EditText input) {
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

                input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        try {
                            Date date = formatter.parse(input.getText().toString());
                            SimpleDateFormat formatr = new SimpleDateFormat("MMMM dd yyyy");
                            text.setText(formatr.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });

                createCalendarPicker(input);
            }
        });
    }

    public void clickTimeUpdateListener(TextView text, EditText input) {
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat formatter = new SimpleDateFormat("h:m a");
                input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String[] val = input.getText().toString().split(":");
                        Date d = new Date();
                        Date date = new Date(d.getYear(), d.getMonth(),d.getDate(), Integer.parseInt(val[0]),Integer.parseInt(val[1]));

                        text.setText(formatter.format(date));
                    }
                });

                createTimePicker(input);
            }
        });
    }


    public void createCalendarPicker(EditText dateInput) {
        dialogBuilder = new AlertDialog.Builder(this);

        final View view = getLayoutInflater().inflate(R.layout.layout_calendar_picker, null);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        DatePicker datePicker = (DatePicker) view.findViewById(R.id.calendarPicker);
        String current = dateInput.getText().toString();

        if (current.length() != 0) {
            String[] s = current.split("/");
            datePicker.updateDate(Integer.parseInt(s[2]), Integer.parseInt(s[0]) - 1, Integer.parseInt(s[1]));
        }

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();

                String dateVal = "" + month + "/" + day + "/" + year;

                dateInput.setText(dateVal);
            }
        });
    }

    public void createTimePicker(EditText timeInput) {
        dialogBuilder = new AlertDialog.Builder(this);

        final View view = getLayoutInflater().inflate(R.layout.layout_time_picker, null);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        TimePicker timePicker = view.findViewById(R.id.timePicker);
        String current = timeInput.getText().toString();

        if (current.length() != 0) {
            String[] t = current.split(":");
            timePicker.setCurrentHour(Integer.parseInt(t[0]));
            timePicker.setCurrentMinute(Integer.parseInt(t[1]));
        }

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                String timeVal = "" + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
                timeInput.setText(timeVal);
            }
        });
    }

    public void createColorRingPicker(EditText input, CardView colorPreview) {
        dialogBuilder = new AlertDialog.Builder(this);

        final View view = getLayoutInflater().inflate(R.layout.layout_color_ring_picker, null);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        String[] ringNames = getResources().getStringArray(R.array.ring_color_name);
        String[] ringColors = getResources().getStringArray(R.array.ring_color);
        int[] ringContainer = {R.id.aeRingContainer1,R.id.aeRingContainer2,R.id.aeRingContainer3,R.id.aeRingContainer4,R.id.aeRingContainer5,R.id.aeRingContainer6,R.id.aeRingContainer7,R.id.aeRingContainer8,R.id.aeRingContainer9,R.id.aeRingContainer10,R.id.aeRingContainer11,R.id.aeRingContainer12};
        int[] rings = {R.id.aeRing1,R.id.aeRing2,R.id.aeRing3,R.id.aeRing4,R.id.aeRing5,R.id.aeRing6,R.id.aeRing7,R.id.aeRing8,R.id.aeRing9,R.id.aeRing10,R.id.aeRing11,R.id.aeRing12};

        for (int i = 0; i < rings.length; i++) {
            ConstraintLayout constraintLayout = view.findViewById(rings[i]);
            constraintLayout.getBackground().setColorFilter(Color.parseColor(ringColors[i]), PorterDuff.Mode.SRC_IN);
        }

        for (int i = 0; i < ringContainer.length; i++) {
            int finalI = i;
            LinearLayout layout = view.findViewById(ringContainer[i]);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    aaDefaultColorPreview.setCardBackgroundColor(   Color.parseColor(ringColors[finalI]));
                    input.setText(ringNames[finalI]);
                    dialog.hide();
                }
            });
        }
    }

    private void addPeople() {

    }

    private void uploadAttachments(View view) {
        TOOL.ToastText("Currently Unavailable");
    }

    private void createNewEvent() {
        SessionManagement sessionManagement = new SessionManagement(this);
        Admin admin = sessionManagement.getSession();
        Bundle eventInfo = new Bundle();
        String _title = TOOL.getValueFromInput(title);
        String _description = TOOL.getValueFromInput(description);
        String _fromDate = TOOL.getValueFromInput(fromDateInput);
        String _toDate = TOOL.getValueFromInput(toDateInput);
        String _fromTime = TOOL.getValueFromInput(fromTimeInput);
        String _toTime = TOOL.getValueFromInput(toTimeInput);
        String _location = TOOL.getValueFromInput(location);
        String _color = TOOL.getValueFromInput(addDefaultColorInput);
        String[] _peoples = {};

        eventInfo.putString("title", _title);
        eventInfo.putString("description", _description);
        eventInfo.putString("fromDate", _fromDate);
        eventInfo.putString("toDate", _toDate);
        eventInfo.putString("fromTime", _fromTime);
        eventInfo.putString("toTime", _toTime);
        eventInfo.putString("location", _location);
        eventInfo.putString("color", _color);
        eventInfo.putStringArray("peoples", _peoples);

        Event event = new Event(admin, eventInfo);

        insertNewEvent(event);
    }

    private void insertNewEvent(Event event) {
        HashMap<String, String> params = new HashMap<String, String>();
        Bundle eventInfo = event.getBundle();

        params.put("adminID", eventInfo.getString("adminID"));
        params.put("adminEmail", eventInfo.getString("adminEmail"));
        params.put("title", eventInfo.getString("title"));
        params.put("description", eventInfo.getString("description"));
        params.put("fromDate", eventInfo.getString("fromDate"));
        params.put("toDate", eventInfo.getString("toDate"));
        params.put("fromTime", eventInfo.getString("fromTime"));
        params.put("toTime", eventInfo.getString("toTime"));
        params.put("location", eventInfo.getString("location"));
        params.put("color", eventInfo.getString("color"));
        params.put("peoples", "");

        AlertDialog dialog = previewProgressLoader();

        TOOL.Ajax(insertEventURL, Request.Method.POST, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getBoolean("success")) {
                        dialog.hide();
                        TOOL.ToastText("Event Successfully added!");
                        TOOL.startNewActivity(MainActivity.class);
                    } else {
                        TOOL.ToastText(obj.getString("message"));
                    }

                } catch (Exception error) {
                    TOOL.ToastText(error.getMessage());
                    enableForm(true);
//                    verifyForm();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TOOL.ToastText(error.getMessage());
                enableForm(true);
            }
        });
    }

    private void  enableForm(Boolean bool) {
        title.setEnabled(bool);
        description.setEnabled(bool);
        fromDate.setEnabled(bool);
        toDate.setEnabled(bool);
        fromTime.setEnabled(bool);
        toTime.setEnabled(bool);
        location.setEnabled(bool);
        defaultColorText.setEnabled(bool);
    }

    private AlertDialog previewProgressLoader() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.layout_loading, null);
        dialogBuilder.setView(view);
        AlertDialog dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        return dialog;
    }
}

