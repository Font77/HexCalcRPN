package com.alloydflanagan.hexcalcrpn.model

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager

/**
 * BitsPreference enum for startup bitsmode preference setting. Not to be confused with [BitsMode]!
 */
@Suppress("SpellCheckingInspection")
enum class BitsPreference(private val value: String) {
    EIGHT("bits8"), SIXTEEN("bits16"), THIRTY_TWO("bits32"),
    SIXTY_FOUR("bits64"), INFINITE("bitsinf"), PREVIOUS("bitslast");

    fun toValue() = this.value

    companion object {
        private val mapping = mapOf("bits8" to EIGHT, "bits16" to SIXTEEN,
                "bits32" to THIRTY_TWO, "bits64" to SIXTY_FOUR, "bitsinf" to INFINITE,
                "bitslast" to PREVIOUS)

        fun fromValue(value: String?): BitsPreference? = mapping[value]

    }
}

/**
 * Class for getting application-wide user preference settings.
 *
 * These settings are set by [androidx.preference.PreferenceFragmentCompat] so setters are not required.
 */
class AppPreferences(context: Context) {
    /**
     * The preferences file used by the [androidx.preference.PreferenceFragmentCompat] fragments.
     */
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    var prefInitSize: BitsPreference
        get() {
            val setting = prefs.getString(PREF_INIT_WORD_SIZE_KEY, PREF_INIT_WORD_SIZE_DEFAULT)
            return BitsPreference.fromValue(setting) ?: BitsPreference.THIRTY_TWO
        }
        set(value) {
            val setting = prefs.getString(PREF_INIT_WORD_SIZE_KEY, PREF_INIT_WORD_SIZE_DEFAULT)
            val current = BitsPreference.fromValue(setting)
            if (current != value) {
                prefs.edit {
                    putString(PREF_INIT_WORD_SIZE_KEY, value.toValue())
                }
            }
        }

    var prefDigitSep: Boolean
        get() = prefs.getBoolean(PREF_SEPARATE_DIGITS_KEY, PREF_SEPARATE_DIGITS_DEFAULT)
        set(value) {
            val setting = prefs.getBoolean(PREF_SEPARATE_DIGITS_KEY, PREF_SEPARATE_DIGITS_DEFAULT)
            if (setting != value) {
                prefs.edit {
                    putBoolean(PREF_SEPARATE_DIGITS_KEY, value)
                }
            }
        }

    var lastWordSize: BitsPreference
        get() {
            val size = prefs.getString(PREF_LAST_WORD_SIZE_KEY, PREF_INIT_WORD_SIZE_DEFAULT)
            return BitsPreference.fromValue(size)!!
        }
        set(value) {
            val setting = prefs.getString(PREF_LAST_WORD_SIZE_KEY, PREF_INIT_WORD_SIZE_DEFAULT)
            val current = BitsPreference.fromValue(setting)
            if (current != value) {
                prefs.edit {
                    putString(PREF_LAST_WORD_SIZE_KEY, value.toValue())
                }
            }
        }

    companion object {
        const val PREF_INIT_WORD_SIZE_KEY = "init_size"
        const val PREF_INIT_WORD_SIZE_DEFAULT = "bits32"

        const val PREF_SEPARATE_DIGITS_KEY = "digit_group"
        const val PREF_SEPARATE_DIGITS_DEFAULT = true

        const val PREF_LAST_WORD_SIZE_KEY = "last_size"
    }
}
