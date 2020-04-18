package com.tr1984.firebasesample.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.InfoWindow
import com.tr1984.firebasesample.R
import com.tr1984.firebasesample.data.Pois
import com.tr1984.firebasesample.databinding.ActivityMapsBinding
import com.tr1984.firebasesample.databinding.DrawerHeaderBinding
import com.tr1984.firebasesample.extensions.alert
import com.tr1984.firebasesample.extensions.disposeBag
import com.tr1984.firebasesample.extensions.toast
import com.tr1984.firebasesample.extensions.uiSubscribe
import com.tr1984.firebasesample.firebase.AnalyticsHelper
import com.tr1984.firebasesample.firebase.DynamicLinkHelper
import com.tr1984.firebasesample.firebase.RemoteConfigHelper
import com.tr1984.firebasesample.ui.feeds.FeedsActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction

class MapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: MapsViewModel
    private var naverMap: NaverMap? = null

    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnalyticsHelper.instance.trackScreen(this)

        viewModel = MapsViewModel()
        binding = ActivityMapsBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        setContentView(binding.root)

        subscribeViewModel()

        Observable.combineLatest(
            getMap(),
            viewModel.getConfiguration(),
            BiFunction<NaverMap, Unit, NaverMap> { map, _ ->
                map
            })
            .uiSubscribe({
                settingMap(it)
                setupDrawer()
                viewModel.start()
            }, {
                this@MapsActivity.alert("알림", it.localizedMessage)
            }).disposeBag(compositeDisposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
        viewModel.destroy()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun showDrawer(v: View) {
        if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            AnalyticsHelper.instance.logEvent("show_drawer")
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    fun share() {
        AnalyticsHelper.instance.logEvent("click_menu", "menu" to "share")
        DynamicLinkHelper.getShortDynamicLink("https://github.com/1984tr") {
            it?.let { link ->
                startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, link)
                }, "공유하기"))
            }
        }
    }

    fun sendMailToContact() {
        AnalyticsHelper.instance.logEvent("click_menu", "menu" to "mail")
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("mailto:jollytris@gmail.com")))
    }

    fun startFeedActivity() {
        AnalyticsHelper.instance.logEvent("click_menu", "menu" to "board")
        startActivity(Intent(this@MapsActivity, FeedsActivity::class.java))
    }

    fun moveToSource() {
        AnalyticsHelper.instance.logEvent("click_menu", "menu" to "contact")
        val link =
            RemoteConfigHelper.instance.getString(RemoteConfigHelper.Key.DATA_SOURCE_LINK) ?: ""
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    private fun getMap(): Observable<NaverMap> {
        return Observable.create<NaverMap> { emit ->
            val fm = supportFragmentManager
            val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
                ?: MapFragment.newInstance().also {
                    fm.beginTransaction().add(R.id.map, it).commit()
                }
            mapFragment.getMapAsync { naverMap ->
                emit.onNext(naverMap)
                emit.onComplete()
            }
        }
    }

    private fun settingMap(map: NaverMap) {
        this.naverMap = map
        naverMap?.uiSettings?.run {
            isZoomControlEnabled = false
        }
    }

    private fun subscribeViewModel() {
        viewModel.run {
            positionSubject
                .uiSubscribe({
                    naverMap?.moveCamera(CameraUpdate.scrollTo(it))
                }, {
                    it.printStackTrace()
                }).disposeBag(compositeDisposable)

            poiGroupsSubject
                .uiSubscribe({ pois ->
                    binding.poiGroups.run {
                        layoutManager = LinearLayoutManager(this@MapsActivity)
                        adapter =
                            PoisAdapter(pois) { selected ->
                                toast("click: ${selected.name}")
                                removeAllMarker()
                                drawMarker(selected)
                            }
                        adapter?.notifyDataSetChanged()
                    }
                }, {
                    it.printStackTrace()
                }).disposeBag(compositeDisposable)

            poisSubject
                .uiSubscribe({
                    drawMarker(it)
                }, {
                    it.printStackTrace()
                }).disposeBag(compositeDisposable)
        }
    }

    private fun setupDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        val headerBinding = DrawerHeaderBinding.bind(binding.navView.getHeaderView(0))
        headerBinding.viewModel = viewModel
        headerBinding.activity = this
    }

    private fun drawMarker(pois: Pois) {
        binding.lblPoisTitle.run {
            visibility = View.VISIBLE
            text = pois.name
        }
        var isFirst = true
        pois.items.forEach { poi ->
            poi.circleOverlay.map = naverMap
            poi.infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(this@MapsActivity) {
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    return poi.getMessage()
                }
            }
            naverMap?.let { map -> poi.infoWindow.open(map) }
            if (isFirst) {
                isFirst = false
                viewModel.positionSubject.onNext(LatLng(poi.point.latitude, poi.point.longitude))
            }
        }
        if (pois.paths.coords.isNotEmpty()) {
            pois.paths.map = naverMap
        }
    }

    private fun removeAllMarker() {
        viewModel.pois.forEach {
            it.paths.map = null
            it.items.forEach {
                it.circleOverlay.map = null
                it.infoWindow.map = null
            }
        }
    }
}
