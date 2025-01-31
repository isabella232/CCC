package com.github.mustafaozhan.ccc.client.viewmodel.settings

import com.github.mustafaozhan.ccc.client.base.BaseData
import com.github.mustafaozhan.ccc.client.base.BaseEffect
import com.github.mustafaozhan.ccc.client.base.BaseEvent
import com.github.mustafaozhan.ccc.client.base.BaseState
import com.github.mustafaozhan.ccc.client.model.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow

// State
data class SettingsState(
    val activeCurrencyCount: Int = 0,
    val appThemeType: AppTheme = AppTheme.SYSTEM_DEFAULT,
    val addFreeEndDate: String = "",
    val loading: Boolean = false
) : BaseState() {
    // for ios
    constructor() : this(0, AppTheme.SYSTEM_DEFAULT, "", false)

    companion object {
        fun MutableStateFlow<SettingsState>.update(
            activeCurrencyCount: Int = value.activeCurrencyCount,
            appThemeType: AppTheme = value.appThemeType,
            addFreeEndDate: String = value.addFreeEndDate,
            loading: Boolean = value.loading
        ) {
            value = value.copy(
                activeCurrencyCount = activeCurrencyCount,
                appThemeType = appThemeType,
                addFreeEndDate = addFreeEndDate,
                loading = loading
            )
        }
    }
}

// Event
interface SettingsEvent : BaseEvent {
    fun onBackClick()
    fun onCurrenciesClick()
    fun onFeedBackClick()
    fun onShareClick()
    fun onSupportUsClick()
    fun onOnGitHubClick()
    fun onRemoveAdsClick()
    fun onSyncClick()
    fun onThemeClick()
}

// Effect
sealed class SettingsEffect : BaseEffect() {
    object Back : SettingsEffect()
    object OpenCurrencies : SettingsEffect()
    object FeedBack : SettingsEffect()
    object Share : SettingsEffect()
    object SupportUs : SettingsEffect()
    object OnGitHub : SettingsEffect()
    object RemoveAds : SettingsEffect()
    object ThemeDialog : SettingsEffect()
    object Synchronising : SettingsEffect()
    object Synchronised : SettingsEffect()
    object OnlyOneTimeSync : SettingsEffect()
    object AlreadyAdFree : SettingsEffect()
    data class ChangeTheme(val themeValue: Int) : SettingsEffect()
}

// Data
data class SettingsData(var synced: Boolean = false) : BaseData() {
    companion object {
        internal const val SYNC_DELAY = 10.toLong()
    }
}
