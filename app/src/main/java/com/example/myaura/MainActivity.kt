package com.example.myaura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import com.example.myaura.onBoarding.OnBoardingScreen
import com.example.myaura.onBoarding.OnBoardingScreen2
import com.example.myaura.onBoarding.OnBoardingScreen3
import com.example.myaura.splashScreen.SplashScreen
import com.example.myaura.login.SignIn
import com.example.myaura.ui.theme.MyAuraTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import com.example.myaura.login.ForgotPassScreen
import com.example.myaura.login.SignUp
import com.example.myaura.login.Verification

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAuraTheme {
                var currentScreen by remember { mutableStateOf("splash") }

                when (currentScreen) {
                    "splash" -> SplashScreen { currentScreen = "onboarding1" }
                    "onboarding1" -> OnBoardingScreen { currentScreen = "onboarding2" }
                    "onboarding2" -> OnBoardingScreen2 { currentScreen ="onboarding3" }
                    "onboarding3" -> OnBoardingScreen3 { currentScreen = "signing" }
                    "signing" -> SignIn { route -> currentScreen = route }
                    "signup" -> SignUp { route -> currentScreen = route }
                    "forgot" -> ForgotPassScreen { route -> currentScreen = route }
                    "verification" -> Verification { route -> currentScreen = route }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    MyAuraTheme {
        SplashScreen(onNavigateNext = {})
    }
}
