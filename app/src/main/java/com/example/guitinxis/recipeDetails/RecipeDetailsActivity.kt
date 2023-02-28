package com.example.guitinxis.recipeDetails
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.guitinxis.R
import com.example.guitinxis.api.composants.Recipe
import okhttp3.*
import java.io.IOException
import org.jsoup.Jsoup

class RecipeDetailsActivity : AppCompatActivity() {

    val apiKey = "3373002c024c4c41a6fdef3b77c37199"
    val baseUrl = "https://api.spoonacular.com"

    private val client = OkHttpClient()
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
        runOnUiThread {
            val recipeImageView = findViewById<ImageView>(R.id.imageView)
            Glide.with(this).load(recipe.image).into(recipeImageView)

            val titleTextView = findViewById<TextView>(R.id.nameTextView)
            titleTextView.text = recipe.title

            val instructionsTextView = findViewById<TextView>(R.id.instructionsTextView)
            val instructionsHtml = recipe.instructions
            val instructionsText = Jsoup.parse(instructionsHtml).text()
            instructionsTextView.text = instructionsText

            val ingredientTitleTextView = findViewById<TextView>(R.id.ingredientTitleTextView)
            ingredientTitleTextView.text = "INGREDIENTS"

            val ingredientsTextView = findViewById<TextView>(R.id.ingredientsTextView)
            val ingredientsString = recipe.extendedIngredients.joinToString(separator = "\n") {
                "${it.amount} ${it.unit} ${it.name}"
            }
            ingredientsTextView.text = ingredientsString

            val preparationTime = findViewById<TextView>(R.id.preparationTimeTextView)
            preparationTime.text = "Preparation time: ${recipe.readyInMinutes} minutes"

            val recipeURL = findViewById<TextView>(R.id.recipeURLTextView)
            recipeURL.text = "Recipe URL: ${recipe.sourceURL}"

            val imageVeg = findViewById<ImageView>(R.id.imageViewVeg)
            if (recipe.vegetarian) {
                Glide.with(this).load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRIHRGgBuhriFmjyi3J-QLbty-66J-mhoQefw&usqp=CAU").into(imageVeg)
            } else if (recipe.vegan){
                Glide.with(this).load("https://www.shutterstock.com/image-vector/icon-vegan-food-260nw-778394854.jpg").into(imageVeg)
            }
        }
    }
}