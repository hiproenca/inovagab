package com.hig.inovagab.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hig.inovagab.R
import com.hig.inovagab.data.utils.UserRole
import com.hig.inovagab.ui.viewmodel.ProjectUiState
import com.hig.inovagab.ui.viewmodel.ProjectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(
    role: UserRole,
    userId: String,
    onNavigateBack: () -> Unit,
    viewModel: ProjectViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadProjects(role, userId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.active_projects_text)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.button_return_app)
                        )
                    }
                }


            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (val state = uiState){
                is ProjectUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is ProjectUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.message, color = androidx.compose.material3.MaterialTheme.colorScheme.error)
                    }
                }
                is ProjectUiState.Success -> {
                    ProjectListContent(projects = state.projects)
                }
            }

        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProjectScreenPreview() {
    ProjectScreen(
        role = UserRole.MANAGER,
        userId = "123",
        onNavigateBack = {}
    )}
