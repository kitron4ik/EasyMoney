package com.dockix.myapplication.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dockix.myapplication.R

@Composable
fun BudgetSummaryCard(
    income: Double,
    expense: Double,
    balance: Double,
    modifier: Modifier = Modifier
) {
    val progress by animateFloatAsState(
        targetValue = if (income > 0.0) (expense / income).toFloat().coerceIn(0f, 1f) else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "progress_animation"
    )
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Balance section
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = stringResource(id = R.string.nav_budget),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Income row
            BudgetSummaryRow(
                icon = Icons.Default.ArrowUpward,
                label = stringResource(id = R.string.income),
                value = income.toFloat(),
                tint = Color(0xFF4CAF50)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Expense row
            BudgetSummaryRow(
                icon = Icons.Default.ArrowDownward,
                label = stringResource(id = R.string.expense),
                value = expense.toFloat(),
                tint = Color(0xFFE53935)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Progress indicator
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth(),
                color = if (balance >= 0.0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Balance
            Text(
                text = stringResource(id = R.string.nav_budget) + ": ${if (balance >= 0.0) "+" else ""}$balance",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = if (balance >= 0.0) Color(0xFF4CAF50) else Color(0xFFE53935)
            )
        }
    }
}

@Composable
fun BudgetSummaryRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: Float,
    tint: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = tint
        )
    }
} 