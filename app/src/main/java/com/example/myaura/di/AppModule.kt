package com.example.myaura.di

import com.example.myaura.data.remote.AuthRemoteDataSource
import com.example.myaura.data.remote.ProfileRemoteDataSource
import com.example.myaura.domain.repository.AuthRepository
import com.example.myaura.data.repository.AuthRepositoryImpl
import com.example.myaura.data.repository.ProfileRepositoryImpl
import com.example.myaura.domain.repository.ProfileRepository
import com.example.myaura.domain.usecase.ForgotPasswordUseCase
import com.example.myaura.domain.usecase.SaveUserProfileUseCase
import com.example.myaura.domain.usecase.SignInUseCase
import com.example.myaura.domain.usecase.SignUpUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides @Singleton
    fun provideAuthRemoteDataSource(auth: FirebaseAuth): AuthRemoteDataSource = AuthRemoteDataSource(auth)

    @Provides @Singleton
    fun provideAuthRepository(dataSource: AuthRemoteDataSource): AuthRepository = AuthRepositoryImpl(dataSource)

    @Provides @Singleton
    fun provideSignUpUseCase(repo: AuthRepository): SignUpUseCase = SignUpUseCase(repo)

    @Provides @Singleton
    fun provideSignInUseCase(repo: AuthRepository): SignInUseCase = SignInUseCase(repo)

    @Provides @Singleton
    fun provideForgotPasswordUseCase(repo: AuthRepository): ForgotPasswordUseCase = ForgotPasswordUseCase(repo)

    @Provides @Singleton
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides @Singleton
    fun provideProfileRemoteDataSource(firestore: FirebaseFirestore): ProfileRemoteDataSource = ProfileRemoteDataSource(firestore)

    @Provides @Singleton
    fun provideProfileRepository(dataSource: ProfileRemoteDataSource): ProfileRepository = ProfileRepositoryImpl(dataSource)

    @Provides @Singleton
    fun provideSaveUserProfileUseCase(repo: ProfileRepository): SaveUserProfileUseCase = SaveUserProfileUseCase(repo)

}