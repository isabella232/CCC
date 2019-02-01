package mustafaozhan.github.com.mycurrencies.main.fragment


import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.crashlytics.android.Crashlytics
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_keyboard_content.*
import kotlinx.android.synthetic.main.layout_main_toolbar.*
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmFragment
import mustafaozhan.github.com.mycurrencies.extensions.*
import mustafaozhan.github.com.mycurrencies.main.activity.MainActivity
import mustafaozhan.github.com.mycurrencies.main.fragment.adapter.CurrencyAdapter
import mustafaozhan.github.com.mycurrencies.tools.Currencies
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class MainFragment : BaseMvvmFragment<MainFragmentViewModel>() {

    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }

    override fun getViewModelClass(): Class<MainFragmentViewModel> = MainFragmentViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_main

    private val currencyAdapter: CurrencyAdapter by lazy { CurrencyAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViews()
        setListeners()
        initData()
    }

    @SuppressLint("SetTextI18n", "CheckResult")
    private fun initData() {

        txtMainToolbar.textChanges()
                .subscribe {
                    if (viewModel.currencyList.size > 1) {
                        loading.smoothToShow()

                        viewModel.calculateOutput(it.toString())

                        viewModel.getCurrencies()
                        if (viewModel.output != "NaN" && viewModel.output != "") {
                            txtResult.text = "=    ${viewModel.output}"
                        } else {
                            txtResult.text = ""
                        }

                        if (viewModel.currencyList.size < 2) {
                            (activity as MainActivity).snacky(getString(R.string.choose_at_least_two_currency), true, getString(R.string.select))
                        }
                    }
                }

        viewModel.currenciesLiveData.reObserve(this, Observer { rates ->
            viewModel.currencyList.forEach { currency ->
                try {
                    currency.rate = calculateResultByCurrency(currency.name, viewModel.output, rates)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Crashlytics.logException(e)
                }
            }
            if (rates == null) {
                (activity as MainActivity).snacky(getString(R.string.rate_not_avaiable_offline), false, getString(R.string.select), true)
                currencyAdapter.refreshList(mutableListOf(), viewModel.mainData.currentBase, true)
            } else {
                currencyAdapter.refreshList(viewModel.currencyList, viewModel.mainData.currentBase, true)
            }
            loading.smoothToHide()

        })
    }

    private fun initViews() {
        loading.apply {
            bringToFront()
            smoothToHide()
        }
        context?.let {
            mRecViewCurrency.layoutManager = LinearLayoutManager(it)
            mRecViewCurrency.adapter = currencyAdapter
        }
//        currencyAdapter.onItemClickListener = { currency: Currency, _: View, itemView: View, _: Int ->
//            txtMainToolbar.text = itemView.txtAmount.text.toString()
//            mSpinner.selectedIndex = viewModel.currencyList.indexOf(currency)
//            viewModel.mainData.currentBase = Currencies.valueOf(currency.name)
//            viewModel.calculateOutput(itemView.txtAmount.text.toString())
//            viewModel.getCurrencies()
//            updateUi()
//        }
    }

    private fun updateUi() {
        doAsync {
            viewModel.refreshData()
            uiThread {
                try {
                    val spinnerList = viewModel.currencyList.filter { currency -> currency.isActive == 1 }.map { it.name }

                    if (spinnerList.size < 2) {
                        (activity as MainActivity).snacky(getString(R.string.choose_at_least_two_currency), true, getString(R.string.select))
                        imgBase.setBackgroundByName("transparent")
                        mSpinner.setItems("")
                    } else {
                        mSpinner.setItems(spinnerList)

                        if (viewModel.mainData.currentBase == Currencies.NULL && viewModel.currencyList.isNotEmpty()) {
                            viewModel.currencyList.firstOrNull { currency -> currency.isActive == 1 }?.name.let { firsActive ->
                                viewModel.mainData.currentBase = Currencies.valueOf(firsActive
                                        ?: "NULL")
                                mSpinner.selectedIndex = spinnerList.indexOf(firsActive)
                            }
                        } else {
                            if (viewModel.mainData.currentBase == Currencies.NULL) {
                                viewModel.mainData.currentBase = (Currencies.valueOf(viewModel.currencyList.firstOrNull { currency -> currency.isActive == 1 }?.name
                                        ?: "NULL"))
                            }
                            if (viewModel.currencyList.any { currency -> currency.isActive == 1 && currency.name == viewModel.mainData.currentBase.toString() }) {
                                mSpinner.selectedIndex = spinnerList.indexOf(viewModel.mainData.currentBase.toString())
                            }
                        }
                        imgBase.setBackgroundByName(mSpinner.text.toString())
                    }
                    currencyAdapter.refreshList(viewModel.currencyList, viewModel.mainData.currentBase, true)
                } catch (e: Exception) {
                    Crashlytics.logException(e)
                    e.printStackTrace()
                }
            }

        }
    }

    private fun setListeners() {
        mSpinner.setOnItemSelectedListener { _, _, _, item ->
            viewModel.mainData.apply {
                Currencies.valueOf(item.toString()).let { currency ->
                    currentBase = currency
                }
                viewModel.getCurrencies()
            }
            imgBase.setBackgroundByName(item.toString())
            txtMainToolbar.text = txtMainToolbar.text//invoking rx in case of different currency selected
            viewModel.rates = null
        }

        mConstraintLayout.setOnClickListener {
            mSpinner.apply {
                if (isActivated) {
                    collapse()
                } else {
                    expand()
                }
            }
        }

        btnSeven.setOnClickListener { txtMainToolbar.addText("7") }
        btnEight.setOnClickListener { txtMainToolbar.addText("8") }
        btnNine.setOnClickListener { txtMainToolbar.addText("9") }
        btnDivide.setOnClickListener { txtMainToolbar.addText("/") }
        btnFour.setOnClickListener { txtMainToolbar.addText("4") }
        btnFive.setOnClickListener { txtMainToolbar.addText("5") }
        btnSix.setOnClickListener { txtMainToolbar.addText("6") }
        btnMultiply.setOnClickListener { txtMainToolbar.addText("*") }
        btnOne.setOnClickListener { txtMainToolbar.addText("1") }
        btnTwo.setOnClickListener { txtMainToolbar.addText("2") }
        btnThree.setOnClickListener { txtMainToolbar.addText("3") }
        btnMinus.setOnClickListener { txtMainToolbar.addText("-") }
        btnDot.setOnClickListener { txtMainToolbar.addText(".") }
        btnZero.setOnClickListener { txtMainToolbar.addText("0") }
        btnPercent.setOnClickListener { txtMainToolbar.addText("%") }
        btnPlus.setOnClickListener { txtMainToolbar.addText("+") }
        btnDoubleZero.setOnClickListener { txtMainToolbar.addText("000") }
        btnAc.setOnClickListener {
            txtMainToolbar.text = ""
            txtResult.text = ""
        }
        btnDelete.setOnClickListener {
            if (txtMainToolbar.text.toString() != "") {
                txtMainToolbar.text = txtMainToolbar.text.toString().substring(0, txtMainToolbar.text.toString().length - 1)
            }
        }
    }


    override fun onPause() {
        viewModel.savePreferences()
        super.onPause()
    }

    override fun onResume() {
        viewModel.apply {
            loadPreferences()
            getCurrencies()
        }
        updateUi()
        try {
            adView.loadAd(R.string.banner_ad_unit_id_main)
        } catch (e: Exception) {
            e.printStackTrace()
            Crashlytics.logException(e)
        }
        super.onResume()
    }
}