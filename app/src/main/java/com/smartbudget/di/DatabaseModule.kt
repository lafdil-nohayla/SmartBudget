package com.smartbudget.di

import android.content.Context
import com.smartbudget.data.local.AppDatabase
import com.smartbudget.data.local.dao.CategoryDao
import com.smartbudget.data.local.dao.ExpenseDao
import com.smartbudget.data.local.dao.MonthlyBudgetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideExpenseDao(database: AppDatabase): ExpenseDao = database.expenseDao()

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao = database.categoryDao()

    @Provides
    fun provideMonthlyBudgetDao(database: AppDatabase): MonthlyBudgetDao = database.monthlyBudgetDao()
}
