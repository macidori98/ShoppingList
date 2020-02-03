package com.example.shoppinglist.fragments;

import android.os.Bundle;
import android.service.autofill.Dataset;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.R;
import com.example.shoppinglist.adapter.ElementsAdapter;
import com.example.shoppinglist.adapter.ListsAdapter;
import com.example.shoppinglist.fragments.dialog.AddListElementDialog;
import com.example.shoppinglist.interfaces.AddListElementDialogListener;
import com.example.shoppinglist.interfaces.OnItemClickListener;
import com.example.shoppinglist.model.Elements;
import com.example.shoppinglist.model.Lists;
import com.example.shoppinglist.utils.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ElementsFragment extends Fragment implements AddListElementDialogListener {
    public static final String TAG = ElementsFragment.class.getSimpleName();

    private View view;
    private RecyclerView rvElements;
    private ElementsAdapter elementsAdapter;
    private ImageView ivAddElement;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private List<Elements> elementsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_elements, container, false);
        initializeElements(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivAddElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addElement();
            }
        });

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String sListID = snapshot.child(Constant.LIST_ID).getValue().toString();
                    if (Constant.SELECTED_LIST.getsID().equals(sListID)){
                        String sID = snapshot.child(Constant.ID).getValue().toString();
                        String sName = snapshot.child(Constant.ELEMENT_NAME).getValue().toString();
                        boolean bChecked= Boolean.valueOf(snapshot.child(Constant.bChecked).getValue().toString());
                        Elements elements = new Elements(sID, sName, sListID, bChecked);
                        boolean found = false;
                        for (Elements e : elementsList){
                            if (e.getsID().equals(sID)){
                                found = true;
                                break;
                            }
                        }

                        if (!found){
                            elementsList.add(elements);
                        }
                    }
                }

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                rvElements.setLayoutManager(linearLayoutManager);
                elementsAdapter = new ElementsAdapter(elementsList,getContext());
                elementsAdapter.setOnClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                    }
                });
                rvElements.setAdapter(elementsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializeElements(View view){
        rvElements = view.findViewById(R.id.recyclerView_elements);
        ivAddElement = view.findViewById(R.id.imageView_add_element);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference(Constant.LIST_ELEMENTS);
        elementsList = new ArrayList<>();
    }

    private void addElement(){
        AddListElementDialog addListElementDialog = new AddListElementDialog();
        addListElementDialog.setListener(this);
        addListElementDialog.show(getActivity().getSupportFragmentManager(),"Add new list element");
    }

    @Override
    public void addListElement(Elements elements) {
        elementsList.add(elements);

    }
}
