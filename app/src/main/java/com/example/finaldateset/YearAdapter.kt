package com.example.finaldateset

import CustomAnimation
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class YearAdapter(
    private val recyclerView: RecyclerView,
    private val onItemClicked: (Int) -> Unit,
    private val onCenterItemChanged: (Int) -> Unit
) : ListAdapter<String, YearAdapter.ViewHolder>(diff) {
    companion object {
        val diff = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var selectedPosition = RecyclerView.NO_POSITION
    private lateinit var layoutManager: LinearLayoutManager

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = getItem(position)
        if (position == selectedPosition) {
            holder.itemView.post {
                val itemView = layoutManager.findViewByPosition(selectedPosition) ?: return@post
                val itemViewCenter = itemView.top + itemView.height / 2
                val parentCenter = recyclerView.height / 2
                val scrollBy = itemViewCenter - parentCenter
                recyclerView.smoothScrollBy(0, scrollBy)
            }
        }
        holder.itemView.setOnClickListener {
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
//            onItemClicked(selectedPosition)
        }
        CustomAnimation().applyAnimation(recyclerView)
    }
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        layoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                highlightCenterItems()
            }
        })
    }

    private fun highlightCenterItems() {
        val parentCenter = recyclerView.height / 2
        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
        var minDistance = Int.MAX_VALUE
        var centerPosition = RecyclerView.NO_POSITION

        for (position in firstVisiblePosition..lastVisiblePosition) {
            val itemView = layoutManager.findViewByPosition(position) ?: continue
            val itemViewCenter = itemView.top + itemView.height / 2
            val distance = Math.abs(itemViewCenter - parentCenter)
            if (distance < minDistance) {
                minDistance = distance
                centerPosition = position
            }
        }

        if (centerPosition != RecyclerView.NO_POSITION) {
            onCenterItemChanged(centerPosition)
        }
    }

    fun updateData(newItems: List<String>) {
        selectedPosition = RecyclerView.NO_POSITION
        submitList(newItems)
    }
}
