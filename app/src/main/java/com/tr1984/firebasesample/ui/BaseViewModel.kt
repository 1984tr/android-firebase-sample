package com.tr1984.firebasesample.ui

import android.content.Intent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

open class BaseViewModel {

    var startPageSubject = PublishSubject.create<StartPageBundle>()
    var finishPageSubject = PublishSubject.create<FinishPageBundle>()

    var popupSubject = PublishSubject.create<PopupBundle>()
    var errorSubject = PublishSubject.create<ErrorBundle>()
    var toastSubject = PublishSubject.create<String>()

    var compositeDisposable = CompositeDisposable()

    class StartPageBundle(var clazz: Class<*>, var requestCode: Int? = null, var intent: Intent? = null)

    class FinishPageBundle(var resultCode: Int? = null, var intent: Intent? = null)

    class PopupBundle(var title: String?, var message: String?,
                      var positiveLabel: String? = null, var positiveCallback: (() -> Unit)?,
                      var negativeLabel: String? = null, var negativeCallback: (() -> Unit)?)

    class ErrorBundle(var throwable: Throwable, var callback: (() -> Unit)? = null)
}