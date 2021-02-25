package com.example.mylibrary.base

interface IView {

    fun initView()

    fun initData()
    fun getLayoutId(): Int
    fun initLayout(){
    }

     fun initViewModel(){}
}