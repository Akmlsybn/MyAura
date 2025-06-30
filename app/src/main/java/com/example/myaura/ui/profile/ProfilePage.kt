package com.example.myaura.ui.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myaura.R
import com.example.myaura.domain.model.Article
import com.example.myaura.domain.model.PortfolioItem
import com.example.myaura.domain.model.UserProfile
import com.example.myaura.ui.profile.component.ArticleCard
import com.example.myaura.ui.profile.component.BottomNavBar
import com.example.myaura.ui.profile.component.PortfolioCard
import com.example.myaura.ui.profile.component.ProfileHeader
import com.example.myaura.ui.profile.component.ProfileTabs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(
    mainNavController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<Any?>(null) }

    LaunchedEffect(viewModel.isUserLoggedIn()) {
        if (!viewModel.isUserLoggedIn()) {
            mainNavController.navigate("signing") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(currentRoute) {
        if (currentRoute == "profile_page") {
            viewModel.refreshData()
        }
    }

    if (showDeleteDialog && itemToDelete != null) {
        val title = (itemToDelete as? PortfolioItem)?.title ?: (itemToDelete as? Article)?.title ?: ""
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(id = R.string.delete_confirmation_title)) },
            text = { Text(stringResource(id = R.string.delete_confirmation_message_generic, title)) },
            confirmButton = {
                Button(
                    onClick = {
                        when (val item = itemToDelete) {
                            is PortfolioItem -> item.id.let { viewModel.deletePortfolio(it) }
                            is Article -> item.id.let { viewModel.deleteArticle(it) }
                        }
                        showDeleteDialog = false
                        itemToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(id = R.string.delete_button))
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false; itemToDelete = null }) {
                    Text(stringResource(id = R.string.cancel_button))
                }
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { BottomNavBar(navController = mainNavController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = viewModel.profileState.collectAsState().value) {
                is ProfileState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is ProfileState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            ProfileHeader(navController = mainNavController, userProfile = state.userProfile)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        stickyHeader {
                            ProfileTabs(
                                selectedTabIndex = selectedTabIndex,
                                onTabClick = { selectedTabIndex = it }
                            )
                        }
                        when (selectedTabIndex) {
                            0 -> {
                                item { AddButtonRow(textResId = R.string.add_portfolio, onClick = { mainNavController.navigate("add_portfolio") }) }
                                if (state.portfolios.isEmpty()) {
                                    item {
                                        Text(
                                            stringResource(id = R.string.no_portfolios_added),
                                            modifier = Modifier.padding(16.dp),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                } else {
                                    items(state.portfolios, key = { "portfolio-${it.id}" }) { item ->
                                        PortfolioCard(
                                            modifier = Modifier
                                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                                .clickable { mainNavController.navigate("portfolio_detail/${item.id}") },
                                            item = item,
                                            onEdit = { mainNavController.navigate("edit_portfolio/${item.id}") },
                                            onDelete = {
                                                itemToDelete = item
                                                showDeleteDialog = true
                                            }
                                        )
                                    }
                                }
                            }
                            1 -> {
                                item { AddButtonRow(textResId = R.string.add_article, onClick = { mainNavController.navigate("add_article") }) }
                                if (state.articles.isEmpty()) {
                                    item {
                                        Text(
                                            stringResource(id = R.string.no_articles_written),
                                            modifier = Modifier.padding(16.dp),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                } else {
                                    items(state.articles, key = { "article-${it.id}" }) { item ->
                                        ArticleCard(
                                            modifier = Modifier
                                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                                .clickable { mainNavController.navigate("article_detail/${item.userId}/${item.id}") },
                                            item = item,
                                            onEdit = { mainNavController.navigate("edit_article/${item.id}") },
                                            onDelete = {
                                                itemToDelete = item
                                                showDeleteDialog = true
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
                is ProfileState.Error -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = state.message,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is ProfileState.Empty -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            ProfileHeader(navController = mainNavController, userProfile = UserProfile(name = stringResource(id = R.string.new_user)))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                stringResource(id = R.string.no_profile_prompt),
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun AddButtonRow(textResId: Int, onClick: () -> Unit) {
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val text = stringResource(id = textResId)
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        IconButton(onClick = onClick) {
            Icon(
                Icons.Default.Add,
                contentDescription = text,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}