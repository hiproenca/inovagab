package com.hig.inovagab.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hig.inovagab.data.dto.DtoDashboardResponse
import com.hig.inovagab.ui.theme.InovaGabTheme
import com.hig.inovagab.ui.viewmodel.DashboardUiState
import com.hig.inovagab.ui.viewmodel.DashboardViewModel
import com.hig.inovagab.ui.viewmodel.InsightUiState
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

@Composable
fun DashboardScreen(
    onNavigateBack: () -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val insightState by viewModel.insightState.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadDashboard() }

    DashboardScreenContent(
        uiState = uiState,
        insightState = insightState,
        onNavigateBack = onNavigateBack,
        onGenerateInsight = { viewModel.loadInsight() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreenContent(
    uiState: DashboardUiState,
    insightState: InsightUiState,
    onNavigateBack: () -> Unit,
    onGenerateInsight: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (uiState) {
                is DashboardUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is DashboardUiState.Error -> {
                    Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
                }
                is DashboardUiState.Success -> {
                    SummarySection(uiState.summary)
                    FinancialChart(uiState.summary)
                }
            }

            InsightCard(state = insightState, onGenerate = onGenerateInsight)
        }
    }
}

@Composable
private fun SummarySection(summary: DtoDashboardResponse) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricCard("Projetos", summary.totalProjects.toString(), Modifier.weight(1f))
            MetricCard(
                "ROI global",
                String.format(Locale.forLanguageTag("pt-BR"), "%.1f%%", summary.globalRoiPercentage),
                Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricCard("Investimento total", formatCurrency(summary.totalInvestment), Modifier.weight(1f))
            MetricCard("Retorno financeiro", formatCurrency(summary.totalFinancialReturn), Modifier.weight(1f))
        }
        MetricCard("Lucro obtido", formatCurrency(summary.totalProfit), Modifier.fillMaxWidth())
    }
}

@Composable
private fun MetricCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
private fun FinancialChart(summary: DtoDashboardResponse) {
    val bars = listOf(
        Triple("Investimento", summary.totalInvestment, MaterialTheme.colorScheme.primary),
        Triple("Retorno", summary.totalFinancialReturn, MaterialTheme.colorScheme.secondary),
        Triple(
            "Lucro",
            summary.totalProfit,
            if (summary.totalProfit >= 0) Color(0xFF10B981) else MaterialTheme.colorScheme.error
        )
    )
    val maxValue = bars.maxOf { abs(it.second) }.let { if (it > 0.0) it else 1.0 }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Investimento x retorno", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                bars.forEach { (_, value, color) ->
                    val fraction = (abs(value) / maxValue).toFloat().coerceIn(0.03f, 1f)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .fillMaxHeight(fraction)
                                .background(
                                    color,
                                    RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                bars.forEach { (label, value, _) ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(label, style = MaterialTheme.typography.labelMedium)
                        Text(formatCurrency(value), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun InsightCard(state: InsightUiState, onGenerate: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Insight da IA (Gemini)", style = MaterialTheme.typography.titleMedium)

            when (state) {
                is InsightUiState.Idle -> {
                    Text("Gere uma análise dos resultados dos projetos.")
                }
                is InsightUiState.Loading -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.height(0.dp))
                        Text("  Gerando análise...")
                    }
                }
                is InsightUiState.Success -> {
                    Text(state.insight.insight)
                }
                is InsightUiState.Error -> {
                    Text(state.message, color = MaterialTheme.colorScheme.error)
                }
            }

            Button(
                onClick = onGenerate,
                enabled = state !is InsightUiState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state is InsightUiState.Success) "Gerar novamente" else "Gerar insight")
            }
        }
    }
}

private fun formatCurrency(value: Double): String =
    NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR")).format(value)

@Preview(showBackground = true, showSystemUi = true, name = "Dashboard")
@Composable
private fun DashboardPreview() {
    InovaGabTheme {
        DashboardScreenContent(
            uiState = DashboardUiState.Success(
                DtoDashboardResponse(
                    totalProjects = 3,
                    totalInvestment = 150000.0,
                    totalFinancialReturn = 210000.0,
                    totalProfit = 60000.0,
                    globalRoiPercentage = 40.0
                )
            ),
            insightState = InsightUiState.Idle,
            onNavigateBack = {},
            onGenerateInsight = {}
        )
    }
}