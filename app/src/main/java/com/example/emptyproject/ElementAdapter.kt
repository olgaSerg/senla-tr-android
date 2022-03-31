package com.example.emptyproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class ElementAdapter(private val elements: ArrayList<Element>) : RecyclerView.Adapter<ElementAdapter.ElementViewHolder>() {

    class ElementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val editTextItem: EditText = itemView.findViewById(R.id.edit_text_item)
        val imageButtonDelete: ImageButton = itemView.findViewById(R.id.image_button_delete)
        val imageButtonDecrease: ImageButton = itemView.findViewById(R.id.image_button_decrease)
        val imageButtonIncrease: ImageButton = itemView.findViewById(R.id.image_button_increase)
        val textViewValue: TextView = itemView.findViewById(R.id.text_view_value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item, parent, false)

        val holder = ElementViewHolder(itemView)
        holder.editTextItem.addTextChangedListener {
            elements[holder.adapterPosition].name = it.toString()
        }
        return holder
    }

    override fun onBindViewHolder(holder: ElementViewHolder, position: Int) {
        val adapterPosition = holder.adapterPosition
        holder.textViewValue.text = elements[adapterPosition].value.toString()
        holder.imageButtonDelete.setOnClickListener {
            removeItem(adapterPosition)
        }
        holder.imageButtonDecrease.setOnClickListener {
            if (elements[adapterPosition].value == 0) return@setOnClickListener
            decreaseQuantity(adapterPosition)
        }
        holder.imageButtonIncrease.setOnClickListener {
            increaseQuantity(adapterPosition)
        }
        holder.editTextItem.setText(elements[adapterPosition].name)
    }

    override fun getItemCount(): Int {
        return elements.size
    }

    private fun removeItem(position: Int) {
        elements.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun increaseQuantity(position: Int) {
        elements[position].value++
        notifyItemChanged(position)
    }
    private fun decreaseQuantity(position: Int) {
        elements[position].value--
        notifyItemChanged(position)
    }
}