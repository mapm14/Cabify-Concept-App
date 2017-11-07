package com.manuelperera.cabifychallenge.screen.activities.estimates

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import co.develoop.androidcleanarchitecture.screen.presenter.recyclerview.RecyclerViewAdapterItem
import com.manuelperera.cabifychallenge.R
import com.manuelperera.cabifychallenge.extensions.transitionDownAnimation
import com.manuelperera.cabifychallenge.extensions.transitionUpAnimation
import com.manuelperera.cabifychallenge.screen.activities.estimates.section.EstimatesActivityRecyclerAdapter
import kotlinx.android.synthetic.main.activity_estimates.*

class EstimatesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        transitionUpAnimation()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estimates)
        setToolbar()
        initRecyclerView()
    }

    private fun setToolbar() {
        setSupportActionBar(estimateToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
    }

    override fun onBackPressed() {
        super.onBackPressed()
        transitionDownAnimation()
    }

    override fun onDestroy() {
        estimateRecyclerView.adapter = null
        super.onDestroy()
    }

    private fun initRecyclerView() {
        estimateRecyclerView?.apply {
            val gridLayoutManager = GridLayoutManager(this@EstimatesActivity, 3)
            val estimatesActivityRecyclerAdapter = EstimatesActivityRecyclerAdapter(this@EstimatesActivity)
            val mSpanSizeLookup: GridLayoutManager.SpanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val type = estimatesActivityRecyclerAdapter.estimatesActivityRecyclerAdapterPresenter.listData[position].type
                    return if (type == RecyclerViewAdapterItem.Type.LOADING || type == RecyclerViewAdapterItem.Type.ERROR) {
                        3
                    } else 1
                }
            }
            gridLayoutManager.spanSizeLookup = mSpanSizeLookup

            layoutManager = gridLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = estimatesActivityRecyclerAdapter
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

}
