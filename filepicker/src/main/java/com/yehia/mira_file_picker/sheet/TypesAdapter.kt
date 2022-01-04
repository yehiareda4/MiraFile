package com.yehia.mira_file_picker.sheet

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yehia.mira_file_picker.MiraFilePickerActivity
import com.yehia.mira_file_picker.databinding.ItemTypesBinding
import com.yehia.mira_file_picker.sheet.model.FileData
import com.yehia.mira_file_picker.sheet.model.Type

class TypesAdapter(
    private val dataList: MutableList<Type>,
    val onItemClick: (Type) -> Unit
) : RecyclerView.Adapter<TypesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTypesBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvTitle.text = dataList[position].name
        holder.binding.civImage.setImageResource(dataList[position].image)

        holder.binding.root.setOnClickListener {
            onItemClick(dataList[position])
        }
    }

    class ViewHolder(val binding: ItemTypesBinding) : RecyclerView.ViewHolder(binding.root)
}
