package com.example.venu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.venu.features.login.LoginScreen
import com.example.venu.core.core_ui.theme.VenuTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.venu.features.home.HomeRoute
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VenuTheme {
                val navController = rememberNavController()

                var isSignedIn by remember { mutableStateOf(false) }

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") {
                        LoginScreen(
                            onLoginClick = {
                                isSignedIn = true
                                navController.navigate("app"){
                                    popUpTo("login") { inclusive = true}
                                    launchSingleTop = true
                                }
                            },
                            onContinueAsGuestClick = {
                                isSignedIn = false
                                navController.navigate("app"){
                                    popUpTo(route="login"){ inclusive = true}
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                    composable("app") {
                        AppScaffold(isSignedIn = isSignedIn,
                            onSignInClick = {
                                navController.navigate("login") {
                                    popUpTo("app") { inclusive = true }
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

// to preview UI
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
                LoginScreen(onLoginClick = { navController.navigate("home") },
                    onContinueAsGuestClick = { navController.navigate("home") }
                )
            }
            composable("home") {
                HomeRoute()
            }
        }
    }
}