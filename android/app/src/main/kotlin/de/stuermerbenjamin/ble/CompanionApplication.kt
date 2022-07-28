package de.stuermerbenjamin.ble

import android.app.Application
import de.stuermerbenjamin.ble.data.dataModule
import de.stuermerbenjamin.ble.domain.domainModule
import de.stuermerbenjamin.ble.ui.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class CompanionApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Logging
        Timber.plant(Timber.DebugTree())

        // Dependency injection
        startKoin {
            androidLogger(Level.INFO)

            androidContext(this@CompanionApplication)

            modules(
                listOf(
                    dataModule,
                    domainModule,
                    uiModule
                )
            )
        }
    }
}
