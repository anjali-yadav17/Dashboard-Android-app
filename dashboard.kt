package com.example.rating

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Tech(
    val name: String,
    val category: String,
    var rating: Float = 0f
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = Color(0xFFF5F5F5)) {
                TechDashboard()
            }
        }
    }
}

@Composable
fun TechDashboard() {

    val techList = remember {
        mutableStateListOf(
            Tech("Java", "Programming"),
            Tech("Kotlin", "Programming"),
            Tech("Android", "Mobile"),
            Tech("AI", "Technology"),
            Tech("Cybersecurity", "Security")
        )
    }

    val categories = listOf("All", "Programming", "Mobile", "Technology", "Security")
    var selectedCategory by remember { mutableStateOf("All") }

    val filteredList = techList.filter {
        selectedCategory == "All" || it.category == selectedCategory
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()   // ✅ FIX TOP SPACE
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {

        CategoryDropdown(categories, selectedCategory) {
            selectedCategory = it
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredList.size) { index ->
                TechItem(tech = filteredList[index])
            }
        }
    }
}

@Composable
fun CategoryDropdown(
    categories: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = selected)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(onClick = {
                    onSelected(category)
                    expanded = false
                }) {
                    Text(text = category)
                }
            }
        }
    }
}

@Composable
fun TechItem(tech: Tech) {

    var rating by remember { mutableStateOf(tech.rating) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Text(
                text = tech.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = tech.category,
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            RatingBar(rating = rating) {
                rating = it
                tech.rating = it
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Rating: $rating",
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun RatingBar(
    rating: Float,
    onRatingChanged: (Float) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= rating)
                    Icons.Filled.Star
                else
                    Icons.Outlined.StarBorder,
                contentDescription = "Star",
                tint = Color(0xFFFFC107),
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onRatingChanged(i.toFloat()) }
            )
        }
    }
}
