package com.tutorial.hng9_stage3_task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tutorial.hng9_stage3_task.databinding.CustomViewholderBinding
import com.tutorial.hng9_stage3_task.models.main.CountriesItem

class CountriesAdapter : ListAdapter<CountriesItem, CountriesAdapter.ViewHolder>(DiffCallBack) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = CustomViewholderBinding.bind(view)
        fun bind(data: CountriesItem) {
            binding.msg.text = data.toString()
        }
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<CountriesItem>() {
        override fun areItemsTheSame(oldItem: CountriesItem, newItem: CountriesItem) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: CountriesItem, newItem: CountriesItem) =
            oldItem.capital == newItem.capital
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_viewholder, parent, false)
        return ViewHolder(view)

    }


    override fun onBindViewHolder(holder: CountriesAdapter.ViewHolder, position: Int) {
        val pos = getItem(position)
        if (pos != null) {
            holder.bind(pos)
        }
    }

}
