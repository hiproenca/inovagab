package com.hig.inovagab.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hig.inovagab.R
import com.hig.inovagab.data.utils.IdeaStatus
import com.hig.inovagab.data.utils.UserRole
import com.hig.inovagab.model.Idea

@Composable
fun IdeaListContent(
    ideas: List<Idea>,
    role: UserRole,
    onUpdateStatus: (String, IdeaStatus) -> Unit
) {
    if (ideas.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.idea_notfound_text))
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(ideas) { idea ->
                IdeaCard(idea = idea, role = role, onUpdateStatus = onUpdateStatus)
            }
        }
    }
}

@Composable
fun IdeaCard(
    idea: Idea,
    role: UserRole,
    onUpdateStatus: (String, IdeaStatus) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = idea.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = idea.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Status: ${idea.status}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            if (role == UserRole.MANAGER && idea.status == IdeaStatus.PENDING) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { onUpdateStatus(idea.id ?: "", IdeaStatus.APPROVED) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.approve_idea_text))
                    }
                    OutlinedButton(
                        onClick = { onUpdateStatus(idea.id ?: "", IdeaStatus.REJECTED) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.rejected_idea_text))
                    }
                }
            }
        }
    }
}

@Composable
fun IdeaFormContent(
    onSubmit: (title: String, description: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(R.string.idea_title_text)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(R.string.idea_description_text)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4
        )

        Button(
            onClick = {
                if (title.isNotBlank() && description.isNotBlank()) {
                    onSubmit(title, description)

                    title = ""
                    description = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_idea_text))
        }
    }
}