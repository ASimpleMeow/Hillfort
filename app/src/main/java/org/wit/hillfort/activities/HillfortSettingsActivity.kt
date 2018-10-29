package org.wit.hillfort.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.EditTextPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceManager
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

    supportFragmentManager.beginTransaction()
        .add(R.id.settings_fragment_container, HillfortSettingsFragment())
        .commit()
  }

  override fun onBackPressed() {
    val newEmail = sharedPrefs.getString("pref_user_email", "")
    val newPasswordHash = sharedPrefs.getString("pref_user_password", "")

    if (newEmail.isNotEmpty()) app.currentUser.email = newEmail
    if (newPasswordHash.isNotEmpty()) app.currentUser.passwordHash = newPasswordHash
    app.users.update(app.currentUser)

    super.onBackPressed()
  }


}