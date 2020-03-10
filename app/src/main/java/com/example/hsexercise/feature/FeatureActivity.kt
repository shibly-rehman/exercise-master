package com.example.hsexercise.feature

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hsexercise.R
import com.example.hsexercise.common.BaseActivity
import com.example.hsexercise.feature.adapter.FeatureAdapter
import kotlinx.android.synthetic.main.activity_feature.*

class FeatureActivity : BaseActivity<FeatureViewModel>() {
    override val viewModelClass = FeatureViewModel::class.java
    override val layoutResId = R.layout.activity_feature

    private val adapter by lazy { FeatureAdapter(this) }

    override fun provideViewModelFactory() = FeatureViewModel.Factory()

    override fun onViewLoad(savedInstanceState: Bundle?) {
        // Setup features list
        list.adapter = adapter
        val layoutManager = LinearLayoutManager( this )
        list.layoutManager = layoutManager

        // Observe changes to Feature data (Lifecycle aware)
        viewModel.observeFeatures().observe(this, Observer {
            adapter.updateData(if (it.isNullOrEmpty()) mutableListOf() else it.toMutableList())
        })
        viewModel.getFeatures()
    }
}
