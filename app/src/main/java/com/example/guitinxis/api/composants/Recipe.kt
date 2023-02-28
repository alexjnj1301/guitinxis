package com.example.guitinxis.api.composants
import com.google.gson.Gson




// To parse the JSON, install Gson and do:
//
//   val Recipe = Recipe.fromJson(jsonString)

data class Recipe (
    val vegetarian: Boolean,
    val vegan: Boolean,
    val glutenFree: Boolean,
    val dairyFree: Boolean,
    val veryHealthy: Boolean,
    val cheap: Boolean,
    val veryPopular: Boolean,
    val sustainable: Boolean,
    val lowFodmap: Boolean,
    val weightWatcherSmartPoints: Long,
    val gaps: String,
    val preparationMinutes: Long,
    val cookingMinutes: Long,
    val aggregateLikes: Long,
    val healthScore: Long,
    val creditsText: String,
    val sourceName: String,
    val pricePerServing: Double,
    val extendedIngredients: List<ExtendedIngredient>,
    val id: Long,
    val title: String,
    val readyInMinutes: Long,
    val servings: Long,

    val sourceURL: String,

    val image: String,
    val imageType: String,
    val summary: String,
    val cuisines: List<Any?>,
    val dishTypes: List<String>,
    val diets: List<String>,
    val occasions: List<Any?>,
    val instructions: String,
    val analyzedInstructions: List<AnalyzedInstruction>,

    val originalID: Any? = null,

    val spoonacularSourceURL: String
) {
    public fun toJson() = Gson().toJson(this)

    companion object {
        public fun fromJson(json: String) = Gson().fromJson(json, Recipe::class.java)
    }
}

data class AnalyzedInstruction (
    val name: String,
    val steps: List<Step>
)

data class Step (
    val number: Long,
    val step: String,
    val ingredients: List<Ent>,
    val equipment: List<Ent>
)

data class Ent (
    val id: Long,
    val name: String,
    val localizedName: String,
    val image: String
)

data class ExtendedIngredient (
    val id: Long?,
    val aisle: String,
    val image: String,
    val consistency: Consistency,
    val name: String,
    val nameClean: String,
    val original: String,
    val originalName: String,
    val amount: Double?,
    val unit: String,
    val meta: List<String>?,
    val measures: Measures
)

enum class Consistency(val value: String) {
    Liquid("LIQUID"),
    Solid("SOLID");

    companion object {
        public fun fromValue(value: String): Consistency = when (value) {
            "LIQUID" -> Liquid
            "SOLID"  -> Solid
            else     -> throw IllegalArgumentException()
        }
    }
}

data class Measures (
    val us: Metric,
    val metric: Metric
)

data class Metric (
    val amount: Double,
    val unitShort: String,
    val unitLong: String
)
