package com.tr1984.firebasesample.ui.dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import com.tr1984.firebasesample.databinding.PopupRereplyBinding
import com.tr1984.firebasesample.extensions.toast


class ReReplyPopup(val activity: Activity, private val submit: (String) -> Unit) :
    Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar) {

    private var binding = PopupRereplyBinding.inflate(layoutInflater)

    init {
        window?.setBackgroundDrawable(ColorDrawable(Color.argb(180, 0, 0, 0)))
        setContentView(binding.root)
        binding.popup = this
    }

    fun submit() {
        val reReply = binding.editMessage.text.toString()
        if (reReply.isEmpty()) {
            activity.toast("내용을 입력해주세요 :)")
        } else {
            submit.invoke(reReply)
            dismiss()
        }
    }

    override fun show() {
        window?.attributes?.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        super.show()
    }
}