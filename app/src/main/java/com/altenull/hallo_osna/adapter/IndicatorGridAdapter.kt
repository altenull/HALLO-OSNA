package com.altenull.hallo_osna.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.altenull.hallo_osna.R
import com.altenull.hallo_osna.data.Picture

class IndicatorGridAdapter(
    private val pictures: List<Picture>,
    private val finishBindingAllIndicators: () -> Unit,
) :
    RecyclerView.Adapter<IndicatorGridAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int = pictures.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_indicator, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == pictures.size - 1) {
            finishBindingAllIndicators()
        }
    }
}