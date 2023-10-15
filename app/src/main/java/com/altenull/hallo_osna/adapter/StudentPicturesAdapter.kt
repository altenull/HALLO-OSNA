package com.altenull.hallo_osna.adapter

import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.altenull.hallo_osna.R
import com.altenull.hallo_osna.data.Picture

class StudentPicturesAdapter(
    private val pictures: List<Picture>,
    private val switchZoom: () -> Unit,
    private val finishBindingAllPictures: () -> Unit,
    private val progressBarColorFilter: PorterDuffColorFilter,
    private val progressBarLoadingAnim: Animation,
) :
    RecyclerView.Adapter<StudentPicturesAdapter.ViewHolder>() {
    /**
     * In order to scale all the pictures appropriately,
     * save all the views into this variable and provide it.
     */
    private var _pictureViews: MutableList<View> = mutableListOf()
    val pictureViews: MutableList<View> get() = _pictureViews

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView
        val progressBar: ProgressBar

        init {
            imageView = view.findViewById(R.id.student_picture)
            progressBar = view.findViewById(R.id.student_progress_bar)
        }
    }

    override fun getItemCount(): Int = pictures.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_student_scene, viewGroup, false)

        view.setOnClickListener {
            switchZoom()
        }

        pictureViews.add(view)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val targetPicture: Picture = pictures[position]

        holder.progressBar.indeterminateDrawable.colorFilter = progressBarColorFilter

        holder.imageView.load(targetPicture.url) {
            crossfade(true)
            target(
                onStart = {
                    holder.progressBar.progress = 0
                    holder.progressBar.startAnimation(progressBarLoadingAnim)
                    holder.progressBar.visibility = View.VISIBLE
                },
                onSuccess = {
                    holder.progressBar.clearAnimation()
                    holder.progressBar.visibility = View.GONE

                    holder.imageView.setImageDrawable(it)
                },
                onError = {
                    holder.progressBar.clearAnimation()
                    holder.progressBar.visibility = View.GONE

                    holder.imageView.updateLayoutParams {
                        width = ViewGroup.LayoutParams.WRAP_CONTENT
                        height = ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                    holder.imageView.setBackgroundResource(R.drawable.failed_picture)
                }
            )
        }

        if (position == pictures.size - 1) {
            finishBindingAllPictures()
        }
    }
}