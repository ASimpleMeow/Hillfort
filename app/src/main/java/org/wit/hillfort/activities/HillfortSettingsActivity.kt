package org.wit.hillfort.activities

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_hillfort_settings.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp

class HillfortSettingsActivity: AppCompatActivity(), AnkoLogger {

  private lateinit var app: MainApp
  private lateinit var sharedPrefs: SharedPreferences

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort_settings)

    app = application as MainApp

    toolbarSettings.title = title
    setSupportActionBar(toolbarSettings)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
    sharedPrefs.edit().clear().apply()

    val visited = app.currentUser.hillforts.filter { it.visited }.size
    val total = app.currentUser.hillforts.size

    sharedPrefs.edit().putInt("pref_stats_visited", visited).putInt("pref_stats_total", total).apply()

    supportFragmentManager.beginTransaction()
        .add(R.id.settings_fragment_container, HillfortSettingsFragment())
        .commit()
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when(item?.itemId){
      android.R.id.home -> onBackPressed()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onBackPressed() {
    val newEmail = sharedPrefs.getString("pref_user_email", "")
    val newPasswordHash = sharedPrefs.getString("pref_user_password", "")

    if (newEmail.isNotEmpty()) app.currentUser.email = newEmail
    if (newPasswordHash.isNotEmpty()) app.currentUser.passwordHash = newPasswordHash
    app.users.update(app.currentUser.copy())

    super.onBackPressed()
  }


}