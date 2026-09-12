package com.hig.inovagab.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hig.inovagab.R
import androidx.compose.ui.tooling.preview.Preview
@Composable
fun LoginScreen(onLoginSuccess: (String) -> Unit){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(text = stringResource(R.string.app_name),
             style = MaterialTheme.typography.headlineLarge,
             color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { onLoginSuccess("OPERATOR") },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(text = stringResource(R.string.login_operator))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onLoginSuccess("MANAGER") },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(text = stringResource(R.string.login_manager))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onLoginSuccess("LEADER") },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(text = stringResource(R.string.login_leader))
        }
    }




}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview(){
    LoginScreen(onLoginSuccess = {})
}