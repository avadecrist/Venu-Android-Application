package com.example.venu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.venu.auth.FirebaseAuthClient
import com.example.venu.auth.GoogleAuthClient
import com.example.venu.core.core_common.core_ui.theme.VenuTheme
import com.example.venu.core.core_data.remote.firestore.ReviewFireStoreRepository
import com.example.venu.core.core_data.remote.firestore.UserFirestoreRepository
import com.example.venu.features.home.HomeRoute
import com.example.venu.features.login.LoginScreen
import com.example.venu.features.onboarding.CompleteProfileScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT
            )
        )

        super.onCreate(savedInstanceState)

        setContent {
            var isDarkMode by rememberSaveable {
                mutableStateOf(false)
            }

            var isSignedIn by rememberSaveable {
                mutableStateOf(false)
            }

            var isSigningIn by rememberSaveable {
                mutableStateOf(false)
            }

            var loginErrorMessage by rememberSaveable {
                mutableStateOf<String?>(null)
            }

            var currentUserEmail by rememberSaveable {
                mutableStateOf<String?>(null)
            }

            var currentUserDisplayName by rememberSaveable {
                mutableStateOf<String?>(null)
            }

            var currentUserReviewsCount by rememberSaveable {
                mutableStateOf(0)
            }

            var currentUserEventsVisitedCount by rememberSaveable {
                mutableStateOf(0)
            }

            VenuTheme(
                darkTheme = isDarkMode,
                dynamicColor = false,
            ) {
                val context = LocalContext.current
                val scope = rememberCoroutineScope()

                val googleAuthClient = remember {
                    GoogleAuthClient(context)
                }

                val firebaseAuthClient = remember {
                    FirebaseAuthClient()
                }

                val userFirestoreRepository = remember {
                    UserFirestoreRepository()
                }

                val reviewFireStoreRepository = remember {
                    ReviewFireStoreRepository()
                }

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") {
                        LoginScreen(
                            isSigningIn = isSigningIn,
                            errorMessage = loginErrorMessage,
                            onLoginClick = {
                                if (isSigningIn) {
                                    return@LoginScreen
                                }

                                isSigningIn = true
                                loginErrorMessage = null

                                scope.launch {
                                    val googleResult = googleAuthClient.signIn()

                                    googleResult
                                        .onSuccess { googleUser ->
                                            try {
                                                val firebaseUser =
                                                    firebaseAuthClient.signInWithGoogleIdToken(
                                                        idToken = googleUser.idToken
                                                    )

                                                userFirestoreRepository.createUserIfMissing(
                                                    uid = firebaseUser.uid,
                                                    email = firebaseUser.email,
                                                    displayName = null,
                                                    photoUrl = firebaseUser.photoUrl
                                                )

                                                val firestoreUser =
                                                    userFirestoreRepository.getUser(firebaseUser.uid)

                                                currentUserEmail = firebaseUser.email
                                                currentUserDisplayName = firestoreUser?.displayName
                                                currentUserReviewsCount =
                                                    reviewFireStoreRepository.getReviewCountForCurrentUser()
                                                currentUserEventsVisitedCount = 0

                                                isSignedIn = true
                                                isSigningIn = false

                                                if (firestoreUser?.displayName.isNullOrBlank()) {
                                                    navController.navigate("complete_profile") {
                                                        popUpTo("login") {
                                                            inclusive = true
                                                        }
                                                        launchSingleTop = true
                                                    }
                                                } else {
                                                    navController.navigate("app") {
                                                        popUpTo("login") {
                                                            inclusive = true
                                                        }
                                                        launchSingleTop = true
                                                    }
                                                }
                                            } catch (error: Exception) {
                                                isSigningIn = false
                                                loginErrorMessage =
                                                    "${error::class.simpleName}: ${error.message ?: "Firebase sign-in failed."}"
                                            }
                                        }
                                        .onFailure { error ->
                                            isSigningIn = false

                                            loginErrorMessage = when {
                                                error.message?.contains(
                                                    "Account reauth failed",
                                                    ignoreCase = true
                                                ) == true ->
                                                    "Google account re-auth failed. Remove and re-add the Google account on this device, then try again."

                                                error.message?.contains(
                                                    "No credentials",
                                                    ignoreCase = true
                                                ) == true ->
                                                    "No Google account is available. Add a Google account to this device, then try again."

                                                else ->
                                                    "${error::class.simpleName}: ${error.message ?: "Google sign-in failed."}"
                                            }
                                        }
                                }
                            },
                            onContinueAsGuestClick = {
                                isSignedIn = false
                                currentUserEmail = null
                                currentUserDisplayName = null
                                currentUserReviewsCount = 0
                                currentUserEventsVisitedCount = 0
                                loginErrorMessage = null

                                navController.navigate("app") {
                                    popUpTo("login") {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        )
                    }

                    composable("complete_profile") {
                        CompleteProfileScreen(
                            suggestedDisplayName = currentUserDisplayName,
                            onContinueClick = { displayName ->
                                scope.launch {
                                    try {
                                        val firebaseUser = firebaseAuthClient.currentUser()

                                        if (firebaseUser != null) {
                                            userFirestoreRepository.updateDisplayName(
                                                uid = firebaseUser.uid,
                                                displayName = displayName
                                            )
                                        }

                                        currentUserDisplayName = displayName

                                        navController.navigate("app") {
                                            popUpTo("complete_profile") {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    } catch (error: Exception) {
                                        loginErrorMessage =
                                            "${error::class.simpleName}: ${error.message ?: "Failed to save display name."}"
                                    }
                                }
                            },
                            onSkipClick = {
                                navController.navigate("app") {
                                    popUpTo("complete_profile") {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        )
                    }

                    composable("app") {
                        AppScaffold(
                            isSignedIn = isSignedIn,
                            isDarkMode = isDarkMode,
                            currentUserDisplayName = currentUserDisplayName,
                            currentUserEmail = currentUserEmail,
                            currentUserReviewsCount = currentUserReviewsCount,
                            currentUserEventsVisitedCount = currentUserEventsVisitedCount,
                            onDarkModeChange = { darkMode ->
                                isDarkMode = darkMode
                            },
                            onSignInClick = {
                                navController.navigate("login") {
                                    popUpTo("app") {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            },
                            onEditProfileSave = { newDisplayName ->
                                scope.launch {
                                    try {
                                        val firebaseUser = firebaseAuthClient.currentUser()

                                        if (firebaseUser != null) {
                                            userFirestoreRepository.updateDisplayName(
                                                uid = firebaseUser.uid,
                                                displayName = newDisplayName
                                            )
                                        }

                                        currentUserDisplayName = newDisplayName
                                    } catch (error: Exception) {
                                        loginErrorMessage =
                                            "${error::class.simpleName}: ${error.message ?: "Failed to update display name."}"
                                    }
                                }
                            },
                            onSignOutClick = {
                                scope.launch {
                                    firebaseAuthClient.signOut()
                                    googleAuthClient.signOut()

                                    isSignedIn = false
                                    currentUserEmail = null
                                    currentUserDisplayName = null
                                    currentUserReviewsCount = 0
                                    currentUserEventsVisitedCount = 0
                                    loginErrorMessage = null

                                    navController.navigate("login") {
                                        popUpTo("app") {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "App Nav Preview")
@Composable
fun AppNavPreview() {
    VenuTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "login"
        ) {
            composable("login") {
                LoginScreen(
                    isSigningIn = false,
                    errorMessage = null,
                    onLoginClick = {
                        navController.navigate("home")
                    },
                    onContinueAsGuestClick = {
                        navController.navigate("home")
                    }
                )
            }

            composable("home") {
                HomeRoute(
                    displayName = "Explorer",
                    onNavigateToExploreDirections = {
                        // no explore route for preview
                    }
                )
            }
        }
    }
}