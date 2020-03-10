package com.viasverdes.viasverdesespana.utils

import com.google.android.gms.maps.model.UrlTileProvider
import java.net.MalformedURLException
import java.net.URL
import java.util.*


class MtnRasterTileProvider : UrlTileProvider(256, 256) {
  override fun getTileUrl(
        x: Int,
        y: Int,
        zoom: Int
  ): URL {
    val s = String.format(Locale.US,
          "http://www.ign.es/wmts/mapa-raster?layer=MTN&style=default&tilematrixset=GoogleMapsCompatible&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image/jpeg&TileMatrix=%d&TileCol=%d&TileRow=%d",
          zoom, x, y)
    val url: URL?
    try {
      url = URL(s)
    } catch (e: MalformedURLException) {
      throw AssertionError(e)
    }
    return url
  }
}