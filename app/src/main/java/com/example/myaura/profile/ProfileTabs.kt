package com.example.myaura.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color


@Composable
fun ProfileTabs(navController: NavController){
    var selectedTabIndex by remember{ mutableIntStateOf(0) }
    val tabs = listOf("Resume", "Article")

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White

            ) {
            tabs.forEachIndexed{index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {selectedTabIndex = index},
                    text = { Text(title) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        when(selectedTabIndex){
            0 -> ResumeContent(navController = navController)
            1 -> ArticleContent(navController = navController)
        }
    }
}