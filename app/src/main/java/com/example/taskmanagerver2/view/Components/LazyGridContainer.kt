package com.example.taskmanagerver2.view.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanagerver2.view.Components.StringConstants.mainFontSize

@Composable
fun LazyGridContainer(
    modifier: Modifier,
    elements : List<String>,
    columns : StaggeredGridCells,
    tagAndColorList : List<TagAndColor>,
    onTagClick:(String)-> Unit

){
    val gridState = rememberLazyStaggeredGridState()
    Box(
        modifier = modifier
            .padding(start = 16.dp)
            .width(220.dp)
            .heightIn(30.dp, 100.dp)
    ) {
        LazyVerticalStaggeredGrid(
            columns = columns,
            modifier = Modifier.wrapContentWidth(),
            state = gridState,
            horizontalArrangement = Arrangement.Start,
            verticalItemSpacing = 2.dp,
        ) {
            items(elements) { tag ->
                tagAndColorList.find { it.name == tag }?.let { tagColor ->
                    RoundCornerBox(
                        modifier = Modifier.padding(2.dp),
                        color = tagColor.color,
                        cornerSize = 20.dp,
                        onClickAction = { onTagClick(tag) },
                        insideContent = {
                            Text(
                                text = tag,
                                fontSize = mainFontSize
                            )
                        },
                        contentAlignment = Alignment.Center
                    )
                }
            }
        }
    }
    }