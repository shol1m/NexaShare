package com.example.nexashare;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.nexashare.Adapter.Ride;
import com.example.nexashare.Adapter.RideAdapter;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {
    private ImageView notification;
    private CardView singles,groups;

    public Home() {
        // Required empty public constructor
    }



    private OnCardClickListener onCardClickListener;

    public interface OnCardClickListener {
        void onCard1Clicked();
        void onCard2Clicked();
        // Add more methods for other card clicks if needed
    }

    public void setOnCardClickListener(OnCardClickListener listener) {
        this.onCardClickListener = listener;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnCardClickListener) {
            onCardClickListener = (OnCardClickListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnCardClickListener");
        }
    }

    // In your card view click listeners, call the appropriate method of the interface
    


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        notification= view.findViewById(R.id.notification);
        singles= view.findViewById(R.id.singles);
        groups= view.findViewById(R.id.groups);

//        notification.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getContext(), Notifications.class));
//            }
//        });

        return view;
    }

    private void setupCardViewClickListeners() {
//        singles.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onCardClickListener.onCard1Clicked();
//            }
//        });
//
//        groups.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onCardClickListener.onCard2Clicked();
//            }
//        });
        singles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCardClickListener.onCard1Clicked();
                if (onCardClickListener != null) {
                    onCardClickListener.onCard1Clicked();
                }
            }
        });
        groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCardClickListener != null) {
                    onCardClickListener.onCard2Clicked();
                }
            }
        });
    }
}