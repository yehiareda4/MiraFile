package com.yehia.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.yehia.mira_file_picker.FileUtils;
import com.yehia.myapplication.databinding.ActivityMainBinding;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;
    private final int REQUEST_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 132;
    private boolean mutiple = false;
    private List<String> selectedFiles;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        selectedFiles = new ArrayList<>();
//        adapter = new ItemAdapter(selectedFiles, this);
//        binding.rvFiles.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        binding.rvFiles.setAdapter(adapter);
//        binding.ivChooseFile.setOnClickListener(this);
//        binding.tvChooseFile.setOnClickListener(this);

//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frame, new Test2Fragment());
//        transaction.addToBackStack(null);
//        transaction.commit();
    }

    @Override
    public void onClick(View v) {
//        Intent intent = new Intent(this, MiraFilePickerActivity.class);
//        intent.putExtra("multiple", true);
//        intent.putExtra("type", MIME_TYPE_IMAGE);
//        intent.putExtra("requestCode", REQUEST_CODE);
//        startActivityForResult(intent, REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            if (data.getData() != null) {
                File file = FileUtils.getFile(this, data.getData());
                if (file != null) {
                    selectedFiles.add(file.getName());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "", Toast.LENGTH_SHORT);
                }
            }
            if (data.getClipData() != null) {
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    File file = FileUtils.getFile(this, uri);
                    if (file != null) {
                        selectedFiles.add(file.getName());
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "", Toast.LENGTH_SHORT);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
//        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            binding.ivChooseFile.setImageBitmap(photo);
//        }
    }
}
