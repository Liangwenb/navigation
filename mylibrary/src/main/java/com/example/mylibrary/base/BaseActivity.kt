package com.example.mylibrary.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class BaseActivity : AppCompatActivity(), IView {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        initLayout()
        initView()
        initData()
    }

    override fun initLayout(){
        setContentView(getLayoutId())
    }



}

abstract class BaseDataBindActivity<mViewDataBinding : ViewDataBinding, viewModel : BaseViewModel> :BaseActivity() {

 lateinit var mViewDataBinding:mViewDataBinding
 lateinit var mViewModel:viewModel


    override fun initLayout() {
        mViewDataBinding=  DataBindingUtil.setContentView(this, getLayoutId())

    }
    override fun initViewModel() {
        mViewModel= ViewModelProvider(this).get(getViewModelClass())
    }
    private fun getViewModelClass():Class<viewModel>{
        val type: Type? = this.javaClass.genericSuperclass
        val arrayOfTypes = if (type is ParameterizedType) {
            type.actualTypeArguments
        } else arrayOf<Type>()
        return arrayOfTypes[1] as Class<viewModel>

    }
}