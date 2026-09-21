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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.hig.inovagab.data.utils.UserRole
import com.hig.inovagab.model.Strategy

@Composable
fun StrategyListContent(
    strategies: List<Strategy>,
    role: UserRole = UserRole.OPERATOR,
    onCreate: (title: String, category: String, campaign: String, date: String, description: String) -> Unit =
        { _, _, _, _, _ -> },
    onUpdate: (id: String, title: String, category: String, campaign: String, date: String, description: String) -> Unit =
        { _, _, _, _, _, _ -> },
    onDelete: (id: String) -> Unit = {}
) {
    val isLeader = role == UserRole.LEADER
    var showCreate by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<Strategy?>(null) }
    var deleting by remember { mutableStateOf<Strategy?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (strategies.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.no_strategy_mapped_text))
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(strategies) { strategy ->
                    StrategyCard(
                        strategy = strategy,
                        canManage = isLeader,
                        onEdit = { editing = strategy },
                        onDelete = { deleting = strategy }
                    )
                }
            }
        }

        if (isLeader) {
            FloatingActionButton(
                onClick = { showCreate = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Nova estratégia")
            }
        }
    }

    if (showCreate) {
        StrategyFormDialog(
            initial = null,
            onDismiss = { showCreate = false },
            onConfirm = { title, category, campaign, date, description ->
                onCreate(title, category, campaign, date, description)
                showCreate = false
            }
        )
    }

    editing?.let { strategy ->
        StrategyFormDialog(
            initial = strategy,
            onDismiss = { editing = null },
            onConfirm = { title, category, campaign, date, description ->
                strategy.id?.let { id -> onUpdate(id, title, category, campaign, date, description) }
                editing = null
            }
        )
    }

    deleting?.let { strategy ->
        AlertDialog(
            onDismissRequest = { deleting = null },
            title = { Text("Excluir estratégia?") },
            text = { Text("\"${strategy.title}\" será removida. Essa ação não pode ser desfeita.") },
            confirmButton = {
                TextButton(onClick = {
                    strategy.id?.let { id -> onDelete(id) }
                    deleting = null
                }) { Text("Excluir") }
            },
            dismissButton = {
                TextButton(onClick = { deleting = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun StrategyCard(
    strategy: Strategy,
    canManage: Boolean = false,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = strategy.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = strategy.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Categoria: ${strategy.category}  •  Campanha: ${strategy.campaign}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Data: ${strategy.date}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            if (canManage) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = onEdit) { Text("Editar") }
                    TextButton(onClick = onDelete) { Text("Excluir") }
                }
            }
        }
    }
}

@Composable
private fun StrategyFormDialog(
    initial: Strategy?,
    onDismiss: () -> Unit,
    onConfirm: (title: String, category: String, campaign: String, date: String, description: String) -> Unit
) {
    var title by remember { mutableStateOf(initial?.title ?: "") }
    var category by remember { mutableStateOf(initial?.category ?: "") }
    var campaign by remember { mutableStateOf(initial?.campaign ?: "") }
    var date by remember { mutableStateOf(initial?.date ?: "") }
    var description by remember { mutableStateOf(initial?.description ?: "") }

    val dateValid = Regex("""\d{4}-\d{2}-\d{2}""").matches(date)
    val canSave = title.isNotBlank() && category.isNotBlank() && campaign.isNotBlank() &&
            description.isNotBlank() && dateValid

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Nova estratégia" else "Editar estratégia") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = title, onValueChange = { title = it },
                    label = { Text("Título") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = category, onValueChange = { category = it },
                    label = { Text("Categoria") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = campaign, onValueChange = { campaign = it },
                    label = { Text("Campanha") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = date, onValueChange = { date = it },
                    label = { Text("Data (AAAA-MM-DD)") }, singleLine = true,
                    isError = date.isNotBlank() && !dateValid,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description, onValueChange = { description = it },
                    label = { Text("Descrição") }, minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(title.trim(), category.trim(), campaign.trim(), date.trim(), description.trim())
                },
                enabled = canSave
            ) { Text("Salvar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}