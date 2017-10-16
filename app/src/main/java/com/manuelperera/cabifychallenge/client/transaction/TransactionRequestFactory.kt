package com.manuelperera.cabifychallenge.client.transaction

interface TransactionRequestFactory<T: Any> {

    fun createTransactionRequest(): TransactionRequest<T>
}