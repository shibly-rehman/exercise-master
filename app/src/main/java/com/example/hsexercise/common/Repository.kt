package com.example.hsexercise.common

/**
 * Repository - Facade layer for all persisted data retrieval.
 */
object Repository {

    private val database by lazy { DatabaseProvider.provideRoomDatabase(App.context) }
    private val api by lazy { NetworkProvider.api }

    private val TAG = Repository::class.java.simpleName

}