package com.example.myaura.ui.profile.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myaura.domain.model.PortfolioItem
import com.example.myaura.ui.profile.ProfileState
import com.example.myaura.ui.profile.ProfileViewModel

@Composable
fun ResumeContent(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profileState by viewModel.profileState.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var portfolioToDelete by remember { mutableStateOf<PortfolioItem?>(null) }

    if (showDeleteDialog && portfolioToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Konfirmasi Hapus") },
            text = { Text("Apakah Anda yakin ingin menghapus portofolio '${portfolioToDelete?.title}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        portfolioToDelete?.id?.let { viewModel.deletePortfolio(it) }
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Add Portfolio",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            IconButton(onClick = { navController.navigate("add_portfolio") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Portfolio")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        when (val state = profileState) {
            is ProfileState.Success -> {
                if (state.portfolios.isEmpty()) {
                    Text("Anda belum menambahkan portofolio.", modifier = Modifier.padding(vertical = 16.dp))
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.portfolios, key = { it.id }) { portfolioItem ->
                            PortfolioCard(
                                item = portfolioItem,
                                onDelete = {
                                    portfolioToDelete = portfolioItem
                                    showDeleteDialog = true
                                },
                                onEdit = {
                                    navController.navigate("edit_portfolio/${portfolioItem.id}")
                                }
                            )
                        }
                    }
                }
            }
            else -> {
            }
        }
    }
}

@Composable
fun PortfolioCard(item: PortfolioItem, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.dateRange, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = item.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Skill: ${item.skill}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Portfolio")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Portfolio")
                }
            }
        }
    }
}