package com.example.shoppinglist.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.R;
import com.example.shoppinglist.interfaces.OnItemClickListener;
import com.example.shoppinglist.model.Lists;
import com.example.shoppinglist.utils.Constant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.MyViewHolder>{

    private List<Lists> lists;
    private Context context;
    private OnItemClickListener listener;
    private FragmentManager fragmentManager;
    private Activity activity;

    public ListsAdapter(List<Lists> lists, Context context, FragmentManager fragmentManager, Activity activity){
        this.lists = lists;
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ListsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_lists_element, parent, false);
        return new MyViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListsAdapter.MyViewHolder holder, final int position) {
        holder.tvTitle.setText(lists.get(position).getsTitle());
        holder.tvLastEdit.setText(lists.get(position).getsLastEditDate());
        holder.tvCreatedBy.setText(lists.get(position).getsCreatedByUserName());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.itemView);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference mRef = mDatabase.getReference(Constant.LISTS);
                        switch (menuItem.getItemId()) {
                            case R.id.delete:
                                mRef.child(lists.get(position).getsID()).removeValue();
                                lists.remove(position);
                                notifyDataSetChanged();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tvTitle, tvLastEdit, tvCreatedBy;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener){
            super(itemView);
            tvCreatedBy = itemView.findViewById(R.id.textView_recyclerView_lists_element_created_by);
            tvLastEdit = itemView.findViewById(R.id.textView_recyclerView_lists_element_last_edit_date);
            tvTitle = itemView.findViewById(R.id.textView_recyclerView_lists_element_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
