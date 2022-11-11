package com.tutorial.hng9_stage3_task.utils

import com.tutorial.hng9_stage3_task.models.MapperNew
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tutorial.hng9_stage3_task.R
import com.tutorial.hng9_stage3_task.databinding.CustomParentViewholderBinding
import com.tutorial.hng9_stage3_task.models.main.CountriesItem

class ParentAdapter(private val onClick: RegisterClicks) : RecyclerView.Adapter<ParentAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = CustomParentViewholderBinding.bind(view)
        fun bind(categories: MapperNew) {
            binding.apply {
                categoryName.text = categories.char
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_parent_viewholder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos = differ.currentList[position]
        if (pos.items.isNotEmpty()){
            holder.bind(pos)
            addRV(holder.binding.ChildRV, pos.items)
        }


    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val diffCallback =
        object : DiffUtil.ItemCallback<MapperNew>() {
            override fun areItemsTheSame(
                oldItem: MapperNew,
                newItem: MapperNew
            ): Boolean {
                return (oldItem.char == newItem.char)
            }

            override fun areContentsTheSame(
                oldItem: MapperNew,
                newItem: MapperNew
            ): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<MapperNew>) = differ.submitList(list)

   private fun addRV(recyclerView: RecyclerView, list: List<CountriesItem>) {
        val adapter = ChildAdapter(onClick)
        adapter.submitList(list)
        recyclerView.adapter = adapter
    }


}
