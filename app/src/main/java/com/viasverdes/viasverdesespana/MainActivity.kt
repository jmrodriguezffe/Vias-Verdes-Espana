package com.viasverdes.viasverdesespana

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import butterknife.BindView
import butterknife.ButterKnife
import com.viasverdes.viasverdesespana.utils.removeShiftMode

class MainActivity : AppCompatActivity() {
    @BindView(R.id.main__bottom_navigation) lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        bottomNavigationView.removeShiftMode()
    }
}
