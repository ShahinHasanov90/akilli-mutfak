package com.akilliMutfak.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onCameraClick: () -> Unit,
    onIngredientSelect: (String) -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        "Akıllı Mutfak",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                actions = {
                    IconButton(onClick = onFavoriteClick) {
                        Icon(Icons.Rounded.Favorite, "Favoriler")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCameraClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Rounded.CameraAlt, "Kamera")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { /* Arama işlemi */ },
                    active = false,
                    onActiveChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Malzeme veya tarif ara...") },
                    leadingIcon = { Icon(Icons.Rounded.Search, "Ara") }
                )
            }

            item {
                QuickActionButtons(
                    onScanClick = onCameraClick,
                    onSearchClick = { /* Arama */ },
                    onFavoritesClick = onFavoriteClick
                )
            }

            item {
                CategorySection(onCategoryClick = { /* Kategori seçildi */ })
            }

            item {
                RecentIngredientsSection(onIngredientClick = onIngredientSelect)
            }

            item {
                PopularRecipesSection()
            }
        }
    }
}

@Composable
fun QuickActionButtons(
    onScanClick: () -> Unit,
    onSearchClick: () -> Unit,
    onFavoritesClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ActionButton(
            icon = Icons.Rounded.QrCodeScanner,
            text = "Tara",
            onClick = onScanClick
        )
        ActionButton(
            icon = Icons.Rounded.Search,
            text = "Ara",
            onClick = onSearchClick
        )
        ActionButton(
            icon = Icons.Rounded.Favorite,
            text = "Favoriler",
            onClick = onFavoritesClick
        )
    }
}

@Composable
fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(56.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon,
                    contentDescription = text,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun CategorySection(onCategoryClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Kategoriler",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(getCategories()) { category ->
                CategoryChip(category = category, onClick = { onCategoryClick(category.name) })
            }
        }
    }
}

@Composable
fun CategoryChip(category: Category, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                category.icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                category.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun RecentIngredientsSection(onIngredientClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Son Kullanılan Malzemeler",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(getRecentIngredients()) { ingredient ->
                IngredientCard(ingredient = ingredient, onClick = { onIngredientClick(ingredient.name) })
            }
        }
    }
}

@Composable
fun IngredientCard(ingredient: Ingredient, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(120.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            AsyncImage(
                model = ingredient.imageUrl,
                contentDescription = ingredient.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    ingredient.name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    "${ingredient.calories} kcal",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun PopularRecipesSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Popüler Tarifler",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(getPopularRecipes()) { recipe ->
                RecipeCard(recipe = recipe)
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe) {
    Card(
        modifier = Modifier.width(280.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = recipe.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    recipe.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    RecipeInfo(
                        icon = Icons.Rounded.Timer,
                        text = "${recipe.cookingTime} dk"
                    )
                    RecipeInfo(
                        icon = Icons.Rounded.LocalFireDepartment,
                        text = "${recipe.calories} kcal"
                    )
                    RecipeInfo(
                        icon = Icons.Rounded.Star,
                        text = recipe.rating.toString()
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeInfo(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

data class Category(
    val name: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

data class Ingredient(
    val name: String,
    val imageUrl: String,
    val calories: Int
)

data class Recipe(
    val name: String,
    val imageUrl: String,
    val cookingTime: Int,
    val calories: Int,
    val rating: Float
)

fun getCategories() = listOf(
    Category("Et Yemekleri", Icons.Rounded.Restaurant),
    Category("Sebze Yemekleri", Icons.Rounded.Grass),
    Category("Çorbalar", Icons.Rounded.SoupKitchen),
    Category("Tatlılar", Icons.Rounded.Cake),
    Category("Salatalar", Icons.Rounded.LocalFlorist)
)

fun getRecentIngredients() = listOf(
    Ingredient("Domates", "https://example.com/tomato.jpg", 22),
    Ingredient("Soğan", "https://example.com/onion.jpg", 40),
    Ingredient("Patates", "https://example.com/potato.jpg", 77),
    Ingredient("Havuç", "https://example.com/carrot.jpg", 41)
)

fun getPopularRecipes() = listOf(
    Recipe("Karnıyarık", "https://example.com/recipe1.jpg", 45, 320, 4.5f),
    Recipe("Mercimek Çorbası", "https://example.com/recipe2.jpg", 30, 180, 4.8f),
    Recipe("Mantı", "https://example.com/recipe3.jpg", 60, 450, 4.7f)
)