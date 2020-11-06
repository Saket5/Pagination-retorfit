package com.example.payo;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import org.json.JSONArray;
import org.json.JSONException;

import org.json.JSONObject;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment implements jsonAdapter.objectClickListener{

    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar scrollProgressBar;
    ArrayList<JsonObjectAPI.Data> jsonapi;
    jsonAdapter adapter;
    JSONObject jsonObject;
    int page;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkConnection(this);
        nestedScrollView=view.findViewById(R.id.nestedScrollView);
        recyclerView=view.findViewById(R.id.recyclerView);
        scrollProgressBar=view.findViewById(R.id.scrollProgressBar);
        jsonapi=new ArrayList<>();
        adapter = new jsonAdapter(requireActivity(),jsonapi,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);
        page=1;

        getJData(page);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight());

                if(page==1)
                scrollProgressBar.setVisibility(View.VISIBLE);
                page++;
                if(page==2)
                getJData(page);

            }
        });
    }

    private void getJData(int page) {
        String BaseUrl="https://reqres.in";
        Retrofit retrofit=new Retrofit.Builder().baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MainInterface mainInterface=retrofit.create(MainInterface.class);

        Call<JsonObjectAPI> call = mainInterface.doGetUserList(page);
        call.enqueue(new Callback<JsonObjectAPI>() {
            @Override
            public void onResponse(Call<JsonObjectAPI> call, Response<JsonObjectAPI> response) {
                if(response.isSuccessful() && response.body() !=null)
                {
                    scrollProgressBar.setVisibility(View.GONE);
                    JsonObjectAPI json=response.body();
                    jsonapi.addAll(json.data);

                    //Log.e("Retrofit", String.valueOf(response.body()));
                    adapter=new jsonAdapter(requireActivity(),jsonapi,HomeFragment.this);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<JsonObjectAPI> call, Throwable t) {
                call.cancel();
            }


        });
    }

    @Override
    public void onLongClick(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete User");
        builder.setMessage("Are you sure you want to delete user " + jsonapi.get(position).getFirstName().trim() + " " + jsonapi.get(position).getLastName().trim() + "?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.notifyItemRemoved(position);
                jsonapi.remove(position);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();

    }
    private void checkConnection(final Fragment fragment) {
        ConnectivityManager connectivityManager = (ConnectivityManager) fragment.requireActivity().getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(fragment.requireContext());
            builder.setMessage("Please connect to the internet to proceed further")
                    .setCancelable(false)
                    .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fragment.requireActivity().finishAffinity();
                        }
                    })
                    .setNeutralButton("Reload", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkConnection(fragment);
                        }
                    });
            builder.show();
        }
    }
}