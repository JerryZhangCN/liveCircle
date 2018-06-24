package com.lvr.livecircle.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lvr.livecircle.R;

/**
 * Created by Byron on 2017-01-10.
 * <p>
 * Dialog辅助类
 */

public class DialogUtil {
    //选择dialog
    public static void showCheckDialog(Context context, String info, String hint,String checkInfo, String cancelInfo, final int type, final DialogTwoButtonClickListener clickListener) {
        final Dialog dialog = new Dialog(context, R.style.MyDialog);
        dialog.setContentView(R.layout.public_dialog_remove);
        TextView msg = (TextView) dialog.getWindow().findViewById(R.id.dialog_msg);
        TextView hintText=(TextView)dialog.getWindow().findViewById(R.id.dialog_hint);
        Button sure = (Button) dialog.getWindow().findViewById(R.id.remove_sure);
        Button cancel = (Button) dialog.getWindow().findViewById(R.id.remove_cancel);
        msg.setText(info);
        sure.setText(checkInfo);
        cancel.setText(cancelInfo);
        hintText.setText(hint);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.public_shape_dialog);
        sure.setText(checkInfo);
        cancel.setText(cancelInfo);
        dialog.show();
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                clickListener.clickYes(type);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                clickListener.clickNo(type);
            }
        });
    }


}
