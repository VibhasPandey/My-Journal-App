package com.example.myjournal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewRecyclerViewAdapter extends RecyclerView.Adapter<NewRecyclerViewAdapter.ViewHolder> {

        private Context context;
        private List<Page> pageList;

    public NewRecyclerViewAdapter(Context context, List<Page> pageList) {
            this.context = context;
            this.pageList = pageList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.inside_diary_layout,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Page page=pageList.get(position);
            Date date=page.getDateCreated();
            SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM", Locale.ENGLISH);
            String formattedDate = dateFormat.format(date);
            holder.dateHolder.setText(formattedDate);
            holder.textInit.setText("");

        }



        @Override
        public int getItemCount() {
            return pageList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView dateHolder;
            public TextView textInit;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                dateHolder=itemView.findViewById(R.id.date);
                textInit=itemView.findViewById(R.id.textInit);

            }

            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,EditPageActivity.class);
                context.startActivity(intent);


            }
        }

}