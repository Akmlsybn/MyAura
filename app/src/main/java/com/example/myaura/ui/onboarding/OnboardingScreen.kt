package com.example.myaura.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myaura.R
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(navController: NavController){
    val pagerState = rememberPagerState ( pageCount = { onboardingPages.size} )
    val scope = rememberCoroutineScope()

    Column (
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { pageIndex: Int ->
            val page = onboardingPages[pageIndex]
            OnboardingPageContent(page = page)
        }
        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 24.dp)
        ){
            repeat(pagerState.pageCount) {iteration ->
                val color = if (pagerState.currentPage == iteration) Color(0xFF141E61) else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(12.dp)
                )
            }
        }
        Button (
            onClick = {
                scope.launch {
                    if (pagerState.currentPage < pagerState.pageCount - 1) {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    } else {
                        navController.navigate("signing"){
                            popUpTo("onboarding") { inclusive = true}
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF141E61))
        ){
            val buttonText = if (pagerState.currentPage < pagerState.pageCount - 1) {
                stringResource(id = R.string.next)
            } else {
                "Selesai" //ini nanti dirubah ke string jangan lupa
            }
            Text(text = buttonText, color = Color.White)
        }
    }
}

@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column (
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(id = page.image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )
        Text(
            text = stringResource(id = page.title),
            color = Color(0xFF141E61),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = page.description),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}
