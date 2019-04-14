package mustafaozhan.github.com.mycurrencies.settings

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_settings.adView
import kotlinx.android.synthetic.main.fragment_settings.eTxtSearch
import kotlinx.android.synthetic.main.fragment_settings.mRecViewSettings
import kotlinx.android.synthetic.main.item_setting.view.checkBox
import kotlinx.android.synthetic.main.layout_settings_toolbar.btnDeSelectAll
import kotlinx.android.synthetic.main.layout_settings_toolbar.btnSelectAll
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmFragment
import mustafaozhan.github.com.mycurrencies.extensions.loadAd
import mustafaozhan.github.com.mycurrencies.room.model.Currency
import mustafaozhan.github.com.mycurrencies.settings.adapter.SettingAdapter
import mustafaozhan.github.com.mycurrencies.tools.Currencies
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class SettingsFragment : BaseMvvmFragment<SettingsFragmentViewModel>() {

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }

    override fun getViewModelClass(): Class<SettingsFragmentViewModel> = SettingsFragmentViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_settings

    private val settingAdapter: SettingAdapter by lazy { SettingAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViews()
        initRx()
        setListeners()
    }

    private fun initRx() {
        eTxtSearch
            .textChanges()
            .subscribe { txt ->
                viewModel.currencyList.filter { currency ->
                    currency.name.contains(txt.toString(), true) ||
                        currency.longName.contains(txt.toString(), true) ||
                        currency.symbol.contains(txt.toString(), true)
                }.toMutableList().let { settingAdapter.refreshList(it) }
            }.addTo(compositeDisposable)
    }

    private fun initViews() {
        context?.let { ctx ->
            mRecViewSettings.apply {
                layoutManager = LinearLayoutManager(ctx)
                setHasFixedSize(true)
                adapter = settingAdapter
            }
        }
    }

    private fun setListeners() {
        btnSelectAll.setOnClickListener {
            updateUi(1)
            eTxtSearch.setText("")
        }
        btnDeSelectAll.setOnClickListener {
            updateUi(0)
            eTxtSearch.setText("")
            viewModel.setCurrentBase(null)
        }

        settingAdapter.onItemClickListener = { currency: Currency, itemView, _ ->
            when (currency.isActive) {
                0 -> {
                    currency.isActive = 1
                    itemView.checkBox.isChecked = true
                    viewModel.updateCurrencyStateByName(currency.name, 1)
                }
                1 -> {
                    if (currency.name == viewModel.mainData.currentBase.toString()) {
                        viewModel.setCurrentBase(viewModel.currencyList.firstOrNull { it.isActive == 1 }?.name)
                    }
                    currency.isActive = 0
                    itemView.checkBox.isChecked = false
                    viewModel.updateCurrencyStateByName(currency.name, 0)
                }
            }
        }
    }

    private fun updateUi(value: Int? = null) {
        doAsync {
            value?.let { viewModel.updateAllCurrencyState(it) }
            uiThread {
                when {
                    viewModel.currencyList.filter { it.isActive == 1 }.count() < 2 -> {
                        snacky(getString(R.string.choose_currencies), getString(R.string.ok))
                    }
                    viewModel.mainData.currentBase == Currencies.NULL -> {
                        viewModel.setCurrentBase(viewModel.currencyList.firstOrNull { it.isActive == 1 }?.name)
                    }
                }
                viewModel.currencyList.let { settingAdapter.refreshList(it) }
            }
        }
    }

    override fun onPause() {
        viewModel.savePreferences()
        super.onPause()
    }

    override fun onResume() {
        viewModel.loadPreferences()
        viewModel.initData()
        updateUi()
        adView.loadAd(R.string.banner_ad_unit_id_settings)
        super.onResume()
    }
}