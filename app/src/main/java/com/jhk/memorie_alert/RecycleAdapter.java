package com.jhk.memorie_alert;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import  java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ItemViewHolder> {
    private  ArrayList<Data> listData = new ArrayList<>();
    AlertDialog dialog;
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return  new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder Holder, int position) {
     Holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    void addItem(Data data) {
        listData.add(data);
    }



    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        private TextView textView1;
        private TextView textView2;
        private Switch sw;
        private LinearLayout layout;

        ItemViewHolder(View itemView){
            super(itemView);
            final Context context = (MainActivity)MainActivity.mContext;
            layout = itemView.findViewById(R.id.layout);
            layout.setOnLongClickListener(this);
            textView1 = itemView.findViewById(R.id.textView1);
           // textView1.setOnLongClickListener(this);
            textView2 = itemView.findViewById(R.id.textView2);
            //textView2.setOnLongClickListener(this);
            sw = itemView.findViewById(R.id.switch1);
           SharedPreferences sharedPreferences =  context.getSharedPreferences("com.jhk.memorie_alert",context.MODE_PRIVATE);
            boolean check = sharedPreferences.getBoolean("status", true);
            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        Toast.makeText(((MainActivity)MainActivity.mContext), "checked",Toast.LENGTH_LONG).show();
                        ((MainActivity) context).StopService();
                        ((MainActivity) context).startService(textView1.getText().toString(), textView2.getText().toString());


                    }else {
                        Toast.makeText(((MainActivity)MainActivity.mContext), "unchecked",Toast.LENGTH_LONG).show();
                        ((MainActivity) context).StopService();
                    }
                }
            });
        }

        void onBind(Data data) {
            textView1.setText(data.getTitle());
            textView2.setText(data.getContent());
        }

        @Override
        public boolean onLongClick(View v) {

            String s = textView2.getText().toString();
            create_dialog(s);

            return true;
        }

        public void create_dialog(String s){
            final String input = s;
            AlertDialog.Builder alert = new AlertDialog.Builder((MainActivity)MainActivity.mContext);
            alert.setTitle("삭제하기");
            alert.setMessage("정말 삭제하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((MainActivity)MainActivity.mContext).delete_items(input);
                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            dialog = alert.create();
            dialog.show();
        }
    }
}
