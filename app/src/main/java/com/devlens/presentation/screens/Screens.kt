package com.devlens.presentation.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devlens.domain.models.ActivityType
import com.devlens.domain.models.DashboardMetrics
import com.devlens.domain.models.DeveloperActivity
import com.devlens.domain.models.ManagerReport
import com.devlens.domain.models.MisalignmentCategory
import com.devlens.presentation.components.ActivityCard
import com.devlens.presentation.components.CategoryChip
import com.devlens.presentation.components.MetricTile
import com.devlens.presentation.components.ScoreBar
import com.devlens.presentation.components.SectionTitle
import com.devlens.presentation.viewmodels.ActivityFormState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OnboardingScreen(onContinue: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Text("DevLens", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
            Text(
                "IA local para revelar o desalinhamento entre valor efetivo e valor percebido em times de software.",
                style = MaterialTheme.typography.titleMedium
            )
        }
        item {
            SectionTitle("Problema")
            Text("Refatoracoes, suporte, revisoes e investigacoes reduzem risco e melhoram produto, mas muitas vezes ficam invisiveis para a gestao.")
        }
        item {
            SectionTitle("Como funciona")
            Text("Cada atividade e analisada no aparelho por um classificador local. O app compara impacto real, visibilidade percebida, divergencia, confianca e recomendacoes praticas.")
        }
        item {
            Button(onClick = onContinue, modifier = Modifier.fillMaxWidth()) {
                Text("Comecar analise")
            }
        }
    }
}

@Composable
fun DashboardScreen(
    metrics: DashboardMetrics,
    activities: List<DeveloperActivity>,
    onAdd: () -> Unit,
    onActivityClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(Modifier.weight(1f)) {
                    Text("Painel DevLens", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("Resumo dos sinais de valor e visibilidade.")
                }
                FilledTonalButton(onClick = onAdd) { Text("Registrar") }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricTile("Atividades", metrics.totalActivities.toString(), Modifier.weight(1f))
                MetricTile("Invisiveis", metrics.invisibleHighValueCount.toString(), Modifier.weight(1f))
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricTile("Valor efetivo", metrics.averageEffective.toString(), Modifier.weight(1f))
                MetricTile("Valor percebido", metrics.averagePerceived.toString(), Modifier.weight(1f))
            }
        }
        item {
            SectionTitle("Desalinhamento")
            metrics.categoryDistribution.forEach { (category, count) ->
                Text("${category.label}: $count", style = MaterialTheme.typography.bodyLarge)
            }
            if (metrics.categoryDistribution.isEmpty()) Text("Sem dados analisados.")
        }
        item { SectionTitle("Atividades criticas") }
        items(metrics.criticalActivities) { activity ->
            ActivityCard(activity = activity, onClick = { onActivityClick(activity.id) })
        }
        if (activities.isEmpty()) {
            item { Text("Cadastre uma atividade para iniciar o acompanhamento.") }
        }
    }
}

@Composable
fun ActivityFormScreen(
    state: ActivityFormState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onMinutesChange: (String) -> Unit,
    onTypeChange: (ActivityType) -> Unit,
    onTagsChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onVisibilityChange: (Float) -> Unit,
    onSave: () -> Unit
) {
    var menuOpen by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Nova atividade", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }
        item {
            OutlinedTextField(
                value = state.title,
                onValueChange = onTitleChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Titulo") },
                singleLine = true
            )
        }
        item {
            OutlinedTextField(
                value = state.description,
                onValueChange = onDescriptionChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Descricao") },
                minLines = 4
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = state.minutesSpent,
                    onValueChange = onMinutesChange,
                    modifier = Modifier.weight(1f),
                    label = { Text("Tempo min") },
                    singleLine = true
                )
                Column(Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = state.type.label,
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Tipo") },
                        readOnly = true,
                        trailingIcon = {
                            TextButton(onClick = { menuOpen = true }) { Text("Trocar") }
                        }
                    )
                    DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                        ActivityType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.label) },
                                onClick = {
                                    onTypeChange(type)
                                    menuOpen = false
                                }
                            )
                        }
                    }
                }
            }
        }
        item {
            OutlinedTextField(
                value = state.tags,
                onValueChange = onTagsChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Tags separadas por virgula") },
                singleLine = true
            )
        }
        item {
            OutlinedTextField(
                value = state.activityDate,
                onValueChange = onDateChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Data da atividade (dd/MM/aaaa)") },
                singleLine = true
            )
        }
        item {
            Text("Visibilidade percebida: ${state.selfPerceivedVisibility.toInt()}/100")
            Slider(
                value = state.selfPerceivedVisibility,
                onValueChange = onVisibilityChange,
                valueRange = 0f..100f
            )
        }
        if (state.error != null) {
            item { Text(state.error, color = MaterialTheme.colorScheme.error) }
        }
        item {
            Button(
                onClick = onSave,
                enabled = !state.isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Text(if (state.isSaving) "Salvando..." else "Salvar e analisar")
            }
        }
    }
}

@Composable
fun ActivitiesScreen(
    activities: List<DeveloperActivity>,
    onActivityClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Atividades", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Historico local com scores e explicacoes da IA.")
        }
        items(activities) { activity ->
            ActivityCard(activity = activity, onClick = { onActivityClick(activity.id) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailScreen(activity: DeveloperActivity?, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalhe") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        if (activity == null) {
            Text("Atividade nao encontrada.", modifier = Modifier.padding(padding).padding(16.dp))
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(activity.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("${activity.type.label} | ${activity.minutesSpent} min | ${formatDate(activity.activityDate)}")
                CategoryChip(activity.analysis.category)
            }
            item { Text(activity.description) }
            item { ScoreBar("Valor efetivo", activity.analysis.effectiveScore) }
            item { ScoreBar("Valor percebido", activity.analysis.perceivedScore) }
            item {
                Text("Diferenca: ${activity.analysis.gap}", fontWeight = FontWeight.Bold)
                Text("Confianca: ${(activity.analysis.confidence * 100).toInt()}%")
            }
            item {
                SectionTitle("Explicacao da IA")
                Text(activity.analysis.explanation)
            }
            item {
                SectionTitle("Fatores de valor efetivo")
                BulletList(activity.analysis.effectiveFactors)
            }
            item {
                SectionTitle("Fatores de percepcao")
                BulletList(activity.analysis.perceivedFactors)
            }
            item {
                SectionTitle("Recomendacoes")
                BulletList(activity.analysis.recommendations)
            }
        }
    }
}

@Composable
fun InsightsScreen(activities: List<DeveloperActivity>) {
    val invisible = activities.filter { it.analysis.category == MisalignmentCategory.InvisibleHighValue }
    val overestimated = activities.filter { it.analysis.category == MisalignmentCategory.OverestimatedLowValue }
    val recommendations = activities.flatMap { it.analysis.recommendations }.groupingBy { it }.eachCount()
        .entries.sortedByDescending { it.value }.take(6)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Insights", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Recomendacoes agregadas a partir das classificacoes locais.")
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricTile("Alto valor invisivel", invisible.size.toString(), Modifier.weight(1f))
                MetricTile("Superestimadas", overestimated.size.toString(), Modifier.weight(1f))
            }
        }
        item {
            SectionTitle("Acoes recomendadas")
            BulletList(recommendations.map { "${it.key} (${it.value}x)" })
        }
        item {
            SectionTitle("Casos prioritarios")
        }
        items(invisible.take(5)) { activity ->
            ActivityCard(activity = activity, onClick = {})
        }
    }
}

@Composable
fun ReportScreen(report: ManagerReport) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(Modifier.weight(1f)) {
                    Text("Relatorio gerencial", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("Resumo pronto para revisao semanal.")
                }
                IconButton(onClick = { copyReport(context, report.shareableSummary) }) {
                    Icon(Icons.Default.ContentCopy, contentDescription = "Copiar")
                }
            }
        }
        item {
            SectionTitle("Resumo da semana")
            Text(report.weeklySummary)
        }
        item {
            SectionTitle("Atividades de alto impacto")
            BulletList(report.highImpactActivities.map { "${it.title}: efetivo ${it.analysis.effectiveScore}, percebido ${it.analysis.perceivedScore}" })
        }
        item {
            SectionTitle("Invisiveis para gestao")
            BulletList(report.invisibleActivities.map { "${it.title}: gap ${it.analysis.gap}" })
        }
        item {
            SectionTitle("Sugestoes")
            BulletList(report.alignmentSuggestions)
        }
        item {
            SectionTitle("Texto para envio")
            Text(report.shareableSummary)
        }
    }
}

@Composable
private fun BulletList(items: List<String>) {
    if (items.isEmpty()) {
        Text("Sem itens para exibir.")
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items.forEach { Text("- $it") }
        }
    }
}

private fun formatDate(time: Long): String =
    SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(Date(time))

private fun copyReport(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("Relatorio DevLens", text))
}
