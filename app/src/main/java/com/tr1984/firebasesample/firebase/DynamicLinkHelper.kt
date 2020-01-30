package com.tr1984.firebasesample.firebase

import android.net.Uri
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks

object DynamicLinkHelper {

    private val prefix = "https://1984tr.page.link"
    private val pkgName = "com.tr1984.firebasesample"

    fun getDynamiLink(link: String, title: String = "", description: String = ""): Uri {
        val dynamicLink = getBuilder(link, title, description).buildDynamicLink()
        return dynamicLink.uri
    }

    fun getShortDynamicLink(link: String, title: String = "", description: String = "", completion: (Uri?) -> Unit) {
        getBuilder(link, title, description)
            .buildShortDynamicLink()
            .addOnSuccessListener { result ->
                completion.invoke(result.shortLink)
            }.addOnFailureListener {
                completion.invoke(null)
            }
    }

    private fun getBuilder(
        link: String,
        title: String = "",
        description: String = ""
    ): DynamicLink.Builder {
        val builder = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse(link))
            .setDomainUriPrefix(prefix)
            .setAndroidParameters(
                DynamicLink.AndroidParameters.Builder(pkgName)
                    .setMinimumVersion(1)
                    .build()
            )
            .setIosParameters(
                DynamicLink.IosParameters.Builder(pkgName)
                    .setAppStoreId("1234")
                    .setMinimumVersion("1.0.0")
                    .build()
            )
            .setGoogleAnalyticsParameters(
                DynamicLink.GoogleAnalyticsParameters.Builder()
                    .setSource("orkut")
                    .setMedium("social")
                    .setCampaign("example-promo")
                    .build()
            )
            .setItunesConnectAnalyticsParameters(
                DynamicLink.ItunesConnectAnalyticsParameters.Builder()
                    .setProviderToken("123456")
                    .setCampaignToken("example-promo")
                    .build()
            )
        if (title.isNotEmpty()) {
            builder.setSocialMetaTagParameters(
                DynamicLink.SocialMetaTagParameters.Builder()
                    .setTitle(title)
                    .setDescription(description)
                    .build()
            )
        }
    }
}