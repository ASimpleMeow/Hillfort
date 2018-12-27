package org.wit.hillfort.views.settings

import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import com.google.firebase.auth.FirebaseAuth
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
    val currentUser = FirebaseAuth.getInstance().currentUser
    currentUser?.updateEmail(newValue as String)?.addOnCompleteListener { task ->
      if (task.isSuccessful){
        Toast.makeText(context, "User Email Updated", Toast.LENGTH_LONG).show()
      }
    }
    return false
  }

  private fun changeUserPassword(newValue: Any): Boolean{
    val currentUser = FirebaseAuth.getInstance().currentUser
    currentUser?.updatePassword(newValue as String)?.addOnCompleteListener { task ->
      if (task.isSuccessful){
        Toast.makeText(context, "User Password Updated", Toast.LENGTH_LONG).show()
      }
    }
    return false
  }
}