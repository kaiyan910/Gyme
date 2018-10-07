package com.architecture.kotlinmvvm.manager

import com.architecture.kotlinmvvm.model.OAuthToken
import com.architecture.kotlinmvvm.preferences.PreferencesRepository

class SecureTokenManager(
        private val preferencesRepository: PreferencesRepository
) : TokenManager<OAuthToken> {

    override fun save(token: OAuthToken) {
        preferencesRepository.setToken(token.token)
    }

    override fun get(): OAuthToken {

        val token = preferencesRepository.getToken()
        return OAuthToken(token, "", 0L)
    }

    override fun clean() {
        preferencesRepository.cleanToken()
    }

}