package com.example.guitinxis.recipeDetails
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.guitinxis.R
import com.example.guitinxis.api.composants.Recipe
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import com.squareup.picasso.Picasso

class RecipeDetailsActivity : AppCompatActivity() {

    val apiKey = "d6c61ad93c444a01a0f0336d4c84faaa"
    val baseUrl = "https://api.spoonacular.com"

    private val client = OkHttpClient()
    private var json = JSONObject()
    private lateinit var recipeId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        recipeId = intent.getStringExtra("recipeId") ?: ""
        if (recipeId.isEmpty()) {
            Toast.makeText(this, "No recipe ID found", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Get recipe details from API
        getRecipeDetails(recipeId) { recipe ->
            // Update UI with recipe details
            displayRecipeDetails(recipe)
        }
    }

    private fun getRecipeDetails(recipeId: String, completion: (recipe: Recipe) -> Unit) {
        val request = Request.Builder()
            .url("$baseUrl/recipes/$recipeId/information?apiKey=$apiKey")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val jsonData = response.body()?.string()
                    ?: throw IOException("No data found")

                val recipe = Recipe.fromJson(jsonData)
                    ?: throw IOException("Could not parse recipe details")

                completion(recipe)
            }

            override fun onFailure(call: Call, e: IOException) {
                throw e
            }
        })
    }

    private fun displayRecipeDetails(recipe: Recipe) {
// Get recipe details from API
        getRecipeDetails(recipeId) { recipe ->
            // Update UI with recipe details
            displayRecipeDetails(recipe)
        }
    }
}