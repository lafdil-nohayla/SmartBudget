package com.smartbudget.ui.add

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Long
import kotlin.jvm.JvmStatic

public data class AddExpenseFragmentArgs(
  public val expenseId: Long = -1L,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putLong("expenseId", this.expenseId)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("expenseId", this.expenseId)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): AddExpenseFragmentArgs {
      bundle.setClassLoader(AddExpenseFragmentArgs::class.java.classLoader)
      val __expenseId : Long
      if (bundle.containsKey("expenseId")) {
        __expenseId = bundle.getLong("expenseId")
      } else {
        __expenseId = -1L
      }
      return AddExpenseFragmentArgs(__expenseId)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): AddExpenseFragmentArgs {
      val __expenseId : Long?
      if (savedStateHandle.contains("expenseId")) {
        __expenseId = savedStateHandle["expenseId"]
        if (__expenseId == null) {
          throw IllegalArgumentException("Argument \"expenseId\" of type long does not support null values")
        }
      } else {
        __expenseId = -1L
      }
      return AddExpenseFragmentArgs(__expenseId)
    }
  }
}
