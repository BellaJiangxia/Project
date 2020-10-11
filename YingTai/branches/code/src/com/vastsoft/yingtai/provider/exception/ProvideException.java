package com.vastsoft.yingtai.provider.exception;

import com.vastsoft.util.exception.BaseException;

/**
 * Created by HonF on 17/1/17.
 */
public class ProvideException extends BaseException {

    public ProvideException(String strMessage) {
        super(strMessage);
    }

    @Override
    public int getCode() {
        return 1000;
    }
}
