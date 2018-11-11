package com.viasverdes.viasverdesespana.utils

fun <T> Collection<T>?.isNotNullOrEmpty(): Boolean = this != null && isNotEmpty()