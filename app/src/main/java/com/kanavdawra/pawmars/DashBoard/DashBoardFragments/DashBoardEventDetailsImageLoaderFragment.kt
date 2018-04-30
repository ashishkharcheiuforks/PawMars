package com.kanavdawra.pawmars.DashBoard.DashBoardFragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.kanavdawra.pawmars.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_dash_board_event_details_image_loader.*
import java.io.File


class DashBoardEventDetailsImageLoaderFragment: Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dash_board_event_details_image_loader, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Picasso.get().load(File(arguments!!.getString("URL"))).placeholder(R.mipmap.event_default_image).into(event_details_imageloadar_image)
    }
}
