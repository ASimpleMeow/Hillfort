package org.wit.hillfort.views.hillfortlist

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.wit.hillfort.R
import org.wit.hillfort.helpers.RecyclerItemTouchHelper
import org.wit.hillfort.helpers.RecyclerItemTouchHelperListener
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BaseView

class HillfortListView : BaseView(), HillfortListener, RecyclerItemTouchHelperListener {

  lateinit var presenter: HillfortListPresenter
  private var filterFavorites: Boolean = false
  lateinit var adapter: HillfortAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort_list)
    init(toolbarMain, false)
    val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
    val navView: NavigationView = findViewById(R.id.hillfort_nav_view)
    navView.setNavigationItemSelectedListener { menuItem ->
      onOptionsItemSelected(menuItem)
      drawerLayout.closeDrawers()
      true
    }

    val bottomNavView: BottomNavigationView = findViewById(R.id.hillfort_bottom_nav_view)
    bottomNavView.setOnNavigationItemSelectedListener { menuItem -> onOptionsItemSelected(menuItem) }

    presenter = initPresenter(HillfortListPresenter(this)) as HillfortListPresenter

    val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager
    val swipeHelper = RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this, resources.getColor(R.color.colorCardMenu))
    ItemTouchHelper(swipeHelper).attachToRecyclerView(recyclerView)

    presenter.loadHillforts()
  }

  override fun showHillforts(hillforts: List<HillfortModel>) {
    adapter = HillfortAdapter(if (filterFavorites) hillforts.filter { it.favorite } else hillforts, this)
    recyclerView.adapter = adapter
    recyclerView.adapter?.notifyDataSetChanged()
  }

  override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
    menu?.findItem(R.id.item_filter_favorites)?.isChecked = filterFavorites
    return super.onPrepareOptionsMenu(menu)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)

    val searchView: SearchView = menu?.findItem(R.id.item_search)?.actionView as SearchView
    searchView.queryHint = resources.getString(R.string.hint_searchHillfort)
    searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
      override fun onQueryTextChange(newText: String): Boolean {
        presenter.loadHillforts()
        return false
      }

      override fun onQueryTextSubmit(query: String): Boolean {
        if (query.isBlank() || query.isEmpty()) presenter.loadHillforts()
        else presenter.loadHillforts(query)
        return false
      }
    })

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

  override fun onHillfortSwiped(hillfort: HillfortModel) {
    presenter.doDeleteHillfort(hillfort)
  }

  override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
    adapter.removeItem(viewHolder.adapterPosition)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    presenter.loadHillforts()
    super.onActivityResult(requestCode, resultCode, data)
  }
}