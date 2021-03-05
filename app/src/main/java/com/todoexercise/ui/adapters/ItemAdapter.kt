package com.todoexercise.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todoexercise.R
import com.todoexercise.extensions.loadUrls
import com.todoexercise.model.Item
import kotlinx.android.synthetic.main.view_item.view.*
import kotlin.properties.Delegates

class ItemAdapter(private val onItemSelectCallback: OnItemSelectCallback) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private var oldList = mutableListOf<Item>()
    private lateinit var mContext: Context

    private var itemList: List<Item> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_item, parent, false))
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(itemList[position], position)

    }

    fun updateContext(contextParam: Context){
        mContext = contextParam
    }

    fun updateList(newDataSet: MutableList<Item>) {
        oldList.clear()
        oldList.addAll(newDataSet)
        itemList = emptyList()
        itemList = oldList
        notifyDataSetChanged()
    }

    fun updateEmptyList() {
        oldList.clear()
        itemList = emptyList()
        itemList = oldList
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(obj: Item, position: Int) {
            var name    = ""
            var type    = ""
            obj.title?.let { name    = "Title: $it" }
            obj.createdAt.let { type = "Date Created: $it" }

            itemView.tvTitle.text          = name
            itemView.tvDateCreated.text    = type
            if (obj.iconUrl != ""){
                itemView.ivImage.loadUrls(mContext, obj.iconUrl)
            } else {
                itemView.ivImage.loadUrls(mContext, null)
            }

            itemView.userLayout.setOnClickListener { onItemSelectCallback.onSelectItem(obj)}
            itemView.userLayout.setOnLongClickListener {
                onItemSelectCallback.onLongSelectItem(obj)
                return@setOnLongClickListener true
            }
        }
    }

    interface OnItemSelectCallback {
        fun onSelectItem(item: Item)
        fun onLongSelectItem(item: Item)
    }

}