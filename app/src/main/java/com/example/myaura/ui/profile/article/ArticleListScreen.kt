package com.example.myaura.ui.profile.article

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myaura.R
import com.example.myaura.domain.model.Article
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.example.myaura.domain.model.UserProfile
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ArticleListViewModel = hiltViewModel()
) {
    val articlesState by viewModel.articlesState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.all_articles_title)) },
                actions = {
                    IconButton(onClick = { viewModel.fetchArticles() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(id = R.string.refresh_button)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (val state = articlesState) {
                is ArticleListState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is ArticleListState.Success -> {
                    if (state.articles.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                stringResource(id = R.string.no_articles_available),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.articles, key = { it.article.id }) { articleWithAuthor ->
                                ReadOnlyArticleCard(
                                    item = articleWithAuthor.article,
                                    author = articleWithAuthor.author,
                                    onClick = { navController.navigate("article_detail/${articleWithAuthor.author.uid}/${articleWithAuthor.article.id}") }
                                )
                            }
                        }
                    }
                }
                is ArticleListState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(state.message, color = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.fetchArticles() }) {
                                Text(stringResource(id = R.string.retry_button))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReadOnlyArticleCard(
    item: Article,
    author: UserProfile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val diff = remember(item.createdAt) {
        System.currentTimeMillis() - item.createdAt.seconds * 1000
    }

    val timeAgo = when {
        diff < TimeUnit.MINUTES.toMillis(1) -> stringResource(id = R.string.time_just_now)
        diff < TimeUnit.HOURS.toMillis(1) -> stringResource(id = R.string.time_minutes_format, TimeUnit.MILLISECONDS.toMinutes(diff))
        diff < TimeUnit.DAYS.toMillis(1) -> stringResource(id = R.string.time_hours_format, TimeUnit.MILLISECONDS.toHours(diff))
        diff < TimeUnit.DAYS.toMillis(7) -> stringResource(id = R.string.time_days_format, TimeUnit.MILLISECONDS.toDays(diff))
        else -> stringResource(id = R.string.time_long_ago)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = author.profilePictureUrl.ifEmpty { R.drawable.ic_launcher_background },
                    contentDescription = stringResource(id = R.string.author_profile_picture),
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
                    error = painterResource(id = R.drawable.ic_launcher_background)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = author.name.ifEmpty { stringResource(id = R.string.default_user_name) },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = author.job.ifEmpty { stringResource(id = R.string.no_job_title) },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = timeAgo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (item.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = stringResource(id = R.string.article_cover_image),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
                    error = painterResource(id = R.drawable.ic_launcher_background)
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = item.title, style = MaterialTheme.typography.titleLarge)
                if (item.subTitle.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.subTitle,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.content,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}