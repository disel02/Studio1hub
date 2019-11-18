package com.studio1hub.app.studi1hub.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EditPaymentRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL="http://app.studio1hub.com/apk/editpayment.php";
    private Map<String,String> params;
    public EditPaymentRequest(String pyid, String cid, String pid , String amount, String date, Response.Listener<String> listener){
        super(Method.POST,LOGIN_REQUEST_URL ,listener, null);
        params=new HashMap<>();
        params.put("pyid",pyid);
        params.put("cid",cid);
        params.put("pid",pid);
        params.put("amount",amount);
        params.put("date",date);
        params.put("btnsubmit","btnsubmit");
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
