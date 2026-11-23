package com.example.batterymanager

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.slider.Slider
import com.google.android.material.cardmaterial.card.MaterialCardView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

private const val PREFS_NAME = "battery_prefs"
private val Context.dataStore by preferencesDataStore(name = PREFS_NAME)

class MainActivity : AppCompatActivity() {

    private lateinit var textViewBatteryLevel: TextView
    private lateinit var textViewChargingStatus: TextView
    private lateinit var sliderStartThreshold: Slider
    private lateinit var sliderStopThreshold: Slider
    private lateinit var textStartThresholdValue: TextView
    private lateinit var textStopThresholdValue: TextView
    private lateinit var buttonSave: MaterialButton
    private lateinit var buttonReset: MaterialButton
    private lateinit var cardPermission: MaterialCardView
    private lateinit var buttonOpenSettings: MaterialButton
    private lateinit var textViewStatus: TextView

    private var startThreshold = 30
    private var stopThreshold = 80
    private var batteryLevel = 0
    private var isCharging = false

    private val startThresholdKey = intPreferencesKey("start_threshold")
    private val stopThresholdKey = intPreferencesKey("stop_threshold")
    private val serviceRunningKey = booleanPreferencesKey("service_running")

    // Permission launcher for battery optimization
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        checkBatteryOptimizationPermission()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        textViewBatteryLevel = findViewById(R.id.textViewBatteryLevel)
        textViewChargingStatus = findViewById(R.id.textViewChargingStatus)
        sliderStartThreshold = findViewById(R.id.sliderStartThreshold)
        sliderStopThreshold = findViewById(R.id.sliderStopThreshold)
        textStartThresholdValue = findViewById(R.id.textStartThresholdValue)
        textStopThresholdValue = findViewById(R.id.textStopThresholdValue)
        buttonSave = findViewById(R.id.buttonSave)
        buttonReset = findViewById(R.id.buttonReset)
        cardPermission = findViewById(R.id.cardPermission)
        buttonOpenSettings = findViewById(R.id.buttonOpenSettings)
        textViewStatus = findViewById(R.id.textViewStatus)

        // Load preferences
        loadPreferences()

        // Setup slider listeners
        setupSliders()

        // Setup button listeners
        buttonSave.setOnClickListener {
            savePreferences()
            startBatteryService()
        }

        buttonReset.setOnClickListener {
            resetToDefaults()
        }

        buttonOpenSettings.setOnClickListener {
            requestBatteryOptimizationPermission()
        }

        // Check battery optimization permission
        checkBatteryOptimizationPermission()

        // Initial update
        updateUI()
    }

    private fun loadPreferences() {
        lifecycleScope.launch {
            val prefs = dataStore.data.first()
            startThreshold = prefs[startThresholdKey] ?: 30
            stopThreshold = prefs[stopThresholdKey] ?: 80

            // Ensure thresholds are valid
            startThreshold = startThreshold.coerceIn(10, 75)
            stopThreshold = stopThreshold.coerceIn(startThreshold + 5, 95)

            sliderStartThreshold.value = startThreshold.toFloat()
            sliderStopThreshold.value = stopThreshold.toFloat()
            updateThresholdLabels()
        }
    }

    private fun savePreferences() {
        lifecycleScope.launch {
            dataStore.edit { prefs ->
                prefs[startThresholdKey] = startThreshold
                prefs[stopThresholdKey] = stopThreshold
            }
            Toast.makeText(this@MainActivity, "Settings saved!", Toast.LENGTH_SHORT).show()
            textViewStatus.text = "Settings saved - Tap Start to begin"
        }
    }

    private fun resetToDefaults() {
        startThreshold = 30
        stopThreshold = 80
        sliderStartThreshold.value = 30f
        sliderStopThreshold.value = 80f

        lifecycleScope.launch {
            dataStore.edit { prefs ->
                prefs[startThresholdKey] = 30
                prefs[stopThresholdKey] = 80
            }
            Toast.makeText(this@MainActivity, "Defaults restored", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSliders() {
        sliderStartThreshold.addOnChangeListener { slider, value, _ ->
            startThreshold = value.toInt()
            // Ensure stop threshold is always higher
            if (startThreshold >= stopThreshold) {
                sliderStopThreshold.value = (startThreshold + 5).toFloat()
            }
            updateThresholdLabels()
        }

        sliderStopThreshold.addOnChangeListener { slider, value, _ ->
            stopThreshold = value.toInt()
            // Ensure start threshold is always lower
            if (stopThreshold <= startThreshold) {
                sliderStartThreshold.value = (stopThreshold - 5).toFloat()
            }
            updateThresholdLabels()
        }
    }

    private fun updateThresholdLabels() {
        textStartThresholdValue.text = getString(R.string.label_start_threshold, startThreshold)
        textStopThresholdValue.text = getString(R.string.label_stop_threshold, stopThreshold)
    }

    private fun startBatteryService() {
        val intent = Intent(this, BatteryMonitorService::class.java).apply {
            putExtra("start_threshold", startThreshold)
            putExtra("stop_threshold", stopThreshold)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }

        Toast.makeText(this, "Battery manager started!", Toast.LENGTH_SHORT).show()
        textViewStatus.text = "Active"
    }

    private fun checkBatteryOptimizationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val packageName = packageName
            val pm = getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                cardPermission.visibility = android.view.View.VISIBLE
            } else {
                cardPermission.visibility = android.view.View.GONE
            }
        }
    }

    private fun requestBatteryOptimizationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(Settings.RequestIgnoreBatteryOptimizations).apply {
                data = android.net.Uri.parse("package:$packageName")
            }
            permissionLauncher.launch(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        updateBatteryStatus()
    }

    private fun updateBatteryStatus() {
        val batteryManager = getSystemService(Context.BATTERY_SERVICE) as android.os.BatteryManager
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = registerReceiver(null, intentFilter)

        batteryLevel = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: 0
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: 100
        val level = (batteryLevel / scale.toFloat() * 100).toInt()

        val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: BatteryManager.BATTERY_STATUS_UNKNOWN
        isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL

        textViewBatteryLevel.text = getString(R.string.current_battery_level, level)
        textViewChargingStatus.text = getString(
            R.string.is_charging,
            if (isCharging) {
                if (status == BatteryManager.BATTERY_STATUS_FULL) getString(R.string.full)
                else getString(R.string.charging)
            } else getString(R.string.not_charging)
        )

        // Color coding for charging status
        textViewChargingStatus.setTextColor(
            ContextCompat.getColor(
                this,
                if (isCharging) R.color.green_500 else R.color.orange_500
            )
        )
    }

    private fun updateUI() {
        updateBatteryStatus()
    }
}