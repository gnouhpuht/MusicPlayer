package com.phuong.musicplayer.controll;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.phuong.musicplayer.R;

public class DialogPlaylist extends AppCompatDialogFragment {
    private EditText editCreatePlaylist;
    private DialogPlaylistListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_playlist,null);
        builder.setView(view)
                .setTitle("Create playlist")
                .setCancelable(true)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        return;
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String createPlaylist=editCreatePlaylist.getText().toString();
                        listener.applyTexts(createPlaylist);
                    }
                });
        editCreatePlaylist=view.findViewById(R.id.et_name_playlist);
        return builder.create();
    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            listener=(DialogPlaylistListener)context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() +
//                    "must implement ExampleDialogListener");
//        }
//
//    }

    public interface DialogPlaylistListener {
        void applyTexts(String createPlaylist);
    }

}
