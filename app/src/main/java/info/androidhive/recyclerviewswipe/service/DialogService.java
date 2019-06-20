package info.androidhive.recyclerviewswipe.service;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import info.androidhive.recyclerviewswipe.MainActivity;

public class DialogService {
    public static void createSubscribeDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Нет действующей подписки!")
                .setMessage("Вы хотите продлить подписку?")
                .setCancelable(false)
                .setNegativeButton("Да",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
