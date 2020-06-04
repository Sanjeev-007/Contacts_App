package com.example.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class conadaptar extends ArrayAdapter<contacttable> {
    private Context context;
    private List<contacttable> contcts;
    public conadaptar(Context context, List<contacttable> list){
        super(context,R.layout.rowlayout,list);
    this.context=context;
    this.contcts=list;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View  sv=convertView;
        convertView=inflater.inflate(R.layout.rowlayout,parent,false);
        TextView tvchar =convertView.findViewById(R.id.tvchar);
        TextView tvname=convertView.findViewById(R.id.tvname);
        TextView tvmails=convertView.findViewById(R.id.tvmails);
        tvchar.setText(contcts.get(position).getName().toUpperCase().charAt(0)+"");
        tvname.setText(contcts.get(position).getName().trim());
        tvmails.setText(contcts.get(position).getName().trim());

        return convertView;
    }
}
