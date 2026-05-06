package com.example.venu

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.core.content.ContextCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.venu.features.explore.ExploreRoute
import com.example.venu.features.home.HomeRoute
import com.example.venu.features.lists.ListsRoute
import com.example.venu.features.profile.ProfileRoute
import com.example.venu.features.profile.menu.MyReviewsScreen
import com.example.venu.features.profile.menu.SettingsScreen
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.venu.features.reviews.MyReviewsRoute

@Composable
fun AppScaffold(
    isSignedIn: Boolean,
    isDarkMode: Boolean,
    currentUserDisplayName: String?,
    currentUserPhotoUrl: String?,
    currentUserEmail: String?,
    onDarkModeChange: (Boolean) -> Unit,
    onSignInClick: () -> Unit,
    onEditProfileSave: (String) -> Unit,
    onSignOutClick: () -> Unit,
    currentUserReviewsCount: Int,
    currentUserEventsVisitedCount: Int
) {
    val navController = rememberNavController()
    val hasLocationPermission = rememberLocationPermissionState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            VenuBottomBar(navController = navController)
        }
    ) { padding ->
        AppNavHost(
            navController = navController,
            hasLocationPermission = hasLocationPermission,
            isSignedIn = isSignedIn,
            isDarkMode = isDarkMode,
            currentUserDisplayName = currentUserDisplayName,
            currentUserPhotoUrl = currentUserPhotoUrl,
            currentUserEmail = currentUserEmail,
            currentUserReviewsCount = currentUserReviewsCount,
            currentUserEventsVisitedCount = currentUserEventsVisitedCount,
            onDarkModeChange = onDarkModeChange,
            onSignInClick = onSignInClick,
            onEditProfileSave = onEditProfileSave,
            onSignOutClick = onSignOutClick,
            modifier = Modifier.padding(bottom = padding.calculateBottomPadding())
        )
    }
}

@Composable
private fun VenuBottomBar(
    navController: NavController
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val items = listOf(
        BottomNavItem(
            route = "home",
            label = "Home",
            icon = Icons.Filled.Home,
            isSelected = currentRoute == "home"
        ),
        BottomNavItem(
            route = "explore",
            label = "Explore",
            icon = Icons.Filled.Search,
            isSelected = currentRoute?.startsWith("explore") == true
        ),
        BottomNavItem(
            route = "lists",
            label = "Lists",
            icon = Icons.AutoMirrored.Filled.List,
            isSelected = currentRoute == "lists"
        ),
        BottomNavItem(
            route = "profile",
            label = "Profile",
            icon = Icons.Filled.Person,
            isSelected = currentRoute == "profile"
        )
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp,
        windowInsets = WindowInsets.navigationBars
    ){
        items.forEach { item ->
            NavigationBarItem(
                selected = item.isSelected,
                onClick = {
                    navController.navigateBottomTab(item.route)
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

// NavHost
@Composable
private fun AppNavHost(
    navController: NavHostController,
    hasLocationPermission: Boolean,
    isSignedIn: Boolean,
    isDarkMode: Boolean,
    currentUserDisplayName: String?,
    currentUserPhotoUrl: String?,
    currentUserEmail: String?,
    currentUserReviewsCount: Int,
    currentUserEventsVisitedCount: Int,
    onDarkModeChange: (Boolean) -> Unit,
    onSignInClick: () -> Unit,
    onEditProfileSave: (String) -> Unit,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            HomeRoute(
                displayName = currentUserDisplayName,
                onNavigateToExploreDirections = { eventId ->
                    navController.navigate("explore?eventId=$eventId&startDirections=true") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(
            route = "explore?eventId={eventId}&startDirections={startDirections}",
            arguments = listOf(
                navArgument("eventId") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("startDirections") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            val startDirections =
                backStackEntry.arguments?.getBoolean("startDirections") ?: false

            ExploreRoute(
                hasLocationPermission = hasLocationPermission,
                eventId = eventId,
                startDirections = startDirections
            )
        }

        composable("lists") {
            ListsRoute(
                onNavigateToExploreDirections = { eventId ->
                    navController.navigate("explore?eventId=$eventId&startDirections=true") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable("profile") {
            ProfileRoute(
                isSignedIn = isSignedIn,
                displayName = currentUserDisplayName,
                photoUrl = currentUserPhotoUrl,
                email = currentUserEmail,
                reviewsCount = currentUserReviewsCount,
                eventsVisitedCount = currentUserEventsVisitedCount,
                onSignInClick = onSignInClick,
                onEditProfileSave = onEditProfileSave,
                onMyReviewsClick = {
                    navController.navigate("my_reviews")
                },
                onSettingsClick = {
                    navController.navigate("settings")
                }
            )
        }

        composable("settings") {
            SettingsScreen(
                isDarkMode = isDarkMode,
                onDarkModeChange = onDarkModeChange,
                onBackClick = { navController.popBackStack() },
                onSignOutClick = onSignOutClick
            )
        }

        composable("my_reviews") {
            MyReviewsRoute(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun rememberLocationPermissionState(): Boolean {
    val context = LocalContext.current
    val isInspection = LocalInspectionMode.current

    fun hasLocationPermissionNow(): Boolean {
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineGranted || coarseGranted
    }

    var hasLocationPermission by rememberSaveable {
        mutableStateOf(hasLocationPermissionNow())
    }

    var askedForLocationPermission by rememberSaveable {
        mutableStateOf(false)
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        hasLocationPermission =
            result[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        if (!isInspection && !askedForLocationPermission && !hasLocationPermission) {
            askedForLocationPermission = true
            launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    return hasLocationPermission
}

private fun NavController.navigateBottomTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

private data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val isSelected: Boolean
)
