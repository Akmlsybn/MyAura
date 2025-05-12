package com.example.myaura.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myaura.R

@Composable
fun EditFormPort(
    navController: NavController,
){
    val title = remember { mutableStateOf("") }
    val dateRange = remember { mutableStateOf("") }
    val skill = remember { mutableStateOf("") }
    val url = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F5F9))
            .padding(horizontal = 24.dp)
            .padding(top = 64.dp)
            .verticalScroll(scrollState),
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
                text = stringResource(R.string.Add_Box),
                textAlign = TextAlign.Center,
                color = Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title.value,
            onValueChange = { title.value = it },
            label = { Text( stringResource(R.string.EditTitle)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = dateRange.value,
            onValueChange = { dateRange.value = it },
            label = { Text(stringResource(R.string.years_until)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = skill.value,
            onValueChange = { skill.value = it },
            label = { Text(stringResource(R.string.skill) ) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = url.value,
            onValueChange = { url.value = it },
            label = { Text(stringResource(R.string.url)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description.value,
            onValueChange = { description.value = it },
            label = { Text(stringResource(R.string.Desc)) },
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
            Text(stringResource(R.string.SaveEdit), color = Color.White)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun EditFormPreview() {
    val navController = rememberNavController()
    EditFormPort(navController = navController)
}