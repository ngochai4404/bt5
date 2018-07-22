package com.hai.bt6.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.hai.bt6.R;
import com.hai.bt6.custom.adapter.ContactAdapter;
import com.hai.bt6.db.DatabaseManager;
import com.hai.bt6.db.table.ContactTable;
import com.hai.bt6.interfaces.DialogClick;
import com.hai.bt6.interfaces.ItemOnClick;
import com.hai.bt6.model.Contact;
import com.hai.bt6.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemOnClick {
    RecyclerView mRcvContact;
    List<Contact> mContacts;
    ContactAdapter mAdapter;
    boolean mGrid = false;
    DatabaseManager mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bindingData();
    }


    private void initView() {
        mRcvContact = findViewById(R.id.rcv_contact);

    }

    private void bindingData() {
        mDatabase = new DatabaseManager(this);
        mContacts = new ContactTable().getAllContact(mDatabase);
        mRcvContact.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ContactAdapter(mContacts, this, this);
        mRcvContact.setAdapter(mAdapter);
    }

    public void add(View v) {
        DialogUtil.showDialogAdd(this, new DialogClick() {
            @Override
            public void positiveClick(String name, String number) {
                Contact contact = new Contact(name, number);
                contact.setId(new ContactTable().insertContact(contact, mDatabase));
                mContacts.add(contact);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void inNegativeClick() {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change) {
            if (mGrid) {
                mRcvContact.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
                mGrid = false;
            } else {
                mRcvContact.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));
                mGrid = true;
            }
            mAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(Object o, final int pos) {
        final Contact contact = (Contact) o;
        DialogUtil.showDialogEdit(this, contact, new DialogClick() {
            @Override
            public void positiveClick(String name, String number) {
                contact.setName(name);
                contact.setNumber(number);
                new ContactTable().updateNote(contact, mDatabase);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void inNegativeClick() {
                new ContactTable().deleteContact(contact, mDatabase);
                mContacts.remove(pos);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
