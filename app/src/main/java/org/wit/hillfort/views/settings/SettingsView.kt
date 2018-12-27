package org.wit.hillfort.views.settings

import android.os.Bundle
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.activity_hillfort_settings.*
import org.wit.hillfort.R
import org.wit.hillfort.views.BaseView

class SettingsView : BaseView() {

  lateinit var presenter: SettingsPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort_settings)

    init(toolbarSettings, true)

    presenter = initPresenter(SettingsPresenter(this)) as SettingsPresenter

    supportFragmentManager.beginTransaction()
        .add(R.id.settings_fragment_container, HillfortSettingsFragment())
        .commit()
    Slidr.attach(this)
  }

  override fun onBackPressed() {
    presenter.doCancel()
  }
}