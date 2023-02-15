package com.myshopproject.data.repository

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigClientException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.myshopproject.domain.repository.FirebaseRepository
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val fcm: FirebaseRemoteConfig
) : FirebaseRepository {
    override fun getPaymentMethod(): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading)

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1
        }
        fcm.setConfigSettingsAsync(configSettings)
        fcm.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val getRemoteKey = fcm.getString("payment_json")
                    trySend(Resource.Success(getRemoteKey))
                } else {
                    close(task.exception ?: Exception("Unknown error occurred"))
                }
            }
            .addOnFailureListener {
                when (it) {
                    is FirebaseNetworkException -> trySend(Resource.Error(it.localizedMessage, null, null))
                    is FirebaseRemoteConfigClientException -> {Resource.Error(it.localizedMessage, null, null)}
                    is FirebaseTooManyRequestsException -> {Resource.Error(it.localizedMessage, null, null)}
                    is FirebaseRemoteConfigException -> {Resource.Error(it.localizedMessage, null, null)}
                    is FirebaseException -> {Resource.Error(it.localizedMessage, null, null)}
                }
            }
        awaitClose()
    }
}