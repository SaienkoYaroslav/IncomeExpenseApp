package com.example.incomeexpenseapp.di

import android.content.Context
import androidx.room.Room
import com.example.incomeexpenseapp.data.repository.Repository
import com.example.incomeexpenseapp.data.repository.RepositoryImpl
import com.example.incomeexpenseapp.data.db.AppDatabase
import com.example.incomeexpenseapp.data.db.dao.CategoryDao
import com.example.incomeexpenseapp.data.db.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao {
        return db.categoryDao()
    }

    @Provides
    fun provideTransactionDao(db: AppDatabase): TransactionDao {
        return db.transactionDao()
    }

    @Provides
    @Singleton
    fun provideRepository(
        categoryDao: CategoryDao,
        transactionDao: TransactionDao
    ): Repository {
        return RepositoryImpl(categoryDao, transactionDao)
    }

}