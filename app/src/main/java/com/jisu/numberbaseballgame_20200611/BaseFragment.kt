package com.jisu.numberbaseballgame_20200611

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class BaseFragment :Fragment() {

    lateinit var mContext: Context

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mContext = activity as Context
    }
    abstract fun setupEvents()
    abstract fun setValues()
}