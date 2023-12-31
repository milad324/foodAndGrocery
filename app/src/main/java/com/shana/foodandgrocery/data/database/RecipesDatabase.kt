package com.shana.foodandgrocery.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shana.foodandgrocery.data.database.dao.ExtendedIngredientDao
import com.shana.foodandgrocery.data.database.dao.FavoriteRecipeDao
import com.shana.foodandgrocery.data.database.dao.PlannerDao
import com.shana.foodandgrocery.data.database.dao.RecipesDao
import com.shana.foodandgrocery.data.database.dao.ShoppingItemDao
import com.shana.foodandgrocery.data.database.entitis.ExtendedIngredientEntity
import com.shana.foodandgrocery.data.database.entitis.FavoriteRecipeEntity
import com.shana.foodandgrocery.data.database.entitis.PlannerEntity
import com.shana.foodandgrocery.data.database.entitis.RecipeExtendedIngredientCrossRefEntity
import com.shana.foodandgrocery.data.database.entitis.RecipesEntity
import com.shana.foodandgrocery.data.database.entitis.ShoppingItemEntity
import com.shana.foodandgrocery.util.Constants.Companion.DATABASE_NAME

@Database(
    entities = [RecipesEntity::class, FavoriteRecipeEntity::class, ExtendedIngredientEntity::class, RecipeExtendedIngredientCrossRefEntity::class, ShoppingItemEntity::class, PlannerEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ExtendedIngredientTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase() {
    abstract val recipesDao: RecipesDao
    abstract val extendedIngredientDao: ExtendedIngredientDao
    abstract val favoriteRecipeDao: FavoriteRecipeDao
    abstract val plannerDao: PlannerDao
    abstract val shoppingItemDao: ShoppingItemDao

    companion object {
        @Volatile
        var instance: RecipesDatabase? = null
        fun getInstance(context: Context): RecipesDatabase {
            synchronized(this) {
                return instance ?: Room.databaseBuilder(
                    context,
                    RecipesDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration().build().also {
                    instance = it
                }
            }
        }
    }
}