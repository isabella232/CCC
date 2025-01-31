/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.PurchaseHistory
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.client.viewmodel.adremove.AdRemoveEffect
import com.github.mustafaozhan.ccc.client.viewmodel.adremove.AdRemoveViewModel
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.runTest
import com.github.mustafaozhan.ccc.common.util.nowAsLong
import kotlinx.coroutines.flow.first
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AdRemoveViewModelTest : BaseViewModelTest<AdRemoveViewModel>() {

    override val viewModel: AdRemoveViewModel by lazy {
        koin.getDependency(AdRemoveViewModel::class)
    }

    @Test
    fun setLoading() = runTest {
        viewModel.showLoadingView(true)
        assertEquals(true, viewModel.state.first().loading)

        viewModel.showLoadingView(false)
        assertEquals(false, viewModel.state.first().loading)
    }

    @Test
    fun updateAddFreeDate() = runTest {
        RemoveAdType.values().forEach {
            viewModel.updateAddFreeDate(it)
            assertEquals(AdRemoveEffect.RestartActivity, viewModel.effect.first())
        }
    }

    @Test
    fun restorePurchase() = runTest {
        viewModel.restorePurchase(
            listOf(
                PurchaseHistory(nowAsLong(), RemoveAdType.MONTH),
                PurchaseHistory(nowAsLong(), RemoveAdType.YEAR)
            )
        )
        viewModel.effect.first().let {
            assertTrue {
                it is AdRemoveEffect.AlreadyAdFree || it is AdRemoveEffect.RestartActivity
            }
        }
    }

    @Test
    fun addInAppBillingMethods() = runTest {
        RemoveAdType.values()
            .map { it.data }
            .forEach {
                viewModel.addInAppBillingMethods(listOf(it))
                assertEquals(
                    true,
                    viewModel.state.first().adRemoveTypes.contains(RemoveAdType.getBySku(it.skuId))
                )
            }
    }

    // Event
    @Test
    fun onBillingClick() = runTest {
        viewModel.event.onAdRemoveItemClick(RemoveAdType.VIDEO)
        assertEquals(AdRemoveEffect.RemoveAd(RemoveAdType.VIDEO), viewModel.effect.first())

        viewModel.event.onAdRemoveItemClick(RemoveAdType.MONTH)
        assertEquals(AdRemoveEffect.RemoveAd(RemoveAdType.MONTH), viewModel.effect.first())

        viewModel.event.onAdRemoveItemClick(RemoveAdType.QUARTER)
        assertEquals(AdRemoveEffect.RemoveAd(RemoveAdType.QUARTER), viewModel.effect.first())

        viewModel.event.onAdRemoveItemClick(RemoveAdType.HALF_YEAR)
        assertEquals(AdRemoveEffect.RemoveAd(RemoveAdType.HALF_YEAR), viewModel.effect.first())

        viewModel.event.onAdRemoveItemClick(RemoveAdType.YEAR)
        assertEquals(AdRemoveEffect.RemoveAd(RemoveAdType.YEAR), viewModel.effect.first())
    }
}
