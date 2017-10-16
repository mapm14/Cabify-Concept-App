package com.manuelperera.cabifychallenge.screen.activities.estimates.section

import android.view.View
import co.develoop.androidcleanarchitecture.client.transaction.Transaction
import co.develoop.androidcleanarchitecture.screen.presenter.recyclerview.RecyclerViewAdapterItem
import co.develoop.androidcleanarchitecture.screen.presenter.recyclerview.SimpleRecyclerViewAdapterPresenter
import com.manuelperera.cabifychallenge.domain.objects.Estimate
import com.manuelperera.cabifychallenge.usecase.estimate.GetEstimatesUseCase
import io.reactivex.Completable
import io.reactivex.Observable

class EstimatesActivityRecyclerAdapterPresenter(private val getEstimatesUseCase: GetEstimatesUseCase) : SimpleRecyclerViewAdapterPresenter<EstimatesActivityRecyclerAdapterView, Estimate>() {

    override fun getLoadObservable(): Observable<Transaction<List<Estimate>>> = getEstimatesUseCase.bind()

    override fun getItemClickCompletable(itemView: View, estimate: Estimate?): Completable =
            Completable.create {
                estimate?.let {
                    view.showInfoDialog(estimate)
                }
                it.onComplete()
            }

    override fun getLoadingList(): MutableList<Estimate> = mutableListOf(Estimate(RecyclerViewAdapterItem.Type.LOADING))

    override fun getNetworkErrorList(): MutableList<Estimate> = mutableListOf(Estimate(RecyclerViewAdapterItem.Type.ERROR))

}