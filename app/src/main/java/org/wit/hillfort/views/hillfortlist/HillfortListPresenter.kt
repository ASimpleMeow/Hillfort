package org.wit.hillfort.views.hillfortlist

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.VIEW

class HillfortListPresenter(view: HillfortListView): BasePresenter(view) {

  fun doAddHillfort() {
    view?.navigateTo(VIEW.PLACEMARK)
  }

  fun doEditHillfort(hillfort: HillfortModel) {
    view?.navigateTo(VIEW.PLACEMARK, 0, "hillfort_edit", hillfort)
  }

  fun doShowHillfortsMap() {
    view?.navigateTo(VIEW.MAPS)
  }

  fun doLogout() {
    FirebaseAuth.getInstance().signOut()
    app.hillforts.clear()
    view?.navigateTo(VIEW.LOGIN)
  }

  fun loadHillforts(){
    async(UI) {
      view?.showHillforts(app.hillforts.findAll())
    }
  }

  fun loadHillforts(query: String){
    async(UI) {
      view?.showHillforts(app.hillforts.findAll().filter { it.title.contains(query) || it.description.contains(query) })
    }
  }

  fun doShowSettings() {
    view?.navigateTo(VIEW.SETTINGS)
  }
}