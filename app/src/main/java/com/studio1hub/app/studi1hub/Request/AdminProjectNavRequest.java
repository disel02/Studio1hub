package com.studio1hub.app.studi1hub.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AdminProjectNavRequest extends StringRequest {
    private static final String
            LOGIN_REQUEST_URL="http://app.studio1hub.com/apk/adminprojectnav.php";
    private Map<String,String> params;
    public AdminProjectNavRequest(String username, String status2, Response.Listener<String> listener){
        super(Method.POST,LOGIN_REQUEST_URL ,listener, null);
        params=new HashMap<>();
        params.put("username",username);
        params.put("status",status2);
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
