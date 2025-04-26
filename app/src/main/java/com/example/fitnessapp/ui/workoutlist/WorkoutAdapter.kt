package com.example.fitnessapp.ui.workoutlist

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.data.model.Workout
import com.example.fitnessapp.ui.videoplayer.VideoPlayerActivity

class WorkoutAdapter(
    private var items: List<Workout>,
    private val onClick: (Workout) -> Unit
) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    inner class WorkoutViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.titleTextView)
        private val duration = view.findViewById<TextView>(R.id.durationTextView)
        private val description = view.findViewById<TextView>(R.id.descriptionTextView)

        fun bind(item: Workout) {

            "${getTypeEmoji(item.type)} ${item.title}".also { title.text = it }
            duration.text = item.duration
            description.text = item.description ?: ""

            itemView.setOnClickListener {
                onClick(item)
            }
        }

        private fun getTypeEmoji(type: Int): String {
            return when (type) {
                1 -> "\uD83C\uDFC3" // Бегун
                2 -> "\uD83D\uDCFD️" // Телевизор (эфир)
                3 -> "\uD83D\uDCAA" // Мышца
                else -> ""
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun update(newItems: List<Workout>) {
        items = newItems
        notifyDataSetChanged()
    }
}
