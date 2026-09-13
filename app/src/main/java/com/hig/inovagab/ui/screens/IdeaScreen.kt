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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hig.inovagab.R
import com.hig.inovagab.model.UserRole
import com.hig.inovagab.ui.viewmodel.IdeaUiState
import com.hig.inovagab.ui.viewmodel.IdeaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdeaScreen(
    role: UserRole,
    userId: String,
    onNavigateBack: () -> Unit,
    viewModel: IdeaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.loadIdeas(role, userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.ideas_inovations_title)) },
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
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (role == UserRole.OPERATOR){
                TabRow(selectedTabIndex = selectedTabIndex) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = { Text(stringResource(R.string.ideas_myideas_text)) }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = { Text(stringResource(R.string.new_idea_text)) }
                    )
                }
            }
            when (val state = uiState) {
                is IdeaUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is IdeaUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    }
                }
                is IdeaUiState.Success -> {
                    // Se for Gestor/Líder OU se for Operador na aba 0, mostra a Lazy
                    if (role != UserRole.OPERATOR || selectedTabIndex == 0) {
                        IdeaListContent(
                            ideas = state.ideas,
                            role = role,
                            onUpdateStatus = { ideaId, newStatus ->
                                viewModel.updateIdeaStatus(ideaId, newStatus, role, userId)
                            }
                        )
                    } else {

                        IdeaFormContent(
                            onSubmit = { title, description ->
                                viewModel.submitIdeas(title, description, userId, role)
                                selectedTabIndex = 0 //Volta para a lista
                            }
                        )
                    }
                }
            }
        }
    }
}