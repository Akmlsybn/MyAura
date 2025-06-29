package com.example.myaura.ui.profile.portfolio

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myaura.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class DatePickerTarget { START, END }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioForm(
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
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }
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
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date(millis))
                            if (datePickerTarget == DatePickerTarget.START) {
                                startDateText = formattedDate
                            } else {
                                endDateText = formattedDate
                            }
                        }
                        showDatePicker = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) { Text("OK") }
            },
            dismissButton = {
                Button(onClick = { showDatePicker = false }) { Text("Batal") }
            }
        ){
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
            .padding(vertical = 32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(8.dp))
                .clickable { imagePickerLauncher.launch("image/*")},
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null){
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = stringResource(R.string.Add_Box),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Judul Portofolio") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
                    .clickable {
                        datePickerTarget = DatePickerTarget.START
                        showDatePicker = true
                    }
                    .padding(16.dp)
            ) {
                Text(text = startDateText, color = MaterialTheme.colorScheme.onSurface)
            }

            val isEndDateEnabled = !isCurrentProject
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = if (isEndDateEnabled) 1f else 0.4f), RoundedCornerShape(4.dp))
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = isCurrentProject,
                onCheckedChange = { isCurrentProject = it },
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
            )
            Text("Sekarang", color = MaterialTheme.colorScheme.onSurface)
        }

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = skill, onValueChange = { skill = it }, label = { Text(stringResource(R.string.skill)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = url, onValueChange = { url = it }, label = { Text(stringResource(R.string.url)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text(stringResource(R.string.Desc)) }, modifier = Modifier.fillMaxWidth().height(100.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val finalEndDate = if (isCurrentProject) "Saat Ini" else endDateText
                val dateRange = "$startDateText - $finalEndDate"
                viewModel.onAddPortfolioClicked(
                    title = title,
                    dateRange = dateRange,
                    skill = skill,
                    projectUrl = url,
                    description = description,
                    imageUri = imageUri
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = !addState.isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            if (addState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text(stringResource(R.string.PostPort))
            }
        }
    }
}