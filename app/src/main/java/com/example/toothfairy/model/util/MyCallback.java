package com.example.toothfairy.model.util;

import okhttp3.Callback;
import okhttp3.ResponseBody;

public interface MyCallback extends Callback {
    public ResponseBody returnResponse();
}
