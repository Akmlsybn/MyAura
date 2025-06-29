package com.example.myaura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myaura.ui.login.SignIn
import com.example.myaura.ui.login.SignUp
import com.example.myaura.ui.onboarding.OnboardingScreen
import com.example.myaura.ui.profile.ProfileScreen
import com.example.myaura.ui.profile.SettingsViewModel
import com.example.myaura.ui.profile.article.AddArticle
import com.example.myaura.ui.profile.article.ArticleDetailScreen
import com.example.myaura.ui.profile.article.EditArticle
import com.example.myaura.ui.profile.edit.EditProfileScreen
import com.example.myaura.ui.profile.portfolio.EditPortfolio
import com.example.myaura.ui.profile.portfolio.PortfolioDetailScreen
import com.example.myaura.ui.profile.portfolio.PortfolioForm
import com.example.myaura.ui.splash.SplashScreen
import com.example.myaura.ui.theme.MyAuraTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import com.example.myaura.ui.profile.component.BottomNavBar
import com.example.myaura.ui.profile.SearchScreen
import com.example.myaura.ui.profile.SettingsScreen
import com.example.myaura.ui.profile.article.ArticleListScreen


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
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val isDarkMode by settingsViewModel.isDarkMode.collectAsState()

    MyAuraTheme(
        darkTheme = isDarkMode,
        dynamicColor = false
    ) {
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
            composable("profile_page") {
                ProfileScreen(mainNavController = navController)
            }
            composable("search_screen") {
                Scaffold(
                    bottomBar = { BottomNavBar(navController = navController) }
                ) { innerPadding ->
                    SearchScreen(navController = navController, modifier = Modifier.padding(innerPadding))
                }
            }
            composable("article_list") {
                Scaffold(
                    bottomBar = { BottomNavBar(navController = navController) }
                ) { innerPadding ->
                    ArticleListScreen(navController = navController, modifier = Modifier.padding(innerPadding))
                }
            }
            composable("settings_screen") {
                Scaffold(
                    bottomBar = { BottomNavBar(navController = navController) }
                ) { innerPadding ->
                    SettingsScreen(mainNavController = navController, modifier = Modifier.padding(innerPadding))
                }
            }
            composable("edit_profile") {
                EditProfileScreen(navController = navController)
            }
            composable("add_portfolio") {
                PortfolioForm(navController = navController)
            }
            composable(
                route = "edit_portfolio/{portfolioId}",
                arguments = listOf(navArgument("portfolioId") { type = NavType.StringType })
            ) {
                EditPortfolio(navController = navController)
            }
            composable("add_article") {
                AddArticle(navController = navController)
            }
            composable(
                route = "edit_article/{articleId}",
                arguments = listOf(navArgument("articleId") { type = NavType.StringType })
            ) {
                EditArticle(navController = navController)
            }
            composable(
                route = "portfolio_detail/{portfolioId}",
                arguments = listOf(navArgument("portfolioId") { type = NavType.StringType })
            ) {
                PortfolioDetailScreen(navController = navController)
            }
            composable(
                route = "article_detail/{articleId}",
                arguments = listOf(navArgument("articleId") { type = NavType.StringType })
            ) {
                ArticleDetailScreen(navController = navController)
            }
            composable(
                route = "article_detail/{userId}/{articleId}",
                arguments = listOf(
                    navArgument("userId") { type = NavType.StringType },
                    navArgument("articleId") { type = NavType.StringType }
                )
            ) {
                ArticleDetailScreen(navController = navController)
            }
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