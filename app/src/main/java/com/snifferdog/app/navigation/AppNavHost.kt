package com.snifferdog.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.snifferdog.app.ui.browser.BrowserScreen
import com.snifferdog.app.ui.home.HomeScreen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object Routes {
    const val Home = "home"
    const val Browser = "browser?url={url}"

    fun browser(url: String): String {
        val encoded = URLEncoder.encode(url, StandardCharsets.UTF_8.name())
        return "browser?url=$encoded"
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Home) {
        composable(Routes.Home) {
            HomeScreen(
                onSniff = { url -> navController.navigate(Routes.browser(url)) },
            )
        }
        composable(
            route = Routes.Browser,
            arguments = listOf(
                navArgument("url") {
                    type = NavType.StringType
                    defaultValue = ""
                },
            ),
        ) { entry ->
            val url = entry.arguments?.getString("url").orEmpty()
            BrowserScreen(
                targetUrl = url,
                onClose = { navController.popBackStack() },
            )
        }
    }
}
