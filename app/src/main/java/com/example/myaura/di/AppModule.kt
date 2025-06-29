package com.example.myaura.di

import android.content.Context
import androidx.room.Room
import com.example.myaura.data.local.AppDatabase
import com.example.myaura.data.local.SessionRepository
import com.example.myaura.data.local.dao.ArticleDao
import com.example.myaura.data.local.dao.PortfolioDao
import com.example.myaura.data.local.dao.UserProfileDao
import com.example.myaura.data.local.dao.UserSessionDao
import com.example.myaura.data.remote.AuthRemoteDataSource
import com.example.myaura.data.remote.ImgurApiService
import com.example.myaura.data.remote.ProfileRemoteDataSource
import com.example.myaura.domain.repository.AuthRepository
import com.example.myaura.data.repository.AuthRepositoryImpl
import com.example.myaura.data.repository.ProfileRepositoryImpl
import com.example.myaura.domain.repository.ProfileRepository
import com.example.myaura.domain.usecase.AddArticleUseCase
import com.example.myaura.domain.usecase.AddPortfolioUseCase
import com.example.myaura.domain.usecase.DeleteArticleUseCase
import com.example.myaura.domain.usecase.DeletePortfolioUseCase
import com.example.myaura.domain.usecase.GetArticleUseCase
import com.example.myaura.domain.usecase.GetArticlesUseCase
import com.example.myaura.domain.usecase.GetPortfolioUseCase
import com.example.myaura.domain.usecase.GetPortfoliosUseCase
import com.example.myaura.domain.usecase.GetUserProfileUseCase
import com.example.myaura.domain.usecase.SaveUserProfileUseCase
import com.example.myaura.domain.usecase.SignInUseCase
import com.example.myaura.domain.usecase.SignUpUseCase
import com.example.myaura.domain.usecase.UpdateArticleUseCase
import com.example.myaura.domain.usecase.UpdatePortfolioUseCase
import com.example.myaura.ui.profile.article.ArticleListViewModel
import com.google.firebase.auth.FirebaseAuth
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
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides @Singleton
    fun provideProfileRemoteDataSource(firestore: FirebaseFirestore): ProfileRemoteDataSource = ProfileRemoteDataSource(firestore)

    @Provides @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "my_aura_database"
        ).build()
    }

    @Provides @Singleton
    fun provideUserProfileDao(database: AppDatabase): UserProfileDao = database.userProfileDao()

    @Provides @Singleton
    fun providePortfolioDao(database: AppDatabase): PortfolioDao = database.portfolioDao()

    @Provides @Singleton
    fun provideArticleDao(database: AppDatabase): ArticleDao = database.articleDao()

    @Provides @Singleton
    fun provideUserSessionDao(database: AppDatabase): UserSessionDao = database.userSessionDao()

    @Provides @Singleton
    fun provideProfileRepository(
        dataSource: ProfileRemoteDataSource,
        userProfileDao: UserProfileDao,
        portfolioDao: PortfolioDao,
        articleDao: ArticleDao
    ): ProfileRepository = ProfileRepositoryImpl(dataSource, userProfileDao, portfolioDao, articleDao)

    @Provides @Singleton
    fun provideSaveUserProfileUseCase(repo: ProfileRepository): SaveUserProfileUseCase = SaveUserProfileUseCase(repo)

    @Provides @Singleton
    fun provideGetUserProfileUseCase(repo: ProfileRepository): GetUserProfileUseCase = GetUserProfileUseCase(repo)

    @Provides @Singleton
    fun provideAddPortfolioUseCase(repo: ProfileRepository): AddPortfolioUseCase = AddPortfolioUseCase(repo)

    @Provides @Singleton
    fun provideGetPortfoliosUseCase(repo: ProfileRepository): GetPortfoliosUseCase = GetPortfoliosUseCase(repo)

    @Provides @Singleton
    fun provideDeletePortfolioUseCase(repo: ProfileRepository): DeletePortfolioUseCase = DeletePortfolioUseCase(repo)

    @Provides @Singleton
    fun provideGetPortfolioUseCase(repo: ProfileRepository): GetPortfolioUseCase = GetPortfolioUseCase(repo)

    @Provides @Singleton
    fun provideUpdatePortfolioUseCase(repo: ProfileRepository): UpdatePortfolioUseCase = UpdatePortfolioUseCase(repo)

    @Provides @Singleton
    fun provideAddArticleUseCase(repo: ProfileRepository): AddArticleUseCase = AddArticleUseCase(repo)

    @Provides @Singleton
    fun provideGetArticlesUseCase(repo: ProfileRepository): GetArticlesUseCase = GetArticlesUseCase(repo)

    @Provides @Singleton
    fun provideUpdateArticleUseCase(repo: ProfileRepository): UpdateArticleUseCase = UpdateArticleUseCase(repo)

    @Provides @Singleton
    fun provideDeleteArticleUseCase(repo: ProfileRepository): DeleteArticleUseCase = DeleteArticleUseCase(repo)

    @Provides @Singleton
    fun provideGetArticleUseCase(repo: ProfileRepository): GetArticleUseCase = GetArticleUseCase(repo)

    @Provides @Singleton
    fun provideImgurApiService(): ImgurApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.imgur.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImgurApiService::class.java)
    }

    @Provides @Singleton
    fun provideSessionRepository(userSessionDao: UserSessionDao): SessionRepository {
        return SessionRepository(userSessionDao)
    }

    @Provides @Singleton
    fun provideArticleListViewModel(profileRemoteDataSource: ProfileRemoteDataSource): ArticleListViewModel = ArticleListViewModel(profileRemoteDataSource)
}