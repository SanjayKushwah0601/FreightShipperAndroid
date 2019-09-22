package com.freight.shipper.ui.authentication.signup.individual

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.freight.shipper.core.persistence.network.dispatchers.DispatcherProvider
import com.freight.shipper.core.persistence.network.dispatchers.DispatcherProviderImpl
import com.freight.shipper.core.persistence.network.result.APIResult
import com.freight.shipper.core.platform.ActionLiveData
import com.freight.shipper.core.platform.BaseViewModel
import com.freight.shipper.core.persistence.network.response.Country
import com.freight.shipper.core.persistence.network.response.State
import com.freight.shipper.repository.AuthenticationRepository
import com.freight.shipper.ui.authentication.signup.CompanySignup
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SignupViewModel(
    private val model: AuthenticationRepository,
    val dispatcher: DispatcherProvider = DispatcherProviderImpl()
) : BaseViewModel() {

    val nextButtonAction = ActionLiveData<CompanySignup>()
    val companySignupAction = ActionLiveData<Pair<String, Boolean>>()
    var companySignup = CompanySignup()

    private val selectedCountry = MutableLiveData<Country>()
    var countries: LiveData<List<Country>> = model.countries
    var states: LiveData<List<State>> =
        Transformations.switchMap(selectedCountry) { model.getPickStates(it.countryId) }


    // region - First page Text change listeners
    fun onAddressLine1Changed(line1: String) {
        companySignup.addressLine1 = line1
        Timber.d(companySignup.companyName)
    }

    fun onAddressLine2Changed(line2: String) {
        companySignup.addressLine2 = line2
        Timber.d(companySignup.companyName)
    }

    fun onCityChanged(city: String) {
        companySignup.city = city
        Timber.d(companySignup.companyName)
    }

    fun onPostCodeChanged(postCode: String) {
        companySignup.postcode = postCode
        Timber.d(companySignup.companyName)
    }

    fun onPasswordChanged(password: String) {
        companySignup.password = password
        Timber.d(companySignup.companyName)
    }

    fun onConfirmPasswordChanged(password: String) {
        if (companySignup.password != password) {
            Timber.d("Password Mismatch")
        }
    }

    fun onFirstNameChanged(firstName: String) {
        companySignup.firstName = firstName
        Timber.d(companySignup.companyName)
    }

    fun onLastNameChanged(lastName: String) {
        companySignup.lastName = lastName
        Timber.d(companySignup.companyName)
    }

    fun onPersonEmailChanged(email: String) {
        companySignup.email = email
        Timber.d(companySignup.companyName)
    }

    fun onPersonMobileChanged(phone: String) {
        companySignup.phone = phone
        Timber.d(companySignup.companyName)
    }
    // endregion

    fun onCountrySelect(country: Country) {
        companySignup.country = country.countryId
        selectedCountry.postValue(country)
    }

    fun onNextButtonClicked() {
        nextButtonAction.sendAction(companySignup)
    }

    fun onSignupButtonClicked() {
        GlobalScope.launch(dispatcher.io) {
            val result = model.individualSignup(companySignup)
            withContext(dispatcher.main) {
                when (result) {
                    is APIResult.Success -> {
                        model.saveLoginUser(result.response.data)
                        model.saveToken(result.response.data.sessionToken)
                        companySignupAction.sendAction(Pair("Signup Success", true))
                        Timber.e("Success Sanjay: ${result.response}")
                    }
                    is APIResult.Failure -> {
                        companySignupAction.sendAction(Pair("Signup Failed", false))
                        Timber.e("Failure Sanjay: ${result.error}")
                    }
                }
            }
        }
    }

    fun setCompanySignupForm(signup: CompanySignup?) {
        signup?.also { companySignup = it }
        Timber.d(companySignup.toString())
    }
}