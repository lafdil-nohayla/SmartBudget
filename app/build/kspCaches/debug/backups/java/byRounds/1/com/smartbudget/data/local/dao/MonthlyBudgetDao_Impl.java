package com.smartbudget.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.smartbudget.data.local.entity.MonthlyBudget;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MonthlyBudgetDao_Impl implements MonthlyBudgetDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MonthlyBudget> __insertionAdapterOfMonthlyBudget;

  private final EntityDeletionOrUpdateAdapter<MonthlyBudget> __deletionAdapterOfMonthlyBudget;

  private final EntityDeletionOrUpdateAdapter<MonthlyBudget> __updateAdapterOfMonthlyBudget;

  public MonthlyBudgetDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMonthlyBudget = new EntityInsertionAdapter<MonthlyBudget>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `monthly_budgets` (`id`,`month`,`categoryId`,`limitAmount`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MonthlyBudget entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getMonth());
        statement.bindLong(3, entity.getCategoryId());
        statement.bindDouble(4, entity.getLimitAmount());
      }
    };
    this.__deletionAdapterOfMonthlyBudget = new EntityDeletionOrUpdateAdapter<MonthlyBudget>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `monthly_budgets` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MonthlyBudget entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfMonthlyBudget = new EntityDeletionOrUpdateAdapter<MonthlyBudget>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `monthly_budgets` SET `id` = ?,`month` = ?,`categoryId` = ?,`limitAmount` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MonthlyBudget entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getMonth());
        statement.bindLong(3, entity.getCategoryId());
        statement.bindDouble(4, entity.getLimitAmount());
        statement.bindLong(5, entity.getId());
      }
    };
  }

  @Override
  public Object insertBudget(final MonthlyBudget budget,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMonthlyBudget.insertAndReturnId(budget);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteBudget(final MonthlyBudget budget,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMonthlyBudget.handle(budget);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBudget(final MonthlyBudget budget,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMonthlyBudget.handle(budget);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<List<MonthlyBudget>> getBudgetsForMonth(final String month) {
    final String _sql = "SELECT * FROM monthly_budgets WHERE month = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, month);
    return __db.getInvalidationTracker().createLiveData(new String[] {"monthly_budgets"}, false, new Callable<List<MonthlyBudget>>() {
      @Override
      @Nullable
      public List<MonthlyBudget> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "month");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfLimitAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "limitAmount");
          final List<MonthlyBudget> _result = new ArrayList<MonthlyBudget>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MonthlyBudget _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpMonth;
            _tmpMonth = _cursor.getString(_cursorIndexOfMonth);
            final long _tmpCategoryId;
            _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            final double _tmpLimitAmount;
            _tmpLimitAmount = _cursor.getDouble(_cursorIndexOfLimitAmount);
            _item = new MonthlyBudget(_tmpId,_tmpMonth,_tmpCategoryId,_tmpLimitAmount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getBudgetForMonthAndCategory(final String month, final long categoryId,
      final Continuation<? super MonthlyBudget> $completion) {
    final String _sql = "SELECT * FROM monthly_budgets WHERE month = ? AND categoryId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, month);
    _argIndex = 2;
    _statement.bindLong(_argIndex, categoryId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MonthlyBudget>() {
      @Override
      @Nullable
      public MonthlyBudget call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "month");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfLimitAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "limitAmount");
          final MonthlyBudget _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpMonth;
            _tmpMonth = _cursor.getString(_cursorIndexOfMonth);
            final long _tmpCategoryId;
            _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            final double _tmpLimitAmount;
            _tmpLimitAmount = _cursor.getDouble(_cursorIndexOfLimitAmount);
            _result = new MonthlyBudget(_tmpId,_tmpMonth,_tmpCategoryId,_tmpLimitAmount);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
