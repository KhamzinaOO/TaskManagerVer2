package com.example.taskmanagerver2

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmanagerver2.model.Constants.tagColorList
import com.example.taskmanagerver2.model.database.TasksDbEntity

@Composable
fun BoardContentItem(
    modifier: Modifier = Modifier,
    boardItem: TasksDbEntity,
    isExpanded: Boolean,
    listState: LazyListState
) {
    val tagsList = boardItem.tag.split(",").map { it.trim() }
    DragTarget(
        modifier = modifier,
        dataToDrop = boardItem,
        listState = listState
    ) {
        val color = tagColorList.find { it.name == tagsList[0] }?.color

        if (isExpanded) {
            ExpandedItem(boardItem, tagsList)
        } else {
            ItemHeading(boardItem, tagsList)
        }
    }
}

@Composable
fun ItemHeading(
    task: TasksDbEntity,
    tagsList : List<String>
){

    val color = tagColorList.find { it.name == tagsList[0] }?.color

    Row(
        Modifier
            .wrapContentSize()
            .background(color ?: Color.White, RoundedCornerShape(4.dp, 4.dp))
            .border(BorderStroke(1.dp, Color.DarkGray), RoundedCornerShape(4.dp, 4.dp))
            .clickable {
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(task.title,
            fontSize = 24.sp,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(4.dp,2.dp))
    }
}


@Composable
fun ExpandedItem(
    task: TasksDbEntity,
    tagsList : List<String>
){
    var lines by remember { mutableIntStateOf(1) }
    var arrowIcon by remember { mutableStateOf(Icons.Default.KeyboardArrowDown) }
    Column (
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp)
            .border(BorderStroke(1.dp, Color.DarkGray), RoundedCornerShape(4.dp, 4.dp))
    ){
        ItemHeading(task = task, tagsList)
        Column(

        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = task.content,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.wrapContentWidth(),
                    maxLines = lines,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 24.sp
                )
                IconButton(
                    modifier = Modifier.size(10.dp),
                    onClick = {
                        if (lines == 1) {
                            lines = 50
                            arrowIcon = Icons.Default.KeyboardArrowUp
                        } else {
                            lines = 1
                            arrowIcon = Icons.Default.KeyboardArrowDown
                        }
                    }) {
                    Icon(imageVector = arrowIcon, contentDescription = "")
                }
            }
            val gridState = rememberLazyStaggeredGridState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(30.dp, 100.dp),
                horizontalArrangement = Arrangement.Center
            )
            {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(3),
                    modifier = Modifier.wrapContentWidth(),
                    state = gridState,
                    horizontalArrangement = Arrangement.Start,
                    verticalItemSpacing = 1.dp,
                ) {
                    items(tagsList) { tag ->
                        tagColorList.find { it.name == tag }?.let { tagColor ->
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .background(
                                        color = tagColor.color,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .border(
                                        border = BorderStroke(1.dp, Color.DarkGray),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .padding(2.dp, 1.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    tag,
                                    fontSize = 5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
