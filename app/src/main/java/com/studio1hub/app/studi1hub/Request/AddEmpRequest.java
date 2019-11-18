package com.studio1hub.app.studi1hub.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddEmpRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL="http://app.studio1hub.com/apk/adduser.php";
    private Map<String,String> params;
    public AddEmpRequest(String fname, String mname,String lname ,String email,String phone,String address,String city,String pincode,String country,String state,String type,String dob,Response.Listener<String> listener){
        super(Method.POST,LOGIN_REQUEST_URL ,listener, null);
        params=new HashMap<>();
        params.put("fname",fname);
        params.put("mname",mname);
        params.put("lname",lname);
        params.put("email",email);
        params.put("phone",phone);
        params.put("dob",dob);
        params.put("address",address);
        params.put("city",city);
        params.put("pincode",pincode);
        params.put("country",country);
        params.put("state",state);
        params.put("type",type);
        params.put("btnsubmit","btnsubmit");
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
