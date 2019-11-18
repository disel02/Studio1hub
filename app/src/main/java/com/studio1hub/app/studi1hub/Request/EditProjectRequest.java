package com.studio1hub.app.studi1hub.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EditProjectRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL="http://app.studio1hub.com/apk/editproject.php";
    private Map<String,String> params;
    public EditProjectRequest(String title, String discription, String cid , String eid, String pid,String cost,Response.Listener<String> listener){
        super(Method.POST,LOGIN_REQUEST_URL ,listener, null);
        params=new HashMap<>();
        params.put("title",title);
        params.put("discription",discription);
        params.put("cid",cid);
        params.put("eid",eid);
        params.put("pid",pid);
        params.put("cost",cost);
        params.put("btnsubmit","btnsubmit");
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
