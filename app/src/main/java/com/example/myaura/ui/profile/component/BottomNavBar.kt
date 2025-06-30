package com.example.myaura.ui.profile.component


import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myaura.R

data class BottomNavItem(
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
    val route: String
)

@Composable
fun BottomNavBar(navController: NavController) {
    val navItems = listOf(
        BottomNavItem(R.string.bottom_nav_profile, R.drawable.home, "profile_page"),
        BottomNavItem(R.string.bottom_nav_search, R.drawable.search, "search_screen"),
        BottomNavItem(R.string.bottom_nav_article, R.drawable.article, "article_list"),
        BottomNavItem(R.string.bottom_nav_settings, R.drawable.setting, "settings_screen")
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        navItems.forEach { item ->
            NavigationBarItem(
                icon = { Image(painter = painterResource(id = item.icon), contentDescription = stringResource(id = item.label), modifier = Modifier.size(20.dp)) },
                label = { Text(stringResource(id = item.label)) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.surfaceContainerHighest
                )
            )
        }
    }
}