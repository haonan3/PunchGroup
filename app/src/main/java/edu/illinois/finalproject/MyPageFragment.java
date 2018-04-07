package edu.illinois.finalproject;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static com.firebase.ui.auth.ui.email.RegisterEmailFragment.TAG;

/**
 * Created by haonanwang on 12/5/17.
 */

/**
 * In MypageFragment, the messages have been sent by the current will be shown
 */
public class MyPageFragment extends Fragment {
    public static final String BUNDLE_TITLE = "title";
    private String mTitle = "DefaultValue";
    private View thisview;
    private RecyclerView rvTabView;
    private MyPageFragment main;
    private Context context;
    private FirebaseRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisview = inflater.inflate(R.layout.mypage_layout, container, false);
        main = this;
        context = this.context;
        displayMyPunchMessages();
        return thisview;
    }

    /**
     * displayMyPunchMessages will query current user's messages from this user's directory
     * No matter whether messages are belong to the current plan, they will be shown.
     */
    private void displayMyPunchMessages() {
        RecyclerView catRecycler = (RecyclerView) thisview.findViewById(R.id.myPageRecyclerView);
        Query query = FirebaseDatabase.getInstance()
                .getReference(getCurrentUser())
                .child("record")
                .limitToLast(50);

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
        query.addChildEventListener(childEventListener);


        adapter = new FirebaseRecyclerAdapter<PunchMessage, MyPageViewHolder>(options) {

            @Override
            public MyPageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mypage_item, parent, false);

                return new MyPageViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(MyPageViewHolder holder, int position, PunchMessage model) {
                // Bind the Chat object to the ChatHolder
                // ...
                holder.bindChatMessage(model);
            }
        };
        catRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,
                false));
        catRecycler.setAdapter(adapter);
    }

    /**
     * In the onStart() method, all TextViews will be initialized.
     */
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        TextView nameText = (TextView) thisview.findViewById(R.id.name);
        TextView emailAddress = (TextView) thisview.findViewById(R.id.email);
        nameText.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        emailAddress.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /**
     * This method is to help MainActivity pass information to TextFragment.
     * @return a new fragment
     */
    public static MyPageFragment newInstance() {
        MyPageFragment fragment = new MyPageFragment();
        return fragment;
    }

    /**
     * this help method will return the name of current user
     * @return
     */
    private String getCurrentUser() {
        String user;
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Log.d(TAG, "getCurrentUser: "+"NoCurrentUser");
            user = "NoCurrentUser";
        }
        else{
            user= FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            Log.d(TAG, "getCurrentUser: "+user);
        }
        return user;
    }
}
