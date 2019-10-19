package com.freight.shipper.ui.bookings.assigned

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.freight.shipper.core.persistence.network.response.ActiveLoad
import com.freight.shipper.core.persistence.network.response.EmptyResponse
import com.freight.shipper.core.persistence.network.response.PastLoad
import com.freight.shipper.core.persistence.network.result.NetworkCallback
import com.freight.shipper.core.platform.ActionLiveData
import com.freight.shipper.core.platform.BaseViewModel
import com.freight.shipper.model.LoadStatus
import com.freight.shipper.repository.LoadRepository
import com.freight.shipper.ui.bookings.assigned.pager.ActiveLoadEventListener

class LoadPagerViewModel(private val model: LoadRepository) : BaseViewModel(),
    ActiveLoadEventListener {

    var isLoading = MutableLiveData<Boolean>()
    var error = MediatorLiveData<String>()
    val startRouteAction = ActionLiveData<ActiveLoad>()


    val activeLoads: LiveData<List<ActiveLoad>>
        get() {
            fetchActiveLoad()
            return _activeLoads
        }

    val pastLoads: LiveData<List<PastLoad>>
        get() {
            fetchPastLoad()
            return _pastLoads
        }

    private val _activeLoads = MediatorLiveData<List<ActiveLoad>>()
    private val _pastLoads = MediatorLiveData<List<PastLoad>>()

    private fun fetchActiveLoad() {
        isLoading.postValue(true)
        model.fetchActiveLoad(Pair(_activeLoads, error))
    }

    private fun fetchPastLoad() {
        isLoading.postValue(true)
        model.fetchPastLoad(Pair(_pastLoads, error))
    }

    override fun onStartRoute(load: ActiveLoad) {
        Log.e("ActiveLoad", load.toString())
        load.loadsId?.also {
            isLoading.postValue(true)
            model.updateLoadStatus(
                it, LoadStatus.Delivery_on_his_way,
                object : NetworkCallback<EmptyResponse> {
                    override fun success(result: EmptyResponse) {
                        // TODO : Save status
                        load.loadStatusId = LoadStatus.Delivery_on_his_way.id.toString()
                        startRouteAction.sendAction(load)
                    }

                    override fun failure(errorMessage: String) {
                        error.postValue(errorMessage)
                    }
                })
        }
    }
}
