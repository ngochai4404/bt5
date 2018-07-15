package com.hai.bt6.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hai.bt6.R;
import com.hai.bt6.interfaces.DialogClick;
import com.hai.bt6.model.Contact;

/**
 * Created by Hai on 15/07/2018.
 */

public class DialogUtil {
    public static void showDialogAdd(Activity activity, final DialogClick dialogClick){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_add);
        dialog.setTitle(activity.getString(R.string.add));
        final EditText edtNumber = dialog.findViewById(R.id.edt_number);
        final EditText edtName = dialog.findViewById(R.id.edt_name);
        Button btnAdd = dialog.findViewById(R.id.btn_add);
        Button btnCancel = dialog.findViewById(R.id.btn_can);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogClick.positiveClick(edtName.getText().toString(),edtNumber.getText().toString());
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public static void showDialogEdit(Activity activity, Contact contact, final DialogClick dialogClick){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_edit);
        dialog.setTitle(activity.getString(R.string.edit));
        final EditText edtNumber = dialog.findViewById(R.id.edt_number);
        final EditText edtName = dialog.findViewById(R.id.edt_name);
        edtName.setText(contact.getName());
        edtNumber.setText(contact.getNumber());
        Button btnAdd = dialog.findViewById(R.id.btn_save);
        Button btnCancel = dialog.findViewById(R.id.btn_del);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogClick.positiveClick(edtName.getText().toString(),edtNumber.getText().toString());
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogClick.inNegativeClick();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
