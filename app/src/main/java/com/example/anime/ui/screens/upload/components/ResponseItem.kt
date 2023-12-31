package com.example.anime.ui.screens.upload.components

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.anime.R
import com.example.anime.data.remote.dto.image_upload.UploadResult
import com.example.anime.domain.model.Anime

@Composable
fun ResponseItem(modifier: Modifier = Modifier, result: UploadResult) {
    Surface(
        onClick = { },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            AsyncImage(
                model = result.image, contentDescription = result.filename + " image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Text(
                    text = result.filename, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inversePrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.list),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "ID:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.inversePrimary
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = result.anilist.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.inversePrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.similarity),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Similarity:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.inversePrimary
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = (result.similarity * 100).toInt().toString() + "%",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.inversePrimary
                    )
                }
            }
        }
    }
}