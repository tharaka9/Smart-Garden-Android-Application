package com.example.smartgarden.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartgarden.R;
import com.example.smartgarden.adapters.AdapterNotification;
import com.example.smartgarden.models.ModelNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment {

    //recyclerview
    RecyclerView notificationsRv;

    private FirebaseAuth firebaseAuth;

    private ArrayList<ModelNotification> notificationsList;

    private AdapterNotification adapterNotification;

    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notifications, container, false);

        //init recyclerview
        notificationsRv = view.findViewById(R.id.notificationsRv);

        firebaseAuth = FirebaseAuth.getInstance();

        getAllNotifications();

        return view;
    }

    private void getAllNotifications() {
        notificationsList = new ArrayList<>();
        //Database > currentUser > Notifications > all notifications list
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Notifications")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        notificationsList.clear();
                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            //get data
                            ModelNotification model = ds.getValue(ModelNotification.class);

                            //add to list
                            notificationsList.add(model);
                        }

                        //adapter
                        adapterNotification = new AdapterNotification(getActivity(), notificationsList);
                        //set to recyclerview
                        notificationsRv.setAdapter(adapterNotification);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
