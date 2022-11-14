package com.tutorial.hng9_stage3_task.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tutorial.hng9_stage3_task.R
import com.tutorial.hng9_stage3_task.databinding.CustomViewholderBinding
import com.tutorial.hng9_stage3_task.models.main.CountriesItem
import com.tutorial.hng9_stage3_task.models.main.Translations

class ChildAdapter(private val onClick: RegisterClicks, private val translate:Boolean,private val language:String) : RecyclerView.Adapter<ChildAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
       private val binding = CustomViewholderBinding.bind(view)
        fun bind(items: CountriesItem) {
            binding.apply {
                countryLogo.load(items.flags?.png)
                if (translate){
                    countryText.text = items.translations?.let { getCountryName(language, it) }
                    capitalText.text = items.capital?.get(0)
                }else{
                    countryText.text = items.name?.common
                    capitalText.text = items.capital?.get(0)
                }

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

    fun getCountryName(value:String,translation: Translations):String? = when(value){
        "Ara"-> translation.ara?.common

        "Bre"-> translation.bre?.common

        "Ces"-> translation.ces?.common

        "Cym"-> translation.cym?.common

        "Deu"-> translation.deu?.common

        "Est"-> translation.est?.common

        "Fin"-> translation.fin?.common

        "Fra"-> translation.fra?.common

        "Hrv"-> translation.hrv?.common

        "Hun"-> translation.hun?.common

        "Ita"-> translation.ita?.common

        "Jpn"-> translation.jpn?.common

        "Kor"-> translation.kor?.common

        "Nld"-> translation.nld?.common

        "Per"-> translation.per?.common

        "Pol"-> translation.pol?.common

        "Por"-> translation.por?.common

        "Rus"-> translation.rus?.common

        "Slk"-> translation.slk?.common

        "Spa"-> translation.spa?.common

        "Swe"-> translation.swe?.common

        "Tur"-> translation.tur?.common

        "Urd"-> translation.urd?.common

        "Zho"-> translation.zho?.common
        else -> "Translation Not Available"
    }
}