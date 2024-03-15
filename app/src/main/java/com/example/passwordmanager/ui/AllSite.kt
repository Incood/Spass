package com.example.passwordmanager.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.passwordmanager.R
import com.example.passwordmanager.data.Website
import com.example.passwordmanager.ui.theme.PasswordManagerTheme
import kotlinx.coroutines.delay


@Composable
fun AddButton(
    onNextButtonClicked: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onNextButtonClicked,
        modifier = modifier.padding(
            start = dimensionResource(R.dimen.padding_small),
            end = dimensionResource(R.dimen.padding_small),
            bottom = dimensionResource(R.dimen.padding_medium)
        )
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
    }
}

@Composable
fun SiteApp(viewModel: PassViewModel = viewModel()) {
    val sites by viewModel.sites.collectAsState()

    LazyColumn(
        state = rememberLazyListState(),
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_small))
    ) {
        items(sites) { site ->
            SwipeToDeleteContainer(
                site = site,
                onDelete = { viewModel.removeSite(site) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteContainer(
    site: Website,
    onDelete: (Website) -> Unit,
    animationDuration: Int = 500
) {
    val currentItem by rememberUpdatedState(site)
    var isRemoved by remember {
        mutableStateOf(false)
    }
    val state = rememberDismissState(
        confirmValueChange = { value ->
            if (value == DismissValue.DismissedToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(key1 = isRemoved) {
        if(isRemoved) {
            delay(animationDuration.toLong())
            onDelete(currentItem)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismiss(
            state = state,
            directions = setOf(DismissDirection.EndToStart),
            background = {
                Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(id = R.dimen.padding_small),
                        end = dimensionResource(id = R.dimen.padding_small),
                        top = dimensionResource(id = R.dimen.padding_medium),
                        bottom = dimensionResource(id = R.dimen.padding_medium)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Red)
                        .size(dimensionResource(R.dimen.card_size))
                        .padding(dimensionResource(R.dimen.padding_small))
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        },
            dismissContent = { SiteItem(site = currentItem) }
        )
    }
}

@Composable
fun SiteItem(
    site: Website,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(id = R.dimen.padding_small),
                end = dimensionResource(id = R.dimen.padding_small),
                top = dimensionResource(id = R.dimen.padding_medium),
                bottom = dimensionResource(id = R.dimen.padding_medium)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            Image(
                painter = painterResource(site.icon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .size(dimensionResource(R.dimen.image_size))
                    .clip(MaterialTheme.shapes.small)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.padding()) {
                Text(
                    text = site.siteName,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(
                        top = dimensionResource(R.dimen.padding_small)
                    )
                )
                Text(text = "Person: ${site.personName}")
                Text(text = "Password: ${site.password}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSite() {
    PasswordManagerTheme {
        SiteApp()
    }
}