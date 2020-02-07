package com.example.shoppinglist.fragments.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.shoppinglist.R;
import com.example.shoppinglist.interfaces.AddListDialogListener;
import com.example.shoppinglist.model.Lists;
import com.example.shoppinglist.utils.Constant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class AddListDialog extends AppCompatDialogFragment {
    public static final String TAG = AddListDialog.class.getSimpleName();

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private EditText etListTitle;
    private AddListDialogListener listener;

    public AddListDialog(){}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_list, null);
        etListTitle = view.findViewById(R.id.editText_add_list_title);
        addNewList(builder,view);
        return builder.create();
    }

    private void addNewList(AlertDialog.Builder builder, View view){
        builder.setView(view).setTitle("Add List").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase = FirebaseDatabase.getInstance();
                mRef = mDatabase.getReference(Constant.LISTS);
                String title = etListTitle.getText().toString();
                if (isTextLengthOk(title)){
                    String ID = mRef.push().getKey();
                    Date date = Calendar.getInstance().getTime();
                    Lists lists = new Lists(ID, Constant.CURRENT_USER, String.valueOf(date), title);
                    listener.addList(lists);
                    mRef.child(ID).setValue(lists);
                    Toast.makeText(getContext(), R.string.list_added, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(),R.string.name_fail, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isTextLengthOk(String string){
        if (string.length() >= 1 ){
            return true;
        }
        else {
            return false;
        }
    }

    public void setListener(AddListDialogListener listener){
        this.listener = listener;
    }
}
