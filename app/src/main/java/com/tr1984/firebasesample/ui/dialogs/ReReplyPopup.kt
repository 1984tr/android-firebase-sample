package com.tr1984.firebasesample.ui.dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import com.tr1984.firebasesample.databinding.PopupRereplyBinding


class ReReplyPopup(val activity: Activity, val submitCallback: (String) -> Unit) :
    Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar) {

    private var binding = PopupRereplyBinding.inflate(layoutInflater)

    init {
        window?.run {
            setBackgroundDrawable(ColorDrawable(Color.argb(180, 0, 0, 0)))
            val param = attributes
            param.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
            attributes = param
        }
        setContentView(binding.root)
        binding.popup = this
    }

    fun submit() {
        submitCallback.invoke(binding.editMessage.text.toString())
        dismiss()
    }
}