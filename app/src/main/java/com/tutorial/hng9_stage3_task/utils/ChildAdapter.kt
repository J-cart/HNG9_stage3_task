package com.tutorial.hng9_stage3_task.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tutorial.hng9_stage3_task.R
import com.tutorial.hng9_stage3_task.databinding.CustomViewholderBinding
import com.tutorial.hng9_stage3_task.models.main.CountriesItem

class ChildAdapter(private val onClick: RegisterClicks) : RecyclerView.Adapter<ChildAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
       private val binding = CustomViewholderBinding.bind(view)
        fun bind(items: CountriesItem) {
            binding.apply {
                countryText.text = items.name?.common
                binding.root.setOnClickListener {
                    listener?.invoke(items)
                    onClick.onChildClicked(items)
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_viewholder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos = differ.currentList[position]
        holder.bind(pos)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    private val diffCallback = object : DiffUtil.ItemCallback<CountriesItem>() {
        override fun areItemsTheSame(
            oldItem: CountriesItem,
            newItem: CountriesItem
        ): Boolean {
            return (oldItem.name == newItem.name)
        }

        override fun areContentsTheSame(
            oldItem: CountriesItem,
            newItem: CountriesItem
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<CountriesItem>) = differ.submitList(list)

    private var listener:((CountriesItem)->Unit)? = null

    fun onAdapterClick(onclick:(CountriesItem)->Unit){
        listener = onclick
    }

}