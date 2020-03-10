package com.example.hsexercise.common

import android.annotation.SuppressLint
import android.util.Log
import com.example.hsexercise.feature.database.FeatureModel
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * Repository - Facade layer for all persisted data retrieval.
 */
object Repository {

    private val database by lazy { DatabaseProvider.provideRoomDatabase(App.context) }
    private val api by lazy { NetworkProvider.api }

    private val TAG = Repository::class.java.simpleName

    /** Get Features from database */
    private fun getFeaturesFromDb (): Flowable<List<FeatureModel>> {
        return database.featureTableDao().getAll()
            .toFlowable()
            .doOnNext {
                Log.d(TAG,"Dispatching ${it.size} features from DB...")
            }
    }

    /** Get Features from api and cache them to database */
    private fun getFeaturesFromApi(): Flowable<List<FeatureModel>> {
        return api.getFeatures()
            .onErrorReturn { e -> Log.d(TAG, "Request failed $e"); emptyList() }
            .doOnNext {
                Log.d(TAG,"Dispatching ${it.size} features from API...")
                storeFeaturesInDb(it)
            }
    }

    fun getFeatures() : Flowable<List<FeatureModel>> = getFeaturesFromApi()

    @SuppressLint("CheckResult")
    /** Store features in database */
    private fun storeFeaturesInDb(users: List<FeatureModel>) {
        Observable.fromCallable { database.featureTableDao().insertAll(users) }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                Log.d(TAG, "Inserted ${users.size} features from API in DB...")
            }
    }
}