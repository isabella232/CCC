/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel.calculator

import com.github.mustafaozhan.ccc.client.base.BaseSEEDViewModel
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.model.RateState
import com.github.mustafaozhan.ccc.client.model.mapToModel
import com.github.mustafaozhan.ccc.client.model.toModel
import com.github.mustafaozhan.ccc.client.util.MINIMUM_ACTIVE_CURRENCY
import com.github.mustafaozhan.ccc.client.util.calculateResult
import com.github.mustafaozhan.ccc.client.util.getCurrencyConversionByRate
import com.github.mustafaozhan.ccc.client.util.getFormatted
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.util.toRates
import com.github.mustafaozhan.ccc.client.util.toStandardDigits
import com.github.mustafaozhan.ccc.client.util.toSupportedCharacters
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorData.Companion.CHAR_DOT
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorData.Companion.KEY_AC
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorData.Companion.KEY_DEL
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorData.Companion.MAXIMUM_INPUT
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorData.Companion.MAXIMUM_OUTPUT
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorData.Companion.PRECISION
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorState.Companion.update
import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.db.CurrencyDao
import com.github.mustafaozhan.ccc.common.db.OfflineRatesDao
import com.github.mustafaozhan.ccc.common.error.BackendCanBeDownException
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.Rates
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.logmob.kermit
import com.github.mustafaozhan.scopemob.mapTo
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
class CalculatorViewModel(
    private val settingsRepository: SettingsRepository,
    private val apiRepository: ApiRepository,
    private val currencyDao: CurrencyDao,
    private val offlineRatesDao: OfflineRatesDao
) : BaseSEEDViewModel(), CalculatorEvent {
    // region SEED
    private val _state = MutableStateFlow(CalculatorState())
    override val state: StateFlow<CalculatorState> = _state

    private val _effect = Channel<CalculatorEffect>(1)
    override val effect = _effect.receiveAsFlow().conflate()

    override val event = this as CalculatorEvent

    override val data = CalculatorData()
    // endregion

    init {
        kermit.d { "CalculatorViewModel init" }
        _state.update(base = settingsRepository.currentBase, input = "")

        state.map { it.base }
            .distinctUntilChanged()
            .onEach { currentBaseChanged(it) }
            .launchIn(clientScope)

        state.map { it.input }
            .distinctUntilChanged()
            .onEach { calculateOutput(it) }
            .launchIn(clientScope)

        currencyDao.collectActiveCurrencies()
            .mapToModel()
            .onEach { _state.update(currencyList = it) }
            .launchIn(clientScope)
    }

    private fun getRates() = data.rates?.let { rates ->
        calculateConversions(rates)
        _state.update(rateState = RateState.Cached(rates.date))
    } ?: clientScope.launch {
        apiRepository
            .getRatesViaBackend(settingsRepository.currentBase)
            .execute(::getRatesSuccess, ::getRatesViaBackendFailed)
    }

    private fun getRatesSuccess(currencyResponse: CurrencyResponse) = currencyResponse
        .toRates().let {
            data.rates = it
            calculateConversions(it)
            _state.update(rateState = RateState.Online(it.date))
            offlineRatesDao.insertOfflineRates(it)
        }

    private fun getRatesViaBackendFailed(t: Throwable) {
        clientScope.launch {
            kermit.w(t) { "CalculatorViewModel getRateViaBackendFailed" }
            apiRepository
                .getRatesViaApi(settingsRepository.currentBase)
                .execute(
                    {
                        kermit.e(BackendCanBeDownException()) { "CalculatorViewModel getRateViaBackendFailed" }
                        getRatesSuccess(it)
                    },
                    ::getRatesViaApiFailed
                )
        }
    }

    private fun getRatesViaApiFailed(t: Throwable) {
        kermit.w(t) { "CalculatorViewModel getRateViaApiFailed" }
        offlineRatesDao.getOfflineRatesByBase(
            settingsRepository.currentBase
        )?.let { offlineRates ->
            calculateConversions(offlineRates)
            _state.update(rateState = RateState.Offline(offlineRates.date))
        } ?: clientScope.launch {
            kermit.w { "no offline rate found" }
            state.value.currencyList.size
                .whether { it > 1 }
                ?.let { _effect.send(CalculatorEffect.Error) }
            _state.update(rateState = RateState.Error)
        }
    }

    private fun calculateOutput(input: String) = clientScope.launch {
        _state.update(loading = true)
        data.parser
            .calculate(input.toSupportedCharacters(), PRECISION)
            .mapTo { if (isFinite()) getFormatted() else "" }
            .whether(
                { output -> output.length <= MAXIMUM_OUTPUT },
                { input.length <= MAXIMUM_INPUT }
            )?.let { output ->
                _state.update(output = output)
                state.value.currencyList.size
                    .whether { it < MINIMUM_ACTIVE_CURRENCY }
                    ?.whetherNot { state.value.input.isEmpty() }
                    ?.let { _effect.send(CalculatorEffect.FewCurrency) }
                    ?: run { getRates() }
            } ?: run {
            _effect.send(CalculatorEffect.MaximumInput)
            _state.update(
                input = input.dropLast(1),
                loading = false
            )
        }
    }

    private fun calculateConversions(rates: Rates?) = _state.update(
        currencyList = _state.value.currencyList.onEach {
            it.rate = rates.calculateResult(it.name, _state.value.output)
        },
        loading = false
    )

    private fun currentBaseChanged(newBase: String) {
        data.rates = null
        settingsRepository.currentBase = newBase
        calculateOutput(_state.value.input)
        _state.update(
            base = newBase,
            input = _state.value.input,
            symbol = currencyDao.getCurrencyByName(newBase)?.toModel()?.symbol ?: ""
        )
    }

    fun isRewardExpired() = settingsRepository.adFreeEndDate.isRewardExpired()

    override fun onCleared() {
        kermit.d { "CalculatorViewModel onCleared" }
        super.onCleared()
    }

    // region Event
    override fun onKeyPress(key: String) {
        kermit.d { "CalculatorViewModel onKeyPress $key" }
        when (key) {
            KEY_AC -> _state.update(input = "")
            KEY_DEL -> state.value.input
                .whetherNot { isEmpty() }
                ?.apply {
                    _state.update(input = substring(0, length - 1))
                }
            else -> _state.update(input = if (key.isEmpty()) "" else state.value.input + key)
        }
    }

    override fun onItemClick(currency: Currency) {
        kermit.d { "CalculatorViewModel onItemClick ${currency.name}" }
        var finalResult = currency.rate
            .getFormatted()
            .toStandardDigits()
            .toSupportedCharacters()

        while (finalResult.length >= MAXIMUM_OUTPUT || finalResult.length >= MAXIMUM_INPUT) {
            finalResult = finalResult.dropLast(1)
        }

        if (finalResult.last() == CHAR_DOT) {
            finalResult = finalResult.dropLast(1)
        }

        _state.update(base = currency.name, input = finalResult)
    }

    override fun onItemLongClick(currency: Currency): Boolean {
        kermit.d { "CalculatorViewModel onItemLongClick ${currency.name}" }
        clientScope.launch {
            _effect.send(
                CalculatorEffect.ShowRate(
                    currency.getCurrencyConversionByRate(
                        settingsRepository.currentBase,
                        data.rates
                    ),
                    currency.name
                )
            )
        }
        return true
    }

    override fun onBarClick() = clientScope.launch {
        kermit.d { "CalculatorViewModel onBarClick" }
        _effect.send(CalculatorEffect.OpenBar)
    }.toUnit()

    override fun onSpinnerItemSelected(base: String) {
        kermit.d { "CalculatorViewModel onSpinnerItemSelected $base" }
        _state.update(base = base)
    }

    override fun onSettingsClicked() = clientScope.launch {
        kermit.d { "CalculatorViewModel onSettingsClicked" }
        _effect.send(CalculatorEffect.OpenSettings)
    }.toUnit()

    override fun onBaseChange(base: String) = currentBaseChanged(base)
    // endregion
}
