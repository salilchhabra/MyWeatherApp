package com.example.myweatherapp.view

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myweatherapp.R
import com.example.myweatherapp.helper.CustomProgressDialog
import com.example.myweatherapp.helper.SharedPreferenceUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_user_name.*
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var firstFragment: Fragment
    lateinit var secondFragment: Fragment
    lateinit var thirdFragment: Fragment
    lateinit var fourthFragment: Fragment
    lateinit var activeFragment: Fragment
    private var mDialog: Dialog? = null
    private var userName: String? = null

    private var fusedLocationClient: FusedLocationProviderClient? = null
    var cityName: String? = null
    private var mProgressDialog: CustomProgressDialog? = null
    private var mSharedPreferenceUtils: SharedPreferenceUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mSharedPreferenceUtils = applicationContext?.let {
            SharedPreferenceUtils.getInstance(
                it
            )
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mProgressDialog = CustomProgressDialog(this)
        fetchUserLocation()


        bottom_navigation_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.first -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(firstFragment).commit()
                    activeFragment = firstFragment
                }
                R.id.second -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(secondFragment).commit()
                    activeFragment = secondFragment
                }
                R.id.third -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(thirdFragment).commit()
                    activeFragment = thirdFragment
                }
                R.id.fourth -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(fourthFragment).commit()
                    activeFragment = fourthFragment
                }
            }
            true
        }

    }

    private fun fetchUserLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            }
        } else {
            getLastLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED)
                    ) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                        getLastLocation()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                    finishAffinity()
                }
                return
            }
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mProgressDialog?.show("please wait")
        fusedLocationClient?.lastLocation?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                val latitude = task.result.latitude
                val longitude = task.result.longitude
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses: List<Address> =
                    geocoder.getFromLocation(latitude, longitude, 1)
                cityName = addresses[0].locality
                mProgressDialog?.hide()
                showUserDialog(addresses, latitude, longitude)

            } else {
                Toast.makeText(this, " unable to fetch location", Toast.LENGTH_SHORT).show()
                mProgressDialog?.hide()
            }
        }
    }

    private fun showUserDialog(
        addresses: List<Address>,
        latitude: Double,
        longitude: Double
    ) {
        mSharedPreferenceUtils?.let {
            if (!it.containsKey("User_name")) {
                mDialog = Dialog(this)
                mDialog?.apply {
                    setContentView(R.layout.dialog_user_name)
                    show()
                    btn_button.setOnClickListener {
                        mSharedPreferenceUtils?.putSharedPref("User_name", et_name.text.toString())
                        userName = et_name.text.toString()
                        dismiss()
                        initializeFragment(addresses, latitude, longitude)
                    }

                }
            } else {
                userName = mSharedPreferenceUtils?.getSharedPref("User_name")
                initializeFragment(addresses, latitude, longitude)

            }
        }
    }

    private fun initializeFragment(
        addresses: List<Address>,
        latitude: Double,
        longitude: Double
    ) {
        firstFragment = FirstFragment.newInstance(addresses[0].locality, userName)
        secondFragment = SecondFragment()
        thirdFragment =
            ThirdFragment.newInstance(
                latitude.toString(),
                longitude.toString(),
                userName
            )
        fourthFragment = FourthFragment.newInstance(userName)
        activeFragment = firstFragment
        setCurrentFragment()
    }

    private fun setCurrentFragment() {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fl_fragment, firstFragment).hide(firstFragment)
            add(R.id.fl_fragment, secondFragment).hide(secondFragment)
            add(R.id.fl_fragment, thirdFragment).hide(thirdFragment)
            add(R.id.fl_fragment, fourthFragment).hide(fourthFragment)
        }.commit()
        supportFragmentManager.beginTransaction().hide(activeFragment).show(firstFragment).commit()
        activeFragment = firstFragment
    }
}