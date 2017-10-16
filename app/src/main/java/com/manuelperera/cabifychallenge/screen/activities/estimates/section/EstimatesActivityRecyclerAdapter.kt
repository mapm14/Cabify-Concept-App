package com.manuelperera.cabifychallenge.screen.activities.estimates.section

import android.support.v7.app.AlertDialog
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import co.develoop.androidcleanarchitecture.screen.presenter.actions.PresenterBinder
import co.develoop.androidcleanarchitecture.screen.presenter.recyclerview.RecyclerViewAdapterItem
import com.manuelperera.cabifychallenge.CabifyChallengeApp
import com.manuelperera.cabifychallenge.R
import com.manuelperera.cabifychallenge.domain.objects.Estimate
import com.manuelperera.cabifychallenge.extensions.convertDpToPixel
import com.manuelperera.cabifychallenge.extensions.inflate
import com.manuelperera.cabifychallenge.screen.activities.estimates.EstimatesActivity
import com.manuelperera.cabifychallenge.screen.activities.estimates.section.injection.DaggerEstimatesActivityRecyclerAdapterComponent
import com.manuelperera.cabifychallenge.view.view_holder.ErrorSectionViewHolder
import com.manuelperera.cabifychallenge.view.view_holder.LoadingSectionViewHolder
import com.manuelperera.cabifychallenge.view.view_holder.RecyclerViewViewHolder
import com.manuelperera.cabifychallenge.view.widget.EstimateChromeView
import kotlinx.android.synthetic.main.item_network_error.view.*
import javax.inject.Inject

class EstimatesActivityRecyclerAdapter(private val estimatesActivity: EstimatesActivity) : RecyclerView.Adapter<RecyclerViewViewHolder<Estimate>>(), EstimatesActivityRecyclerAdapterView {

    @Inject
    lateinit var estimatesActivityRecyclerAdapterPresenter: EstimatesActivityRecyclerAdapterPresenter

    init {
        DaggerEstimatesActivityRecyclerAdapterComponent.builder().appComponent(CabifyChallengeApp.Companion.daggerAppComponent()).build().inject(this)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        estimatesActivityRecyclerAdapterPresenter.init(this)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
        estimatesActivityRecyclerAdapterPresenter.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerViewViewHolder<Estimate>? =
            when (viewType) {
                RecyclerViewAdapterItem.Type.ITEM.ordinal -> EstimateViewHolder(EstimateChromeView(parent!!.context), estimatesActivityRecyclerAdapterPresenter)
                RecyclerViewAdapterItem.Type.LOADING.ordinal -> LoadingSectionViewHolder((parent!!.inflate(R.layout.item_loading)))
                else -> ErrorSectionViewHolder((parent!!.inflate(R.layout.item_network_error)), { view ->
                    estimatesActivityRecyclerAdapterPresenter.bindReloadDataObservable(view.networkErrorRetryButton.asObservable())
                })
            }

    override fun onBindViewHolder(holder: RecyclerViewViewHolder<Estimate>, position: Int) {
        holder.configure(estimatesActivityRecyclerAdapterPresenter.listData[position])
    }

    override fun getItemCount(): Int = estimatesActivityRecyclerAdapterPresenter.listData!!.size

    override fun getItemViewType(position: Int): Int = estimatesActivityRecyclerAdapterPresenter.listData!![position].type.ordinal

    override fun getDiffResultBinder(): PresenterBinder<DiffUtil.DiffResult> = PresenterBinder { diffResult -> diffResult!!.dispatchUpdatesTo(this@EstimatesActivityRecyclerAdapter) }

    override fun showInfoDialog(estimate: Estimate) {
        val dialog = AlertDialog.Builder(estimatesActivity, R.style.AppCompatAlertDialogStyle)
        dialog.setTitle(estimate.vehicleType.name)
        dialog.setMessage(estimate.vehicleType.description)
        dialog.setPositiveButton(android.R.string.ok, { _, _ -> })
        dialog.show()
    }

    private class EstimateViewHolder(view: View, val presenter: EstimatesActivityRecyclerAdapterPresenter) : RecyclerViewViewHolder<Estimate>(view) {
        override fun configure(item: Estimate?) {
            val categoryChromeView: EstimateChromeView = (itemView as EstimateChromeView)
            categoryChromeView.setCategoryChrome(item!!)
            presenter.bindItemClick(itemView, presenter.listData?.get(adapterPosition)!!)

            val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.topMargin = convertDpToPixel(16f).toInt()
            layoutParams.marginStart = convertDpToPixel(4f).toInt()
            layoutParams.marginEnd = convertDpToPixel(4f).toInt()
            categoryChromeView.layoutParams = layoutParams
        }
    }

}