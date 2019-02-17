package com.viasverdes.viasverdesespana.ui.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.underlegendz.underactivity.ActivityBuilder
import com.underlegendz.underactivity.UnderActivity
import com.viasverdes.viasverdesespana.R
import com.viasverdes.viasverdesespana.data.VVDatabase


class DeepLinkActivity : UnderActivity() {

  override fun configureActivityBuilder(builder: ActivityBuilder): ActivityBuilder {
    return builder
          .enableToolbar(false)
          .setContentLayout(R.layout.layout_loading)
  }
//<a href="viaverde://detail?id="> </a>
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    try {
      val itineraryId = intent.data.getQueryParameter("id")
      val id = itineraryId.toLong()
      VVDatabase.getInstance(this)?.itineraryDAO()?.getById(id)?.observe(this,
            Observer {
              if (it != null) {
                ItineraryActivity.start(this, it)
              }
              finish()
            })
    } catch (e: Exception) {
      finish()
    }
  }
}
