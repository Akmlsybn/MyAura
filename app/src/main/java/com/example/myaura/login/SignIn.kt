package com.example.myaura.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myaura.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun SignIn (
    onNavigate: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = stringResource(R.string.app_name),
            fontSize = 36.sp,
            color = Color(0xFF0D1B4C),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Column (horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()){
            Text(text = stringResource(R.string.SignIn), fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            Text(text = stringResource(R.string.Welcome_sign), fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.Normal )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
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
            onValueChange = {password = it},
            label = { Text(stringResource(R.string.L_Pass)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        TextButton(
            onClick = { onNavigate("forgot") },
            modifier = Modifier.align(Alignment.Start)
        )
        {
            Text(stringResource(R.string.forgot_password), color = Color.Black)
        }
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
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
            Text(stringResource(R.string.Google))
        }
        TextButton(
            onClick = { onNavigate("signup") }
        ) {
            Text(stringResource(R.string.SignUp_3), color = Color.Black)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInPreview() {
    SignIn(onNavigate = {})
}
