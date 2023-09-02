package com.example.anime.ui.screens.upload

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.ehsanmsz.mszprogressindicator.progressindicator.BallPulseProgressIndicator
import com.ehsanmsz.mszprogressindicator.progressindicator.BallSpinFadeLoaderProgressIndicator
import com.example.anime.R
import com.example.anime.data.remote.dto.image_upload.UploadImageDto
import com.example.anime.data.utils.StateWrapper
import com.example.anime.ui.screens.upload.components.BottomButtonSection
import com.example.anime.ui.screens.upload.components.ResponseItem
import com.example.anime.ui.theme.AnimeTheme
import com.example.anime.ui.theme.BlueGrey11
import com.example.anime.ui.theme.Green54
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.nryde.driver.utils.file.FileUtil
import com.nryde.driver.utils.file.MultipartBodyUtil
import kotlinx.coroutines.launch

@Composable
fun UploadScreen(navController: NavController) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val viewModel: UploadViewModel = hiltViewModel()
    val imageState by viewModel.imageState.collectAsStateWithLifecycle()
    val uploadState by viewModel.uploadState.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        viewModel.onImageSelect(uri)
    }


    SideEffect {
        systemUiController.setSystemBarsColor(
            color = if (useDarkIcons)
                Color.White else BlueGrey11,
            darkIcons = useDarkIcons
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .scrollable(scrollState, Orientation.Vertical)
                .padding(all = 16.dp),
        ) {

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,

                ) {

                Text(
                    text = "Select image from your phone gallery to upload",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.inversePrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(30.dp))

                if (imageState.uri != null) {
                    AsyncImage(
                        model = imageState.uri, contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )

                    when (uploadState.state) {
                        is StateWrapper.Empty -> {}
                        is StateWrapper.Loading -> {
                            Column(
                                modifier = Modifier
                                    .padding(top = 30.dp, bottom = 30.dp)
                                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {

                                Spacer(modifier = Modifier.height(30.dp))

                                BallPulseProgressIndicator(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(30.dp)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "Uploading Image",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.inversePrimary
                                )

                                Spacer(modifier = Modifier.height(30.dp))
                            }
                        }

                        is StateWrapper.Success -> {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                item {
                                    Column {
                                        Spacer(modifier = Modifier.height(20.dp))
                                        Text(
                                            text = "Response",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.inversePrimary
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                    }
                                }
                                items((uploadState.state as StateWrapper.Success<UploadImageDto>).data.result) { uploadResult ->
                                    ResponseItem(
                                        result = uploadResult,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        is StateWrapper.Failure -> {
                            Box(
                                modifier = Modifier
                                    .padding(top = 30.dp, bottom = 30.dp)
                                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                                    .clip(RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (uploadState.state as StateWrapper.Failure).errorMessage,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Red,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                } else {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.dashedBorder(
                            1.dp,
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                            10.dp
                        )
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    20.dp
                                )
                        ) {
                            Spacer(modifier = Modifier.height(30.dp))

                            Icon(
                                painter = painterResource(id = R.drawable.gallery),
                                contentDescription = "gallery icon",
                                modifier = Modifier.size(50.dp)
                            )

                            Spacer(modifier = Modifier.height(30.dp))

                            Button(
                                onClick = {
                                    launcher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.onPrimary,
                                    disabledContainerColor = Green54
                                )
                            ) {

                                Text(
                                    text = "Select Image ",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }

                            Spacer(modifier = Modifier.height(30.dp))

                        }

                    }
                }

            }

            if (imageState.uri != null && (uploadState.state is StateWrapper.Empty ||
                        uploadState.state is StateWrapper.Failure)
            )
                BottomButtonSection(text = "Upload", imageState = imageState,
                    onButtonClick = {
                        coroutineScope.launch {
                            viewModel.uploadImage(
                                MultipartBodyUtil.getMultipartBody(
                                    FileUtil.from(context, imageState.uri)!!,
                                    "imageLeft"
                                )
                            )
                        }
                    },
                    onClearButtonClick = {
                        viewModel.onImageSelect(null)
                    }
                )
        }
    }
}


fun Modifier.dashedBorder(strokeWidth: Dp, color: Color, cornerRadiusDp: Dp) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }
        val cornerRadiusPx = density.run { cornerRadiusDp.toPx() }

        this.then(
            Modifier.drawWithCache {
                onDrawBehind {
                    val stroke = Stroke(
                        width = strokeWidthPx,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )

                    drawRoundRect(
                        color = color,
                        style = stroke,
                        cornerRadius = CornerRadius(cornerRadiusPx)
                    )
                }
            }
        )
    }
)


@Preview
@Composable
fun UploadScreenPreview() {
    AnimeTheme {
        UploadScreen(rememberNavController())
    }
}

