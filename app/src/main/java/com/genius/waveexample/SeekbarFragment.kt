package com.genius.waveexample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_seekbar.*

class SeekbarFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_seekbar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sb_main.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sw_main.updateWaviness(progress.toFloat() / sb_main.max)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    companion object {

        const val TAG = "SeekbarFragment"

        fun newInstance(): SeekbarFragment {
            return SeekbarFragment()
        }
    }
}