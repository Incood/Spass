package com.example.passwordmanager.navigation
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.passwordmanager.R
import com.example.passwordmanager.authenticator.LockScreen
import com.example.passwordmanager.ui.AddButton
import com.example.passwordmanager.ui.AddSiteScreen
import com.example.passwordmanager.ui.SiteApp
import com.example.passwordmanager.ui.theme.PasswordManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.detail_screen_toolbar_title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back),
                    )
                }
            }
        }
    )
}

enum class SiteID(@StringRes val title: Int) {
    ONCREATE(title = R.string.lockscreen),
    START(title = R.string.all_site),
    FINAL(title = R.string.add_site)
}

@Composable
fun ManagerApp(
    navController: NavHostController = rememberNavController()
) {
    var isAddButtonVisible  by remember { mutableStateOf(false) }
    var isAuthenticationSuccessful by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (isAuthenticationSuccessful) {
            DetailTopAppBar(
                canNavigateBack = false,
                navigateUp = { navController.navigateUp() }
            ) }
                 },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = if (isAuthenticationSuccessful) SiteID.START.name else SiteID.ONCREATE.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = SiteID.ONCREATE.name) {
                    LockScreen(
                        onNextButtonClicked = {
                            isAuthenticationSuccessful = true
                            navController.navigate(SiteID.START.name) {
                                popUpTo(SiteID.ONCREATE.name) {
                                    inclusive = true
                                }
                            }
                            isAddButtonVisible = true
                        }
                    )
                }
                composable(route = SiteID.START.name) {

                    SiteApp()
                }
                composable(route = SiteID.FINAL.name) {
                    AddSiteScreen {
                        navController.popBackStack(SiteID.START.name, inclusive = false)
                        isAddButtonVisible = true
                    }
                }
            }
        },
        floatingActionButton = {
            if (isAddButtonVisible ) {
                AddButton(
                    onNextButtonClicked = {
                        navController.navigate(SiteID.FINAL.name)
                        isAddButtonVisible  = false
                    },
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PasswordManagerTheme {
        ManagerApp()
    }
}