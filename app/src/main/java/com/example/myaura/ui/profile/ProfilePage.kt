package com.example.myaura.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Scaffold
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myaura.ui.profile.component.BottomNavBar
import com.example.myaura.ui.profile.component.ProfileHeader
import com.example.myaura.ui.profile.component.ProfileTabs

@Composable
fun ProfileScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    Scaffold(
        bottomBar = { BottomNavBar() }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .background(Color(0xFFF6F9FB))
                .verticalScroll(scrollState)

        ) {
            Spacer(modifier = Modifier.height(24.dp))
            ProfileHeader(navController)

            Spacer(modifier = Modifier.height(16.dp))
            ProfileTabs(navController)
        }
    }
}

@Preview (showBackground = true)
@Composable
fun ProfilePagePreview() {
    val navController = rememberNavController()
    ProfileScreen(navController = navController)
}