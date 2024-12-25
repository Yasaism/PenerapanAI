package com.example.tugaspenerapanai

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CourseAdapter(
    private val courseList: MutableList<Course>,
    private val deleteCourse: (Course) -> Unit,
    private val editCourse: (Course) -> Unit
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courseList[position]
        holder.bind(course)
    }

    override fun getItemCount(): Int = courseList.size

    fun updateCourses(newCourses: List<Course>) {
        courseList.clear()
        courseList.addAll(newCourses)
        notifyDataSetChanged()
    }

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val courseNameTextView: TextView = itemView.findViewById(R.id.textViewCourseName)
        private val courseScoreTextView: TextView = itemView.findViewById(R.id.textViewCourseScore)
        private val deleteButton: Button = itemView.findViewById(R.id.buttonDelete)
        private val editButton: Button = itemView.findViewById(R.id.buttonEdit)

        fun bind(course: Course) {
            courseNameTextView.text = course.name
            courseScoreTextView.text = course.score.toString()

            deleteButton.setOnClickListener {
                deleteCourse(course)
            }

            editButton.setOnClickListener {
                editCourse(course)
            }
        }
    }
}
