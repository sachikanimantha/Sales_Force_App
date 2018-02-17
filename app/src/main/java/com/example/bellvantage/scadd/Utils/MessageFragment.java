package com.example.bellvantage.scadd.Utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bellvantage.scadd.R;

/**
 * Created by Sachika on 7/2/2017.
 */

public class MessageFragment extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //create the View
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_for_popup,null);

        //EditText etUserName = (EditText) view.findViewById(R.id.editText);
        //EditText etPassword = (EditText) view.findViewById(R.id.editText2);
        Button btnLogin = (Button) view.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "You Clicked Message", Toast.LENGTH_SHORT).show();
            }
        });


        //create a buttonListener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                System.out.println("You Clicked Dailog Message");
            }
        };

        //Build MessageDialog
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();

    }


}
