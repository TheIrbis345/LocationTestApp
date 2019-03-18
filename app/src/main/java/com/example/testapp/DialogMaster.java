package com.example.testapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class DialogMaster {
    private Context context;

    public DialogMaster(Context context) {
        this.context = context;
    }

    public AlertDialog getWaitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder( context );
        ProgressBar pb = new ProgressBar( context );
        pb.setLayoutParams( new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ) );
        builder.setView( pb );
        builder.setCancelable( false );
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
        return dialog;
    }
}
