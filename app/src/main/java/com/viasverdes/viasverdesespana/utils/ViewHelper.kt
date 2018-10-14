package com.viasverdes.viasverdesespana.utils

import android.view.View

fun View.setVisible(visible: Boolean) {
  this.visibility =
        if (visible) {
          View.VISIBLE
        } else {
          View.GONE
        }
}

fun View.toogleVisibility() {
  this.setVisible(this.visibility != View.VISIBLE)
}