package com.barrytu.photorecord

import android.view.View

/**
 * Created by Barry Tu on 2021/11/28.
 */
abstract class SingleClickListener : View.OnClickListener {

    @Synchronized
    override fun onClick(view: View?) {
        onSingleClick(view)
        view?.isEnabled = false
        view?.postDelayed(Runnable { view?.isEnabled = true }, 1000)
    }

    abstract fun onSingleClick(view: View?)
}