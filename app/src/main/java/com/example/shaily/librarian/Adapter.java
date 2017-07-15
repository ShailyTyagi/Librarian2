package com.example.shaily.librarian;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shaily on 14-07-2017.
 */

public class Adapter extends ArrayAdapter {

    List list=new ArrayList();
    public Adapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }


    public void add(BookItem object){
        list.add(object);
        super.add(object);
    }

    @Override
    public int getCount(){
        return list.size();
    }

    @Override
    public Object getItem(int pos){
        return list.get(pos);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        View row=convertView;
        ProductHolder pd;
        if(row==null){
            LayoutInflater lf=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=lf.inflate(R.layout.list_item,parent,false);
            pd=new ProductHolder();
            pd.bn=(TextView)row.findViewById(R.id.b1);
            pd.au=(TextView)row.findViewById(R.id.b2);
            pd.due=(TextView)row.findViewById(R.id.b3);
            pd.fine=(TextView)row.findViewById(R.id.b4);
            row.setTag(pd);
        }
        else{
            pd=(ProductHolder)row.getTag();
        }

        BookItem it=(BookItem)getItem(pos);
        pd.bn.setText(it.getBna().toString());
        pd.au.setText(it.getAuthn().toString());
        pd.due.setText(it.getIss().toString());
        pd.fine.setText(Integer.toString(it.getTotal_fine()));


        return row;
    }

    static class ProductHolder{

        TextView bn,au,due,fine;
    }

}
