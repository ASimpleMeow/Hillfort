package org.wit.hillfort.views.splash

import android.content.Context
import android.os.Handler
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW

class SplashPresenter(view: BaseView) : BasePresenter(view) {

  fun scheduleSplashScreen(){
    val splashScreenDuration = getSplashScreenDuration()
    Handler().postDelayed({
      view?.navigateTo(VIEW.LOGIN)
    }, splashScreenDuration)
  }

  private fun getSplashScreenDuration(): Long{
    val sp = view?.getPreferences(Context.MODE_PRIVATE)
    val prefixKeyFirstLaunch = "pref_first_launch"

    return when (sp!!.getBoolean(prefixKeyFirstLaunch, true)) {
      true -> {
        // For first time launch return longer duration and set first launch key to false
        sp.edit().putBoolean(prefixKeyFirstLaunch, false).apply()
        5000
      }
      false -> {
        // Not first launch, make the splash screen shorter
        1000
      }
    }
  }
}