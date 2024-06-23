package com.gustavo.projeto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.gustavo.projeto.R
import com.gustavo.projeto.model.Crud1Model

class Crud1Adapter(private val crud1List: ArrayList<Crud1Model>) :
    RecyclerView.Adapter<Crud1Adapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener) {
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.crud1_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: Crud1Adapter.ViewHolder, position: Int) {
        val currentCrud = crud1List[position]
        holder.tvCrudName.text = currentCrud.nome
    }

    override fun getItemCount(): Int {
        return crud1List.size
    }

    inner class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val tvCrudName: TextView = itemView.findViewById(R.id.tvCrudName)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

}