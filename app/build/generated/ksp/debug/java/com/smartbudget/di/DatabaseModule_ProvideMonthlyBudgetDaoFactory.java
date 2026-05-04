package com.smartbudget.di;

import com.smartbudget.data.local.AppDatabase;
import com.smartbudget.data.local.dao.MonthlyBudgetDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class DatabaseModule_ProvideMonthlyBudgetDaoFactory implements Factory<MonthlyBudgetDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideMonthlyBudgetDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public MonthlyBudgetDao get() {
    return provideMonthlyBudgetDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideMonthlyBudgetDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideMonthlyBudgetDaoFactory(databaseProvider);
  }

  public static MonthlyBudgetDao provideMonthlyBudgetDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideMonthlyBudgetDao(database));
  }
}
