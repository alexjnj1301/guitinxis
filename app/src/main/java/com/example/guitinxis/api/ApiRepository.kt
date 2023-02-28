package com.example.guitinxis.api

import com.example.guitinxis.api.composants.Recipe
import com.example.guitinxis.api.composants.Results
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class ApiRepository {
    val apiKey = "d6c61ad93c444a01a0f0336d4c84faaa"
    val basUrl = "https://api.spoonacular.com"

    private val client = OkHttpClient()
    private var json = JSONObject()

    fun getRandomRecipes(number: Int, completion: (recipes: ArrayList<Recipe>) -> Unit) {

        // Make sure the parameter is between 1 and 10
        if(number<1 || number>100){
            throw IOException("Please enter a number between 1 and 100")
        }

        val request = Request.Builder()
            .url("$basUrl/recipes/random?number=$number&apiKey=$apiKey")
            .build()

        client.newCall(request).enqueue(object : Callback {
            // If the request was successful
            override fun onResponse(call: Call, response: Response){
                val jsonData = client.newCall(request).execute().body()?.string()
                    ?: throw IOException("No data found")

                json = JSONObject(jsonData)

                if(json.getJSONArray("recipes").length() == 0){
                    throw IOException("No recipes found")
                }

                var myRecipes = ArrayList<Recipe>()
                val recipeArray = json.getJSONArray("recipes")

                for (i in 0 until recipeArray.length() - 1) {
                    val actualRecipe = recipeArray.getJSONObject(i)
                    Recipe.fromJson(actualRecipe.toString())?.let {
                        myRecipes.add(it)
                    }
                }

                completion(myRecipes)
            }

            // If the request failed
            override fun onFailure(call: Call, e: IOException) {
                completion(ArrayList<Recipe>())
                throw e
            }
        })
        //return recipes
    }

    fun researchByName(name: String, completion: (recipes: ArrayList<Results>) -> Unit) {
        if (name == "") {
            throw IOException("Please enter a name")
        }

        val request = Request.Builder()
            .url("$basUrl/recipes/complexSearch?query=$name&apiKey=$apiKey")
            .build()

        client.newCall(request).enqueue(object : Callback {
            // If the request was successful
            override fun onResponse(call: Call, response: Response){
                val jsonData = client.newCall(request).execute().body()?.string()
                    ?: throw IOException("No data found")

                json = JSONObject(jsonData)

                if(json.getJSONArray("results").length() == 0){
                    throw IOException("No recipes found")
                }

                val recipeArray = json.getJSONArray("results")
                var myRecipes = ArrayList<Results>()
                for (i in 0 until recipeArray.length()) {
                    val actualRecipe = recipeArray.getJSONObject(i)
                    Results.fromJson(actualRecipe.toString())?.let {
                        myRecipes.add(it)
                    }

                }
                completion(myRecipes)
            }

            // If the request failed
            override fun onFailure(call: Call, e: IOException) {
                throw e
            }
        })
    }

    fun getRecipeById(id: Long, completion: (recipe: Recipe) -> Unit){
        val request = Request.Builder()
            .url("$basUrl/recipes/$id/information?apiKey=$apiKey")
            .build()

        client.newCall(request).enqueue(object : Callback {
            // If the request was successful
            override fun onResponse(call: Call, response: Response) {
                val jsonData = client.newCall(request).execute().body()?.string()
                    ?: throw IOException("No data found")

                completion(Recipe.fromJson(jsonData))
            }

            // If the request failed
            override fun onFailure(call: Call, e: IOException) {
                throw e
            }
        })
    }
}