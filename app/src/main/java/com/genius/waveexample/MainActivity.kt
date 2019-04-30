package com.genius.waveexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FragmentNavigation {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bnv_main.setOnNavigationItemSelectedListener { menuItem ->
            return@setOnNavigationItemSelectedListener when (menuItem.itemId) {
                R.id.menu_seekbar -> {
                    attachFragment(R.id.fl_main, SeekbarFragment.newInstance(), SeekbarFragment.TAG)
                    true
                }
                R.id.menu_toolbar -> {
                    attachFragment(R.id.fl_main, ToolbarFragment.newInstance(), ToolbarFragment.TAG)
                    true
                }
                else -> false
            }
        }

        attachFragment(R.id.fl_main, SeekbarFragment.newInstance(), SeekbarFragment.TAG)
    }
}
