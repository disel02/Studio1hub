package com.studio1hub.app.studi1hub.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ProjectPaymentMasterRequest extends StringRequest {
    private static final String
            LOGIN_REQUEST_URL="http://app.studio1hub.com/apk/projectPyMaster.php";
    private Map<String,String> params;
    public ProjectPaymentMasterRequest(String cid,Response.Listener<String> listener){
        super(Method.POST,LOGIN_REQUEST_URL ,listener, null);
        params=new HashMap<>();
        params.put("cid",cid);
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
