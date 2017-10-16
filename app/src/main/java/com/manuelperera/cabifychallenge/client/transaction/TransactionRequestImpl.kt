package com.manuelperera.cabifychallenge.client.transaction

import co.develoop.androidcleanarchitecture.client.transaction.Transaction
import co.develoop.androidcleanarchitecture.client.transaction.TransactionStatus
import io.reactivex.Observable
import io.reactivex.ObservableSource
import java.util.concurrent.TimeUnit

class TransactionRequestImpl<T : Any> : TransactionRequest<T> {

    override fun modifyObservable(observable: Observable<T>): Observable<Transaction<T>> =
            observable.flatMap { data ->
                    ObservableSource<Transaction<T>> { subscriber ->
                        subscriber.onNext(Transaction(data, TransactionStatus.SUCCESS))
                        subscriber.onComplete()
                    }
                }
                .onExceptionResumeNext {
                    ObservableSource<Transaction<T>> { subscriber ->
                        subscriber.onNext(Transaction(TransactionStatus.EXCEPTION))
                        subscriber.onComplete()
                    }
                }
                .timeout(10, TimeUnit.SECONDS, Observable.create<Transaction<T>> { subscriber ->
                    subscriber.onNext(Transaction(TransactionStatus.TIMEOUT))
                    subscriber.onComplete()
                })

    override fun modifyObservableList(observable: Observable<List<T>>): Observable<Transaction<List<T>>> =
            observable.flatMap { data ->
                    ObservableSource<Transaction<List<T>>> { subscriber ->
                        subscriber.onNext(Transaction(data, TransactionStatus.SUCCESS))
                        subscriber.onComplete()
                    }
                }
                .onExceptionResumeNext {
                    ObservableSource<Transaction<List<T>>> { subscriber ->
                        subscriber.onNext(Transaction(TransactionStatus.EXCEPTION))
                        subscriber.onComplete()
                    }
                }
                .timeout(10, TimeUnit.SECONDS, Observable.create<Transaction<List<T>>> { subscriber ->
                    subscriber.onNext(Transaction(TransactionStatus.TIMEOUT))
                    subscriber.onComplete()
                })

}