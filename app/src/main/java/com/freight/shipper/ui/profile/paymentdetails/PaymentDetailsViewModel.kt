package com.freight.shipper.ui.profile.paymentdetails

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.freight.shipper.core.persistence.network.request.PaymentRequest
import com.freight.shipper.core.platform.BaseViewModel
import com.freight.shipper.repository.ProfileRepository
import timber.log.Timber


class PaymentDetailsViewModel(
    private val model: ProfileRepository
) : BaseViewModel() {

    val requestModel: PaymentRequest by lazy { PaymentRequest() }

    var error = MediatorLiveData<String>()

    var isLoading = ObservableField<Boolean>().apply { set(false) }
    val paymentDetailResponse: LiveData<String>
        get() = _paymentDetailResponse

    private val _paymentDetailResponse = MediatorLiveData<String>()


    fun submitPaymentDetails() {
        Timber.e(requestModel.toString())
        if (validateFormField()) {
            isLoading.set(true)
            model.savePaymentDetails(requestModel, Pair(_paymentDetailResponse, error))
        }
    }

    private fun validateFormField(): Boolean {
        return if (requestModel.accountNumber.isEmpty() || requestModel.bankName.isEmpty() ||
            requestModel.bankAddress.isEmpty() || requestModel.wireTransNumber.isEmpty() ||
            requestModel.currency.isEmpty()
        ) {
//            btnSignupEnable.set(false)
            Timber.d(requestModel.accountNumber)
            false
        } else {
//            btnSignupEnable.set(true)
            true
        }
    }
}