/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.ui.bar

import com.github.mustafaozhan.ccc.client.base.BaseViewModel
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.model.mapToModel
import com.github.mustafaozhan.ccc.client.ui.bar.BarState.Companion.update
import com.github.mustafaozhan.ccc.client.util.MINIMUM_ACTIVE_CURRENCY
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.common.db.CurrencyDao
import com.github.mustafaozhan.ccc.common.log.kermit
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BarViewModel(private val currencyDao: CurrencyDao) : BaseViewModel(), BarEvent {
    // region SEED
    private val _state = MutableStateFlow(BarState())
    val state: StateFlow<BarState> = _state

    private val _effect = BroadcastChannel<BarEffect>(Channel.BUFFERED)
    val effect = _effect.asFlow()

    fun getEvent() = this as BarEvent
    // endregion

    init {
        kermit.d { "BarViewModel init" }

        clientScope.launch {
            currencyDao.collectActiveCurrencies()
                .mapToModel()
                .collect {
                    _state.update(
                        currencyList = it,
                        loading = false,
                        enoughCurrency = it.size >= MINIMUM_ACTIVE_CURRENCY
                    )
                }
        }
    }

    override fun onCleared() {
        kermit.d { "BarViewModel onCleared" }
        super.onCleared()
    }

    // region Event
    override fun onItemClick(currency: Currency) = clientScope.launch {
        kermit.d { "BarViewModel onItemClick ${currency.name}" }
        _effect.send(ChangeBaseNavResultEffect(currency.name))
    }.toUnit()

    override fun onSelectClick() = clientScope.launch {
        kermit.d { "BarViewModel onSelectClick" }
        _effect.send(OpenCurrenciesEffect)
    }.toUnit()
    // endregion
}
