package com.manuelperera.cabifychallenge.client.transaction

class TransactionRequestFactoryImpl<T : Any> : TransactionRequestFactory<T> {

    override fun createTransactionRequest(): TransactionRequest<T> = TransactionRequestImpl()
}