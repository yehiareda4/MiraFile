package com.yehia.miranewfilepiker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aait.miranewfilepiker.R;
import com.yehia.mira_file_picker.sheet.model.FileData;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private final OnClick onClick;
    private List<FileData> data;
    private Activity context;

    public ItemAdapter(List<FileData> data, Activity context, OnClick onClick) {
        this.data = data;
        this.context = context;
        this.onClick = onClick;
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
        holder.textView.setText(position + " - " + data.get(position).getPath() + "\n" + data.get(position).getThumbnail());

        holder.textView.setOnClickListener(view -> {
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(Uri.parse(data.get(position)), "application/pdf");
//
//            try {
//                context.startActivity(intent);
//            } catch (ActivityNotFoundException e) {
//                //if user doesn't have pdf reader instructing to download a pdf reader
//                Log.d("TAG", "onBindViewHolder: $e");
//            }
//
////            Intent i = new Intent(Intent.ACTION_VIEW);
////            i.setData(Uri.parse(data.get(position).substring(0, 1)));
////            context.startActivity(i);
            onClick.onClick(data.get(position));
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
