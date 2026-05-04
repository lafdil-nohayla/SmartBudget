package com.smartbudget.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.smartbudget.data.local.dao.CategoryDao
import com.smartbudget.data.local.dao.ExpenseDao
import com.smartbudget.data.local.dao.MonthlyBudgetDao
import com.smartbudget.data.local.entity.Category
import com.smartbudget.data.local.entity.Expense
import com.smartbudget.data.local.entity.MonthlyBudget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

@Database(
    entities = [Category::class, Expense::class, MonthlyBudget::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun monthlyBudgetDao(): MonthlyBudgetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smartbudget_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database.categoryDao(), database.expenseDao())
                }
            }
        }

        suspend fun populateDatabase(categoryDao: CategoryDao, expenseDao: ExpenseDao) {
            val categories = listOf(
                Category(name = "Alimentation", icon = "🍔", color = "#FF6B35"),
                Category(name = "Transport",    icon = "🚌", color = "#4ECDC4"),
                Category(name = "Logement",     icon = "🏠", color = "#45B7D1"),
                Category(name = "Santé",        icon = "💊", color = "#96CEB4"),
                Category(name = "Loisirs",      icon = "🎮", color = "#FFEAA7"),
                Category(name = "Études",       icon = "📚", color = "#DDA0DD"),
                Category(name = "Vêtements",    icon = "👕", color = "#F0A500"),
                Category(name = "Autre",        icon = "📦", color = "#B0BEC5")
            )
            val ids = categories.map { categoryDao.insertCategory(it) }

            val cal = Calendar.getInstance()
            val currentYear  = cal.get(Calendar.YEAR)
            val currentMonth = cal.get(Calendar.MONTH)

            fun dateMillis(year: Int, month: Int, day: Int): Long {
                val c = Calendar.getInstance()
                c.set(year, month, day, 12, 0, 0)
                c.set(Calendar.MILLISECOND, 0)
                return c.timeInMillis
            }

            // ── Toutes les dépenses de démo sont explicitement en MAD ──
            val sampleExpenses = listOf(
                // Mois courant (Mai 2026)
                Expense(amount = 1500.0, currency = "MAD", date = dateMillis(currentYear, currentMonth,  1), categoryId = ids[2], note = "Loyer"),
                Expense(amount =   85.0, currency = "MAD", date = dateMillis(currentYear, currentMonth,  2), categoryId = ids[0], note = "Courses Marjane"),
                Expense(amount =   45.0, currency = "MAD", date = dateMillis(currentYear, currentMonth,  3), categoryId = ids[1], note = "Bus mensuel"),
                Expense(amount =  120.0, currency = "MAD", date = dateMillis(currentYear, currentMonth,  5), categoryId = ids[3], note = "Pharmacie"),
                Expense(amount =   60.0, currency = "MAD", date = dateMillis(currentYear, currentMonth,  7), categoryId = ids[4], note = "Cinéma"),
                Expense(amount =  200.0, currency = "MAD", date = dateMillis(currentYear, currentMonth,  8), categoryId = ids[5], note = "Livres universitaires"),
                Expense(amount =   55.0, currency = "MAD", date = dateMillis(currentYear, currentMonth, 10), categoryId = ids[0], note = "Restaurant"),
                Expense(amount =   30.0, currency = "MAD", date = dateMillis(currentYear, currentMonth, 11), categoryId = ids[1], note = "Taxi"),
                Expense(amount =  150.0, currency = "MAD", date = dateMillis(currentYear, currentMonth, 12), categoryId = ids[6], note = "T-shirt + jean"),
                Expense(amount =   25.0, currency = "MAD", date = dateMillis(currentYear, currentMonth, 14), categoryId = ids[4], note = "Netflix"),
                Expense(amount =   70.0, currency = "MAD", date = dateMillis(currentYear, currentMonth, 15), categoryId = ids[0], note = "Supermarché"),
                Expense(amount =   40.0, currency = "MAD", date = dateMillis(currentYear, currentMonth, 16), categoryId = ids[3], note = "Consultation"),
                Expense(amount =   90.0, currency = "MAD", date = dateMillis(currentYear, currentMonth, 18), categoryId = ids[5], note = "Inscription cours"),
                Expense(amount =   35.0, currency = "MAD", date = dateMillis(currentYear, currentMonth, 20), categoryId = ids[1], note = "Essence"),
                Expense(amount =  110.0, currency = "MAD", date = dateMillis(currentYear, currentMonth, 22), categoryId = ids[0], note = "Courses hebdo"),
                Expense(amount =   80.0, currency = "MAD", date = dateMillis(currentYear, currentMonth, 24), categoryId = ids[4], note = "Sortie amis"),
                Expense(amount =   45.0, currency = "MAD", date = dateMillis(currentYear, currentMonth, 25), categoryId = ids[7], note = "Divers"),

                // Mois précédent (Avril 2026)
                Expense(amount = 1500.0, currency = "MAD", date = dateMillis(currentYear, currentMonth - 1,  1), categoryId = ids[2], note = "Loyer"),
                Expense(amount =   95.0, currency = "MAD", date = dateMillis(currentYear, currentMonth - 1,  3), categoryId = ids[0], note = "Courses"),
                Expense(amount =   45.0, currency = "MAD", date = dateMillis(currentYear, currentMonth - 1,  4), categoryId = ids[1], note = "Bus"),
                Expense(amount =  180.0, currency = "MAD", date = dateMillis(currentYear, currentMonth - 1,  6), categoryId = ids[3], note = "Dentiste"),
                Expense(amount =   75.0, currency = "MAD", date = dateMillis(currentYear, currentMonth - 1,  8), categoryId = ids[4], note = "Jeux"),
                Expense(amount =  250.0, currency = "MAD", date = dateMillis(currentYear, currentMonth - 1, 10), categoryId = ids[5], note = "Formation en ligne"),
                Expense(amount =   60.0, currency = "MAD", date = dateMillis(currentYear, currentMonth - 1, 12), categoryId = ids[0], note = "Restaurant"),
                Expense(amount =   50.0, currency = "MAD", date = dateMillis(currentYear, currentMonth - 1, 15), categoryId = ids[6], note = "Chaussures"),
                Expense(amount =   35.0, currency = "MAD", date = dateMillis(currentYear, currentMonth - 1, 17), categoryId = ids[1], note = "Taxi aéroport"),
                Expense(amount =  100.0, currency = "MAD", date = dateMillis(currentYear, currentMonth - 1, 19), categoryId = ids[0], note = "Supermarché"),
                Expense(amount =   25.0, currency = "MAD", date = dateMillis(currentYear, currentMonth - 1, 21), categoryId = ids[4], note = "Spotify"),
                Expense(amount =   65.0, currency = "MAD", date = dateMillis(currentYear, currentMonth - 1, 23), categoryId = ids[7], note = "Divers"),
                Expense(amount =   40.0, currency = "MAD", date = dateMillis(currentYear, currentMonth - 1, 25), categoryId = ids[3], note = "Médicaments"),
                Expense(amount =  120.0, currency = "MAD", date = dateMillis(currentYear, currentMonth - 1, 27), categoryId = ids[5], note = "Papeterie")
            )
            sampleExpenses.forEach { expenseDao.insertExpense(it) }
        }
    }
}
