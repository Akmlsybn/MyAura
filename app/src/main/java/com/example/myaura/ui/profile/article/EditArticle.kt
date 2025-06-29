package com.example.myaura.ui.profile.article

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myaura.R

@Composable
fun EditArticle(
    navController: NavController,
    viewModel: EditArticleViewModel = hiltViewModel()
) {
    val editState by viewModel.editState.collectAsState()
    val context = LocalContext.current

    var newImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        newImageUri = uri
    }

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
            // 1. Ganti warna background hardcode dengan warna dari tema
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (editState.isLoading && editState.title.isBlank()) {
            CircularProgressIndicator()
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    // 2. Ganti warna background box dengan warna dari tema
                    .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(8.dp))
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                val imageUrlToDisplay = remember(editState.imageUrl, newImageUri) {
                    newImageUri?.toString() ?: editState.imageUrl
                }

                if (imageUrlToDisplay.isNotBlank()) {
                    AsyncImage(
                        model = imageUrlToDisplay,
                        contentDescription = "Cover Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.ic_launcher_background),
                        error = painterResource(id = R.drawable.ic_launcher_background)
                    )
                } else {
                    Text(
                        text = "+ Ubah Gambar Sampul",
                        textAlign = TextAlign.Center,
                        // 3. Ganti warna teks dengan warna dari tema
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = editState.title, onValueChange = { viewModel.onTitleChange(it) }, label = { Text("Judul Artikel") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = editState.subTitle, onValueChange = { viewModel.onSubTitleChange(it) }, label = { Text("Sub-Judul / Deskripsi Singkat") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = editState.content, onValueChange = { viewModel.onContentChange(it) }, label = { Text("Konten Artikel") }, modifier = Modifier.fillMaxWidth().height(250.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.onUpdateClicked(newImageUri) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !editState.isLoading,
                // 4. Ganti warna tombol dengan warna dari tema
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                if (editState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(stringResource(R.string.SaveEdit))
                }
            }
        }
    }
}
