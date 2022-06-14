package com.queenieeq.mymoney.notification;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

import com.queenieeq.mymoney.R;

public class Lists extends AppCompatActivity {

    private DatabaseReference uidref, boardref, cardref, due_dateref, descref, name;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private ArrayList<cards> cardlist;
    private cardsadapter cardsadapter;
    private String boardname;
    private final String TITLE = "TITLE";
    private final String DUE = "DUE";
    private final String DESC = "DESC";
    private final String NAME = "NAME";

    Intent intent1 = new Intent(Intent.ACTION_DIAL);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        boardname = "cvk";

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(boardname.toUpperCase());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");


        recyclerView = findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        uidref = FirebaseDatabase.getInstance().getReference().child(user.getUid());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CAMERA},
                    0);
        }



        DatabaseReference rootref = uidref.child(boardname);
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int childerncount = (int) dataSnapshot.getChildrenCount();
                cardlist = new ArrayList<>();
                if (childerncount <= 0) {
                    Toast.makeText(Lists.this, "No cards", Toast.LENGTH_SHORT).show();
                } else {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String due = ds.child(DUE).getValue(String.class);
                        String name = ds.child(NAME).getValue(String.class);
                        String title = ds.child(TITLE).getValue(String.class);
                        String desc = ds.child(DESC).getValue(String.class);
                        cards card = new cards(due, title, desc,name);
                        cardlist.add(card);
                    }
                    cardsadapter = new cardsadapter(cardlist);
                    recyclerView.setAdapter(cardsadapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        findViewById(R.id.add_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!Connection.isInternetAvailable(Lists.this)) {
            Snackbar.make(recyclerView, "No connection", 3000).show();
        }
    }

    //Adapter for recycler view
    public class cardsadapter extends RecyclerView.Adapter<cardsadapter.holder> {
        ArrayList<cards> cardslist;

        cardsadapter(ArrayList<cards> cardslist) {
            this.cardslist = cardslist;
        }

        @NonNull
        @Override
        public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards, parent, false);
            return new holder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull final holder holder, final int position) {

            holder.desc.setText(cardslist.get(position).getDESC());
            holder.time.setText(cardslist.get(position).getDUE());
            holder.title.setText(cardslist.get(position).getTITLE());
            holder.name3.setText(cardslist.get(position).getName());
            holder.downloadfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String posted_by = cardslist.get(position).getDESC();
                    String uri = "tel:" + posted_by.trim();
                    intent1.setData(Uri.parse(uri));
                    startActivity(intent1);
                }
            });
            holder.archive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Lists.this);

                    // Set a title for alert dialog
                    builder.setTitle("Select your answer.");

                    // Ask the final question
                    builder.setMessage("Are you sure to delete the entry?");

                    // Set click listener for alert dialog buttons
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch(which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    progressDialog.setMessage("Please wait..");
                                    boardref = uidref.child(Objects.requireNonNull(boardname)).child(cardslist.get(position).getTITLE());
                                    boardref.removeValue();
                                    cardlist.remove(position);
                                    notifyItemRemoved(position);
                                    progressDialog.dismiss();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    Toast.makeText(getApplicationContext(),"Not Allowed",Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    };

                    // Set the alert dialog yes button click listener
                    builder.setPositiveButton("Yes", dialogClickListener);

                    // Set the alert dialog no button click listener
                    builder.setNegativeButton("No",dialogClickListener);

                    AlertDialog dialog = builder.create();
                    // Display the alert dialog on interface
                    dialog.show();

                }
            });
        }

        @Override
        public int getItemCount() {
            return cardslist.size();
        }

        class holder extends RecyclerView.ViewHolder {
            TextView desc, time, title, name3;
            ImageButton archive;
            LinearLayout downloadfile;

            holder(@NonNull View itemView) {
                super(itemView);
                desc = itemView.findViewById(R.id.desc);
                name3 = itemView.findViewById(R.id.NAME);
                time = itemView.findViewById(R.id.due);
                title = itemView.findViewById(R.id.title);
                archive = itemView.findViewById(R.id.archive);
                downloadfile = itemView.findViewById(R.id.download);
            }
        }
    }
    private boolean isValidMobile(String phone) {
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 9 && phone.length() <= 12;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menulist, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.back) {
            super.onBackPressed();
            return (true);
        }
        return true;
    }

    //Notification intent
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createnotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "Task";
            String desc = "Chanel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("notify", name, importance);
            channel.setDescription(desc);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }

    }

    //Broadcast
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void broadcast(String finalDue_time1) {
        Intent intent = new Intent(Lists.this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Lists.this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
        Date mDate = null;
        try {
            mDate = sdf.parse(finalDue_time1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert mDate != null;
        long timeInMilliseconds = mDate.getTime();
        Objects.requireNonNull(alarmManager).set(AlarmManager.RTC_WAKEUP, timeInMilliseconds, pendingIntent);
    }

    private void showDialog() {
        final MaterialDatePicker.Builder datepicker = MaterialDatePicker.Builder.datePicker();

        datepicker.setTitleText("Select Date");
        final MaterialDatePicker materialDatePicker = datepicker.build();


        final Dialog builder = new Dialog(Lists.this);
        builder.setCancelable(false);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);

        builder.setContentView(R.layout.add_card);

        final int[] hours = new int[1];
        final int[] min = new int[1];
        final EditText card_name = builder.findViewById(R.id.card_name);
        final EditText description = builder.findViewById(R.id.card_desc);
        final EditText name2 = builder.findViewById(R.id.NAME);
        final LinearLayout datePickerLayout = builder.findViewById(R.id.date_picker_actions);
        final TextView datetext = builder.findViewById(R.id.date);
        Button done = builder.findViewById(R.id.done);
        final ImageButton cancle = builder.findViewById(R.id.cancel_button);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        //Date and time picker
        datePickerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }

        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                //Time picker
                final String date = "Due : " + materialDatePicker.getHeaderText();
                Calendar mcurrentTime = Calendar.getInstance();
                final int[] hour = {mcurrentTime.get(Calendar.HOUR_OF_DAY)};
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(Lists.this, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        datetext.setText(date + " " + selectedHour + ":" + selectedMinute);
                        hours[0] = selectedHour;
                        min[0] = selectedMinute;
                    }

                }, hour[0], minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        //select attachment

        done.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                progressDialog.show();
                progressDialog.setCancelable(false);
                final String card_na = card_name.getText().toString().trim().toUpperCase();
                String due_time = datetext.getText().toString();
                final String desc = description.getText().toString().trim();
                final String name1 = name2.getText().toString().trim();

                if(!isValidMobile(desc)){
                    description.setError("This field can not be blank");
                    progressDialog.dismiss();
                }
                boardref = uidref.child(Objects.requireNonNull(boardname)).child(card_na);
                cardref = boardref.child(TITLE);
                due_dateref = boardref.child(DUE);
                name = boardref.child(NAME);
                descref = boardref.child(DESC);
                if (card_na.isEmpty()) {
                    Toast.makeText(Lists.this, "Please enter card name", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (!due_time.contains("Due")) {
                    Toast.makeText(Lists.this, "Please select due time", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        createnotification();
                    }
                    due_time = due_time.replace("Due : ", "").trim();
                    progressDialog.show();

                    try {

                        due_dateref.setValue(due_time);
                        descref.setValue(desc);
                        name.setValue(name1);
                        broadcast(due_time);
                        cardref.setValue(card_na.trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                recreate();
                                progressDialog.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Lists.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        builder.show();

    }

}
