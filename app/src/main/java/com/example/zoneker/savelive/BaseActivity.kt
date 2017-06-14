package com.example.zoneker.savelive

import android.app.Activity
import android.os.Bundle

/**
 * Created by Zoneker on 2017/6/13.
 */
abstract class BaseActivity : Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    abstract fun initView()
}