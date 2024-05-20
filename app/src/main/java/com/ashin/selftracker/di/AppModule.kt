package com.ashin.selftracker.di

import android.content.Context
import com.ashin.selftracker.MyApplication
import com.ashin.selftracker.model.pojo.LocationInfo
import com.ashin.selftracker.model.pojo.User
import com.ashin.selftracker.model.repository.LocationListRepository
import com.ashin.selftracker.model.repository.LocationListRepositoryImplementation
import com.ashin.selftracker.model.repository.LoginSignUpRepository
import com.ashin.selftracker.model.repository.LoginSignUpRepositoryImplementation
import com.ashin.selftracker.utils.PreferenceManger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideRealm():Realm
    {
        val config= RealmConfiguration.Builder(schema = setOf(User::class,LocationInfo::class) ).name("selftrack.realm").schemaVersion(1).deleteRealmIfMigrationNeeded().build()
        return Realm.open(config)
    }

    @Provides
    @Singleton
    fun provideLoginSignupRepoImplementation(realm: Realm):LoginSignUpRepositoryImplementation
    {
        return LoginSignUpRepositoryImplementation(realm)
    }

    @Provides
    @Singleton
    fun providesRepository(loginSignUpRepositoryImplementation: LoginSignUpRepositoryImplementation):LoginSignUpRepository
    {
        return LoginSignUpRepository(loginSignUpRepositoryImplementation)
    }
    @Provides
    @Singleton
    fun provideLocationListRepoImplementation(realm: Realm):LocationListRepositoryImplementation
    {
        return LocationListRepositoryImplementation(realm)
    }

    @Provides
    @Singleton
    fun providesLocationListRepo(locationListRepositoryImplementation: LocationListRepositoryImplementation):LocationListRepository
    {
        return LocationListRepository(locationListRepositoryImplementation)
    }
    @Provides
    @Singleton
    fun provideApplication(@ApplicationContext app: Context):   MyApplication{
        return app as MyApplication
    }
    @Provides
    @Singleton
    fun providesPreferenceMgr(myApplication: MyApplication):PreferenceManger{
        return PreferenceManger(myApplication.applicationContext)
    }


}