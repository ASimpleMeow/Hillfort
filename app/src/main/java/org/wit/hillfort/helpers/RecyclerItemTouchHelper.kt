package org.wit.hillfort.helpers

import android.graphics.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

interface RecyclerItemTouchHelperListener{
  fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction:Int, position: Int)
}

class RecyclerItemTouchHelper constructor(dragDirs:Int,
                                          swipeDirs: Int,
                                          private val listener: RecyclerItemTouchHelperListener,
                                          private val color: Int): ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

  override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    listener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
  }

  override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
    return false
  }

  override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
    val p = Paint()
    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
      val itemView = viewHolder.itemView
      p.color = color
      val background = RectF(itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
      c.drawRect(background, p)
    }
    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
  }
}