package edu.illinois.finalproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

/**
 * Created by haonanwang on 11/25/17.
 */

/**
 * In the PlanFragment, user can create or give a plan.
 * Users can choose start and end date using calender dialog.
 */
public class PlanFragment extends Fragment {
    public static final String BUNDLE_TITLE = "title";
    private String mTitle = "DefaultValue";
    private View thisview;
    private Button saveButton;
    private TextView startDate;
    private TextView endDate;
    private Calendar cal;
    private int year, month, day;
    private String start;
    private String end;
    private Context context;
    private EditText planName;
    boolean flag = true;
    private long num;
    private Plan planInDatabase;
    final Plan plan = new Plan();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisview = inflater.inflate(R.layout.planfragment_layout, container, false);
        context = this.getActivity();

        getDate();

        startDate = (TextView) thisview.findViewById(R.id.start_Date);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.start_Date:
                        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                                //show the selected date on TextView.
                                //Because the default month range is from 0 to 11
                                //we need to use ++month is this place
                                startDate.setText(year + "-" + (++month) + "-" + day);
                                //Because we already use ++month, the month number is correct.
                                //We don't need to use ++month in this place again.
                                start = String.valueOf(year) + "-" + String.valueOf(month) + "-"
                                        + String.valueOf(day);
                                plan.setStart(start);
                            }
                        };

                        //the last three parameters is the default date of calender dialog
                        //month range is 0-11
                        DatePickerDialog dialog = new DatePickerDialog(context,
                                DatePickerDialog.THEME_DEVICE_DEFAULT_DARK,
                                listener, year, month, day);
                        dialog.show();
                        break;

                    default:
                        break;
                }
            }
        });

        endDate = (TextView) thisview.findViewById(R.id.end_Date);
        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.end_Date:
                        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker arg0, int year, int month, int day) {

                                endDate.setText(year + "-" + (++month) + "-" + day);
                                end = String.valueOf(year) + "-" + String.valueOf(month) +
                                        "-" + String.valueOf(day);
                                plan.setEnd(end);
                            }
                        };

                        DatePickerDialog dialog = new DatePickerDialog(context,
                                DatePickerDialog.THEME_DEVICE_DEFAULT_DARK,
                                listener, year, month, day);
                        dialog.show();
                        break;

                    default:
                        break;
                }
            }
        });


        saveButton = (Button) thisview.findViewById(R.id.save_button);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference(getCurrentUser()+"/plan");

        myRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                num = dataSnapshot.getChildrenCount();
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    planInDatabase = data.getValue(Plan.class);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        planName  = thisview.findViewById(R.id.plan_name);


        /**
         * a clickListener will be added to save button.
         * There is a flag for this button to monitor the state of this button.
         * there are two state for this button: SAVE and GIVE UP.
         * For example:
         * When user click this save button first time, this button will become "GIVE UP" and
         * textView will be locked, and the cursor will disappear
         * When the user click this save button second time, this button will become "SAVE" and
         * textView will be clean, and the cursor will appear
         */
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (flag==true)
                {
                   plan.setPlanName(planName.getText().toString());
                    myRef.push().setValue(plan);
                    planName.setInputType(InputType.TYPE_NULL);
                    planName.setCursorVisible(false);
                    startDate.setEnabled(false);
                    endDate.setEnabled(false);
                    saveButton.setText("GIVE UP");
                    flag = false;

                }else
                {

                    Toast.makeText(context,
                            "You can create a new plan now",
                            Toast.LENGTH_SHORT)
                            .show();
                    planName.setInputType(InputType.TYPE_CLASS_TEXT);
                    planName.setCursorVisible(true);
                    startDate.setEnabled(true);
                    endDate.setEnabled(true);
                    saveButton.setText("SAVE");
                    startDate.setText("start date");
                    endDate.setText("end date");
                    planName.setText("");
                    flag = true;
                }
            }
        });
        return thisview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        planName = thisview.findViewById(R.id.plan_name);
        initData();
    }


    @Override
    public void onStart() {
        super.onStart();

        planName  = thisview.findViewById(R.id.plan_name);;

        /*
        Check the plan number. If there is a plan, the initial state of the button will be "GIVE UP"
         */
        if(num > 0){
            planName.setText(planInDatabase.getPlanName(),TextView.BufferType.EDITABLE);
            startDate.setText(planInDatabase.getStart());
            endDate.setText(planInDatabase.getEnd());
            flag = false;
            planName.setInputType(InputType.TYPE_NULL);
            startDate.setEnabled(false);
            endDate.setEnabled(false);
            saveButton.setText("GIVE UP");
        }
    }



    private String getCurrentUser() {
        String user;
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            user = "NoCurrentUser";
        }
        else{
            user= FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }
        return user;
    }


    /**
     * this is a helper method to get current date
     */
    private void getDate() {
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        Log.i("wxy", "year" + year);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
    }


    public static PlanFragment newInstance() {
        PlanFragment fragment = new PlanFragment();
        return fragment;
    }


    private void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTitle = arguments.getString(BUNDLE_TITLE);
        }
    }

}
