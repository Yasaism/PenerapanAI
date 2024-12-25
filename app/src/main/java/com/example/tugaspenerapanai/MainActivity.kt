package com.example.tugaspenerapanai

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class MainActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var scoreEditText: EditText
    private lateinit var addButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var courseAdapter: CourseAdapter

    private val courseList = mutableListOf<Course>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText)
        scoreEditText = findViewById(R.id.scoreEditText)
        addButton = findViewById(R.id.addButton)
        recyclerView = findViewById(R.id.recyclerView)

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        courseAdapter = CourseAdapter(courseList, ::deleteCourse, ::editCourse)
        recyclerView.adapter = courseAdapter

        // Button to add course
        addButton.setOnClickListener {
            if (validateInput()) {
                val courseName = nameEditText.text.toString()
                val courseScore = scoreEditText.text.toString().toFloat()
                val course = Course(courseName, courseScore)
                courseList.add(course)
                courseAdapter.notifyDataSetChanged()
                clearInputs()
            }
        }

        // Button to generate recommendation based on courses
        findViewById<Button>(R.id.recommendButton).setOnClickListener {
            fetchRecommendation()
        }
    }

    private fun fetchRecommendation() {
        val courseNames = courseList.joinToString(", ") { it.name }
        val courseScores = courseList.joinToString(", ") { it.score.toString() }

        val prompt = getString(R.string.recommendation_prompt, courseNames, courseScores)

        // Call to GPT API using Retrofit
        CoroutineScope(Dispatchers.IO).launch {
            val recommendation = fetchRecommendationFromGPT(prompt)
            withContext(Dispatchers.Main) {
                showRecommendationDialog(recommendation)
            }
        }
    }

    private suspend fun fetchRecommendationFromGPT(prompt: String): String {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/") // Replace with actual GPT API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(OpenAIService::class.java)
        val response = service.getRecommendation(
            OpenAIPromptRequest(prompt)
        )

        return response.choices?.firstOrNull()?.text ?: getString(R.string.no_recommendation)
    }

    private fun showRecommendationDialog(recommendation: String) {
        AlertDialog.Builder(this)
            .setTitle(R.string.recommendation_title)
            .setMessage(recommendation)
            .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun validateInput(): Boolean {
        val name = nameEditText.text.toString()
        val scoreText = scoreEditText.text.toString()

        if (name.isBlank()) {
            nameEditText.error = getString(R.string.course_name_required)
            return false
        }
        if (scoreText.isBlank()) {
            scoreEditText.error = getString(R.string.course_score_required)
            return false
        }
        if (scoreText.toFloatOrNull() == null || scoreText.toFloat() !in 0f..100f) {
            scoreEditText.error = getString(R.string.invalid_score)
            return false
        }
        return true
    }

    private fun clearInputs() {
        nameEditText.text.clear()
        scoreEditText.text.clear()
    }

    private fun deleteCourse(course: Course) {
        courseList.remove(course)
        courseAdapter.notifyDataSetChanged()
    }

    private fun editCourse(course: Course) {
        // Inflate the dialog layout
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_course, null)
        val nameEditText = dialogView.findViewById<EditText>(R.id.nameEditText)
        val scoreEditText = dialogView.findViewById<EditText>(R.id.scoreEditText)

        // Set the current course values in the dialog
        nameEditText.setText(course.name)
        scoreEditText.setText(course.score.toString())

        // Build and show the dialog
        AlertDialog.Builder(this)
            .setTitle(R.string.edit_course)
            .setView(dialogView)
            .setPositiveButton(R.string.save) { _, _ ->
                // Get the edited values
                val newName = nameEditText.text.toString()
                val newScore = scoreEditText.text.toString().toFloatOrNull() ?: 0f

                // Update the course object
                course.name = newName
                course.score = newScore

                // Notify the adapter that the data has changed
                courseAdapter.notifyDataSetChanged()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

}

// Retrofit API Interface
interface OpenAIService {
    @POST("completions")
    suspend fun getRecommendation(@Body request: OpenAIPromptRequest): OpenAIResponse
}

// Request body for GPT API
data class OpenAIPromptRequest(
    val prompt: String,
    val max_tokens: Int = 150,
    val temperature: Double = 0.7
)

// Response from GPT API
data class OpenAIResponse(
    val choices: List<Choice>?
)

data class Choice(
    val text: String
)
