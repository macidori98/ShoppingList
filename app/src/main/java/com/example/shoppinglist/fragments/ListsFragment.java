package com.example.shoppinglist.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.MainActivity;
import com.example.shoppinglist.R;
import com.example.shoppinglist.adapter.ListsAdapter;
import com.example.shoppinglist.fragments.dialog.AddListDialog;
import com.example.shoppinglist.interfaces.AddListDialogListener;
import com.example.shoppinglist.interfaces.OnItemClickListener;
import com.example.shoppinglist.model.Lists;
import com.example.shoppinglist.utils.Constant;
import com.example.shoppinglist.utils.FragmentNavigation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListsFragment extends Fragment implements AddListDialogListener {
    public static final String TAG = ListsFragment.class.getSimpleName();

    private View view;
    private RecyclerView rvLists;
    private ImageView ivAdd;
    private ListsAdapter listsAdapter;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private List<Lists> lists;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lists, container, false);
        initializeElements(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addList();
            }
        });
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage(Constant.LOADING);
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String sTitle = snapshot.child(Constant.TITLE).getValue().toString();
                    String sCreatedBy = snapshot.child(Constant.CREATED_BY).getValue().toString();
                    String sLastEdit = snapshot.child(Constant.LAST_EDIT).getValue().toString();
                    String sId = snapshot.child(Constant.ID).getValue().toString();
                    Lists list = new Lists(sId, sCreatedBy, sLastEdit, sTitle);
                    boolean found = false;
                    for (Lists l : lists){
                        if (l.getsID().equals(sId)){
                            found = true;
                            break;
                        }
                    }

                    if (!found){
                        lists.add(list);
                    }
                }

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                rvLists.setLayoutManager(linearLayoutManager);
                listsAdapter = new ListsAdapter(lists, getContext(), getFragmentManager(), getActivity());
                listsAdapter.setOnClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Constant.SELECTED_LIST = lists.get(position);
                        //Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                        FragmentNavigation.getInstance(getContext()).replaceFragment(new ElementsFragment(), R.id.fragment_content);
                    }
                });
                rvLists.setAdapter(listsAdapter);
                rvLists.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        dialog.hide();
                        rvLists.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addList(){
        AddListDialog addListDialog = new AddListDialog();
        addListDialog.setListener(this);
        addListDialog.show(getActivity().getSupportFragmentManager(),"Add new list");
    }

    private void initializeElements(View view){
        ivAdd = view.findViewById(R.id.imageView_lists_add);
        rvLists = view.findViewById(R.id.recyclerView_lists);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference(Constant.LISTS);
        lists = new ArrayList<>();
    }

    @Override
    public void addList(Lists list) {
        lists.add(list);
        listsAdapter.notifyItemInserted(lists.size());
    }
}
