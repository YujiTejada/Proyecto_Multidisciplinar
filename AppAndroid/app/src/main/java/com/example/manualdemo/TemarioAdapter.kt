package com.example.manualdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TemarioAdapter(
    private val temarioList: ArrayList<Temario>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<TemarioAdapter.TemarioViewHolder>() {

    class TemarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivImagen)
        val textView: TextView = itemView.findViewById(R.id.tvNombre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemarioViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return TemarioViewHolder(view)
    }

    override fun getItemCount(): Int {
        return temarioList.size
    }

    override fun onBindViewHolder(holder: TemarioViewHolder, position: Int) {
        val temario = temarioList[position]
        holder.imageView.setImageResource(temario.Image)
        holder.textView.text = temario.name
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
