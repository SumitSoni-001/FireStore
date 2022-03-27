package com.example.firebasetopics

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

abstract class SwipeGesture(context: Context) :
    ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT ,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {

    private val deleteColor = ContextCompat.getColor(context, R.color.deleteColor)
    private val archiveColor = ContextCompat.getColor(context, R.color.archiveColor)
    private val deleteIcon = R.drawable.ic_baseline_delete_24
    private val archiveIcon = R.drawable.ic_baseline_archive_24

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {

        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftActionIcon(deleteIcon)
            .addSwipeLeftBackgroundColor(deleteColor)
            .addSwipeRightActionIcon(archiveIcon)
            .addSwipeRightBackgroundColor(archiveColor)
            .create()
            .decorate();

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}