package com.devlens.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devlens.presentation.screens.ActivitiesScreen
import com.devlens.presentation.screens.ActivityDetailScreen
import com.devlens.presentation.screens.ActivityFormScreen
import com.devlens.presentation.screens.DashboardScreen
import com.devlens.presentation.screens.InsightsScreen
import com.devlens.presentation.screens.OnboardingScreen
import com.devlens.presentation.screens.ReportScreen
import com.devlens.presentation.viewmodels.DevLensViewModel

@Composable
fun DevLensNavHost(
    viewModel: DevLensViewModel,
    onboardingComplete: Boolean
) {
    val navController = rememberNavController()
    val activities by viewModel.activities.collectAsStateWithLifecycle()
    val dashboard by viewModel.dashboard.collectAsStateWithLifecycle()
    val report by viewModel.report.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val bottomRoutes = listOf(Route.Dashboard.path, Route.Activities.path, Route.AddActivity.path, Route.Insights.path, Route.Report.path)

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomRoutes) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == Route.Dashboard.path,
                        onClick = { navController.navigateSingleTop(Route.Dashboard.path) },
                        icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
                        label = { Text("Inicio") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Route.Activities.path,
                        onClick = { navController.navigateSingleTop(Route.Activities.path) },
                        icon = { Icon(Icons.Default.Assignment, contentDescription = null) },
                        label = { Text("Atividades") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Route.AddActivity.path,
                        onClick = { navController.navigateSingleTop(Route.AddActivity.path) },
                        icon = { Icon(Icons.Default.Add, contentDescription = null) },
                        label = { Text("Novo") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Route.Insights.path,
                        onClick = { navController.navigateSingleTop(Route.Insights.path) },
                        icon = { Icon(Icons.Default.Insights, contentDescription = null) },
                        label = { Text("Insights") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Route.Report.path,
                        onClick = { navController.navigateSingleTop(Route.Report.path) },
                        icon = { Icon(Icons.Default.Analytics, contentDescription = null) },
                        label = { Text("Relatorio") }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = if (onboardingComplete) Route.Dashboard.path else Route.Onboarding.path,
            modifier = Modifier.padding(padding)
        ) {
            composable(Route.Onboarding.path) {
                OnboardingScreen(
                    onContinue = {
                        viewModel.completeOnboarding()
                        navController.navigate(Route.Dashboard.path) {
                            popUpTo(Route.Onboarding.path) { inclusive = true }
                        }
                    }
                )
            }
            composable(Route.Dashboard.path) {
                DashboardScreen(
                    metrics = dashboard,
                    activities = activities,
                    onAdd = { navController.navigate(Route.AddActivity.path) },
                    onActivityClick = { navController.navigate(Route.Detail.create(it)) }
                )
            }
            composable(Route.AddActivity.path) {
                ActivityFormScreen(
                    state = formState,
                    onTitleChange = viewModel::updateTitle,
                    onDescriptionChange = viewModel::updateDescription,
                    onMinutesChange = viewModel::updateMinutes,
                    onTypeChange = viewModel::updateType,
                    onTagsChange = viewModel::updateTags,
                    onDateChange = viewModel::updateActivityDate,
                    onVisibilityChange = viewModel::updateVisibility,
                    onSave = {
                        viewModel.saveActivity { id ->
                            navController.navigate(Route.Detail.create(id))
                        }
                    }
                )
            }
            composable(Route.Activities.path) {
                ActivitiesScreen(
                    activities = activities,
                    onActivityClick = { navController.navigate(Route.Detail.create(it)) }
                )
            }
            composable(Route.Insights.path) {
                InsightsScreen(activities = activities)
            }
            composable(Route.Report.path) {
                ReportScreen(report = report)
            }
            composable(
                route = Route.Detail.path,
                arguments = listOf(navArgument("activityId") { type = NavType.LongType })
            ) { entry ->
                val id = entry.arguments?.getLong("activityId") ?: 0L
                ActivityDetailScreen(
                    activity = activities.firstOrNull { it.id == id },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

private fun androidx.navigation.NavHostController.navigateSingleTop(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
