package com.viasverdes.viasverdesespana.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.underlegendz.corelegendz.vm.VMFragment
import com.viasverdes.viasverdesespana.R


class InfoResourcesFragment : VMFragment() {

  companion object {

    fun newInstance(): InfoResourcesFragment {
      val args: Bundle = Bundle()
//            args.putSerializable(ARG_CAUGHT, caught)
      val fragment = InfoResourcesFragment()
      fragment.arguments = args
      return fragment
    }

    const val VIDEO = "https://youtu.be/M0YXb5xQLAI "
    const val REPORT = "http://viasverdes.com/publicaciones/PDF/2INFORME%20VVyREDNatura2000.pdf"
    const val MAPA = "http://viasverdes.com/publicaciones/PDF/Mapa%20A0.pdf"
    const val CAMINOS = "http://viasverdes.com/publicaciones/PDF/FolletoVVyRedNatura2000digital.pdf"
    const val FACEBOOK = "https://www.facebook.com/ViasVerdesEspanolas/"
    const val INSTAGRAM = "https://www.instagram.com/viasverdesffe/"
    const val TWITTER = "https://twitter.com/viasverdes_ffe"
    const val YOUTUBE = "https://www.youtube.com/user/vivelavia"
  }


  override fun initializeView() {
    view?.let {
      it.findViewById<View>(R.id.resources__container__rednatura2000).setOnClickListener { goToUrl(VIDEO) }
      it.findViewById<View>(R.id.resources__container__report).setOnClickListener { goToUrl(REPORT) }
      it.findViewById<View>(R.id.resources__container__mapa).setOnClickListener { goToUrl(MAPA) }
      it.findViewById<View>(R.id.resources__container__camino).setOnClickListener { goToUrl(CAMINOS) }
      it.findViewById<View>(R.id.resources__btn__facebook).setOnClickListener { goToUrl(FACEBOOK) }
      it.findViewById<View>(R.id.resources__btn__instagram).setOnClickListener { goToUrl(INSTAGRAM) }
      it.findViewById<View>(R.id.resources__btn__twitter).setOnClickListener { goToUrl(TWITTER) }
      it.findViewById<View>(R.id.resources__btn__youtube).setOnClickListener { goToUrl(YOUTUBE) }
    }
  }

  private fun goToUrl(url: String) {
    val i = Intent(Intent.ACTION_VIEW)
    i.data = Uri.parse(url)
    startActivity(i)
  }

  override fun getLayoutResource(): Int {
    return R.layout.fragment__more_info__resources
  }

}