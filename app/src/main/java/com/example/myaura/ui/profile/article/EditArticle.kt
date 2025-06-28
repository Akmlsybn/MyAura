package com.example.myaura.ui.profile.article

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myaura.R

@Composable
fun EditArticle(
    navController: NavController,
    viewModel: EditArticleViewModel = hiltViewModel()
) {
    val editState by viewModel.editState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = editState.isSuccess) {
        if (editState.isSuccess) {
            Toast.makeText(context, "Artikel berhasil diperbarui!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
            viewModel.onNavigationDone()
        }
    }

    editState.error?.let {
        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F5F9))
            .padding(horizontal = 24.dp, vertical = 32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (editState.isLoading && editState.title.isBlank()) {
            CircularProgressIndicator()
        } else {
            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp).background(Color(0xFFE2E8F0), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "+ Tambah Gambar Sampul", textAlign = TextAlign.Center, color = Color.DarkGray)
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = editState.title, onValueChange = { viewModel.onTitleChange(it) }, label = { Text("Judul Artikel") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = editState.subTitle, onValueChange = { viewModel.onSubTitleChange(it) }, label = { Text("Sub-Judul / Deskripsi Singkat") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = editState.content, onValueChange = { viewModel.onContentChange(it) }, label = { Text("Konten Artikel") }, modifier = Modifier.fillMaxWidth().height(250.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.onUpdateClicked() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                enabled = !editState.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D1B66)),
                shape = RoundedCornerShape(24.dp)
            ) {
                if (editState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text(stringResource(R.string.SaveEdit), color = Color.White)
                }
            }
        }
    }
}