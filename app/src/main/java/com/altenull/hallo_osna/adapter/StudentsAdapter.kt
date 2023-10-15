package com.altenull.hallo_osna.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.GridView
import androidx.recyclerview.widget.RecyclerView
import com.altenull.hallo_osna.constants.Constants
import com.altenull.hallo_osna.data.Student
import com.altenull.hallo_osna.data.StudentFrame

class StudentsAdapter(
    private val context: Context,
    private val chunkedStudents: List<List<Student>>,
    private val allStudents: List<Student>,
    private val studentFrame: StudentFrame,
    private val startRevealSceneStudent: () -> Unit,
) :
    RecyclerView.Adapter<StudentsAdapter.GridViewHolder>() {
    override fun getItemCount() = chunkedStudents.size

    /**
     * There's no separated xml layout for grid view, the grid view is created programmatically.
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): GridViewHolder {
        val gridView = GridView(viewGroup.context)

        gridView.numColumns = Constants.StudentGrid.Cols
        gridView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        return GridViewHolder(gridView)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val gridView = holder.gridView
        val studentsInGrid = chunkedStudents[position]

        val studentGridAdapter =
            StudentGridAdapter(
                context,
                studentsInGrid,
                allStudents,
                studentFrame,
                startRevealSceneStudent
            )
        gridView.adapter = studentGridAdapter
    }

    inner class GridViewHolder(val gridView: GridView) : RecyclerView.ViewHolder(gridView)
}