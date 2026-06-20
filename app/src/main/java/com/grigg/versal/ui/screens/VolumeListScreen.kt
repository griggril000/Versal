package com.grigg.versal.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grigg.versal.data.ScriptureRepository

import androidx.compose.ui.tooling.preview.Preview
import com.grigg.versal.ui.theme.VersalTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolumeListScreen(onVolumeClick: (String) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Scriptures") })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(ScriptureRepository.volumes) { volume ->
                ListItem(
                    headlineContent = { Text(volume.name) },
                    modifier = Modifier.clickable { onVolumeClick(volume.id) }
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VolumeListScreenPreview() {
    VersalTheme {
        VolumeListScreen(onVolumeClick = {})
    }
}
