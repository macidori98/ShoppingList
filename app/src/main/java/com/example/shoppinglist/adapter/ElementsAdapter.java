package com.example.shoppinglist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.R;
import com.example.shoppinglist.interfaces.OnItemClickListener;
import com.example.shoppinglist.model.Elements;
import com.example.shoppinglist.utils.Constant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ElementsAdapter extends RecyclerView.Adapter<ElementsAdapter.MyViewHolder> {

    private List<Elements> elementsList;
    private Context context;
    private OnItemClickListener listener;

    public ElementsAdapter(List<Elements> elementsList, Context context) {
        this.elementsList = elementsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ElementsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_elements, parent, false);
        return new MyViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ElementsAdapter.MyViewHolder holder, final int position) {
        final MyViewHolder mHolder = holder;
        holder.cbElement.setChecked(elementsList.get(position).isbChecked());
        holder.cbElement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                DatabaseReference mRef = mDatabase.getReference(Constant.LIST_ELEMENTS);
                mRef.child(elementsList.get(position).getsID()).child(Constant.bChecked).setValue(b);
                elementsList.get(position).setbChecked(b);
                mHolder.cbElement.setChecked(b);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.itemView);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference mRef = mDatabase.getReference(Constant.LIST_ELEMENTS);
                        switch (menuItem.getItemId()) {
                            case R.id.delete:
                                mRef.child(elementsList.get(position).getsID()).removeValue();
                                elementsList.remove(position);
                                notifyDataSetChanged();
                                break;
                        }
                        return false;
                    }
                });
                return true;
            }
        });
        holder.cbElement.setText(elementsList.get(position).getsName());
    }

    @Override
    public int getItemCount() {
        return elementsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public CheckBox cbElement;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener){
            super(itemView);
            cbElement = itemView.findViewById(R.id.checkBox_element);
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
