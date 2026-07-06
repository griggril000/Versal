package com.grigg.versal.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.grigg.versal.BuildConfig
import com.grigg.versal.R
import com.grigg.versal.data.FormspreeService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBack: () -> Unit) {
    var showFeedbackForm by remember { mutableStateOf(false) }

    if (showFeedbackForm) {
        FeedbackScreen(onBack = { showFeedbackForm = false })
    } else {
        AboutContent(
            onBack = onBack,
            onShowFeedback = { showFeedbackForm = true }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutContent(
    onBack: () -> Unit,
    onShowFeedback: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.about_versal)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.version_format, BuildConfig.VERSION_NAME),
                style = MaterialTheme.typography.bodySmall
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Text(
                text = stringResource(R.string.credits_data_sources),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(R.string.credits_description),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = stringResource(R.string.ben_crowder_credit),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 12.dp)
            )

            Text(
                text = stringResource(R.string.github_link),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier
                    .padding(top = 4.dp, start = 12.dp)
                    .clickable {
                        uriHandler.openUri("https://github.com/bcbooks/scriptures-json")
                    }
            )

            Text(
                text = stringResource(R.string.website_link),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier
                    .padding(top = 8.dp, start = 12.dp)
                    .clickable {
                        uriHandler.openUri("https://bencrowder.net")
                    }
            )

            Text(
                text = stringResource(R.string.gospel_library_credit),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 12.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Button(
                onClick = onShowFeedback,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.send_feedback))
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Text(
                text = stringResource(R.string.official_disclaimer),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(onBack: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val feedbackTypes = listOf(
        stringResource(R.string.general_feedback),
        stringResource(R.string.feature_request),
        stringResource(R.string.bug_report),
        stringResource(R.string.other)
    )
    var selectedType by remember { mutableStateOf(feedbackTypes[0]) }
    var expanded by remember { mutableStateOf(false) }

    var isSubmitting by remember { mutableStateOf(false) }
    var submissionStatus by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val successMessage = stringResource(R.string.success_thanks)
    val errorCodeMessage = stringResource(R.string.error_code_format)
    val errorMessage = stringResource(R.string.error_format)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.send_feedback)) },
                navigationIcon = {
                    IconButton(onClick = onBack, enabled = !isSubmitting) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email)) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSubmitting,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { if (!isSubmitting) expanded = !expanded },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.feedback_type)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    enabled = !isSubmitting
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    feedbackTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                selectedType = type
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text(stringResource(R.string.message)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(200.dp),
                enabled = !isSubmitting,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                )
            )

            submissionStatus?.let { status ->
                Text(
                    text = status,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (status == successMessage) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Button(
                onClick = {
                    scope.launch {
                        isSubmitting = true
                        submissionStatus = null
                        try {
                            val response =
                                FormspreeService.api.submitForm(email, selectedType, message)
                            if (response.isSuccessful) {
                                submissionStatus = successMessage
                                delay(1500.milliseconds)
                                onBack()
                            } else {
                                submissionStatus = errorCodeMessage.format(response.code())
                            }
                        } catch (e: Exception) {
                            submissionStatus = errorMessage.format(e.localizedMessage)
                        } finally {
                            isSubmitting = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                enabled = !isSubmitting && email.isNotBlank() && message.isNotBlank()
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(stringResource(R.string.send))
                }
            }
        }
    }
}
