package de.stuermerbenjamin.ble.data.connection.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import de.stuermerbenjamin.ble.data.bluetooth.BleConnectionServiceImpl
import de.stuermerbenjamin.ble.data.bluetooth.repository.ConnectionRepository
import de.stuermerbenjamin.ble.data.bluetooth.repository.ConnectionRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConnectionRepositoryTests {
    private lateinit var repository: ConnectionRepository
    private val connectionService: BleConnectionServiceImpl = mockk(relaxed = true)

    @Before
    fun setup() {
        repository = ConnectionRepositoryImpl(connectionService = connectionService)
    }

    @Test
    fun verifyThatFirmwareFileGetsUploadedOnlyOnce() = runBlocking {
        // given
        coEvery {
            connectionService.transferFirmware(any())
        } answers {}

        // when
        repository.transferFirmware(byteArrayOf(0x01, 0x02, 0x03))

        // then
        coVerify(exactly = 1) { connectionService::transferFirmware }
    }
}
