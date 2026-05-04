package com.smartbudget.ui.add;

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
public final class AddExpenseViewModel_Factory implements Factory<AddExpenseViewModel> {
  private final Provider<ExpenseRepository> expenseRepositoryProvider;

  private final Provider<CategoryRepository> categoryRepositoryProvider;

  public AddExpenseViewModel_Factory(Provider<ExpenseRepository> expenseRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider) {
    this.expenseRepositoryProvider = expenseRepositoryProvider;
    this.categoryRepositoryProvider = categoryRepositoryProvider;
  }

  @Override
  public AddExpenseViewModel get() {
    return newInstance(expenseRepositoryProvider.get(), categoryRepositoryProvider.get());
  }

  public static AddExpenseViewModel_Factory create(
      Provider<ExpenseRepository> expenseRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider) {
    return new AddExpenseViewModel_Factory(expenseRepositoryProvider, categoryRepositoryProvider);
  }

  public static AddExpenseViewModel newInstance(ExpenseRepository expenseRepository,
      CategoryRepository categoryRepository) {
    return new AddExpenseViewModel(expenseRepository, categoryRepository);
  }
}
