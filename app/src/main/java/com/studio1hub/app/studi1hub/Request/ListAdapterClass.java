package com.studio1hub.app.studi1hub.Request;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.studio1hub.app.studi1hub.R;

import java.util.List;

public class ListAdapterClass extends BaseAdapter {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    String listkey;

    Context context;
    List<subjects> valueList;
    public ListAdapterClass(List<subjects> listValue, Context context)
    {
        this.context = context;
        this.valueList = listValue;
    }
    @Override
    public int getCount()
    {
        return this.valueList.size();
    }
    @Override
    public Object getItem(int position)
    {
        return this.valueList.get(position);
    }
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
        listkey = prefs.getString("listkey", null);

        ViewItem viewItem = null;
        if(convertView == null)
        {
            viewItem = new ViewItem();
            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (listkey.equals("AEF"))
            {
                convertView = layoutInfiater.inflate(R.layout.admin_emp_items, null);
                viewItem.Vid = (TextView)convertView.findViewById(R.id.tvid);
                viewItem.Vname = (TextView)convertView.findViewById(R.id.tvname);
                viewItem.Vcproject = (TextView)convertView.findViewById(R.id.tvcproject);
            }
            else if (listkey.equals("VEF"))
            {
                convertView = layoutInfiater.inflate(R.layout.admin_emp_verified_items, null);
                viewItem.Vid = (TextView)convertView.findViewById(R.id.tvid);
                viewItem.Vname = (TextView)convertView.findViewById(R.id.tvname);
                viewItem.Vverified = (ImageView) convertView.findViewById(R.id.ivverified);
            }
            else if (listkey.equals("APF"))
            {
                convertView = layoutInfiater.inflate(R.layout.admin_project_items, null);
                viewItem.Vpid = (TextView) convertView.findViewById(R.id.tvpid);
                viewItem.Vtitle = (TextView) convertView.findViewById(R.id.tvtitle);
                viewItem.Vstatus = (TextView) convertView.findViewById(R.id.tvstatus);
            }
            else if (listkey.equals("APNF"))
            {
                convertView = layoutInfiater.inflate(R.layout.admin_project_nav_items, null);
                viewItem.Vpid = (TextView) convertView.findViewById(R.id.tvpid);
                viewItem.Vtitle = (TextView) convertView.findViewById(R.id.tvtitle);
            }
            else if (listkey.equals("APyF"))
            {
                convertView = layoutInfiater.inflate(R.layout.admin_payment_items, null);
                viewItem.Vpyid = (TextView) convertView.findViewById(R.id.tvpyid);
                viewItem.Vpyname = (TextView) convertView.findViewById(R.id.tvpyname);
                viewItem.Vpyamount = (TextView) convertView.findViewById(R.id.tvpyamount);
            }
            else if (listkey.equals("APymF"))
            {
                convertView = layoutInfiater.inflate(R.layout.admin_payment_master_items, null);
                viewItem.Vpyid = (TextView) convertView.findViewById(R.id.tvpyid);
                viewItem.Vpyname = (TextView) convertView.findViewById(R.id.tvpyname);
                viewItem.Vpypaid = (TextView) convertView.findViewById(R.id.tvmode1);
                viewItem.Vpyremain = (TextView) convertView.findViewById(R.id.tvmode2);
                viewItem.Vpytotal = (TextView) convertView.findViewById(R.id.tvmode3);
            }
            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItem) convertView.getTag();
        }

        if (listkey.equals("AEF"))
        {
            viewItem.Vid.setText(valueList.get(position).Sid);
            viewItem.Vname.setText(valueList.get(position).Sname);
            viewItem.Vcproject.setText(valueList.get(position).Scproject);
        }
        else if (listkey.equals("VEF"))
        {
            viewItem.Vid.setText(valueList.get(position).Sid);
            viewItem.Vname.setText(valueList.get(position).Sname);
            if (valueList.get(position).Sverified.equals("0"))
            {
                viewItem.Vverified.setImageResource(R.drawable.ic_close_black_24dp);
            }
            else if (valueList.get(position).Sverified.equals("1"))
            {
                viewItem.Vverified.setImageResource(R.drawable.ic_error_outline_grey_24dp);
            }
            else
            {
                viewItem.Vverified.setImageResource(R.drawable.ic_check_orange_24dp);
            }
        }
        else if (listkey.equals("APF"))
        {
            viewItem.Vpid.setText(valueList.get(position).Spid);
            viewItem.Vtitle.setText(valueList.get(position).Stitle);
            viewItem.Vstatus.setText(valueList.get(position).Sstatus);
        }
        else if (listkey.equals("APNF"))
        {
            viewItem.Vpid.setText(valueList.get(position).Spid);
            viewItem.Vtitle.setText(valueList.get(position).Stitle);
        }
        else if (listkey.equals("APyF"))
        {
            viewItem.Vpyid.setText(valueList.get(position).Spyid);
            viewItem.Vpyname.setText(valueList.get(position).Spyname);
            viewItem.Vpyamount.setText(valueList.get(position).Spyamount);
        }
        else if (listkey.equals("APymF"))
        {
            viewItem.Vpyid.setText(valueList.get(position).Spyid);
            viewItem.Vpyname.setText(valueList.get(position).Spyname);
            viewItem.Vpypaid.setText(valueList.get(position).Spypaid);
            viewItem.Vpyremain.setText(valueList.get(position).Spyremain);
            viewItem.Vpytotal.setText(valueList.get(position).Spytotal);
        }

        return convertView;
    }
}

class ViewItem
{
    TextView Vid,Vname,Vpid,Vtitle,Vcproject,Vstatus,Vpyid,Vpyname,Vpypaid,Vpyremain,Vpytotal,Vpyamount;
    ImageView Vverified;
}

