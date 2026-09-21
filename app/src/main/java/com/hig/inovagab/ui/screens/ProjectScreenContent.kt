package com.hig.inovagab.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hig.inovagab.R
import com.hig.inovagab.data.utils.ProjectStage
import com.hig.inovagab.data.utils.ProjectStatus
import com.hig.inovagab.data.utils.UserRole
import com.hig.inovagab.model.Project
import com.hig.inovagab.model.Strategy
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProjectListContent(
    projects: List<Project>,
    role: UserRole = UserRole.OPERATOR,
    strategies: List<Strategy> = emptyList(),
    onCreate: (title: String, investment: Double, deadline: String, description: String, strategyId: String?) -> Unit =
        { _, _, _, _, _ -> },
    onUpdateProgress: (id: String, stage: ProjectStage, status: ProjectStatus, results: String?, financialReturn: Double?) -> Unit =
        { _, _, _, _, _ -> },
    onDelete: (id: String) -> Unit = {}
) {
    val isManager = role == UserRole.MANAGER
    var showCreate by remember { mutableStateOf(false) }
    var updating by remember { mutableStateOf<Project?>(null) }
    var deleting by remember { mutableStateOf<Project?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (projects.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.project_notfound_text))
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(projects) { project ->
                    ProjectCard(
                        project = project,
                        strategyTitle = project.strategyId?.let { sid -> strategies.find { it.id == sid }?.title },
                        canManage = isManager,
                        onUpdate = { updating = project },
                        onDelete = { deleting = project }
                    )
                }
            }
        }

        if (isManager) {
            FloatingActionButton(
                onClick = { showCreate = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Novo projeto")
            }
        }
    }

    if (showCreate) {
        ProjectCreateDialog(
            strategies = strategies,
            onDismiss = { showCreate = false },
            onConfirm = { title, investment, deadline, description, strategyId ->
                onCreate(title, investment, deadline, description, strategyId)
                showCreate = false
            }
        )
    }

    updating?.let { project ->
        ProjectProgressDialog(
            project = project,
            onDismiss = { updating = null },
            onConfirm = { stage, status, results, financialReturn ->
                project.id?.let { id -> onUpdateProgress(id, stage, status, results, financialReturn) }
                updating = null
            }
        )
    }

    deleting?.let { project ->
        AlertDialog(
            onDismissRequest = { deleting = null },
            title = { Text("Excluir projeto?") },
            text = { Text("\"${project.title}\" será removido. Essa ação não pode ser desfeita.") },
            confirmButton = {
                TextButton(onClick = {
                    project.id?.let { id -> onDelete(id) }
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
fun ProjectCard(
    project: Project,
    strategyTitle: String? = null,
    canManage: Boolean = false,
    onUpdate: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = project.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = project.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Etapa: ${project.stage}  •  Status: ${project.status}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = "Investimento: ${formatCurrency(project.investment)}",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "Retorno financeiro: ${formatCurrency(project.financialReturn ?: 0.0)}",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "Prazo: ${project.deadline}",
                style = MaterialTheme.typography.labelMedium
            )
            strategyTitle?.let {
                Text(text = "Estratégia: $it", style = MaterialTheme.typography.labelMedium)
            }
            project.results?.takeIf { it.isNotBlank() }?.let {
                Text(text = "Resultados: $it", style = MaterialTheme.typography.labelMedium)
            }

            if (canManage) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = onUpdate) { Text("Atualizar progresso") }
                    TextButton(onClick = onDelete) { Text("Excluir") }
                }
            }
        }
    }
}

@Composable
private fun ProjectCreateDialog(
    strategies: List<Strategy>,
    onDismiss: () -> Unit,
    onConfirm: (title: String, investment: Double, deadline: String, description: String, strategyId: String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var investmentText by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var strategyId by remember { mutableStateOf<String?>(null) }

    val investment = investmentText.replace(',', '.').toDoubleOrNull()
    val deadlineValid = Regex("""\d{4}-\d{2}-\d{2}""").matches(deadline)
    val canSave = title.isNotBlank() && description.isNotBlank() &&
            investment != null && investment >= 0.0 && deadlineValid

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo projeto") },
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
                    value = description, onValueChange = { description = it },
                    label = { Text("Descrição") }, minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = investmentText, onValueChange = { investmentText = it },
                    label = { Text("Investimento (R$)") }, singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = investmentText.isNotBlank() && investment == null,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = deadline, onValueChange = { deadline = it },
                    label = { Text("Prazo (AAAA-MM-DD)") }, singleLine = true,
                    isError = deadline.isNotBlank() && !deadlineValid,
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Estratégia vinculada", style = MaterialTheme.typography.labelLarge)
                OptionRow(label = "Nenhuma", selected = strategyId == null) { strategyId = null }
                strategies.forEach { strategy ->
                    OptionRow(label = strategy.title, selected = strategyId == strategy.id) {
                        strategyId = strategy.id
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    investment?.let {
                        onConfirm(title.trim(), it, deadline.trim(), description.trim(), strategyId)
                    }
                },
                enabled = canSave
            ) { Text("Salvar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
private fun ProjectProgressDialog(
    project: Project,
    onDismiss: () -> Unit,
    onConfirm: (stage: ProjectStage, status: ProjectStatus, results: String?, financialReturn: Double?) -> Unit
) {
    var stage by remember { mutableStateOf(project.stage) }
    var status by remember { mutableStateOf(project.status) }
    var results by remember { mutableStateOf(project.results ?: "") }
    var returnText by remember { mutableStateOf(project.financialReturn?.toString() ?: "") }

    val financialReturn = returnText.replace(',', '.').toDoubleOrNull()
    val returnValid = returnText.isBlank() || financialReturn != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Atualizar progresso") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text("Etapa", style = MaterialTheme.typography.labelLarge)
                ProjectStage.entries.forEach { option ->
                    OptionRow(label = option.name, selected = stage == option) { stage = option }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Status", style = MaterialTheme.typography.labelLarge)
                ProjectStatus.entries.forEach { option ->
                    OptionRow(label = option.name, selected = status == option) { status = option }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = results, onValueChange = { results = it },
                    label = { Text("Resultados obtidos") }, minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = returnText, onValueChange = { returnText = it },
                    label = { Text("Retorno financeiro (R$)") }, singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = !returnValid,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(stage, status, results.trim().ifBlank { null }, financialReturn) },
                enabled = returnValid
            ) { Text("Salvar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
internal fun OptionRow(label: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
    ) {
        RadioButton(selected = selected, onClick = onSelect)
        Text(label)
    }
}

private fun formatCurrency(value: Double): String =
    NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR")).format(value)