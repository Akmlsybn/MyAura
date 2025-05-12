package com.example.myaura.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Box
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController


@Composable
fun PortfolioForm(
    navController: NavController,
){
    val title = remember { mutableStateOf("") }
    val dateRange = remember { mutableStateOf("") }
    val skill = remember { mutableStateOf("") }
    val url = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F5F9))
            .padding(horizontal = 24.dp)
            .padding(top = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFFE2E8F0), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+\nAdd Image/File",
                textAlign = TextAlign.Center,
                color = Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title.value,
            onValueChange = { title.value = it },
            label = { Text( "Add Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = dateRange.value,
            onValueChange = { dateRange.value = it },
            label = { Text("DD-MM-YYYY until DD-MM-YYYY") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = skill.value,
            onValueChange = { skill.value = it },
            label = { Text( "Skill") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = url.value,
            onValueChange = { url.value = it },
            label = { Text("URL/ID") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description.value,
            onValueChange = { description.value = it },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D1B66)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("Post Portfolio", color = Color.White)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PortfolioFormPreview() {
    val navController = rememberNavController()
    PortfolioForm(navController = navController)
}
