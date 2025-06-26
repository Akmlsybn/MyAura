package com.example.myaura.ui.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myaura.R

@Composable
fun BottomNavBar() {
    val items = listOf(
        Pair("Home", R.drawable.home),
        Pair("Search", R.drawable.search),
        Pair("Bookmark", R.drawable.bookmark),
        Pair("Article", R.drawable.newspaper),
        Pair("Settings", R.drawable.setting),
        Pair("Stats", R.drawable.stats),
    )

    var selectedIndex by remember { mutableIntStateOf(0) }
// scaffold , visualisai button nya jadi parent
    NavigationBar (
        containerColor = Color(0xFF141E61)
    ){
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Image(
                        painter = painterResource(id = item.second),
                        contentDescription = item.first,
                        modifier = Modifier.size(20.dp)
                    )
                },
                selected = selectedIndex == index,
                onClick = { selectedIndex = index },
                label = { Text(item.first) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White,
                )
            )
        }
    }
}