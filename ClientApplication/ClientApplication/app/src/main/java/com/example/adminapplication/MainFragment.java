package com.example.adminapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainFragment extends Fragment {
    private SessionManagement sessionManagement;
    private Admin admin;
    private Tool TOOL;


    public MainFragment(SessionManagement sess, Context ctx) {
        this.sessionManagement = sess;
        this.admin = sess.getSession();
         TOOL = new Tool(ctx);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle fragmentContents = new Bundle();

        fragmentContents.putInt("Home", R.layout.fragment_main);
        fragmentContents.putInt("Events", R.layout.fragment_events);
        fragmentContents.putInt("Account", R.layout.fragment_settings);

        String title = getArguments().getString("title");
        View view = inflater.inflate(fragmentContents.getInt(title), container, false);

        goToView(title, view);

        return view;
    }

    private void goToView(String title, View view) {
        switch (title) {
            case "Home":
                goToHome(view);
                break;
            case "Events":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    goToEvents(view);
                } else {
                    goToHome(view);
                }
                break;
            case "Account":
                goToAccount(view);
                break;
        }
    }

    private void goToHome(View view) {
        RelativeLayout noDataContainer = view.findViewById(R.id.noDataContainer);
        ConstraintLayout loadingContainer = view.findViewById(R.id.loadingContainer);
        CardView announcementBtn = view.findViewById(R.id.mainAddAnnouncementBtn);
        RecyclerView recyclerView = view.findViewById(R.id.mainAnnouncementRecycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        List<Announcement> announcements  = new ArrayList<>();
        String getAnnouncementURL = "https://e2019cc107grouptwo.000webhostapp.com/API/getAnnouncements.php";


        announcementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go(AddAnnouncement.class);
            }
        });

        if (TOOL.isInternetConnectionAvailable()) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("id", "" + admin.id);
            params.put("email", admin.email);

            loadingContainer.setVisibility(View.VISIBLE);

            TOOL.Ajax(getAnnouncementURL, Request.Method.POST, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    loadingContainer.setVisibility(View.GONE);
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getBoolean("success")) {
                            JSONArray EVENTS = obj.getJSONArray("object");

                            if (EVENTS.length() != 0) {
                                for (int i =0; i < EVENTS.length(); i++) {
                                    Announcement announcement = TOOL.createAnnouncementFromJSONOBJ(EVENTS.getJSONObject(i));
                                    announcements.add(announcement);
                                }

                                RVAdapterAnnouncement adapter = new RVAdapterAnnouncement(announcements, view.getContext());
                                recyclerView.setLayoutManager(linearLayoutManager);
                                recyclerView.setAdapter(adapter);
                            } else {
                                noDataContainer.setVisibility(View.VISIBLE);
                            }
                        } else {
                            noDataContainer.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception error) {
                        TOOL.ToastText(error.getMessage());
                    }
                }
            });
        } else {
            TOOL.ToastText("Error, Please check your internet connection!");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void goToEvents(View view) {
        RelativeLayout noDataContainer = view.findViewById(R.id.noDataContainer);
        ConstraintLayout loadingContainerForEvents = view.findViewById(R.id.loadingContainerForEvents);
        CardView mainAddEventBtn = view.findViewById(R.id.mainAddEventBtn);
        RecyclerView recyclerView = view.findViewById(R.id.eventRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        List<Event> events = new ArrayList<>();
        String getEventsURL = "https://e2019cc107grouptwo.000webhostapp.com/API/getEvents.php";

        mainAddEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go(AddEvent.class);
            }
        });

        if (TOOL.isInternetConnectionAvailable()) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("last_event_id", "0");
            loadingContainerForEvents.setVisibility(View.VISIBLE);

            TOOL.Ajax(getEventsURL, Request.Method.POST, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    loadingContainerForEvents.setVisibility(View.GONE);
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getBoolean("success")) {
                            JSONArray EVENTS = obj.getJSONArray("object");

                            if (EVENTS.length() != 0) {
                                for (int i =0; i < EVENTS.length(); i++) {
                                    Event event = TOOL.createEventFromJSONOBJ(EVENTS.getJSONObject(i));
                                    events.add(event);
                                }

                                RVAdapterEvents adapter = new RVAdapterEvents(events, view.getContext());
                                recyclerView.setLayoutManager(linearLayoutManager);
                                recyclerView.setAdapter(adapter);
                            } else {
                                noDataContainer.setVisibility(View.VISIBLE);
                            }
                        } else {
                            noDataContainer.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception error) {
                        TOOL.ToastText(error.getMessage());
                    }
                }
            });
        }
    }

    private void goToAccount(View view) {
        TextView fullname = view.findViewById(R.id.fstUserName);
        ConstraintLayout logoutCardCV = view.findViewById(R.id.fstLogoutCard);
        ConstraintLayout editProfileCV = view.findViewById(R.id.fstEditProfileCard);
        ConstraintLayout reportTechCV = view.findViewById(R.id.fstReportTechCard);
        ConstraintLayout helpCV = view.findViewById(R.id.fstHelpCard);
        ConstraintLayout legalCV = view.findViewById(R.id.fstLegalPolCard);

        fullname.setText(admin.getFirstname() + " " + admin.getLastname());

        reportTechCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go(ReportTechnicalProblem.class);
            }
        });

        editProfileCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go(EditProfile.class);
            }
        });

        logoutCardCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goLogout();
            }
        });

        helpCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go(Help.class);
            }
        });

        legalCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go(LegalPolicies.class);
            }
        });
    }

    private void goLogout() {
        sessionManagement.removeSession();
        Intent intent = new Intent(getActivity(), SignIn.class);
        startActivity(intent);
    }

    private void go(Class classPackage) {
        Intent intent = new Intent(getActivity(), classPackage);
        startActivity(intent);
    }

}