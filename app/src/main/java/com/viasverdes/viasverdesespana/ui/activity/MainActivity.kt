package com.viasverdes.viasverdesespana.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import com.viasverdes.viasverdesespana.R
import com.viasverdes.viasverdesespana.ui.fragment.ListVVFragment
import com.viasverdes.viasverdesespana.ui.fragment.MapFragment
import com.viasverdes.viasverdesespana.utils.removeShiftMode

class MainActivity : AppCompatActivity() {
    val tagListVV = "LIST_VV"
    val tagMap = "MAP"
    val tagMoreInfo = "MORE_INFO"
    @BindView(R.id.main__bottom_navigation)
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        bottomNavigationView.removeShiftMode()
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action__vv_list -> {
                    setFragment(getOrCreateListVV(), tagListVV, true)
                }
                R.id.action__map -> {
//                    val intent = Intent(this, MapsActivity::class.java)
//                    startActivity(intent)
                    setFragment(getOrCreateMap(), tagMap, true)
                }
                R.id.action__more_info -> {
                    setFragment(getOrCreateMoreInfo(), tagMoreInfo, true)
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
            return@setOnNavigationItemSelectedListener true
        }

        bottomNavigationView.selectedItemId = R.id.action__map
    }

    protected fun getOrCreateListVV(): Fragment {
        var fragment: Fragment? = getFragment(tagListVV)
        if (fragment != null) {
            return fragment
        } else {
            return ListVVFragment.newInstance()
        }
    }

    protected fun getOrCreateMap(): Fragment {
        var fragment: Fragment? = getFragment(tagMap)
        if (fragment != null) {
            return fragment
        } else {
            return MapFragment.newInstance()
        }
    }

    protected fun getOrCreateMoreInfo(): Fragment {
        var fragment: Fragment? = getFragment(tagMoreInfo)
        if (fragment != null) {
            return fragment
        } else {
            return ListVVFragment.newInstance()
        }
    }

    protected fun setFragment(fragment: Fragment, tag: String, addToBackStack: Boolean) {
        if (supportFragmentManager != null) {
            val replaceTransaction = supportFragmentManager.beginTransaction()
            replaceTransaction.setTransition(FragmentTransaction.TRANSIT_NONE)
            replaceTransaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out,
                    R.anim.abc_fade_in, R.anim.abc_fade_out)
            replaceTransaction.replace(R.id.main__content, fragment, tag)
            if (addToBackStack) {
                replaceTransaction.addToBackStack(tag)
            }
            replaceTransaction.commit()
        }
    }

    protected fun getFragment(tag: String): Fragment? {
        return if (supportFragmentManager != null) {
            supportFragmentManager.findFragmentByTag(tag)
        } else null
    }
}
