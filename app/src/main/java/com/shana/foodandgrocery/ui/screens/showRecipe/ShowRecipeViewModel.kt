package com.shana.foodandgrocery.ui.screens.showRecipe

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.shana.foodandgrocery.data.Repository
import com.shana.foodandgrocery.data.database.entitis.FavoriteRecipeEntity
import com.shana.foodandgrocery.data.database.entitis.PlannerEntity
import com.shana.foodandgrocery.data.mappers.toShoppingList
import com.shana.foodandgrocery.models.ExtendedIngredient
import com.shana.foodandgrocery.util.Constants.Companion.RECIPE_ID_SAVED_STATE_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShowRecipeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, private val repository: Repository
) : ViewModel() {
    val mealType =
        arrayOf("breakfast", "brunch", "elevenses", "lunch", "tea", "supper", "dinner")
    val tabData = listOf("OVERVIEW", "INGREDIENTS", "INSTRUCTIONS")
    private var recipeId: Long? = savedStateHandle[RECIPE_ID_SAVED_STATE_KEY]
    var recipe = repository.local.getRecipeById(recipeId ?: 1L).asLiveData()
    var isFavorite = repository.local.checkRecipeIsFavorite(recipeId ?: 1).asLiveData()
    val selectedIngredients = mutableStateListOf<ExtendedIngredient>()
    fun handleSelectIngredient(extendedIngredient: ExtendedIngredient) {
        if (selectedIngredients.contains(extendedIngredient)) {
            selectedIngredients.remove(extendedIngredient)
        } else {
            selectedIngredients.add(extendedIngredient)
        }
    }

    fun handleRemoveIngredient(extendedIngredient: ExtendedIngredient) {
        selectedIngredients.remove(extendedIngredient)
    }

    fun handleFavoriteRecipe(favoriteRecipeEntity: FavoriteRecipeEntity) {
        viewModelScope.launch(Dispatchers.IO)  {
            if (isFavorite.value == true) {
                repository.local.deleteFavoriteRecipe(favoriteRecipeEntity)
            } else {
                repository.local.insertFavoriteRecipes(favoriteRecipeEntity)
            }
        }
    }

    fun addToShopping() {
        viewModelScope.launch(Dispatchers.IO) {
            recipe.value?.let {
                repository.local.upsertShoppingList(selectedIngredients.map { it.toShoppingList(recipe.value!!) })
                selectedIngredients.removeRange(0, selectedIngredients.size)
            }

        }

    }


    fun addToPlanner(cookDate:Long,mealType:String){
        viewModelScope.launch {
            recipe.value?.let {
                repository.local.insertPlanner(PlannerEntity(id=0, recipeName = recipe.value!!.title, recipeId = recipe.value!!.recipeId, mealType = mealType, cookDate = cookDate, img = recipe.value!!.image))
            }

        }
    }
}


