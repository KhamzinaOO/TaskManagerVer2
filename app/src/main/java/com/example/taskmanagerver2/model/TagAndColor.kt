package com.example.taskmanagerver2.model

import androidx.compose.ui.graphics.Color

data class TagAndColor(
    val name : String,
    val color : Color
)

object Constants {
    val statusColorList = listOf(
        TagAndColor("Не начато",Color(0xffC3C1EB)),
        TagAndColor("В работе",Color(0xffD4B0D0)),
        TagAndColor("Завершено",Color(0xff8FBB99)),
        TagAndColor("На проверке",Color(0xffCBDFBD)),
        TagAndColor("Отложено",Color(0xff8DA5B9)),
        TagAndColor("Отменено",Color(0xffFAE588)),
        TagAndColor("Просрочено",Color(0xffDB5461))
    )

    val tagColorList = listOf(
        TagAndColor("работа",Color(0xffffe5d9)),
        TagAndColor("встреча",Color(0xffd8e2dc)),
        TagAndColor("срочно",Color(0xffC08497)),
        TagAndColor("без срока",Color(0xffECE4DB)),
        TagAndColor("совещание",Color(0xffd8e8fd)),
        TagAndColor("ТЗ",Color(0xff947391)),
        TagAndColor("none",Color(0xffc4cdc8))
    )
}