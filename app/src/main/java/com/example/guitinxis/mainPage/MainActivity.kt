package com.example.guitinxis.mainPage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guitinxis.R
import com.example.guitinxis.api.ApiRepository

import com.bumptech.glide.Glide
import com.example.guitinxis.api.composants.Recipe
import com.example.guitinxis.recipeDetails.RecipeDetailsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var apiRepo: ApiRepository
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiRepo = ApiRepository()
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        apiRepo.getRandomRecipes(10) { recipes ->
            runOnUiThread {
                recyclerView.adapter = RecipeAdapter(recipes)
            }
        }
    }

    private inner class RecipeAdapter(private val recipes: List<Recipe>) :
        RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nameTextView: TextView = view.findViewById(R.id.nameTextView)
            val imageView: ImageView = view.findViewById(R.id.imageView)
            val cardView: RecyclerView = view.findViewById(R.id.recyclerView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_main, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val recipe = recipes[position]
            holder.nameTextView.text = recipe.sourceName
            Glide.with(this@MainActivity)
                .load(recipe.image)
                .into(holder.imageView)
            holder.cardView.setOnClickListener {
                apiRepo.getRecipeById(recipe.id) { recipeDetails ->
                    val intent = Intent(this@MainActivity, RecipeDetailsActivity::class.java)
                    //intent.putExtra("recipe", recipeDetails)
                    startActivity(intent)
                }
            }
        }

        override fun getItemCount(): Int {
            return recipes.size
        }
    }
}