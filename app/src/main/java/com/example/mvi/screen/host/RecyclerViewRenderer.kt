package com.example.mvi.screen.host

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arch.mvi.view.BaseRenderer
import com.example.mvi.repo.Contact
import javax.inject.Inject

class RecyclerViewRenderer @Inject constructor() : BaseRenderer() {

    private var adapter: HostItemRecyclerViewAdapter? = null

    fun init(view: View, columnCount: Int, onItemAction: (Contact) -> Unit) {
        with(view as RecyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = HostItemRecyclerViewAdapter(onItemAction).also {
                this@RecyclerViewRenderer.adapter = it
            }
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    fun render(state: HostState?) {
        when (state) {
            is HostState.Data -> {
                adapter?.submitList(state.data)
            }
            is HostState.Empty -> {
                //
            }
            else -> {}
        }
    }

    override fun clear() {
        println("clear RecyclerViewRenderer")
    }
}