package com.example.fitnessapp.ui.workoutlist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.ui.videoplayer.VideoPlayerActivity
import android.content.Intent

class WorkoutListActivity : AppCompatActivity() {

    private lateinit var viewModel: WorkoutListViewModel
    private lateinit var adapter: WorkoutAdapter
    private lateinit var searchEditText: EditText
    private lateinit var typeFilterSpinner: Spinner
    private lateinit var workoutRecyclerView: RecyclerView
    private lateinit var loadingProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_list)

        searchEditText = findViewById(R.id.searchEditText)
        typeFilterSpinner = findViewById(R.id.typeFilterSpinner)
        workoutRecyclerView = findViewById(R.id.workoutRecyclerView)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)

        viewModel = ViewModelProvider(this)[WorkoutListViewModel::class.java]

        adapter = WorkoutAdapter(emptyList()) { workout ->
            val intent = Intent(this, VideoPlayerActivity::class.java).apply {
                putExtra("id", workout.id)
                putExtra("title", workout.title)
                putExtra("duration", workout.duration)
                putExtra("description", workout.description)
            }
            startActivity(intent)
        }

        workoutRecyclerView.layoutManager = LinearLayoutManager(this)
        workoutRecyclerView.adapter = adapter

        setupSpinner()
        setupSearch()

        viewModel.workouts.observe(this) {
            filterWorkouts()
        }

        viewModel.isLoading.observe(this) {
            loadingProgressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) {
            it?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }

        viewModel.loadWorkouts()
    }

    private fun setupSpinner() {
        val types = listOf("Все", "Кардио 🏃", "Эфир 📺", "Силовые 💪")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types)
        typeFilterSpinner.adapter = spinnerAdapter

        typeFilterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                filterWorkouts()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupSearch() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterWorkouts()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun filterWorkouts() {
        val query = searchEditText.text.toString().lowercase()
        val typeSelected = typeFilterSpinner.selectedItemPosition

        val filteredList = viewModel.workouts.value?.filter { workout ->
            val matchesType = when (typeSelected) {
                0 -> true // Все
                1 -> workout.type == 1 // Кардио
                2 -> workout.type == 2 // Эфир
                3 -> workout.type == 3 // Силовые
                else -> true
            }
            val matchesQuery = workout.title.lowercase().contains(query)

            matchesType && matchesQuery
        } ?: emptyList()

        adapter.update(filteredList)
    }
}
