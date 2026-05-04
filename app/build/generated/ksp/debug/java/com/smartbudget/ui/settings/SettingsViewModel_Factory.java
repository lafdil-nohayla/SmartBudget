package com.smartbudget.ui.settings;

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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<CategoryRepository> categoryRepositoryProvider;

  private final Provider<ExpenseRepository> expenseRepositoryProvider;

  public SettingsViewModel_Factory(Provider<CategoryRepository> categoryRepositoryProvider,
      Provider<ExpenseRepository> expenseRepositoryProvider) {
    this.categoryRepositoryProvider = categoryRepositoryProvider;
    this.expenseRepositoryProvider = expenseRepositoryProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(categoryRepositoryProvider.get(), expenseRepositoryProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<CategoryRepository> categoryRepositoryProvider,
      Provider<ExpenseRepository> expenseRepositoryProvider) {
    return new SettingsViewModel_Factory(categoryRepositoryProvider, expenseRepositoryProvider);
  }

  public static SettingsViewModel newInstance(CategoryRepository categoryRepository,
      ExpenseRepository expenseRepository) {
    return new SettingsViewModel(categoryRepository, expenseRepository);
  }
}
