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
import com.example.shoppinglist.interfaces.AddListElementDialogListener;
import com.example.shoppinglist.model.Elements;
import com.example.shoppinglist.model.Lists;
import com.example.shoppinglist.utils.Constant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class AddListElementDialog extends AppCompatDialogFragment {
    public static final String TAG = AddListElementDialog.class.getSimpleName();

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef, mRef2;
    private EditText etListElementTitle;
    private AddListElementDialogListener listener;

    public AddListElementDialog(){}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_list_element, null);
        etListElementTitle = view.findViewById(R.id.editText_add_list_element_title);
        addNewListElement(builder,view);
        return builder.create();
    }

    private void addNewListElement(AlertDialog.Builder builder, View view){
        builder.setView(view).setTitle("Add element").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase = FirebaseDatabase.getInstance();
                mRef = mDatabase.getReference(Constant.LIST_ELEMENTS);
                mRef2 = mDatabase.getReference(Constant.LISTS);
                String title = etListElementTitle.getText().toString();
                if (isTextLengthOk(title)){
                    String ID = mRef.push().getKey();
                    Elements elements = new Elements(ID, title, Constant.SELECTED_LIST.getsID(), false);
                    listener.addListElement(elements);
                    mRef.child(ID).setValue(elements);
                    Date date = Calendar.getInstance().getTime();
                    mRef2.child(Constant.SELECTED_LIST.getsID()).child(Constant.LAST_EDIT).setValue(String.valueOf(date));
                    Toast.makeText(getContext(), R.string.list_element_added, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(),R.string.name_fail, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isTextLengthOk(String string){
        if (string.length() >= 4 ){
            return true;
        }
        else {
            return false;
        }
    }

    public void setListener(AddListElementDialogListener listener){
        this.listener = listener;
    }
}
