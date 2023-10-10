package com.mingolab.myapplication.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mingolab.myapplication.MyApplication
import com.mingolab.myapplication.R
import com.mingolab.myapplication.repository.localDB.DayPhoto
import com.mingolab.myapplication.viewModel.PhotoViewModel
import com.webtoonscorp.android.readmore.foundation.ReadMoreTextOverflow
import com.webtoonscorp.android.readmore.material3.ReadMoreText


class PhotoList {

    lateinit var navController: NavController

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun mainPhotoList(
        navController: NavController,
        photoViewModel: PhotoViewModel,
    ) {

        var pList = photoViewModel.photoList
        this.navController = navController

        if (pList == null) {
            photoViewModel.checkLatestPhotos()
        } else {

            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.largeTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary
                        ),
                        title = { Text(stringResource(R.string.app_name)) }
                    )
                }
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(0.dp, 80.dp, 0.dp, 0.dp)
                ) {
                    var pListSize = pList.size
                    items(pListSize) { index ->
                        photoCard(
                            pList[index], photoViewModel
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun photoCard(
        photo: DayPhoto,
        photoViewModel: PhotoViewModel
    ) {

        Card(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colorScheme.background),
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.size(16.dp))

                Text(
                    text = "[ ${photo.date} ]",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )

                AsyncImage(
//                    painter = painterResource(id = photo.url),
                    model = photo.url,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .padding(4.dp)
                        .clip(RoundedCornerShape(0.3f))
                        .fillMaxSize()
                        .fillMaxWidth()
                        .clickable(onClick = {
                            photoViewModel.setPhoto(photo)
                            // set navigation
                            navController.navigate("PhotoDetail")
                        }),
                    contentScale = ContentScale.Fit,
                )

                Text(
                    text = photo.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp)
                )

                val (expanded, onExpandedChange) = rememberSaveable { mutableStateOf(false) }
                ReadMoreText(
                    text = photo.explanation,
                    expanded = expanded,
                    onExpandedChange = onExpandedChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 0.dp, 8.dp, 16.dp),
                    textAlign = TextAlign.Justify,
                    fontStyle = FontStyle.Normal,
                    fontFamily = FontFamily.Default,
                    fontSize = 15.sp,
                    lineHeight = 20.sp,

                    /* read more */
                    readMoreText = "Read more",
                    readMoreColor = Color.Black,
                    readMoreFontSize = 15.sp,
                    readMoreFontStyle = FontStyle.Normal,
                    readMoreFontWeight = FontWeight.Bold,
                    readMoreFontFamily = FontFamily.Default,
                    readMoreTextDecoration = TextDecoration.None,
                    readMoreMaxLines = 4,
                    readMoreOverflow = ReadMoreTextOverflow.Ellipsis,
                    readMoreStyle = SpanStyle(

                    ),

                    /* read less */
                    readLessText = "Read less",
                    readLessColor = Color.Black,
                    readLessFontSize = 15.sp,
                    readLessFontStyle = FontStyle.Normal,
                    readLessFontWeight = FontWeight.Bold,
                    readLessFontFamily = FontFamily.Default,
                    readLessTextDecoration = TextDecoration.None,
                    readLessStyle = SpanStyle(
                        // ...
                    ),
                )
            }
        }
    }
}