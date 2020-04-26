package mustafaozhan.github.com.mycurrencies.data.preferences

import android.content.Context
import com.github.mustafaozhan.basemob.preferences.BasePreferences
import com.squareup.moshi.Moshi
import mustafaozhan.github.com.mycurrencies.model.MainData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:50 PM on Arch Linux wit Love <3.
 */
@Singleton
class PreferencesHelper
@Inject constructor(
    context: Context
) : BasePreferences(context) {
    companion object {
        const val GENERAL_SHARED_PREFS = "GENERAL_SHARED_PREFS"
        const val MAIN_DATA = "MAIN_DATA"
    }

    override val preferencesName: String
        get() = GENERAL_SHARED_PREFS

    override val moshi: Moshi
        get() = Moshi.Builder().build()

    fun persistMainData(mainData: MainData) = setStringEntry(
        MAIN_DATA,
        moshi.adapter(MainData::class.java).toJson(mainData)
    )

    fun loadMainData() = moshi
        .adapter(MainData::class.java)
        .fromJson(getStringEntry(
            MAIN_DATA,
            moshi.adapter(MainData::class.java).toJson(MainData())
        )) ?: MainData()
}
