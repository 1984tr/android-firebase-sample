package com.tr1984.firebasesample.extensions

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.alert(
    title: String? = "알림",
    message: String?,
    positiveLabel: String? = "확인",
    positiveCallback: (() -> Unit)? = null,
    negativeLabel: String? = "취소",
    nagativeCallback: (() -> Unit)? = null
) {
    val builder = AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveLabel) { _, _ ->
            positiveCallback?.invoke()
        }
    nagativeCallback?.let {
        builder.setNegativeButton(negativeLabel) { _, _ ->
            it.invoke()
        }
    }
    builder.create().show()
}