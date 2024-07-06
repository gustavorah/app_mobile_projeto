package com.gustavo.projeto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gustavo.projeto.R
import com.gustavo.projeto.model.Crud1Model
import com.gustavo.projeto.view.Crud1ListActivity

class Crud1Adapter(
    private val crud1List: ArrayList<Crud1Model>
) : RecyclerView.Adapter<Crud1Adapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int, viewHolder: RecyclerView.ViewHolder, itemId: String)
    }

    private var mListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.crud1_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCrud = crud1List[position]
        holder.tvCrudName.text = currentCrud.nome
        holder.itemView.setOnClickListener {
            mListener?.onItemClick(position, holder, currentCrud.id ?: "")
        }
    }

    override fun getItemCount(): Int {
        return crud1List.size
    }

    inner class ViewHolder(itemView: View, private val onItemClickListener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {

        val tvCrudName: TextView = itemView.findViewById(R.id.tvCrudName)
        val btnDeletar: Button = itemView.findViewById(R.id.btnDeletar)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)

        init {
            btnDeletar.setOnClickListener {
                val context = itemView.context as Crud1ListActivity
                val selectedItem = crud1List[adapterPosition]
                context.deleteCrudItem( selectedItem.id ?: "",adapterPosition)
            }

            btnEditar.setOnClickListener {
//                val context = itemView.context as Crud1ListActivity
//                val selectedItem = crud1List[adapterPosition]
//                context.editCrudItem(adapterPosition, selectedItem.id ?: "")
            }
        }
    }
}
