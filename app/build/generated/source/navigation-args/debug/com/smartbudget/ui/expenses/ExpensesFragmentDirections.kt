package com.smartbudget.ui.expenses

import android.os.Bundle
import androidx.navigation.NavDirections
import com.smartbudget.R
import kotlin.Int
import kotlin.Long

public class ExpensesFragmentDirections private constructor() {
  private data class ActionExpensesToAddExpense(
    public val expenseId: Long = -1L,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_expenses_to_add_expense

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putLong("expenseId", this.expenseId)
        return result
      }
  }

  public companion object {
    public fun actionExpensesToAddExpense(expenseId: Long = -1L): NavDirections =
        ActionExpensesToAddExpense(expenseId)
  }
}
