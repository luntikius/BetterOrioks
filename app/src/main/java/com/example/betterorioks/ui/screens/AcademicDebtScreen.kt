package com.example.betterorioks.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.betterorioks.R

@Composable
fun AcademicDebtTopBar(onClick: () -> Unit = {}){
    Card(
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth()
                .defaultMinSize(72.dp)
        ) {
            IconButton(onClick = onClick, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = stringResource(R.string.back_button),
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Row(modifier = Modifier.align(Alignment.Center).wrapContentSize(), verticalAlignment = Alignment.CenterVertically){
                Icon(painter = painterResource(R.drawable.debt),
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = stringResource(R.string.Debts),
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}


@Preview
@Composable
fun AcademicDebtScreen(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
){
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 10.dp,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Scaffold(
            topBar = { AcademicDebtTopBar(onClick = onClick) },
            backgroundColor = MaterialTheme.colors.surface
        ) { innerPadding ->
                LazyColumn(
                    Modifier
                        .padding(innerPadding)
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                ) {

                }
            }
        }
    }