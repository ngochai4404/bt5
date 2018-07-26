package com.hai.bt6.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.hai.bt6.R;
import com.hai.bt6.custom.adapter.ContactAdapter;
import com.hai.bt6.db.DatabaseManager;
import com.hai.bt6.db.table.ContactTable;
import com.hai.bt6.interfaces.DialogClick;
import com.hai.bt6.interfaces.ItemOnClick;
import com.hai.bt6.model.Contact;
import com.hai.bt6.util.DialogUtil;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemOnClick {
    RecyclerView mRcvContact;
    List<Contact> mContacts;
    ContactAdapter mAdapter;
    boolean mGrid = false;
    DatabaseManager mDatabase;
    boolean mDelete = false;
    LinearLayout mLayoutDel;
    FloatingActionButton mBtnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bindingData();
    }


    private void initView() {
        mRcvContact = findViewById(R.id.rcv_contact);
        mLayoutDel = findViewById(R.id.ll_delete);
        mBtnAdd = findViewById(R.id.btn_add);
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
                new AddContact().execute(contact);
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
                new UpdateContact().execute(contact);
            }

            @Override
            public void inNegativeClick() {
                new DeleteContact().execute(Arrays.asList(contact));
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onLongClick(int pos) {
        mDelete = true;
        mLayoutDel.setVisibility(View.VISIBLE);
        mBtnAdd.setVisibility(View.GONE);
        mAdapter.setmShowSelect(true);
    }

    public void cancelDel(View v) {
        mDelete = false;
        mLayoutDel.setVisibility(View.GONE);
        mBtnAdd.setVisibility(View.VISIBLE);
        mAdapter.setmShowSelect(false);
    }

    public void delSelect(View v) {
        new DeleteContact().execute(mAdapter.getContactsSelect());
        mLayoutDel.setVisibility(View.GONE);
        mBtnAdd.setVisibility(View.VISIBLE);
        mAdapter.setmShowSelect(false);
    }

    class UpdateContact extends AsyncTask<Contact, Void, Void> {

        @Override
        protected Void doInBackground(Contact... contacts) {
            new ContactTable().updateNote(contacts[0], mDatabase);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter.notifyDataSetChanged();
        }
    }

    class AddContact extends AsyncTask<Contact, Void, Void> {

        @Override
        protected Void doInBackground(Contact... contacts) {
            contacts[0].setId(new ContactTable().insertContact(contacts[0], mDatabase));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mContacts.clear();
            mContacts.addAll(new ContactTable().getAllContact(mDatabase));
            mAdapter.notifyDataSetChanged();
        }
    }

    class DeleteContact extends AsyncTask<List<Contact>, Void, Void> {

        @Override
        protected Void doInBackground(List<Contact>... contacts) {
            for (Contact c : contacts[0]) {
                new ContactTable().deleteContact(c, mDatabase);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mContacts.clear();
            mContacts.addAll(new ContactTable().getAllContact(mDatabase));
            mAdapter.notifyDataSetChanged();
        }
    }
}
