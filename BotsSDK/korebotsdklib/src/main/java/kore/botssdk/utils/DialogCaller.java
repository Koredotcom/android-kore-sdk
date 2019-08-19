package kore.botssdk.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogCaller {

public static void showDialog(Context context,
                              DialogInterface.OnClickListener onClickListener) {

    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
  //  dialog.setTitle(Constants.SKILL_HOME);
    dialog.setCancelable(false);
//    Updated text - This action will end your conversation with <skill name> and move to Kora. Do you want to continue? (Yes) (No)
    dialog.setMessage("This action will end your conversation with " + Constants.SKILL_SELECTION +
            " skill and move to Kora. Do you want to continue?");
  //  dialog.setMessage("You are conversing with " + Constants.SKILL_HOME + " skill now. Clicking on this will end your conversation with " + Constants.SKILL_SELECTION + " skill and move to Kora skill ");
    dialog.setPositiveButton("Yes",onClickListener);
    dialog.setNegativeButton("No",null);
    dialog.show();
}
}