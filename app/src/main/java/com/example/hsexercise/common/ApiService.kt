package com.example.hsexercise.common

import com.example.hsexercise.feature.database.FeatureModel
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET

interface ApiService {

    @GET("v2/list")
    fun getFeatures (): Flowable<List<FeatureModel>>
}

