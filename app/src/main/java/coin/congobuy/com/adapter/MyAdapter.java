package coin.congobuy.com.adapter;

/**
 * Created by Andymub on 23/06/2018.
 */
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import coin.congobuy.com.R;
import coin.congobuy.com.object.MyItem;

public class MyAdapter extends BaseAdapter {

    Context context;
    ArrayList<MyItem> listforview;
    LayoutInflater inflator=null;
    View v;
    ViewHolder vholder;
    //Constructor
    public MyAdapter(Context con,ArrayList<MyItem> list)
    {
        super();
        context=con;
        listforview=list;
        inflator=LayoutInflater.from(con);
    }
    // return position here
    @Override
    public long getItemId(int position) {
        return position;
    }
    // return size of list
    @Override
    public int getCount() {
        return listforview.size();
    }
    //get Object from each position
    @Override
    public Object getItem(int position) {
        return listforview.get(position);
    }
    //Viewholder class to contain inflated xml views
    private  class ViewHolder
    {
        TextView title;
        ImageView image;
    }
    // Called for each view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        v=convertView;
        if(convertView==null)
        {
            //inflate the view for each row of listview
            v=inflator.inflate(R.layout.list_row,null);
            //ViewHolder object to contain myadapter.xml elements
            vholder=new ViewHolder();
            vholder.title=(TextView)v.findViewById(R.id.txt_title);
            vholder.image=(ImageView)v.findViewById(R.id.list_image);
            //set holder to the view
            v.setTag(vholder);
        }
        else
            vholder=(ViewHolder)v.getTag();
        //getting MyItem Object for each position
        MyItem item=(MyItem)listforview.get(position);
        //setting the values from object to holder views for each row
        vholder.title.setText(item.getImageheading());
        vholder.image.setImageResource(item.getImageid());
        return v;
    }
}