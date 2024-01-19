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

        // Configurar la escala de la imagen para que ocupe todo el espacio posible
        holder.imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        // Establecer la imagen en el ImageView
        holder.imageView.setImageResource(temario.Image)

        // Establecer el texto en el TextView
        holder.textView.text = temario.name

        // Configurar el clic del elemento
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }


    interface OnItemClickListener {
        abstract val supportFragmentManager: Any

        fun onItemClick(position: Int)
    }
}
