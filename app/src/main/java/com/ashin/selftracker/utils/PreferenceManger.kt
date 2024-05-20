package com.ashin.selftracker.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.io.IOException
import java.security.GeneralSecurityException

class PreferenceManger(applicationContext: Context) {
    var context=applicationContext

    fun getEditor(): SharedPreferences.Editor {
        val editor = providesSharedPreference()!!.edit()
        return editor
    }

    fun providesSharedPreference(): SharedPreferences? {
        var sharedPreferences: SharedPreferences? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                sharedPreferences = EncryptedSharedPreferences.create(
                    context!!,
                    context!!.packageName,
                    getMasterKey()!!,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            } catch (e: GeneralSecurityException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            sharedPreferences =
                context!!.getSharedPreferences(
                    context!!.packageName,
                    Context.MODE_PRIVATE
                )
        }
        return sharedPreferences
    }

    private fun getMasterKey(): MasterKey? {
        try {
            return MasterKey.Builder(context!!)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun clearPrefs() {
        val editor = providesSharedPreference()!!.edit()
        editor.clear()
        editor.commit()
    }

    fun setEmail(email: String?) {
        val editor = providesSharedPreference()!!.edit()
        editor.putString("email", email)
        editor.commit()
    }

    fun getEmail(): String? {
        val prefs = providesSharedPreference()
        return prefs!!.getString("email", "")
    }
}