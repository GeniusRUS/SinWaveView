package com.genius.waveexample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_toolbar.*

class ToolbarFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_toolbar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        abl_second.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, i ->
            val scroll = - i.toFloat() / (appBarLayout.height - t_second.height - sv_second.height)

            sv_second.updateWaviness(1 - scroll)
        })
    }

    companion object {
        const val TAG = "ToolbarFragment"

        fun newInstance(): ToolbarFragment {
            return ToolbarFragment()
        }
    }
}