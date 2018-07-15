package com.hai.bt6.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.hai.bt6.R;
import com.hai.bt6.custom.adapter.ContactAdapter;
import com.hai.bt6.interfaces.DialogClick;
import com.hai.bt6.interfaces.ItemOnClick;
import com.hai.bt6.model.Contact;
import com.hai.bt6.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemOnClick{
    RecyclerView rcvContact;
    List<Contact> mContacts;
    ContactAdapter mAdapter;
    boolean grid = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bindingData();

    }



    private void initView(){
        rcvContact = findViewById(R.id.rcv_contact);

    }
    private void bindingData(){
        mContacts = new ArrayList<>();
        mContacts.add(new Contact("1","!23"));
        mContacts.add(new Contact("2","!23"));
        mContacts.add(new Contact("3","!23"));
        rcvContact.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mAdapter = new ContactAdapter(mContacts,this,this);
        rcvContact.setAdapter(mAdapter);
    }
    public void add(View v){
        DialogUtil.showDialogAdd(this, new DialogClick() {
            @Override
            public void positiveClick(String name, String number) {
                mContacts.add(new Contact(name,number));
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
        if ( item.getItemId() == R.id.action_change) {
            if ( grid ){
                rcvContact.setLayoutManager(new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false));
                grid = false;
            }else {
                rcvContact.setLayoutManager(new GridLayoutManager(getBaseContext(),2));
                grid =true;
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
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void inNegativeClick() {
                mContacts.remove(pos);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}