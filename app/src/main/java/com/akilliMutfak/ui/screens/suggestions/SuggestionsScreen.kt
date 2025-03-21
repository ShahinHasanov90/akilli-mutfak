package com.akilliMutfak.ui.screens.suggestions

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionsScreen(
    detectedIngredients: List<Ingredient>,
    onBackClick: () -> Unit,
    onRecipeClick: (Recipe) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tarif Önerileri") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Rounded.ArrowBack, "Geri")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                DetectedIngredientsSection(ingredients = detectedIngredients)
            }

            item {
                Text(
                    "Yapabileceğiniz Tarifler",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            items(getSuggestedRecipes(detectedIngredients)) { recipe ->
                SuggestedRecipeCard(
                    recipe = recipe,
                    onClick = { onRecipeClick(recipe) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun DetectedIngredientsSection(ingredients: List<Ingredient>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Tespit Edilen Malzemeler",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            ingredients.forEach { ingredient ->
                IngredientItem(ingredient)
                if (ingredient != ingredients.last()) {
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }
    }
}

@Composable
fun IngredientItem(ingredient: Ingredient) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ingredient.imageUrl,
            contentDescription = ingredient.name,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                ingredient.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                "${ingredient.calories} kcal / 100g",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestedRecipeCard(
    recipe: Recipe,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = recipe.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        recipe.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                    RecipeRating(rating = recipe.rating)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                RecipeInfoRow(recipe)
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    "Malzeme Uyumu: ${recipe.matchPercentage}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                LinearProgressIndicator(
                    progress = recipe.matchPercentage / 100f,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primaryContainer
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                MissingIngredientsSection(recipe.missingIngredients)
            }
        }
    }
}

@Composable
fun RecipeRating(rating: Float) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Rounded.Star,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            rating.toString(),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
fun RecipeInfoRow(recipe: Recipe) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        RecipeInfoItem(
            icon = Icons.Rounded.Timer,
            text = "${recipe.cookingTime} dk",
            description = "Hazırlama Süresi"
        )
        RecipeInfoItem(
            icon = Icons.Rounded.LocalFireDepartment,
            text = "${recipe.calories} kcal",
            description = "Kalori"
        )
        RecipeInfoItem(
            icon = Icons.Rounded.Restaurant,
            text = "${recipe.servings} kişilik",
            description = "Porsiyon"
        )
    }
}

@Composable
fun RecipeInfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    description: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun MissingIngredientsSection(missingIngredients: List<String>) {
    if (missingIngredients.isNotEmpty()) {
        Column {
            Text(
                "Eksik Malzemeler",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            missingIngredients.forEach { ingredient ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        ingredient,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

fun getSuggestedRecipes(ingredients: List<Ingredient>): List<Recipe> {
    // Burada gerçek bir öneri algoritması kullanılacak
    return listOf(
        Recipe(
            name = "Sebzeli Makarna",
            imageUrl = "https://example.com/recipe1.jpg",
            cookingTime = 30,
            calories = 450,
            rating = 4.7f,
            servings = 4,
            matchPercentage = 85,
            missingIngredients = listOf("Makarna", "Zeytinyağı")
        ),
        Recipe(
            name = "Karışık Sebze Yemeği",
            imageUrl = "https://example.com/recipe2.jpg",
            cookingTime = 45,
            calories = 380,
            rating = 4.5f,
            servings = 6,
            matchPercentage = 95,
            missingIngredients = listOf("Salça")
        )
    )
}

data class Recipe(
    val name: String,
    val imageUrl: String,
    val cookingTime: Int,
    val calories: Int,
    val rating: Float,
    val servings: Int,
    val matchPercentage: Int,
    val missingIngredients: List<String>
)