package com.smartbudget.ui.expenses;

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
public final class ExpensesViewModel_Factory implements Factory<ExpensesViewModel> {
  private final Provider<ExpenseRepository> expenseRepositoryProvider;

  private final Provider<CategoryRepository> categoryRepositoryProvider;

  public ExpensesViewModel_Factory(Provider<ExpenseRepository> expenseRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider) {
    this.expenseRepositoryProvider = expenseRepositoryProvider;
    this.categoryRepositoryProvider = categoryRepositoryProvider;
  }

  @Override
  public ExpensesViewModel get() {
    return newInstance(expenseRepositoryProvider.get(), categoryRepositoryProvider.get());
  }

  public static ExpensesViewModel_Factory create(
      Provider<ExpenseRepository> expenseRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider) {
    return new ExpensesViewModel_Factory(expenseRepositoryProvider, categoryRepositoryProvider);
  }

  public static ExpensesViewModel newInstance(ExpenseRepository expenseRepository,
      CategoryRepository categoryRepository) {
    return new ExpensesViewModel(expenseRepository, categoryRepository);
  }
}
