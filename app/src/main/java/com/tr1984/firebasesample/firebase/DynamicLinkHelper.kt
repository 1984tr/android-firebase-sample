package com.tr1984.firebasesample.firebase

import android.net.Uri
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks

object DynamicLinkHelper {

    private const val prefix = "https://firebasesampletr.page.link"
    private const val pkgName = "com.tr1984.firebasesample"

    fun getDynamicLink(link: String): Uri {
        val dynamicLink = getBuilder(link).buildDynamicLink()
        return dynamicLink.uri
    }

    fun getShortDynamicLink(
        link: String,
        completion: (Uri?) -> Unit
    ) {
        getBuilder(link)
            .buildShortDynamicLink()
            .addOnSuccessListener { result ->
                completion.invoke(result.shortLink)
            }.addOnFailureListener {
                it.printStackTrace()
                completion.invoke(null)
            }
    }

    private fun getBuilder(
        link: String
    ): DynamicLink.Builder {
        return FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse(link))
            .setDomainUriPrefix(prefix)
            .setAndroidParameters(
                DynamicLink.AndroidParameters.Builder(pkgName)
                    .setMinimumVersion(1)
                    .build()
            )
            .setIosParameters(
                DynamicLink.IosParameters.Builder(pkgName)
                    .setAppStoreId("111111")
                    .setMinimumVersion("0.0.1")
                    .build()
            )
    }
}