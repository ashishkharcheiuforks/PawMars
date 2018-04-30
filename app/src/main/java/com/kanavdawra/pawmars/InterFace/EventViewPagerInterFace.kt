package com.kanavdawra.pawmars.InterFace

import java.text.FieldPosition

/**
 * Created by Kanav on 5/2/2018.
 */
interface EventViewPagerInterFace {
    fun pageChange(position: Int)
    fun notifyDataSetChanged(position: Int)

}