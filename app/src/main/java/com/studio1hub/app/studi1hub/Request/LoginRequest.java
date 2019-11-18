package com.studio1hub.app.studi1hub.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL="http://app.studio1hub.com/apk/login.php";
    private Map<String,String> params;
    public LoginRequest(String email1, String password1, Response.Listener<String> listener){
        super(Method.POST,LOGIN_REQUEST_URL ,listener, null);
        params=new HashMap<>();
        params.put("email",email1);
        params.put("password",password1);
        params.put("btnlogin","btnlogin");
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
