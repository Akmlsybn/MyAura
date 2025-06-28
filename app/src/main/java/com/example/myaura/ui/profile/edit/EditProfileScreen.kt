package com.example.myaura.ui.profile.edit

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myaura.R

@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val editState by viewModel.editState.collectAsState()
    val context = LocalContext.current

    // State untuk menyimpan URI gambar yang baru dipilih dari galeri
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher untuk membuka galeri gambar
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // Saat pengguna memilih gambar, URI-nya akan disimpan di state ini
        imageUri = uri
    }

    LaunchedEffect(key1 = editState.isSuccess) {
        if (editState.isSuccess) {
            Toast.makeText(context, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
            viewModel.onNavigationDone()
        }
    }

    editState.error?.let {
        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (editState.isLoading && editState.name.isBlank()) {
            CircularProgressIndicator()
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.clickable { imagePickerLauncher.launch("image/*") }
            ) {
                AsyncImage(
                    model = imageUri ?: editState.profilePictureUrl.ifEmpty { R.drawable.ic_launcher_background },
                    contentDescription = "Profile Picture",
                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
                    modifier = Modifier.size(100.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Edit Photo")

            Spacer(modifier = Modifier.height(24.dp))

            // Semua OutlinedTextField dihubungkan ke ViewModel
            OutlinedTextField(value = editState.name, onValueChange = { viewModel.onNameChange(it) }, label = { Text("Nama") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = editState.job, onValueChange = { viewModel.onJobChange(it) }, label = { Text("Pekerjaan") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = editState.tagline, onValueChange = { viewModel.onTaglineChange(it) }, label = { Text("Tagline") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = editState.bio, onValueChange = { viewModel.onBioChange(it) }, label = { Text("Bio") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = editState.linkedin, onValueChange = { viewModel.onLinkedinChange(it) }, label = { Text("LinkedIn URL") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = editState.github, onValueChange = { viewModel.onGithubChange(it) }, label = { Text("GitHub URL") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = editState.instagram, onValueChange = { viewModel.onInstagramChange(it) }, label = { Text("Instagram URL") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol Save mengirim semua data, termasuk URI gambar baru
            Button(
                onClick = {
                    viewModel.onSaveClicked(newImageUri = imageUri)
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !editState.isLoading,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF141E61))
            ) {
                if (editState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text(stringResource(R.string.Save), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}