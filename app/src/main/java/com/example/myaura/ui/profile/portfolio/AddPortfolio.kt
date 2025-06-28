package com.example.myaura.ui.profile.portfolio

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.border
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myaura.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class DatePickerTarget { START, END }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioForm( // Anda bisa ganti nama ini menjadi AddPortfolioScreen
    navController: NavController,
    viewModel: AddPortfolioViewModel = hiltViewModel()
) {
    var title by rememberSaveable { mutableStateOf("") }

    var startDateText by rememberSaveable { mutableStateOf("Tanggal Mulai") }
    var endDateText by rememberSaveable { mutableStateOf("Tanggal Selesai") }
    var isCurrentProject by rememberSaveable { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    var datePickerTarget by remember { mutableStateOf<DatePickerTarget?>(null) }

    var skill by rememberSaveable { mutableStateOf("") }
    var url by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    val addState by viewModel.addState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = addState) {
        if (addState.isSuccess) {
            Toast.makeText(context, "Portofolio berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
            viewModel.onNavigationDone()
        }
        addState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(
                            Date(millis)
                        )
                        if (datePickerTarget == DatePickerTarget.START) {
                            startDateText = formattedDate
                        } else {
                            endDateText = formattedDate
                        }
                        showDatePicker = false
                    }
                }) { Text("OK") }
            },
            dismissButton = { Button(onClick = { showDatePicker = false }) { Text("Batal") } }
        ){
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F5F9))
            .padding(horizontal = 24.dp)
            .padding(top = 64.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFFE2E8F0), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.Add_Box),
                textAlign = TextAlign.Center,
                color = Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Judul Portofolio") }, modifier = Modifier.fillMaxWidth())

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Tombol untuk Tanggal Mulai
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f), RoundedCornerShape(4.dp)) // <-- Menggunakan Box dengan border
                    .clickable {
                        datePickerTarget = DatePickerTarget.START
                        showDatePicker = true
                    }
                    .padding(16.dp)
            ) {
                Text(text = startDateText)
            }

            val isEndDateEnabled = !isCurrentProject
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.onSurface.copy(alpha = if (isEndDateEnabled) 0.4f else 0.1f),
                        RoundedCornerShape(4.dp)
                    )
                    .clickable(enabled = isEndDateEnabled) {
                        datePickerTarget = DatePickerTarget.END
                        showDatePicker = true
                    }
                    .padding(16.dp)
            ) {
                Text(
                    text = if (isCurrentProject) "Saat Ini" else endDateText,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (isEndDateEnabled) 1f else 0.4f)
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isCurrentProject,
                onCheckedChange = { isCurrentProject = it }
            )
            Text("Sekarang")
        }

        OutlinedTextField(value = skill, onValueChange = { skill = it }, label = { Text(stringResource(R.string.skill)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = url, onValueChange = { url = it }, label = { Text(stringResource(R.string.url)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text(stringResource(R.string.Desc)) }, modifier = Modifier.fillMaxWidth().height(100.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val finalEndDate = if (isCurrentProject) "Sekarang" else endDateText
                val dateRange = "$startDateText - $finalEndDate"
                viewModel.onAddPortfolioClicked(title, dateRange, skill, url, description)
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            enabled = !addState.isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D1B66)),
            shape = RoundedCornerShape(24.dp)
        ) {
            if (addState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text(stringResource(R.string.PostPort), color = Color.White)
            }
        }
    }
}