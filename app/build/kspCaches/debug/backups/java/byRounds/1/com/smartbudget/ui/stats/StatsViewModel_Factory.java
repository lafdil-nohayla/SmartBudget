package com.smartbudget.ui.stats;

import com.smartbudget.data.repository.CategoryRepository;
import com.smartbudget.data.repository.ExpenseRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class StatsViewModel_Factory implements Factory<StatsViewModel> {
  private final Provider<ExpenseRepository> expenseRepositoryProvider;

  private final Provider<CategoryRepository> categoryRepositoryProvider;

  public StatsViewModel_Factory(Provider<ExpenseRepository> expenseRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider) {
    this.expenseRepositoryProvider = expenseRepositoryProvider;
    this.categoryRepositoryProvider = categoryRepositoryProvider;
  }

  @Override
  public StatsViewModel get() {
    return newInstance(expenseRepositoryProvider.get(), categoryRepositoryProvider.get());
  }

  public static StatsViewModel_Factory create(Provider<ExpenseRepository> expenseRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider) {
    return new StatsViewModel_Factory(expenseRepositoryProvider, categoryRepositoryProvider);
  }

  public static StatsViewModel newInstance(ExpenseRepository expenseRepository,
      CategoryRepository categoryRepository) {
    return new StatsViewModel(expenseRepository, categoryRepository);
  }
}
