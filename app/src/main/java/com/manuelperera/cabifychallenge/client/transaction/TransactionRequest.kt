package com.manuelperera.cabifychallenge.client.transaction

import co.develoop.androidcleanarchitecture.client.transaction.Transaction
import io.reactivex.Observable

interface TransactionRequest<T> {

    fun modifyObservable(observable: Observable<T>): Observable<Transaction<T>>

    fun modifyObservableList(observable: Observable<List<T>>): Observable<Transaction<List<T>>>

}