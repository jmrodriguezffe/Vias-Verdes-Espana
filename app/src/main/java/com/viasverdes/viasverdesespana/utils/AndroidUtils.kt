package com.viasverdes.viasverdesespana.utils

inline fun trueRes(f: () -> Unit): Boolean {
  f()
  return true
}