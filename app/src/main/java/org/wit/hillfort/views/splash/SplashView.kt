package org.wit.hillfort.views.splash

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.R

class SplashView : BaseView() {

  lateinit var presenter: SplashPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    window.requestFeature(Window.FEATURE_NO_TITLE)
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

    setContentView(R.layout.activity_splash)

    presenter = initPresenter(SplashPresenter(this)) as SplashPresenter

    presenter.scheduleSplashScreen()
  }
}