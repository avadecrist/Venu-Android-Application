package com.example.venu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.venu.core.core_common.core_ui.theme.VenuTheme
import com.example.venu.features.home.HomeRoute
import com.example.venu.features.login.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // To make status bar transparent
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

            VenuTheme(
                darkTheme = isDarkMode,
                dynamicColor = false,
            ) {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") {
                        LoginScreen(
                            onLoginClick = {
                                isSignedIn = true
                                navController.navigate("app") {
                                    popUpTo("login") {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            },
                            onContinueAsGuestClick = {
                                isSignedIn = false
                                navController.navigate("app") {
                                    popUpTo("login") {
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
                            onDarkModeChange = {
                                isDarkMode = it
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
                                isSignedIn = false
                                navController.navigate("login") {
                                    popUpTo("app") {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
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