package com.viasverdes.viasverdesespana.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.underlegendz.corelegendz.vm.VMFragment
import com.viasverdes.viasverdesespana.R
import kotlinx.android.synthetic.main.fragment__more_info__resources.*


class InfoResourcesFragment : VMFragment() {

  companion object {

    fun newInstance(): InfoResourcesFragment {
      val args: Bundle = Bundle()
//            args.putSerializable(ARG_CAUGHT, caught)
      val fragment = InfoResourcesFragment()
      fragment.arguments = args
      return fragment
    }

    const val RED_NATURA_2000 = "http://viasverdes.com/publicaciones/PDF/2INFORME%20VVyREDNatura2000.pdf"
    const val MAPA = "http://viasverdes.com/publicaciones/PDF/Mapa%20A0.pdf"
    const val CAMINOS = "http://viasverdes.com/publicaciones/PDF/vias%20verdes-caminos%20naturales%20ES%20web.pdf"
    const val FACEBOOK = "https://www.facebook.com/ViasVerdesEspanolas/"
    const val INSTAGRAM = "https://www.instagram.com/viasverdesffe/"
    const val TWITTER = "https://twitter.com/viasverdes_ffe"
    const val YOUTUBE = "https://www.youtube.com/user/vivelavia"
  }


  override fun initializeView() {
    resources__container__rednatura2000.setOnClickListener { goToUrl(RED_NATURA_2000) }
    resources__container__mapa.setOnClickListener { goToUrl(MAPA) }
    resources__container__camino.setOnClickListener { goToUrl(CAMINOS) }
    resources__btn__facebook.setOnClickListener { goToUrl(FACEBOOK) }
    resources__btn__instagram.setOnClickListener { goToUrl(INSTAGRAM) }
    resources__btn__twitter.setOnClickListener { goToUrl(TWITTER) }
    resources__btn__youtube.setOnClickListener { goToUrl(YOUTUBE) }
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