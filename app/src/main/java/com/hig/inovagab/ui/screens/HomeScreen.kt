package com.hig.inovagab.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hig.inovagab.R
import com.hig.inovagab.data.utils.UserRole




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(role: UserRole, onLogout: () -> Unit, onNavigateToIdeas: () -> Unit, onNavigateToProjects: () -> Unit, onNavigateToStrategies: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = stringResource(R.string.logout_title)
                        )
                    }

                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            when (role) {
                UserRole.OPERATOR -> OperatorContent()
                UserRole.MANAGER -> ManagerContent()
                UserRole.LEADER -> LeaderContent()
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onNavigateToIdeas,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(stringResource(R.string.inovations_idea_text))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateToProjects,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)){
                Text(stringResource(R.string.ongoing_projects_text))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateToStrategies,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)){
                Text(stringResource(R.string.ongoing_strategies_text))
            }
        }
    }

}
@Composable
private fun OperatorContent() {
    Text(text = stringResource(R.string.p_operator), style = MaterialTheme.typography.headlineSmall)
    Spacer(modifier = Modifier.height(8.dp))
    Text(stringResource(R.string.home_operator))
}
@Composable
private fun ManagerContent() {
    Text(text = stringResource(R.string.p_manager), style = MaterialTheme.typography.headlineSmall)
    Spacer(modifier = Modifier.height(8.dp))
    Text(stringResource(R.string.home_manager))
}
@Composable
private fun LeaderContent() {
    Text(text = stringResource(R.string.p_leader), style = MaterialTheme.typography.headlineSmall)
    Spacer(modifier = Modifier.height(8.dp))
    Text(stringResource(R.string.home_leader))

}
