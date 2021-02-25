package com.example.mylibrary.base

import android.app.Application

 abstract class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initComponent()
        initFeatures()
    }



     /**
      * 初始化组件
      */
     abstract fun initComponent()

     /**
      * 初始化功能
      */
     abstract fun initFeatures()
 }