package com.architecture.kotlinmvvm.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.facebook.android.crypto.keychain.AndroidConceal
import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain
import com.facebook.crypto.Crypto
import com.facebook.crypto.CryptoConfig
import com.facebook.crypto.Entity
import com.facebook.crypto.keychain.KeyChain
import com.facebook.soloader.SoLoader
import java.security.MessageDigest

class ConcealPreferences(context: Context, prefsName: String, password: String) : SharedPreferences {

    private val mSharedPreferences: SharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    private val mKeyChain: KeyChain
    private val mEntity: Entity
    private val mCrypto: Crypto

    init {

        mKeyChain = SharedPrefsBackedKeyChain(context, CryptoConfig.KEY_256)
        mEntity = Entity.create(password)
        mCrypto = AndroidConceal.get().createCrypto256Bits(mKeyChain)
    }

    companion object {

        private const val HEX_CHARS = "0123456789ABCDEF"

        fun init(context: Context) {
            SoLoader.init(context, false)
        }
    }

    override fun contains(key: String): Boolean = mSharedPreferences.contains(hash(key))

    override fun getBoolean(key: String, defValue: Boolean): Boolean {

        val storedValue = mSharedPreferences.getString(hash(key), null)
                ?: return defValue
        return decrypt(storedValue).toBoolean()
    }

    override fun unregisterOnSharedPreferenceChangeListener(
            listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun getInt(key: String, defValue: Int): Int {

        val storedValue = mSharedPreferences.getString(hash(key), null)
                ?: return defValue
        return decrypt(storedValue).toInt()
    }

    override fun getAll(): MutableMap<String, *>? {

        val encryptedMap = mSharedPreferences.all
        val decryptedMap = hashMapOf<String, String>()

        for (entry in encryptedMap.entries) {
            decryptedMap[entry.key] = decrypt(entry.value.toString())
        }

        return decryptedMap
    }

    override fun edit(): SharedPreferences.Editor = Editor()

    override fun getLong(key: String, defValue: Long): Long {

        val storedValue = mSharedPreferences.getString(hash(key), null)
                ?: return defValue
        return decrypt(storedValue).toLong()
    }

    override fun getFloat(key: String, defValue: Float): Float {

        val storedValue = mSharedPreferences.getString(hash(key), null)
                ?: return defValue
        return decrypt(storedValue).toFloat()
    }

    override fun getStringSet(key: String, defValues: MutableSet<String>): MutableSet<String> {

        val encryptedSet = mSharedPreferences.getStringSet(hash(key), null)
                ?: return defValues
        val decryptedSet = hashSetOf<String>()

        encryptedSet.mapTo(decryptedSet) { decrypt(it) }

        return decryptedSet
    }

    override fun registerOnSharedPreferenceChangeListener(
            listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        mSharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun getString(key: String, defValue: String): String {

        val storedValue = mSharedPreferences.getString(hash(key), null)
                ?: return defValue
        return decrypt(storedValue)
    }

    private fun encrypt(text: String): String = when {
        !mCrypto.isAvailable -> ""
        else -> {
            try {
                val encrypted = mCrypto.encrypt(text.toByteArray(), mEntity)
                Base64.encodeToString(encrypted, Base64.DEFAULT)
            } catch (e: Exception) {
                ""
            }
        }
    }

    private fun decrypt(text: String): String = when {

        !mCrypto.isAvailable -> ""
        else -> {
            try {
                val decrypted = mCrypto.decrypt(Base64.decode(text, Base64.DEFAULT), mEntity)
                String(decrypted)
            } catch (e: Exception) {
                ""
            }
        }
    }

    private fun hash(text: String): String {

        val bytes = MessageDigest
                .getInstance("SHA-256")
                .digest(text.toByteArray())

        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }

        return result.toString()
    }

    inner class Editor : SharedPreferences.Editor {

        private val mEditor: SharedPreferences.Editor = mSharedPreferences.edit()

        override fun putString(key: String, value: String): SharedPreferences.Editor {

            mEditor.putString(hash(key), encrypt(value))
            return this
        }

        override fun putStringSet(key: String, values: Set<String>): SharedPreferences.Editor {

            val encryptedValues = hashSetOf<String>()
            values.mapTo(encryptedValues) { encrypt(it) }
            mEditor.putStringSet(hash(key), encryptedValues)
            return this
        }

        override fun putInt(key: String, value: Int): SharedPreferences.Editor {

            mEditor.putString(hash(key), encrypt(value.toString()))
            return this
        }

        override fun putLong(key: String, value: Long): SharedPreferences.Editor {

            mEditor.putString(hash(key), encrypt(value.toString()))
            return this
        }

        override fun putFloat(key: String, value: Float): SharedPreferences.Editor {

            mEditor.putString(hash(key), encrypt(value.toString()))
            return this
        }

        override fun putBoolean(key: String, value: Boolean): SharedPreferences.Editor {

            mEditor.putString(hash(key), encrypt(value.toString()))
            return this
        }

        override fun remove(key: String): SharedPreferences.Editor {

            mEditor.remove(hash(key))
            return this
        }

        override fun clear(): SharedPreferences.Editor {

            mEditor.clear()
            return this
        }

        override fun commit(): Boolean = mEditor.commit()

        override fun apply() {
            mEditor.apply()
        }
    }
}