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
import org.json.JSONObject
import java.io.IOException
import com.squareup.picasso.Picasso
import org.jsoup.Jsoup

class RecipeDetailsActivity : AppCompatActivity() {

    val apiKey = "3d6c4cd8322646b2b0e876693245bf3a"
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
        runOnUiThread {
            // Afficher l'image de la recette
            val recipeImageView = findViewById<ImageView>(R.id.imageView)
            Glide.with(this).load(recipe.image).into(recipeImageView)


            // Afficher le titre de la recette
            val titleTextView = findViewById<TextView>(R.id.nameTextView)
            titleTextView.text = recipe.title

            val instructionsTextView = findViewById<TextView>(R.id.instructionsTextView)
            val instructionsHtml = recipe.instructions
            val instructionsText = Jsoup.parse(instructionsHtml).text()
            instructionsTextView.text = instructionsText

            val ingredientsTextView = findViewById<TextView>(R.id.ingredientsTextView)
            val ingredientsList = recipe.extendedIngredients.map { it.nameClean } // extraire les noms des ingrédients
            val ingredientsString = ingredientsList.joinToString("\n") // convertir la liste en une chaîne de caractères avec un saut de ligne entre chaque ingrédient
            ingredientsTextView.text = ingredientsString
        }
    }
}