package org.wahyuheriyanto.nutricom.view

import androidx.compose.runtime.Composable
import androidx.constraintlayout.compose.ConstraintLayout

@Composable

fun SchedueScreen(){

    ConstraintLayout {

        val (title,date,calendar,titleTwo,list,button) = createRefs()

        val startGuideline = createGuidelineFromStart(0.1f)
        val endGuideline = createGuidelineFromEnd(0.1f)




    }
}