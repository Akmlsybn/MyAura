package com.example.myaura.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myaura.domain.model.UserProfile
import com.example.myaura.ui.profile.component.BottomNavBar
import com.example.myaura.ui.profile.component.ProfileHeader
import com.example.myaura.ui.profile.component.ProfileTabs

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profileState by viewModel.profileState.collectAsState()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(currentBackStackEntry) {
        if (currentBackStackEntry?.destination?.route == "profile_page"){
            viewModel.refreshData()
        }
    }

    Scaffold(
        bottomBar = { BottomNavBar() }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF6F9FB))
        ) {
            when (val state = profileState) {
                is ProfileState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ProfileState.Success -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        ProfileHeader(navController = navController, userProfile = state.userProfile)
                        Spacer(modifier = Modifier.height(16.dp))
                        ProfileTabs(navController)
                    }
                }
                is ProfileState.Error -> {
                    Text(text = state.message, modifier = Modifier.align(Alignment.Center))
                }
                is ProfileState.Empty -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        ProfileHeader(navController = navController, userProfile = UserProfile(name = "Pengguna Baru"))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Anda belum memiliki profil. Silakan edit profil Anda.", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}