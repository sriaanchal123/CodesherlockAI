package com.example.codesherlockai.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.codesherlockai.ui.agent.AgentExecutionScreen
import com.example.codesherlockai.ui.dashboard.DashboardScreen
import com.example.codesherlockai.ui.evidence.EvidenceDetailsScreen
import com.example.codesherlockai.ui.investigation.NewInvestigationScreen
import com.example.codesherlockai.ui.result.InvestigationResultScreen
import com.example.codesherlockai.ui.splash.SplashScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        // 1. Splash Screen
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // 2. Dashboard Screen
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNewInvestigationClick = {
                    navController.navigate(Screen.NewInvestigation.route)
                },
                onInvestigationClick = { issueNumber ->
                    navController.navigate(Screen.InvestigationResult.createRoute(issueNumber))
                }
            )
        }

        // 3. New Investigation Screen
        composable(Screen.NewInvestigation.route) {
            NewInvestigationScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onStartInvestigation = { issueNumber ->
                    navController.navigate(Screen.AgentExecution.createRoute(issueNumber)) {
                        popUpTo(Screen.NewInvestigation.route) { inclusive = true }
                    }
                }
            )
        }

        // 4. Agent Execution Screen
        composable(
            route = Screen.AgentExecution.route,
            arguments = listOf(navArgument("issueNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val issueNumber = backStackEntry.arguments?.getString("issueNumber") ?: "421"
            AgentExecutionScreen(
                issueNumber = "#$issueNumber",
                onNavigateBack = {
                    navController.popBackStack()
                },
                onViewResult = { num ->
                    navController.navigate(Screen.InvestigationResult.createRoute(num)) {
                        popUpTo(Screen.AgentExecution.route) { inclusive = true }
                    }
                }
            )
        }

        // 5. Investigation Result Screen
        composable(
            route = Screen.InvestigationResult.route,
            arguments = listOf(navArgument("issueNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val issueNumber = backStackEntry.arguments?.getString("issueNumber") ?: "421"
            InvestigationResultScreen(
                issueNumber = "#$issueNumber",
                onShowEvidence = { num ->
                    navController.navigate(Screen.EvidenceDetails.createRoute(num))
                },
                onBackToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }

        // 6. Evidence Details Screen
        composable(
            route = Screen.EvidenceDetails.route,
            arguments = listOf(navArgument("issueNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val issueNumber = backStackEntry.arguments?.getString("issueNumber") ?: "421"
            EvidenceDetailsScreen(
                issueNumber = "#$issueNumber",
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
