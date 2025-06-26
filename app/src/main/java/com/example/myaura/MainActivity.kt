package com.example.myaura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myaura.ui.splash.SplashScreen
import com.example.myaura.ui.login.SignIn
import com.example.myaura.ui.theme.MyAuraTheme
import com.example.myaura.ui.login.ForgotPassScreen
import com.example.myaura.ui.login.SignUp
import com.example.myaura.ui.onboarding.OnboardingScreen
import com.example.myaura.ui.profile.article.AddArticle
import com.example.myaura.ui.profile.article.EditArticle
import com.example.myaura.ui.profile.portfolio.EditFormPort
import com.example.myaura.ui.profile.portfolio.PortfolioForm
import com.example.myaura.ui.profile.ProfileScreen
import com.example.myaura.ui.profile.edit.EditProfileScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAura()
        }
    }
}

@Composable
fun MyAura() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController = navController)
        }
        composable("onboarding") {
            OnboardingScreen(navController = navController)
        }
        composable("signing") {
            SignIn(navController = navController)
        }
        composable("signup") {
            SignUp(navController = navController)
        }
        composable("forgot") {
            ForgotPassScreen(navController = navController)
        }
        composable("edit_profile") {
            EditProfileScreen (navController = navController)
        }
        composable("profile_page"){
            ProfileScreen(navController = navController)
        }
        composable("add_portfolio") {
            PortfolioForm(navController = navController)
        }
        composable("edit_portfolio"){
            EditFormPort(navController = navController)
        }
        composable("add_article"){
            AddArticle(navController = navController)
        }
        composable("edit_article"){
            EditArticle(navController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    MyAuraTheme {
        val navController = rememberNavController()
        SplashScreen(navController = navController)
    }
}