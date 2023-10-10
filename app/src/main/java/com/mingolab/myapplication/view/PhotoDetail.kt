package com.mingolab.myapplication.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mingolab.myapplication.repository.localDB.DayPhoto
import com.mingolab.myapplication.viewModel.PhotoViewModel

class PhotoDetail {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun showPhotoDetailsUI(
        navController: NavController,
        photoViewModel: PhotoViewModel
    ) {

        var photo = photoViewModel.getPhoto()

        var scale by remember { mutableStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }
        val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
            scale *= zoomChange
            offset += offsetChange
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            TopAppBar(
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text(photo!!.date)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("PhotoList") }) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                })

            AsyncImage(
                model = photo!!.url,
                contentDescription = null,
                modifier = Modifier
                    .clickable(onClick = {})
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    .transformable(state = state)
                    .background(Color.White)
                    .fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
        }
    }
}