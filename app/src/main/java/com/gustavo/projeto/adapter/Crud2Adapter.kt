package com.gustavo.projeto.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gustavo.projeto.R
import com.gustavo.projeto.model.Crud1Model
import com.gustavo.projeto.model.Crud2Model
import com.gustavo.projeto.view.Crud2ListActivity

class Crud2Adapter(
    private val crud2List: ArrayList<Crud2Model>,
    private val crud1List: ArrayList<Crud1Model>
) : RecyclerView.Adapter<Crud2Adapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int, viewHolder: RecyclerView.ViewHolder, itemId: String)
    }

    private var mListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.crud2_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCrud2 = crud2List[position]

        // Calcula a porcentagem
        val sameCategoryCount = crud1List.count { it.category == currentCrud2.category }
        val percentage = if (currentCrud2.quantidade?.toIntOrNull() != null) {
            (sameCategoryCount.toDouble() / currentCrud2.quantidade!!.toInt()) * 100
        } else {
            0.0
        }

        holder.tvCrud2Category.text = currentCrud2.category
        holder.tvCrud2Quantidade.text = currentCrud2.quantidade
        holder.tvCrud2Percentage.text = "%.2f%%".format(percentage)

        holder.itemView.setOnClickListener {
            mListener?.onItemClick(position, holder, currentCrud2.id ?: "")
        }
    }

    override fun getItemCount(): Int {
        return crud2List.size
    }

    inner class ViewHolder(itemView: View, private val onItemClickListener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {

        val tvCrud2Category: TextView = itemView.findViewById(R.id.tvCrudCategory)
        val tvCrud2Quantidade: TextView = itemView.findViewById(R.id.tvCrudQuantidade)
        val tvCrud2Percentage: TextView = itemView.findViewById(R.id.tvCrudPercentage)
        val btnDeletar: ImageButton = itemView.findViewById(R.id.btnDeletar)
        val btnEditar: ImageButton = itemView.findViewById(R.id.btnEditar)

        init {
            btnDeletar.setOnClickListener {
                val context = itemView.context as Crud2ListActivity
                val selectedItem = crud2List[adapterPosition]
                context.deleteCrudItem(selectedItem.id ?: "", adapterPosition)
            }

            btnEditar.setOnClickListener {
                val context = itemView.context as Crud2ListActivity
                val selectedItem = crud2List[adapterPosition]
                context.editCrudItem(selectedItem)
            }
        }
    }
}