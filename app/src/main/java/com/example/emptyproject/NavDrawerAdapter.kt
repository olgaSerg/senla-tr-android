package com.example.emptyproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NavDrawerAdapter(

    private var context: Context,
    private val itemsNavigation: ArrayList<NavDrawerItem>,
    private var clickNavigationDrawerMenu: OnClickNavigationDrawerMenu? = null,
) :
    RecyclerView.Adapter<NavDrawerAdapter.ItemViewHolder>() {

    private var selectedItem = 0

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNavigation: TextView = itemView.findViewById(R.id.text_view_navigation)
        val imageViewNavigation: ImageView = itemView.findViewById(R.id.image_view_navigation)
    }

    interface OnClickNavigationDrawerMenu {
        fun sendClickPosition(selectedNavItem: NavDrawerItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.navigation_drawer_item, parent, false)

        clickNavigationDrawerMenu = try {
            context as OnClickNavigationDrawerMenu
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement interface OnClickNavigationDrawerMenu")
        }

        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.textViewNavigation.text = itemsNavigation[position].name
        holder.imageViewNavigation.setImageResource(itemsNavigation[position].image)

        val itemColor = context.resources.getColor(
            if (selectedItem == position)
                R.color.color_checked_item
            else
                R.color.color_unchecked_item
        )

        holder.itemView.setBackgroundColor(itemColor)
        setTextViewNavigationOnClickListener(holder)
    }

    private fun setTextViewNavigationOnClickListener(holder: ItemViewHolder) {
        holder.textViewNavigation.setOnClickListener {
            val clickNavigationDrawerMenu = clickNavigationDrawerMenu ?: return@setOnClickListener

            val previousItem = selectedItem
            selectedItem = holder.adapterPosition

            notifyItemChanged(previousItem)
            notifyItemChanged(selectedItem)
            clickNavigationDrawerMenu.sendClickPosition(itemsNavigation[selectedItem])
        }
    }

    override fun getItemCount(): Int {
        return itemsNavigation.size
    }
}