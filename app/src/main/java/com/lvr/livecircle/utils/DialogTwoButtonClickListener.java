package com.lvr.livecircle.utils;

/**
 * Created by Byron on 2017-01-10.
 *
 *  dialog通用点击回调借口
 *
 */

public interface DialogTwoButtonClickListener {

    public static final int lOGIN_OUT_CONFIRM =1;
    public static final int lOGIN_OUT_ERROR_PASSWORD =2;
    public static final int COMMON_DIALOG = -1;

    void clickYes(int type);
    void clickNo(int type);
}
