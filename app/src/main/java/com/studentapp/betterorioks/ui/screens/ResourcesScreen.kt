package com.studentapp.betterorioks.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studentapp.betterorioks.R
import com.studentapp.betterorioks.model.subjectsFromSite.ResourceCategory
import com.studentapp.betterorioks.ui.components.ErrorScreen
import com.studentapp.betterorioks.ui.components.LoadingScreen
import com.studentapp.betterorioks.ui.states.ResourcesUiState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ResourcesScreen(
    resourcesUiState: ResourcesUiState,
    onBackButtonClick: () -> Unit,
    onRefresh: () -> Unit,
    subjectName: String
){
    val pullRefreshState = rememberPullRefreshState(
        (resourcesUiState == ResourcesUiState.Loading),
        { onRefresh() }
    )
    Scaffold(
        topBar = {
            TopBar(
                name = subjectName,
                onClick = onBackButtonClick
            )
        }
    ){
        Box(
            modifier = Modifier
                .pullRefresh(pullRefreshState)
                .padding(it)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                when (resourcesUiState) {
                    is ResourcesUiState.Success -> {
                        item{Spacer(modifier = Modifier.size(8.dp))}
                        items(resourcesUiState.resourceCategories) {
                            Category(category = it)
                        }
                        item{Spacer(modifier = Modifier.size(8.dp))}
                    }
                    is ResourcesUiState.Error -> {
                        item { ErrorScreen(modifier = Modifier.fillMaxSize()) }
                    }
                    else -> {
                        item { LoadingScreen(modifier = Modifier.fillMaxSize()) }
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = resourcesUiState == ResourcesUiState.Loading,
                state = pullRefreshState, modifier = Modifier.align(Alignment.TopCenter),
                contentColor = MaterialTheme.colors.secondary
            )
        }
    }
}

@Composable
fun TopBar(name: String, onClick: () -> Unit){
    Card(
        shape = RoundedCornerShape(0),
        backgroundColor = MaterialTheme.colors.primaryVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            IconButton(onClick = { onClick() }) {
                Icon(
                    painterResource(id = R.drawable.arrow_back),
                    contentDescription = stringResource(id = R.string.back_button),
                    modifier = Modifier
                        .size(20.dp),
                    tint = MaterialTheme.colors.primary,
                )
            }
            Spacer(modifier = Modifier.size(2.dp))
            Text(
                text = "Ресурсы по предмету $name",
                modifier = Modifier,
                fontSize = 16.sp,
            )
        }
    }
}

@Composable
fun Category(category: ResourceCategory){
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .fillMaxWidth()
            .defaultMinSize(60.dp),
        backgroundColor = MaterialTheme.colors.surface
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
                verticalArrangement = Arrangement.Center
        ) {
            var isExpanded by rememberSaveable { mutableStateOf(false) }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text(
                    text = category.name,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        painter = painterResource(id = R.drawable.expand_more),
                        contentDescription = stringResource(R.string.expand),
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .size(28.dp)
                            .rotate(if (isExpanded) 180f else 0f)
                    )
                }
            }
            if (isExpanded) {
                Spacer(modifier = Modifier.size(8.dp))
                category.resources.forEach {
                    val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(it.link)) }

                    Divider()
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(
                                vertical = 8.dp
                            )
                            .clickable {
                                try {
                                    context.startActivity(intent)
                                }catch (_e:Throwable){
                                    Toast.makeText(context, context.getString(R.string.toast_unsupported_file_format), Toast.LENGTH_LONG).show()
                                }
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(
                                text = it.type,
                                color = MaterialTheme.colors.primary,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.size(4.dp))
                            Text(text = it.name)
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_forward),
                            contentDescription = stringResource(id = R.string.move_next),
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}