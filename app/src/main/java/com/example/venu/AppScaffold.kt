package com.example.venu

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
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

@Composable
fun AppScaffold(
    isSignedIn: Boolean,
    isDarkMode: Boolean,
    currentUserDisplayName: String?,
    currentUserEmail: String?,
    onDarkModeChange: (Boolean) -> Unit,
    onSignInClick: () -> Unit,
    onEditProfileSave: (String) -> Unit,
    onSignOutClick: () -> Unit,
    currentUserReviewsCount: Int,
    currentUserEventsVisitedCount: Int
) {
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
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

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        hasLocationPermission =
            result[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        if (!isInspection && !askedForLocationPermission && !hasLocationPermission) {
            askedForLocationPermission = true
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == "home",
                    onClick = {
                        navController.navigate("home") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text("Home") },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Home"
                        )
                    }
                )

                NavigationBarItem(
                    selected = currentRoute?.startsWith("explore") == true,
                    onClick = {
                        navController.navigate("explore") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text("Explore") },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                )

                NavigationBarItem(
                    selected = currentRoute == "lists",
                    onClick = {
                        navController.navigate("lists") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text("Lists") },
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = "Lists"
                        )
                    }
                )

                NavigationBarItem(
                    selected = currentRoute == "profile",
                    onClick = {
                        navController.navigate("profile") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text("Profile") },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Profile"
                        )
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(
                bottom = padding.calculateBottomPadding()
            )
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
                val startDirections = backStackEntry.arguments?.getBoolean("startDirections") ?: false

                ExploreRoute(
                    hasLocationPermission = hasLocationPermission,
                    eventId = eventId,
                    startDirections = startDirections
                )
            }

            composable("lists") {
                ListsRoute()
            }

            composable("profile") {
                ProfileRoute(
                    isSignedIn = isSignedIn,
                    displayName = currentUserDisplayName,
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
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onSignOutClick = onSignOutClick
                )
            }

            composable("my_reviews") {
                MyReviewsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}