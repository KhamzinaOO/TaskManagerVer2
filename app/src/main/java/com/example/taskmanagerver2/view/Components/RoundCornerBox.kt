package com.example.taskmanagerver2.view.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RoundCornerBox(
    modifier: Modifier,
    color: Color,
    cornerSize: Dp,
    contentAlignment: Alignment,
    onClickAction:() -> Unit,
    insideContent: @Composable () -> Unit,
    )
{
    Box(
    modifier = modifier
        .border(
            1.dp,
            Color.DarkGray,
            RoundedCornerShape(cornerSize)
        )
        .background(
            color,
            RoundedCornerShape(cornerSize)
        )
        .clickable { onClickAction() },
        contentAlignment
    ){
        insideContent()
    }
}