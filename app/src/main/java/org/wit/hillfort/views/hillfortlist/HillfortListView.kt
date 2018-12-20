package org.wit.hillfort.views.hillfortlist

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BaseView

class HillfortListView : BaseView(), HillfortListener {

  lateinit var presenter: HillfortListPresenter
  private var filterFavorites: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort_list)
    init(toolbarMain, false)
    var drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
    var navView: NavigationView = findViewById(R.id.hillfort_nav_view)
    navView.setNavigationItemSelectedListener { menuItem ->
      onOptionsItemSelected(menuItem)
      drawerLayout.closeDrawers()
      true
    }

    presenter = initPresenter(HillfortListPresenter(this)) as HillfortListPresenter

    val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager
    presenter.loadHillforts()
  }

  override fun showHillforts(hillforts: List<HillfortModel>) {
    recyclerView.adapter = HillfortAdapter(if (filterFavorites) hillforts.filter { it.favorite } else hillforts, this)
    recyclerView.adapter?.notifyDataSetChanged()
  }

  override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
    menu?.findItem(R.id.item_filter_favorites)?.isChecked = filterFavorites
    return super.onPrepareOptionsMenu(menu)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId){
      R.id.item_add -> presenter.doAddHillfort()
      R.id.item_map -> presenter.doShowHillfortsMap()
      R.id.item_filter_favorites -> {
        filterFavorites = !item.isChecked
        item.isChecked = filterFavorites
        presenter.loadHillforts()
      }
      R.id.item_settings -> presenter.doShowSettings()
      R.id.item_logout -> presenter.doLogout()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onHillfortClick(hillfort: HillfortModel) {
    presenter.doEditHillfort(hillfort)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    presenter.loadHillforts()
    super.onActivityResult(requestCode, resultCode, data)
  }
}