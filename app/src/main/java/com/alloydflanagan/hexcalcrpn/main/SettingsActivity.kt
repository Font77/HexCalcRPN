package com.alloydflanagan.hexcalcrpn.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference.SummaryProvider
import androidx.preference.PreferenceFragmentCompat
import com.alloydflanagan.hexcalcrpn.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val bitsPreference = findPreference("init_size") as ListPreference

            bitsPreference.summaryProvider = SummaryProvider<ListPreference> {
                "Currently: ${it.entry}\n${getString(R.string.pref_init_size_desc)}"
            }
        }
    }


}
