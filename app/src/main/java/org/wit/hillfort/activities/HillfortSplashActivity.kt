package org.wit.hillfort.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import org.wit.hillfort.R

class HillfortSplashActivity: AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.requestFeature(Window.FEATURE_NO_TITLE)
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    setContentView(R.layout.activity_splash)

    scheduleSplashScreen()
  }

  /**
   * This function (and idea) was referenced from Elvis Chidera.
   * Schedules a splash screen to pop up on the screen then start the proper activity (HillfortList)
   *
   * @Author: Elvis Chidera
   * @Ref: https://android.jlelse.eu/the-complete-android-splash-screen-guide-c7db82bce565
   */
  private fun scheduleSplashScreen() {
    val splashScreenDuration = getSplashScreenDuration()
    Handler().postDelayed({
      startActivity(Intent(this@HillfortSplashActivity, HillfortListActivity::class.java))
      finish()
    }, splashScreenDuration)
  }

  /**
   * This function (and idea) was referenced from Elvis Chidera.
   * It get the persistent preferences key for first launch and depending on that
   * return the duration that the splash screen should last for on screen.
   *
   * @Author: Elvis Chidera
   * @Ref: https://android.jlelse.eu/the-complete-android-splash-screen-guide-c7db82bce565
   */
  private fun getSplashScreenDuration(): Long {
    val sp = getPreferences(Context.MODE_PRIVATE)
    val prefixKeyFirstLaunch = "pref_first_launch"

    return when (sp.getBoolean(prefixKeyFirstLaunch, true)) {
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