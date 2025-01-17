package com.khun.websocketsample.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.khun.websocketsample.presentation.ui.widget.Toolbar
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.khun.websocketsample.presentation.ui.theme.WebSocketSampleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WebSocketSampleTheme (darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                    content = {
                        val systemUiController = rememberSystemUiController()
                        val useDarkIcons = isSystemInDarkTheme()
                        SideEffect {
                            systemUiController.setSystemBarsColor(
                                color = if (useDarkIcons) White else Black,
                                darkIcons = useDarkIcons
                            )
                        }
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            MainScreen()
                        }
                    })

            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var canPop by remember { mutableStateOf(false) }
    val title = remember { mutableStateOf<String>("Crypto") }
    navController.addOnDestinationChangedListener { controller, _, _ ->
        canPop = Screen.Home.route !== (controller.currentBackStackEntry?.destination?.route)
        if(!canPop){
            title.value= "Crypto"
        }
    }
    val navigationIcon: (@Composable () -> Unit)? =
        if (canPop) {
            {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        } else {
            null
        }
    Scaffold(topBar = {
        Toolbar(
            navigationIcon = navigationIcon,
            title = title.value,
            onSwitchTheme = { /*TODO*/ }
        )
    }, content = { padding -> AppNavHost(navController = navController, title, padding) })
}


@Composable
fun AppNavHost(
    navController: NavHostController,
    title: MutableState<String>,
    padding: PaddingValues
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                padding = padding,
                onNavigateToDetail = {
                    title.value = it
                    navController.navigate("Detail/${it}")
                })
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            DetailScreen(backStackEntry.arguments?.getString("productId"))
        }
    }
}
