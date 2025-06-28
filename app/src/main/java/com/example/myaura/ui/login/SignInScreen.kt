package com.example.myaura.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import com.example.myaura.R
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun SignIn(
    navController: NavHostController,
    viewModel: SignInViewModel = hiltViewModel()
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val signInState by viewModel.signInState.collectAsState()
    val context = LocalContext.current

    val scrollState= rememberScrollState()

    LaunchedEffect(key1 = signInState) {
        if (signInState.isSuccess) {
            navController.navigate("profile_page") { popUpTo(0) }
        }
        signInState.error?.let { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = stringResource(R.string.app_name),
            fontSize = 36.sp,
            color = Color(0xFF141E61),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.SignIn), fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            Text(text = stringResource(R.string.Welcome_sign), fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.Normal)
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.L_Email)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.L_Pass)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = { viewModel.onSignInClicked(email, password)},
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = !signInState.isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF141E61),
                contentColor = Color.White
            )
        ) {
            Text(stringResource(R.string.SignIn), fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(R.string.Or), color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Google Icon",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.Google), color = Color.Black)
        }
        TextButton(
            onClick = { navController.navigate("signup") }  // Navigasi ke halaman Sign Up
        ) {
            Text(stringResource(R.string.SignUp_3), color = Color.Black)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInPreview() {
    val navController = rememberNavController()
    SignIn(navController = navController)
}
