package com.alloydflanagan.hexcalcrpn.main

import android.app.Application
import com.alloydflanagan.hexcalcrpn.ui.AbstractStackViewModel
import com.alloydflanagan.hexcalcrpn.ui.HexStackViewModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import timber.log.Timber
import java.math.BigInteger

class MainApplication: Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidModule(this@MainApplication))
        bind<AbstractStackViewModel<BigInteger>>() with singleton { HexStackViewModel() }
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
