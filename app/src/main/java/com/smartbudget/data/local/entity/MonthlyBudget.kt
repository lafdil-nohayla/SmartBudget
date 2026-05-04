package com.smartbudget.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "monthly_budgets",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["categoryId"])]
)
data class MonthlyBudget(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val month: String,
    val categoryId: Long,
    val limitAmount: Double
)
