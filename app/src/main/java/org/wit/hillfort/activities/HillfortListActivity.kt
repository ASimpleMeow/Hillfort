package org.wit.hillfort.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.drawer_header.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.w3c.dom.Text
import org.wit.hillfort.R
import org.wit.hillfort.adapters.HillfortAdapter
import org.wit.hillfort.adapters.HillfortListener
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import kotlin.system.exitProcess


class HillfortListActivity: AppCompatActivity(), HillfortListener {

  lateinit var app: MainApp
  private lateinit var drawerLayout: DrawerLayout
  private lateinit var navView: NavigationView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort_list)
    app = application as MainApp
    drawerLayout = findViewById(R.id.drawer_layout)

    toolbarMain.title = title
    setSupportActionBar(toolbarMain)
    supportActionBar?.apply {
      setDisplayHomeAsUpEnabled(true)
      setHomeAsUpIndicator(R.drawable.ic_menu)
    }

    navView = findViewById<NavigationView>(R.id.hillfort_nav_view)

    navView.setNavigationItemSelectedListener { menuItem ->
      onOptionsItemSelected(menuItem)
      drawerLayout.closeDrawers()
      true
    }

    navView.getHeaderView(0).findViewById<TextView>(R.id.drawer_user_name).text = app.currentUser.name
    navView.getHeaderView(0).findViewById<TextView>(R.id.drawer_user_email).text = app.currentUser.email

    val layoutManager = LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager
    loadHillforts()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_add -> startActivityForResult<HillfortActivity>(0)

      R.id.item_map -> startActivity<HillfortMapsActivity>()

      R.id.item_logout -> {
        toast(R.string.logout_success)
        setResult(AppCompatActivity.RESULT_OK)
        finish()
      }

      R.id.item_settings -> startActivityForResult<HillfortSettingsActivity>(0)

      android.R.id.home -> {
        drawerLayout.openDrawer(GravityCompat.START)
        true
      }

    }
    return super.onOptionsItemSelected(item)
  }

  override fun onHillfortClick(hillfort: HillfortModel) {
    startActivityForResult(intentFor<HillfortActivity>().putExtra("hillfort_edit", hillfort), 0)
  }

  override fun onHillfortMenuDeleteClick(hillfort: HillfortModel) {
    val original : HillfortModel? = app.currentUser.hillforts.find { h -> h.id == hillfort.id }
    val index = app.currentUser.hillforts.indexOf(original)
    app.currentUser.hillforts.removeAt(index)
    app.users.update(app.currentUser.copy())
    loadHillforts()
  }

  override fun onHillfortMenuVisitClick(hillfort: HillfortModel) {
    hillfort.visited = !hillfort.visited
    val original : HillfortModel? = app.currentUser.hillforts.find { h -> h.id == hillfort.id }
    val index = app.currentUser.hillforts.indexOf(original)
    app.currentUser.hillforts[index] = hillfort.copy()
    app.users.update(app.currentUser.copy())
    loadHillforts()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    loadHillforts()
    navView.getHeaderView(0).findViewById<TextView>(R.id.drawer_user_email).text = app.currentUser.email
    super.onActivityResult(requestCode, resultCode, data)
  }

  override fun onBackPressed() {
    super.onBackPressed()
    moveTaskToBack(true)
    exitProcess(0)
  }

  private fun loadHillforts() {
    showHillforts(app.currentUser.hillforts)
  }

  fun showHillforts (hillforts: List<HillfortModel>) {
    recyclerView.adapter = HillfortAdapter(hillforts, this)
    recyclerView.adapter?.notifyDataSetChanged()
  }
}