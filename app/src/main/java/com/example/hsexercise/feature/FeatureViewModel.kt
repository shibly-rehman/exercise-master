package com.example.hsexercise.feature

import androidx.lifecycle.*
import com.example.hsexercise.common.Repository
import com.example.hsexercise.feature.database.FeatureModel
import io.reactivex.schedulers.Schedulers

class FeatureViewModel : ViewModel() {

    val data: MediatorLiveData<List<FeatureModel>> = MediatorLiveData()

    class Factory :
        ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) = FeatureViewModel() as T
    }

    fun getFeatures() {
        // Convert flowable to live data
        val source = LiveDataReactiveStreams.fromPublisher (
            Repository.getFeatures().
                subscribeOn(Schedulers.io())
        )
        data.addSource(source) {
            data.value = it
            data.removeSource(source)
        }
    }

    /** Observe changes to features data */
    fun observeFeatures(): LiveData<List<FeatureModel>> = data
}
