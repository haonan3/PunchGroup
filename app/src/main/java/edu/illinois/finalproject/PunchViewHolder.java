package edu.illinois.finalproject;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import static android.content.ContentValues.TAG;

/**
 * Created by haonanwang on 11/25/17.
 */

public class PunchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private View mView;
    private Context mContext;
    private int flag;
    //String key;
    //int thumbUpNumber;
    //String whoGiveThumbUp;
    //PunchMessage messageInUser;

    public PunchViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
        flag = 0;
    }

    public void bindChatMessage(final PunchMessage punchMessage) {
        TextView text = mView.findViewById(R.id.message_text);
        TextView user = mView.findViewById(R.id.message_user);
        TextView time = mView.findViewById(R.id.message_time);
        TextView plan = mView.findViewById(R.id.message_plan);
        TextView location = mView.findViewById(R.id.message_location);
        //final FloatingActionButton thumbUpButton = mView.findViewById(R.id.thumbUpButton);

        // Set their text
        text.setText(punchMessage.getMessageText());
        user.setText(punchMessage.getMessageUser());
        plan.setText("Plan: " + punchMessage.getPlanname());
        location.setText(punchMessage.getCurrentPosition());

        //this function has some bugs. But I don't want to delete them.
//        thumbUpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                key = punchMessage.getMessageKey();
//                Log.d(TAG, "onClick: "+key);
//                DatabaseReference addKeyToRef= FirebaseDatabase.getInstance().getReference()
//                        .child(getCurrentUser()+"/record");
//
//                addKeyToRef.child(key).child("trash").setValue("0");
//
//
//
//                addKeyToRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot snapshot) {
//                        for(DataSnapshot data : snapshot.getChildren()) {
//                            messageInUser = data.getValue(PunchMessage.class);
//                            if(messageInUser.getMessageKey() == key){
//                                thumbUpNumber = messageInUser.getThumbUpNumber();
//                                whoGiveThumbUp = messageInUser.getWhoGiveThumbUp();
//                                break;
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//
//                });
//
//
//                    if(flag == 0){
//                        thumbUpNumber += 1;
//                        addKeyToRef.child(key).child("thumbUpNumber").setValue(thumbUpNumber);
//                        flag = 1;
//
//                        Toast.makeText(mContext,
//                                "Thumb Up!",
//                                Toast.LENGTH_SHORT)
//                                .show();
//                    }else{
//                        thumbUpNumber -= 1;
//                        addKeyToRef.child(key).child("thumbUpNumber").setValue(thumbUpNumber);
//                        flag = 0;
//
//                        Toast.makeText(mContext,
//                                "Undo",
//                                Toast.LENGTH_SHORT)
//                                .show();
//
//                    }
//
//            }
//        });

        // Format the date before showing it
        time.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", punchMessage.getMessageTime()));
    }

    @Override
    public void onClick(View v) {
    }

//    private String getCurrentUser() {
//        String user;
//        if(FirebaseAuth.getInstance().getCurrentUser() == null){
//            user = "NoCurrentUser";
//        }
//        else{
//            user= FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
//        }
//        return user;
//    }

}


