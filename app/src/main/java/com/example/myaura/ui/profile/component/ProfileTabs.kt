package com.example.myaura.ui.profile.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ProfileTabs(
    selectedTabIndex: Int,
    onTabClick: (Int) -> Unit
){
    val tabs = listOf("Resume", "Article")

    TabRow(
        selectedTabIndex = selectedTabIndex,
        // 1. Mengganti warna latar belakang hardcode dengan warna dari tema
        containerColor = MaterialTheme.colorScheme.surface,
        // 2. Mengatur warna konten default untuk TabRow
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabClick(index) },
                text = { Text(title) },
                // 3. Menambahkan warna untuk status terpilih dan tidak terpilih
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
