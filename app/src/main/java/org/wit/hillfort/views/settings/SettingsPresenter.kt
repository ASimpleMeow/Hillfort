package org.wit.hillfort.views.settings

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import kotlinx.coroutines.experimental.async
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView

class SettingsPresenter(view: BaseView) : BasePresenter(view) {

  var sharedPrefs: SharedPreferences

  init {
    sharedPrefs = PreferenceManager.getDefaultSharedPreferences(view)
    sharedPrefs.edit().clear().apply()

    async {
      val visited = app.hillforts.findAll().filter { it.visited }.size
      val total = app.hillforts.findAll().size
      sharedPrefs.edit().putInt("pref_stats_visited", visited).putInt("pref_stats_total", total).apply()
    }
  }
}