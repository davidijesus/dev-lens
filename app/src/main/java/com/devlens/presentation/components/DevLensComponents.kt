package com.devlens.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.devlens.domain.models.DeveloperActivity
import com.devlens.domain.models.MisalignmentCategory

@Composable
fun MetricTile(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(6.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ScoreBar(label: String, score: Int, modifier: Modifier = Modifier) {
    Column(modifier) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.labelLarge)
            Text("$score/100", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { score / 100f },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun CategoryChip(category: MisalignmentCategory) {
    AssistChip(
        onClick = {},
        label = { Text(category.label) }
    )
}

@Composable
fun ActivityCard(
    activity: DeveloperActivity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(
                        activity.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(activity.type.label, style = MaterialTheme.typography.bodySmall)
                }
                CategoryChip(activity.analysis.category)
            }
            Text(
                activity.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Efetivo ${activity.analysis.effectiveScore}", style = MaterialTheme.typography.labelMedium)
                Text("Percebido ${activity.analysis.perceivedScore}", style = MaterialTheme.typography.labelMedium)
                Text("Gap ${activity.analysis.gap}", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
    )
}
