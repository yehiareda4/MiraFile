package com.yehia.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private List<String> data;
    private Context context;

    public ItemAdapter(List<String> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.adapter_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder holder, int position) {
        holder.textView.setText(data.get(position));
        holder.itemView.setOnClickListener(v -> {
            File file = new File(data.get(position));
            if (file.exists()) {

                Uri pdfPath = Uri.fromFile(file);
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(data.get(position)), "application/pdf");

            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                //if user doesn't have pdf reader instructing to download a pdf reader
                Log.d("TAG", "onBindViewHolder: " + e);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);

        }
    }
}
