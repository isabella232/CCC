package mustafaozhan.github.com.mycurrencies.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Mustafa Ozhan on 2018-07-20.
 */
@Entity(tableName = "offline_rates")
@JsonClass(generateAdapter = true)
data class Rates(
    @Transient @PrimaryKey @ColumnInfo(name = "base") var base: String = "",
    @Transient @ColumnInfo(name = "date") var date: String? = null,
    @field: Json(name = "aed") @ColumnInfo(name = "AED") var aED: Double? = null,
    @field: Json(name = "afn") @ColumnInfo(name = "AFN") var aFN: Double? = null,
    @field: Json(name = "all") @ColumnInfo(name = "ALLL") var aLL: Double? = null,
    @field: Json(name = "amd") @ColumnInfo(name = "AMD") var aMD: Double? = null,
    @field: Json(name = "ang") @ColumnInfo(name = "ANG") var aNG: Double? = null,
    @field: Json(name = "aoa") @ColumnInfo(name = "AOA") var aOA: Double? = null,
    @field: Json(name = "ars") @ColumnInfo(name = "ARS") var aRS: Double? = null,
    @field: Json(name = "aud") @ColumnInfo(name = "AUD") var aUD: Double? = null,
    @field: Json(name = "awg") @ColumnInfo(name = "AWG") var aWG: Double? = null,
    @field: Json(name = "azn") @ColumnInfo(name = "AZN") var aZN: Double? = null,
    @field: Json(name = "bam") @ColumnInfo(name = "BAM") var bAM: Double? = null,
    @field: Json(name = "bbd") @ColumnInfo(name = "BBD") var bBD: Double? = null,
    @field: Json(name = "bdt") @ColumnInfo(name = "BDT") var bDT: Double? = null,
    @field: Json(name = "bgn") @ColumnInfo(name = "BGN") var bGN: Double? = null,
    @field: Json(name = "bhd") @ColumnInfo(name = "BHD") var bHD: Double? = null,
    @field: Json(name = "bif") @ColumnInfo(name = "BIF") var bIF: Double? = null,
    @field: Json(name = "bmd") @ColumnInfo(name = "BMD") var bMD: Double? = null,
    @field: Json(name = "bnd") @ColumnInfo(name = "BND") var bND: Double? = null,
    @field: Json(name = "bob") @ColumnInfo(name = "BOB") var bOB: Double? = null,
    @field: Json(name = "brl") @ColumnInfo(name = "BRL") var bRL: Double? = null,
    @field: Json(name = "bsd") @ColumnInfo(name = "BSD") var bSD: Double? = null,
    @field: Json(name = "btc") @ColumnInfo(name = "BTC") var bTC: Double? = null,
    @field: Json(name = "btn") @ColumnInfo(name = "BTN") var bTN: Double? = null,
    @field: Json(name = "bwp") @ColumnInfo(name = "BWP") var bWP: Double? = null,
    @field: Json(name = "byn") @ColumnInfo(name = "BYN") var bYN: Double? = null,
    @field: Json(name = "byr") @ColumnInfo(name = "BYR") var bYR: Double? = null,
    @field: Json(name = "bzd") @ColumnInfo(name = "BZD") var bZD: Double? = null,
    @field: Json(name = "cad") @ColumnInfo(name = "CAD") var cAD: Double? = null,
    @field: Json(name = "cdf") @ColumnInfo(name = "CDF") var cDF: Double? = null,
    @field: Json(name = "chf") @ColumnInfo(name = "CHF") var cHF: Double? = null,
    @field: Json(name = "clf") @ColumnInfo(name = "CLF") var cLF: Double? = null,
    @field: Json(name = "clp") @ColumnInfo(name = "CLP") var cLP: Double? = null,
    @field: Json(name = "cny") @ColumnInfo(name = "CNY") var cNY: Double? = null,
    @field: Json(name = "cop") @ColumnInfo(name = "COP") var cOP: Double? = null,
    @field: Json(name = "crc") @ColumnInfo(name = "CRC") var cRC: Double? = null,
    @field: Json(name = "cuc") @ColumnInfo(name = "CUC") var cUC: Double? = null,
    @field: Json(name = "cup") @ColumnInfo(name = "CUP") var cUP: Double? = null,
    @field: Json(name = "cve") @ColumnInfo(name = "CVE") var cVE: Double? = null,
    @field: Json(name = "czk") @ColumnInfo(name = "CZK") var cZK: Double? = null,
    @field: Json(name = "djf") @ColumnInfo(name = "DJF") var dJF: Double? = null,
    @field: Json(name = "dkk") @ColumnInfo(name = "DKK") var dKK: Double? = null,
    @field: Json(name = "dop") @ColumnInfo(name = "DOP") var dOP: Double? = null,
    @field: Json(name = "dzd") @ColumnInfo(name = "DZD") var dZD: Double? = null,
    @field: Json(name = "egp") @ColumnInfo(name = "EGP") var eGP: Double? = null,
    @field: Json(name = "ern") @ColumnInfo(name = "ERN") var eRN: Double? = null,
    @field: Json(name = "etb") @ColumnInfo(name = "ETB") var eTB: Double? = null,
    @field: Json(name = "eur") @ColumnInfo(name = "EUR") var eUR: Double? = null,
    @field: Json(name = "fjd") @ColumnInfo(name = "FJD") var fJD: Double? = null,
    @field: Json(name = "fkp") @ColumnInfo(name = "FKP") var fKP: Double? = null,
    @field: Json(name = "gbp") @ColumnInfo(name = "GBP") var gBP: Double? = null,
    @field: Json(name = "gel") @ColumnInfo(name = "GEL") var gEL: Double? = null,
    @field: Json(name = "ggp") @ColumnInfo(name = "GGP") var gGP: Double? = null,
    @field: Json(name = "ghs") @ColumnInfo(name = "GHS") var gHS: Double? = null,
    @field: Json(name = "gip") @ColumnInfo(name = "GIP") var gIP: Double? = null,
    @field: Json(name = "gmd") @ColumnInfo(name = "GMD") var gMD: Double? = null,
    @field: Json(name = "gnf") @ColumnInfo(name = "GNF") var gNF: Double? = null,
    @field: Json(name = "gtq") @ColumnInfo(name = "GTQ") var gTQ: Double? = null,
    @field: Json(name = "gyd") @ColumnInfo(name = "GYD") var gYD: Double? = null,
    @field: Json(name = "hkd") @ColumnInfo(name = "HKD") var hKD: Double? = null,
    @field: Json(name = "hnl") @ColumnInfo(name = "HNL") var hNL: Double? = null,
    @field: Json(name = "hrk") @ColumnInfo(name = "HRK") var hRK: Double? = null,
    @field: Json(name = "htg") @ColumnInfo(name = "HTG") var hTG: Double? = null,
    @field: Json(name = "huf") @ColumnInfo(name = "HUF") var hUF: Double? = null,
    @field: Json(name = "idr") @ColumnInfo(name = "IDR") var iDR: Double? = null,
    @field: Json(name = "ils") @ColumnInfo(name = "ILS") var iLS: Double? = null,
    @field: Json(name = "imp") @ColumnInfo(name = "IMP") var iMP: Double? = null,
    @field: Json(name = "inr") @ColumnInfo(name = "INR") var iNR: Double? = null,
    @field: Json(name = "iqd") @ColumnInfo(name = "IQD") var iQD: Double? = null,
    @field: Json(name = "irr") @ColumnInfo(name = "IRR") var iRR: Double? = null,
    @field: Json(name = "isk") @ColumnInfo(name = "ISK") var iSK: Double? = null,
    @field: Json(name = "jep") @ColumnInfo(name = "JEP") var jEP: Double? = null,
    @field: Json(name = "jmd") @ColumnInfo(name = "JMD") var jMD: Double? = null,
    @field: Json(name = "jod") @ColumnInfo(name = "JOD") var jOD: Double? = null,
    @field: Json(name = "jpy") @ColumnInfo(name = "JPY") var jPY: Double? = null,
    @field: Json(name = "kes") @ColumnInfo(name = "KES") var kES: Double? = null,
    @field: Json(name = "kgs") @ColumnInfo(name = "KGS") var kGS: Double? = null,
    @field: Json(name = "khr") @ColumnInfo(name = "KHR") var kHR: Double? = null,
    @field: Json(name = "kmf") @ColumnInfo(name = "KMF") var kMF: Double? = null,
    @field: Json(name = "kpw") @ColumnInfo(name = "KPW") var kPW: Double? = null,
    @field: Json(name = "krw") @ColumnInfo(name = "KRW") var kRW: Double? = null,
    @field: Json(name = "kwd") @ColumnInfo(name = "KWD") var kWD: Double? = null,
    @field: Json(name = "kyd") @ColumnInfo(name = "KYD") var kYD: Double? = null,
    @field: Json(name = "kzt") @ColumnInfo(name = "KZT") var kZT: Double? = null,
    @field: Json(name = "lak") @ColumnInfo(name = "LAK") var lAK: Double? = null,
    @field: Json(name = "lbp") @ColumnInfo(name = "LBP") var lBP: Double? = null,
    @field: Json(name = "lkr") @ColumnInfo(name = "LKR") var lKR: Double? = null,
    @field: Json(name = "lrd") @ColumnInfo(name = "LRD") var lRD: Double? = null,
    @field: Json(name = "lsl") @ColumnInfo(name = "LSL") var lSL: Double? = null,
    @field: Json(name = "ltl") @ColumnInfo(name = "LTL") var lTL: Double? = null,
    @field: Json(name = "lvl") @ColumnInfo(name = "LVL") var lVL: Double? = null,
    @field: Json(name = "lyd") @ColumnInfo(name = "LYD") var lYD: Double? = null,
    @field: Json(name = "mad") @ColumnInfo(name = "MAD") var mAD: Double? = null,
    @field: Json(name = "mdl") @ColumnInfo(name = "MDL") var mDL: Double? = null,
    @field: Json(name = "mga") @ColumnInfo(name = "MGA") var mGA: Double? = null,
    @field: Json(name = "mkd") @ColumnInfo(name = "MKD") var mKD: Double? = null,
    @field: Json(name = "mmk") @ColumnInfo(name = "MMK") var mMK: Double? = null,
    @field: Json(name = "mnt") @ColumnInfo(name = "MNT") var mNT: Double? = null,
    @field: Json(name = "mop") @ColumnInfo(name = "MOP") var mOP: Double? = null,
    @field: Json(name = "mro") @ColumnInfo(name = "MRO") var mRO: Double? = null,
    @field: Json(name = "mur") @ColumnInfo(name = "MUR") var mUR: Double? = null,
    @field: Json(name = "mvr") @ColumnInfo(name = "MVR") var mVR: Double? = null,
    @field: Json(name = "mwk") @ColumnInfo(name = "MWK") var mWK: Double? = null,
    @field: Json(name = "mxn") @ColumnInfo(name = "MXN") var mXN: Double? = null,
    @field: Json(name = "myr") @ColumnInfo(name = "MYR") var mYR: Double? = null,
    @field: Json(name = "mzn") @ColumnInfo(name = "MZN") var mZN: Double? = null,
    @field: Json(name = "nad") @ColumnInfo(name = "NAD") var nAD: Double? = null,
    @field: Json(name = "ngn") @ColumnInfo(name = "NGN") var nGN: Double? = null,
    @field: Json(name = "nio") @ColumnInfo(name = "NIO") var nIO: Double? = null,
    @field: Json(name = "nok") @ColumnInfo(name = "NOK") var nOK: Double? = null,
    @field: Json(name = "npr") @ColumnInfo(name = "NPR") var nPR: Double? = null,
    @field: Json(name = "nzd") @ColumnInfo(name = "NZD") var nZD: Double? = null,
    @field: Json(name = "omr") @ColumnInfo(name = "OMR") var oMR: Double? = null,
    @field: Json(name = "pab") @ColumnInfo(name = "PAB") var pAB: Double? = null,
    @field: Json(name = "pen") @ColumnInfo(name = "PEN") var pEN: Double? = null,
    @field: Json(name = "pgk") @ColumnInfo(name = "PGK") var pGK: Double? = null,
    @field: Json(name = "php") @ColumnInfo(name = "PHP") var pHP: Double? = null,
    @field: Json(name = "pkr") @ColumnInfo(name = "PKR") var pKR: Double? = null,
    @field: Json(name = "pln") @ColumnInfo(name = "PLN") var pLN: Double? = null,
    @field: Json(name = "pyg") @ColumnInfo(name = "PYG") var pYG: Double? = null,
    @field: Json(name = "qar") @ColumnInfo(name = "QAR") var qAR: Double? = null,
    @field: Json(name = "ron") @ColumnInfo(name = "RON") var rON: Double? = null,
    @field: Json(name = "rsd") @ColumnInfo(name = "RSD") var rSD: Double? = null,
    @field: Json(name = "rub") @ColumnInfo(name = "RUB") var rUB: Double? = null,
    @field: Json(name = "rwf") @ColumnInfo(name = "RWF") var rWF: Double? = null,
    @field: Json(name = "sar") @ColumnInfo(name = "SAR") var sAR: Double? = null,
    @field: Json(name = "sbd") @ColumnInfo(name = "SBD") var sBD: Double? = null,
    @field: Json(name = "scr") @ColumnInfo(name = "SCR") var sCR: Double? = null,
    @field: Json(name = "sdg") @ColumnInfo(name = "SDG") var sDG: Double? = null,
    @field: Json(name = "sek") @ColumnInfo(name = "SEK") var sEK: Double? = null,
    @field: Json(name = "sgd") @ColumnInfo(name = "SGD") var sGD: Double? = null,
    @field: Json(name = "shp") @ColumnInfo(name = "SHP") var sHP: Double? = null,
    @field: Json(name = "sll") @ColumnInfo(name = "SLL") var sLL: Double? = null,
    @field: Json(name = "sos") @ColumnInfo(name = "SOS") var sOS: Double? = null,
    @field: Json(name = "srd") @ColumnInfo(name = "SRD") var sRD: Double? = null,
    @field: Json(name = "std") @ColumnInfo(name = "STD") var sTD: Double? = null,
    @field: Json(name = "svc") @ColumnInfo(name = "SVC") var sVC: Double? = null,
    @field: Json(name = "syp") @ColumnInfo(name = "SYP") var sYP: Double? = null,
    @field: Json(name = "szl") @ColumnInfo(name = "SZL") var sZL: Double? = null,
    @field: Json(name = "thb") @ColumnInfo(name = "THB") var tHB: Double? = null,
    @field: Json(name = "tjs") @ColumnInfo(name = "TJS") var tJS: Double? = null,
    @field: Json(name = "tmt") @ColumnInfo(name = "TMT") var tMT: Double? = null,
    @field: Json(name = "tnd") @ColumnInfo(name = "TND") var tND: Double? = null,
    @field: Json(name = "top") @ColumnInfo(name = "TOP") var tOP: Double? = null,
    @field: Json(name = "try") @ColumnInfo(name = "TRY") var tRY: Double? = null,
    @field: Json(name = "ttd") @ColumnInfo(name = "TTD") var tTD: Double? = null,
    @field: Json(name = "twd") @ColumnInfo(name = "TWD") var tWD: Double? = null,
    @field: Json(name = "tzs") @ColumnInfo(name = "TZS") var tZS: Double? = null,
    @field: Json(name = "uah") @ColumnInfo(name = "UAH") var uAH: Double? = null,
    @field: Json(name = "ugx") @ColumnInfo(name = "UGX") var uGX: Double? = null,
    @field: Json(name = "usd") @ColumnInfo(name = "USD") var uSD: Double? = null,
    @field: Json(name = "uyu") @ColumnInfo(name = "UYU") var uYU: Double? = null,
    @field: Json(name = "uzs") @ColumnInfo(name = "UZS") var uZS: Double? = null,
    @field: Json(name = "vef") @ColumnInfo(name = "VEF") var vEF: Double? = null,
    @field: Json(name = "ves") @ColumnInfo(name = "VES") var vES: Double? = null,
    @field: Json(name = "vnd") @ColumnInfo(name = "VND") var vND: Double? = null,
    @field: Json(name = "vuv") @ColumnInfo(name = "VUV") var vUV: Double? = null,
    @field: Json(name = "wst") @ColumnInfo(name = "WST") var wST: Double? = null,
    @field: Json(name = "xaf") @ColumnInfo(name = "XAF") var xAF: Double? = null,
    @field: Json(name = "xag") @ColumnInfo(name = "XAG") var xAG: Double? = null,
    @field: Json(name = "xau") @ColumnInfo(name = "XAU") var xAU: Double? = null,
    @field: Json(name = "xcd") @ColumnInfo(name = "XCD") var xCD: Double? = null,
    @field: Json(name = "xdr") @ColumnInfo(name = "XDR") var xDR: Double? = null,
    @field: Json(name = "xof") @ColumnInfo(name = "XOF") var xOF: Double? = null,
    @field: Json(name = "xpf") @ColumnInfo(name = "XPF") var xPF: Double? = null,
    @field: Json(name = "yer") @ColumnInfo(name = "YER") var yER: Double? = null,
    @field: Json(name = "zar") @ColumnInfo(name = "ZAR") var zAR: Double? = null,
    @field: Json(name = "zmk") @ColumnInfo(name = "ZMK") var zMK: Double? = null,
    @field: Json(name = "zmw") @ColumnInfo(name = "ZMW") var zMW: Double? = null,
    @field: Json(name = "zwl") @ColumnInfo(name = "ZWL") var zWL: Double? = null
)
