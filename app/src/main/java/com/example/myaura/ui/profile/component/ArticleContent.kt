package com.example.myaura.ui.profile.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myaura.domain.model.Article
import com.example.myaura.ui.profile.ProfileState
import com.example.myaura.ui.profile.ProfileViewModel

@Composable
fun ArticleContent(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profileState by viewModel.profileState.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var articleToDelete by remember { mutableStateOf<Article?>(null) }

    if (showDeleteDialog && articleToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Konfirmasi Hapus") },
            text = { Text("Apakah Anda yakin ingin menghapus artikel '${articleToDelete?.title}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        articleToDelete?.id?.let { viewModel.deleteArticle(it) }
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Tombol "Add Article"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Add Article",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            IconButton(onClick = { navController.navigate("add_article") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Article")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Logika untuk menampilkan daftar artikel
        when (val state = profileState) {
            is ProfileState.Success -> {
                if (state.articles.isEmpty()) {
                    Text("Anda belum menulis artikel.", modifier = Modifier.padding(vertical = 16.dp))
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(state.articles, key = { it.id }) { article ->
                            ArticleCard(
                                item = article,
                                onDelete = {
                                    articleToDelete = article
                                    showDeleteDialog = true
                                },
                                onEdit = {
                                    navController.navigate("edit_article/${article.id}")
                                }
                            )
                        }
                    }
                }
            }
            is ProfileState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            else -> {  }
        }
    }
}

@Composable
fun ArticleCard(item: Article, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.title, style = MaterialTheme.typography.titleLarge)
            if (item.subTitle.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = item.subTitle, style = MaterialTheme.typography.titleSmall, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Article")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Article")
                }
            }
        }
    }
}