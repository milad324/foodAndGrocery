package com.shana.foodandgrocery.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ExtendedIngredient(
    val amount: Double?,
    val consistency: String?,
    val image: String?,
    val name: String?,
    val original: String?,
    val unit: String?,
    val id: Long,
    val aisle: String?,
) : Parcelable