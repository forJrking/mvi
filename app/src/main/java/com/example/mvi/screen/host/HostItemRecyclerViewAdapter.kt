package com.example.mvi.screen.host

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvi.databinding.FragmentHostBinding
import com.example.mvi.repo.Contact

/**
 * [RecyclerView.Adapter] that can display a [Contact].
 * TODO: Replace the implementation with code for your data type.
 */
class HostItemRecyclerViewAdapter(private val onItemAction: (Contact) -> Unit) :
    ListAdapter<Contact, HostItemRecyclerViewAdapter.ViewHolder>(Contact.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentHostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HostItemRecyclerViewAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindTo(item)
    }

    inner class ViewHolder(binding: FragmentHostBinding) : RecyclerView.ViewHolder(binding.root) {
        private val avatar = binding.avatar
        private val idView: TextView = binding.itemNumber
        private val contentView: TextView = binding.content
        private val container: ViewGroup = binding.itemContainer
        fun bindTo(item: Contact) {
            idView.text = item.id
            contentView.text = item.content
            container.setOnClickListener {
                onItemAction.invoke(item)
            }
        }
    }
}