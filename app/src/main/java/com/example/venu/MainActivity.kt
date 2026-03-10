package com.example.venu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.venu.features.home.HomeScreen
import com.example.venu.features.login.LoginScreen
import com.example.venu.core.core_ui.theme.VenuTheme

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.venu.features.explore.ExploreRoute
import com.example.venu.features.home.HomeRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VenuTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") {
                        LoginScreen(
                            onLoginClick = {
                                navController.navigate("app"){
                                    popUpTo("login") { inclusive = true}
                                }
                            }
                        )
                    }
                    composable("app") {
                        AppScaffold()
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
                LoginScreen(onLoginClick = { navController.navigate("home") })
            }
            composable("home") {
                HomeRoute()
            }
        }
    }
}