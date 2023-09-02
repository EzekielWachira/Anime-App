package com.example.anime.ui.screens.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.example.anime.ui.screens.home.components.AnimeItem
import com.example.anime.ui.theme.AnimeTheme
import com.example.anime.ui.theme.BlueGrey11
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun HomeScreen(navController: NavController) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    val context = LocalContext.current
    val viewModel: HomeViewModel = hiltViewModel()
    val animes = viewModel.animes.collectAsLazyPagingItems()
    val isRefreshing = animes.loadState.refresh == LoadState.Loading
    val swipeToRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = if (useDarkIcons)
                Color.White else BlueGrey11,
            darkIcons = useDarkIcons
        )
    }

    LaunchedEffect(key1 = animes.loadState) {
        if (animes.loadState.refresh is LoadState.Error) {
            context.showToast((animes.loadState.refresh as LoadState.Error).error.message)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (animes.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {

            SwipeRefresh(
                state = swipeToRefreshState, onRefresh = { animes.refresh() },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                swipeEnabled = true
            ) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(animes) { anime ->
//                        val anime = animes[index]
                        AnimeItem(
                            anime = anime, onAnimeClick = { context.showToast(it.title) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        if (animes.loadState.append is LoadState.Loading) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }

    }

}


fun <T: Any> LazyListScope.items(
    items: LazyPagingItems<T>,
    key: ( (T) -> Any )? = null,
    contentType: ( (T) -> Any )? = null,
    itemContent: @Composable LazyItemScope.(T) -> Unit
) {
    items(
        items.itemCount,
        key = items.itemKey(key),
        contentType = items.itemContentType(contentType)
    ) loop@ { i ->
        val item = items[i] ?: return@loop
        itemContent(item)
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    AnimeTheme {
        HomeScreen(rememberNavController())
    }
}

fun Context.showToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}