package org.wit.hillfort.views.settings

import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import org.wit.hillfort.R

class HillfortSettingsFragment: PreferenceFragmentCompat() {

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?){
    addPreferencesFromResource(R.xml.preferences)

    findPreference("pref_user_email").setOnPreferenceChangeListener { preference, newValue -> changeUserEmail(newValue) }
    findPreference("pref_user_password").setOnPreferenceChangeListener { preference, newValue -> changeUserPassword(newValue) }

    findPreference("pref_stats_visited").summary = preferenceManager.sharedPreferences
        .getInt("pref_stats_visited", 0).toString()

    findPreference("pref_stats_total").summary = preferenceManager.sharedPreferences
        .getInt("pref_stats_total", 0).toString()
  }

  private fun changeUserEmail(newValue: Any): Boolean{
    Toast.makeText(context, "TO DO: Implement change user email", Toast.LENGTH_LONG).show()
    return false
  }

  private fun changeUserPassword(newValue: Any): Boolean{
    Toast.makeText(context, "DO TO: Implement change user password", Toast.LENGTH_LONG).show()
    return false
  }
}