package com.example.zappysearch.di


import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.zappysearch.R
import com.example.zappysearch.data.repository.AuthRepositoryImpl
import com.example.zappysearch.data.repository.ChatRepositoryImpl
import com.example.zappysearch.data.repository.GeoPostApi
import com.example.zappysearch.domain.repository.AuthRepository
import com.example.zappysearch.domain.repository.ChatRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun providesCredentialManager(@ApplicationContext context: Context): CredentialManager {
        return CredentialManager.create(context)
    }

    @Provides
    @Singleton
    fun provideGoogleIdOption(@ApplicationContext context: Context): GetGoogleIdOption {
        return GetGoogleIdOption.Builder()
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideCredentialRequest(googleIdOption: GetGoogleIdOption): GetCredentialRequest {
        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        credentialManager: CredentialManager,
        request: GetCredentialRequest,
        @ApplicationContext context: Context
    ): AuthRepository {
        return AuthRepositoryImpl(
            auth = auth,
            credentialManager = credentialManager,
            request = request,
            context = context
        )
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        firestore: FirebaseFirestore
    ) : ChatRepository {
        return ChatRepositoryImpl(
            firestore = firestore
        )
    }

    @Provides
    @Singleton
    fun provideFirebaseFireStore() : FirebaseFirestore {
        return Firebase.firestore
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object NetworkModule {

        @Provides
        @Singleton
        fun provideRetrofit(): Retrofit =
            Retrofit.Builder()
                .baseUrl("https://zappy-search-backend.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        @Provides
        @Singleton
        fun provideGeoPostApi(retrofit: Retrofit): GeoPostApi =
            retrofit.create(GeoPostApi::class.java)
    }


}