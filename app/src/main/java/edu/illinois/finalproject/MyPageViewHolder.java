package edu.illinois.finalproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import static edu.illinois.finalproject.MainActivity.TAG;

/**
 * Created by haonanwang on 12/6/17.
 */

/**
 * this is the viewHolder for MePageFragment. Contents of the item in recyclerView will be initialized
 */
class MyPageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private View mView;
    private Context mContext;

    public MyPageViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindChatMessage(PunchMessage punchMessage) {
        TextView text = mView.findViewById(R.id.mypage_message);
        TextView time = mView.findViewById(R.id.mypage_time);
        TextView plan = mView.findViewById(R.id.planname);

        Log.d(TAG, "bindChatMessage: "+punchMessage.getMessageUser());
        text.setText(punchMessage.getMessageText());
        plan.setText("Plan: " + punchMessage.getPlanname());

        // Format the date before showing it
        time.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", punchMessage.getMessageTime()));
    }

    @Override
    public void onClick(View v) {

    }
}

