package com.example.myaura.ui.profile.portfolio

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPortfolio(
    navController: NavController,
    viewModel: EditPortfolioViewModel = hiltViewModel()
) {
    val editState by viewModel.editState.collectAsState()
    val context = LocalContext.current

    var newImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        newImageUri = uri
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var datePickerTarget by remember { mutableStateOf<DatePickerTarget?>(null) }

    val updateSuccessMessage = stringResource(id = R.string.portfolio_update_success)
    val dismissActionLabel = stringResource(id = R.string.action_dismiss)

    LaunchedEffect(key1 = editState.isSuccess) {
        if (editState.isSuccess) {
            Toast.makeText(context, updateSuccessMessage, Toast.LENGTH_SHORT).show()
            navController.popBackStack()
            viewModel.onNavigationDone()
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = editState.error) {
        editState.error?.let { errorMessage ->
            snackbarHostState.showSnackbar(
                message = errorMessage,
                actionLabel = dismissActionLabel,
                duration = SnackbarDuration.Short
            )
            viewModel.onNavigationDone()
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
                                viewModel.onStartDateSelected(formattedDate)
                            } else {
                                viewModel.onEndDateSelected(formattedDate)
                            }
                        }
                        showDatePicker = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) { Text(stringResource(id = R.string.ok_button)) }
            },
            dismissButton = {
                Button(onClick = { showDatePicker = false }) { Text(stringResource(id = R.string.cancel_button)) }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (editState.isLoading && editState.title.isBlank()) {
                CircularProgressIndicator()
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(8.dp))
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    val imageUrlToDisplay = remember(editState.imageUrl, newImageUri) {
                        newImageUri ?: editState.imageUrl
                    }

                    if (imageUrlToDisplay.toString().isNotBlank()) {
                        AsyncImage(
                            model = imageUrlToDisplay,
                            contentDescription = stringResource(id = R.string.portfolio_image_cd),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.ic_launcher_background),
                            error = painterResource(id = R.drawable.ic_launcher_background)
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.edit_portfolio_image_box),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(value = editState.title, onValueChange = { viewModel.onTitleChange(it) }, label = { Text(stringResource(id = R.string.portfolio_title_label)) }, modifier = Modifier.fillMaxWidth())
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
                        Text(text = editState.startDate.ifEmpty { stringResource(id = R.string.portfolio_start_date) }, color = MaterialTheme.colorScheme.onSurface)
                    }

                    val isEndDateEnabled = !editState.isCurrent
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
                            text = if (editState.isCurrent) stringResource(id = R.string.portfolio_current_project) else editState.endDate.ifEmpty { stringResource(id = R.string.portfolio_end_date) },
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (isEndDateEnabled) 1f else 0.4f)
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = editState.isCurrent,
                        onCheckedChange = { viewModel.onIsCurrentChange(it) },
                        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                    )
                    Text(stringResource(id = R.string.now_checkbox_label), color = MaterialTheme.colorScheme.onSurface)
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(value = editState.skill, onValueChange = { viewModel.onSkillChange(it) }, label = { Text(stringResource(R.string.skill_label)) }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = editState.projectUrl, onValueChange = { viewModel.onUrlChange(it) }, label = { Text(stringResource(R.string.url_label)) }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = editState.description, onValueChange = { viewModel.onDescriptionChange(it) }, label = { Text(stringResource(R.string.description_label)) }, modifier = Modifier.fillMaxWidth().height(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.onUpdateClicked(newImageUri) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = !editState.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    if (editState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text(stringResource(R.string.save_edits_button))
                    }
                }
            }
        }
    }
}