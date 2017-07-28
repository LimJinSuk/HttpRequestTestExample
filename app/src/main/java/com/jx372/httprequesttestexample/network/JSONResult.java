package com.jx372.httprequesttestexample.network;

import android.provider.ContactsContract;

/**
 * Created by bit-user on 2017-07-28.
 */

public class JSONResult<DataT> {
    private String result;
    private String message;
    private DataT data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataT getData() {
        return data;
    }

    public void setData(DataT data) {
        this.data = data;
    }
}
