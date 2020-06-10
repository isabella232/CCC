/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main.settings

import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.basemob.model.MutableSingleLiveData
import com.github.mustafaozhan.basemob.model.SingleLiveData
import com.github.mustafaozhan.basemob.util.toUnit
import com.github.mustafaozhan.basemob.viewmodel.BaseViewModel
import com.github.mustafaozhan.scopemob.either
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import mustafaozhan.github.com.mycurrencies.data.db.CurrencyDao
import mustafaozhan.github.com.mycurrencies.data.db.DBInitialise
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.ui.main.MainData.Companion.MINIMUM_ACTIVE_CURRENCY
import mustafaozhan.github.com.mycurrencies.util.extension.removeUnUsedCurrencies
import javax.inject.Inject

class SettingsViewModel
@Inject constructor(
    preferencesRepository: PreferencesRepository,
    private val currencyDao: CurrencyDao,
    private val dbInitialise: DBInitialise
) : BaseViewModel(), SettingsEvent {

    // region SEED
    private val _states = SettingsStateBacking()
    val state = SettingsState(_states)

    private val _effect = MutableSingleLiveData<SettingsEffect>()
    val effect: SingleLiveData<SettingsEffect> = _effect

    val data = SettingsData(preferencesRepository)

    fun getEvent() = this as SettingsEvent
    // endregion

    init {
        initData()
        _states._searchQuery.addSource(state.searchQuery) {
            filterList(it)
        }

        viewModelScope.launch {
            currencyDao.getAllCurrencies()
                .map { it.removeUnUsedCurrencies() }
                .collect { currencyList ->

                    _states._currencyList.value = currencyList
                    data.unFilteredList = currencyList

                    currencyList
                        ?.filter { it.isActive }?.size
                        ?.whether { it < MINIMUM_ACTIVE_CURRENCY }
                        ?.whetherNot { data.firstRun }
                        ?.let { _effect.postValue(FewCurrencyEffect) }

                    verifyCurrentBase()
                    filterList("")
                }
        }

        filterList("")
    }

    private fun initData() = viewModelScope
        .whether { data.firstRun }
        ?.launch {
            _states._loading.value = true
            dbInitialise.insertInitialCurrencies()
            _states._loading.value = false
        }?.toUnit()

    private fun filterList(txt: String) = data.unFilteredList
        ?.filter { (name, longName, symbol) ->
            name.contains(txt, true) ||
                longName.contains(txt, true) ||
                symbol.contains(txt, true)
        }?.toMutableList()
        ?.let { _states._currencyList.value = it }

    private fun verifyCurrentBase() = data.currentBase.either(
        { equals(Currencies.NULL.toString()) },
        { base ->
            state.currencyList.value
                ?.filter { it.name == base }
                ?.toList()?.firstOrNull()?.isActive == false
        }
    )?.let {
        updateCurrentBase(
            state.currencyList.value
                ?.firstOrNull { it.isActive }?.name
                ?: Currencies.NULL.toString()
        )
    }.run {
        _states._searchQuery.value = ""
    }

    private fun updateCurrentBase(newBase: String) {
        data.currentBase = newBase
        _effect.postValue(ChangeBaseNavResultEffect(newBase))
    }

    // region Event
    override fun updateAllCurrenciesState(state: Boolean) = viewModelScope.launch {
        currencyDao.updateAllCurrencyState(state)
    }.toUnit()

    override fun onItemClick(currency: Currency) = viewModelScope.launch {
        currencyDao.updateCurrencyStateByName(currency.name, !currency.isActive)
    }.toUnit()

    override fun onDoneClick() = _states._currencyList.value
        ?.filter { it.isActive }?.size
        ?.whether { it < MINIMUM_ACTIVE_CURRENCY }
        ?.let { _effect.postValue(FewCurrencyEffect) }
        ?: run {
            data.firstRun = false
            _effect.postValue(CalculatorEffect)
        }
    // endregion
}
