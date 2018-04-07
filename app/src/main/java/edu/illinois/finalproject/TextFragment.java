package edu.illinois.finalproject;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by haonanwang on 11/12/17.
 */


/**
 * TextFragment will query information from firebase and display data to recyclerView
 */
public class TextFragment extends Fragment {
    public static final String BUNDLE_TITLE = "title";
    private String mTitle = "DefaultValue";
    private View thisview;
    private RecyclerView rvTabView;
    private TextFragment main;
    private Context context;
    private FirebaseRecyclerAdapter adapter;


    /**
     * the title will be passed in through bundle.
     * the word size ,location and margin of the TextView will be set.
     * Each TextView will have a listener
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisview = inflater.inflate(R.layout.mainpagefragment_layout, container, false);
        main = this;
        context = this.context;

        displayChatMessages();
        return thisview;
    }


    /**
     * First, Query message from database.
     * Then, put the query to the FireBaseRecycler adapter
     */
    private void displayChatMessages() {
        RecyclerView punchRecycler = (RecyclerView) thisview.findViewById(R.id.recyclerView);
        //query message from firebase
        Query query = FirebaseDatabase.getInstance()
                .getReference().child("newMessage").limitToLast(100);

        FirebaseRecyclerOptions<PunchMessage> options =
                new FirebaseRecyclerOptions.Builder<PunchMessage>()
                        .setQuery(query, PunchMessage.class)
                        .build();

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        };

        //add listener to monitor data change
        query.addChildEventListener(childEventListener);

        adapter = new FirebaseRecyclerAdapter<PunchMessage, PunchViewHolder>(options) {

            @Override
            public PunchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item, parent, false);

                return new PunchViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(PunchViewHolder holder, int position, PunchMessage model) {
                // Bind the Chat object to the ChatHolder
                holder.bindChatMessage(model);
            }
        };
        punchRecycler.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        punchRecycler.setAdapter(adapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * This method is to help MainActivity pass information to TextFragment.
     * @return a new fragment
     */
    public static TextFragment newInstance() {
        TextFragment fragment = new TextFragment();
        return fragment;
    }
}
