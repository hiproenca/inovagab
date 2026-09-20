package com.hig.inovagab.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hig.inovagab.data.dto.DtoAuthenticationRequest
import com.hig.inovagab.data.utils.UserRole
import com.hig.inovagab.ui.viewmodel.LoginUiState
import com.hig.inovagab.ui.viewmodel.LoginViewModel
import com.hig.inovagab.ui.theme.InovaGabTheme


@Composable
fun LoginScreen(
    onLoginSuccess: (role: UserRole, userId: String) -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var email by rememberSaveable { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        val state = uiState
        if (state is LoginUiState.Success) {
            onLoginSuccess(state.role, state.userId)
            viewModel.resetState()
        }
    }

    LoginContent(
        email = email,
        password = password,
        state = uiState,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onLoginClick = {
            viewModel.login(DtoAuthenticationRequest(email = email.trim(), password = password))
        }
    )
}

@Composable
fun LoginContent(
    email: String,
    password: String,
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    val isLoading = state is LoginUiState.Loading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "InovaGAB", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("E-mail") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Senha") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        if (state is LoginUiState.Error) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = state.message, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onLoginClick,
            enabled = email.isNotBlank() && password.isNotBlank() && !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text("Entrar")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Login - vazio")
@Composable
private fun LoginContentEmptyPreview() {
    InovaGabTheme {
        LoginContent(
            email = "",
            password = "",
            state = LoginUiState.Idle,
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Login - erro")
@Composable
private fun LoginContentErrorPreview() {
    InovaGabTheme {
        LoginContent(
            email = "operador@empresa.com",
            password = "12345678",
            state = LoginUiState.Error("Sessão expirada ou não autorizada."),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Login - carregando")
@Composable
private fun LoginContentLoadingPreview() {
    InovaGabTheme {
        LoginContent(
            email = "operador@empresa.com",
            password = "12345678",
            state = LoginUiState.Loading,
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {}
        )
    }
}