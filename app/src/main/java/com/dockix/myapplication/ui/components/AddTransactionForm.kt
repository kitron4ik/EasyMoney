package com.dockix.myapplication.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import com.dockix.myapplication.R
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionForm(
    onSubmit: (title: String, amount: Double, isIncome: Boolean, category: String, date: Date, description: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var isIncome by remember { mutableStateOf(true) }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(text = stringResource(id = R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    
    Column(
        horizontalAlignment = Alignment.End,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(tween(300)) + expandVertically(tween(300)),
            exit = fadeOut(tween(300)) + shrinkVertically(tween(300))
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.add_transaction),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    // Type of transaction (income/expense)
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        SegmentedButton(
                            selected = isIncome,
                            onClick = { isIncome = true },
                            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowUpward,
                                    contentDescription = null
                                )
                            }
                        ) {
                            Text(text = stringResource(id = R.string.income))
                        }
                        
                        SegmentedButton(
                            selected = !isIncome,
                            onClick = { isIncome = false },
                            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDownward,
                                    contentDescription = null
                                )
                            }
                        ) {
                            Text(text = stringResource(id = R.string.expense))
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Title
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text(stringResource(id = R.string.description)) },
                        leadingIcon = { Icon(Icons.Default.Title, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Amount
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text(stringResource(id = R.string.amount)) },
                        leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Category
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text(stringResource(id = R.string.category)) },
                        leadingIcon = { Icon(Icons.Default.Category, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Date picker
                    OutlinedTextField(
                        value = if (datePickerState.selectedDateMillis != null) {
                            val date = Date(datePickerState.selectedDateMillis!!)
                            java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault())
                                .format(date)
                        } else {
                            ""
                        },
                        onValueChange = { },
                        readOnly = true,
                        label = { Text(stringResource(id = R.string.date)) },
                        leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(Icons.Default.CalendarToday, contentDescription = null)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Description
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text(stringResource(id = R.string.description)) },
                        leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { isExpanded = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = stringResource(id = R.string.cancel))
                        }
                        
                        Spacer(modifier = Modifier.weight(0.1f))
                        
                        Button(
                            onClick = {
                                val amountValue = amount.toDoubleOrNull() ?: 0.0
                                val date = if (datePickerState.selectedDateMillis != null) {
                                    Date(datePickerState.selectedDateMillis!!)
                                } else {
                                    Date()
                                }
                                
                                if (title.isNotBlank() && amountValue > 0 && category.isNotBlank()) {
                                    onSubmit(title, amountValue, isIncome, category, date, description.takeIf { it.isNotBlank() })
                                    // Reset form
                                    title = ""
                                    amount = ""
                                    category = ""
                                    description = ""
                                    isExpanded = false
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = stringResource(id = R.string.save))
                        }
                    }
                }
            }
        }
        
        if (!isExpanded) {
            val rotation by animateFloatAsState(
                targetValue = if (isExpanded) 45f else 0f,
                label = "fab_rotation"
            )
            
            FloatingActionButton(
                onClick = { isExpanded = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_transaction)
                )
            }
        }
    }
} 