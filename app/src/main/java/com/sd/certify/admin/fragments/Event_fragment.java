package com.sd.certify.admin.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sd.certify.R;
import com.sd.certify.database_files_viewmodels.Event_post;

import java.util.Calendar;

public class Event_fragment extends Fragment {

    ImageView evImg;
    Button imgSelect,submit;
    ImageButton dateSelect;
    EditText evName;
    TextView evDate;
    String date;
    Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST = 100;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_event_fragment, container, false);

       evDate = view.findViewById(R.id.post_event_date);
       evName = view.findViewById(R.id.post_event_name);
       imgSelect = view.findViewById(R.id.post_event_img_piker);
       dateSelect = view.findViewById(R.id.post_event_date_picker);
       evImg = view.findViewById(R.id.post_event_img);
       submit = view.findViewById(R.id.post_event_submit);

       dateSelect.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               pickDate();
           }
       });

       imgSelect.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
               intent.setType("image/*");
               startActivityForResult(intent, PICK_IMAGE_REQUEST);
           }
       });

       submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(evName.getText().toString().equals(""))
                   Toast.makeText(requireContext(), "Enter that event Name", Toast.LENGTH_SHORT).show();
               else if(evDate.getText().equals("")||evDate.getText().equals("Date"))
                   Toast.makeText(requireContext(), "Select event Date", Toast.LENGTH_SHORT).show();
               else {
                   visiblityGone();
                  new Event_post().postEvent(evName.getText().toString(), evDate.getText().toString(), selectedImageUri, requireContext(), new Event_post.OnEventPostListener() {
                      @Override
                      public void onEventPostSuccess() {
                          ClearData();
                      }

                      @Override
                      public void onError() {
                          Toast.makeText(requireContext(), "Something wrong in post event..", Toast.LENGTH_SHORT).show();
                      }
                  });
               }
           }

           private void visiblityGone() {
               evName.setVisibility(View.GONE);
               imgSelect.setVisibility(View.GONE);
               evDate.setVisibility(View.GONE);
               evImg.setVisibility(View.GONE);
               submit.setVisibility(View.GONE);
               submit.setClickable(false);
               imgSelect.setClickable(false);
           }
       });
        return view;
    }

    private void ClearData() {
        evImg.setImageResource(R.drawable.img);
        evName.setText("");
        evDate.setText("");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            evImg.setImageURI(selectedImageUri);
        }
    }

    private void pickDate() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        date = i2 + "/" + (month + 1) + "/" + year;
                        evDate.setText(date);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }
}