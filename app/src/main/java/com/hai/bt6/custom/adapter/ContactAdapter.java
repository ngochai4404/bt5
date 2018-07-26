package com.hai.bt6.custom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hai.bt6.R;
import com.hai.bt6.interfaces.ItemOnClick;
import com.hai.bt6.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hai on 15/07/2018.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {
    List<Contact> mContacts;
    Context mContext;
    ItemOnClick mOnClick;
    boolean mSelect[];
    boolean mShowSelect;

    public ContactAdapter(List<Contact> mContacts, Context mContext, ItemOnClick onClick) {
        this.mContacts = mContacts;
        this.mContext = mContext;
        this.mOnClick = onClick;
        mSelect = new boolean[mContacts.size()];
    }

    public void setmShowSelect(boolean mShowSelect) {
        this.mShowSelect = mShowSelect;
        notifyDataSetChanged();
    }

    public List<Contact> getContactsSelect() {
        List<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < mSelect.length; i++) {
            if (mSelect[i]) {
                contacts.add(mContacts.get(i));
            }
        }
        return contacts;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_contact, viewGroup, false);
        return new ContactHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int i) {
        final int pos = mContacts.size() - i - 1;
        if (mShowSelect) {
            holder.cbSelect.setVisibility(View.VISIBLE);
            holder.cbSelect.setChecked(mSelect[pos]);
        } else {
            holder.cbSelect.setVisibility(View.GONE);
        }
        final Contact contact = mContacts.get(pos);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mShowSelect) {
                    mSelect[pos] = !mSelect[pos];
                    notifyDataSetChanged();
                } else {
                    mOnClick.onClick(contact, pos);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!mShowSelect) {
                    mOnClick.onLongClick(pos);
                    mSelect = new boolean[mContacts.size()];
                    mSelect[pos] = true;
                }
                return false;
            }
        });
        holder.tvName.setText(contact.getName());
        holder.tvNumber.setText(contact.getNumber());

    }

    @Override
    public int getItemCount() {
        if (mContacts == null) {
            return 0;
        }
        return mContacts.size();
    }

    class ContactHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvNumber;
        CheckBox cbSelect;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvNumber = itemView.findViewById(R.id.tv_number);
            cbSelect = itemView.findViewById(R.id.cb_select);
        }
    }
}
