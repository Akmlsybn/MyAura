package com.example.myaura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myaura.onBoarding.OnBoardingScreen
import com.example.myaura.onBoarding.OnBoardingScreen2
import com.example.myaura.onBoarding.OnBoardingScreen3
import com.example.myaura.splashScreen.SplashScreen
import com.example.myaura.login.SignIn
import com.example.myaura.ui.theme.MyAuraTheme
import com.example.myaura.login.ForgotPassScreen
import com.example.myaura.login.SignUp
import com.example.myaura.login.Verification
import com.example.myaura.profile.AddArticle
import com.example.myaura.profile.EditArticle
import com.example.myaura.profile.EditFormPort
import com.example.myaura.profile.EditPP
import com.example.myaura.profile.PortfolioForm
import com.example.myaura.profile.ProfileScreen

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
        composable("onboarding1") {
            OnBoardingScreen (navController = navController)
        }
        composable("onboarding2") {
            OnBoardingScreen2 (navController = navController)
        }
        composable("onboarding3") {
            OnBoardingScreen3 (navController = navController)
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
        composable("verification") {
            Verification  (navController=navController)
        }
        composable("edit_profile") {
            EditPP (navController = navController)
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
