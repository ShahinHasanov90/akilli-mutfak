package com.akilliMutfak.data.model

data class Ingredient(
    val id: String,
    val name: String,
    val imageUrl: String,
    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val fiber: Float,
    val vitamins: Map<String, Float>,
    val minerals: Map<String, Float>,
    val servingSize: Int, // gram cinsinden
    val category: IngredientCategory,
    val allergens: List<String> = emptyList(),
    val seasonality: List<Month> = emptyList()
)

enum class IngredientCategory {
    VEGETABLE,
    FRUIT,
    MEAT,
    DAIRY,
    GRAIN,
    LEGUME,
    HERB,
    SPICE,
    OTHER
}

enum class Month {
    JANUARY,
    FEBRUARY,
    MARCH,
    APRIL,
    MAY,
    JUNE,
    JULY,
    AUGUST,
    SEPTEMBER,
    OCTOBER,
    NOVEMBER,
    DECEMBER
}