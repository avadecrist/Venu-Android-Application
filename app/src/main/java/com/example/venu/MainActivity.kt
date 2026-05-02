package com.example.venu

import android.os.Bundle
import androidx.activity.ComponentActivity
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
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.venu.auth.GoogleAuthClient
import com.example.venu.core.core_common.core_ui.theme.VenuTheme
import com.example.venu.features.home.HomeRoute
import com.example.venu.features.login.LoginScreen
import com.example.venu.features.onboarding.CompleteProfileScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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

            VenuTheme(
                darkTheme = isDarkMode
            ) {
                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                val googleAuthClient = remember {
                    GoogleAuthClient(context)
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
                                    val result = googleAuthClient.signIn()

                                    result
                                        .onSuccess { user ->
                                            currentUserEmail = user.email
                                            currentUserDisplayName = user.displayName
                                            isSignedIn = true
                                            isSigningIn = false

                                            navController.navigate("complete_profile") {
                                                popUpTo("login") {
                                                    inclusive = true
                                                }
                                                launchSingleTop = true
                                            }
                                        }
                                        .onFailure { error ->
                                            isSigningIn = false

                                            loginErrorMessage = when {
                                                error.message?.contains("Account reauth failed", ignoreCase = true) == true ->
                                                    "Google account re-auth failed. Remove and re-add the Google account on this device, then try again."

                                                error.message?.contains("No credentials", ignoreCase = true) == true ->
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
                                currentUserDisplayName = displayName

                                navController.navigate("app") {
                                    popUpTo("complete_profile") {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
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
                            onSignOutClick = {
                                scope.launch {
                                    googleAuthClient.signOut()

                                    isSignedIn = false
                                    currentUserEmail = null
                                    currentUserDisplayName = null
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
                HomeRoute()
            }
        }
    }
}