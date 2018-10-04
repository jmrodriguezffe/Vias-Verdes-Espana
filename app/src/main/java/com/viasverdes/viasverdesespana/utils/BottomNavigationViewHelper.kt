package com.viasverdes.viasverdesespana.utils

import android.annotation.SuppressLint
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.util.Log

@SuppressLint("RestrictedApi")
fun BottomNavigationView.removeShiftMode() {
  val menuView = this.getChildAt(0) as BottomNavigationMenuView
  try {
    val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
    shiftingMode.isAccessible = true
    shiftingMode.setBoolean(menuView, false)
    shiftingMode.isAccessible = false
    for (i in 0 until menuView.childCount) {
      val item = menuView.getChildAt(i) as BottomNavigationItemView
      item.setShiftingMode(false)
      // set once again checked value, so view will be updated
      item.setChecked(item.itemData.isChecked)
    }
  } catch (e: NoSuchFieldException) {
    Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field")
  } catch (e: IllegalAccessException) {
    Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode")
  }
}