@file:OptIn(ExperimentalFoundationApi::class)

package com.shana.foodandgrocery.ui.screens.showRecipe

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.shana.foodandgrocery.R
import com.shana.foodandgrocery.ui.components.recipe.InstructionView
import com.shana.foodandgrocery.ui.components.recipe.FoodRecipeOverview
import com.shana.foodandgrocery.ui.components.recipe.IngredientItemView
import com.shana.foodandgrocery.ui.components.recipe.selectedShoppingItem
import com.shana.foodandgrocery.viewModels.FoodRecipeViewModel
import kotlinx.coroutines.launch


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun ShowRecipe(recipeViewModel: FoodRecipeViewModel = hiltViewModel()) {
    var recipe = recipeViewModel.recipe.observeAsState().value
    val tabData = listOf("OVERVIEW", "INGREDIENTS", "INSTRUCTIONS")
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0,
    )

    val scaffoldState = rememberBottomSheetScaffoldState()
    if (recipe != null)
        Scaffold() { contentPadding ->
            Column(Modifier.padding(contentPadding)) {
                TabRow(
                    selectedTabIndex = (pagerState.currentPage),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp), containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    tabData.forEachIndexed { index, data ->
                        val selected = pagerState.currentPage == index
                        Tab(
                            selected = selected,
                            onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                            modifier = Modifier,
                            enabled = true,
                            interactionSource = MutableInteractionSource(),
                            selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSecondary,
                            content = {
                                Text(
                                    text = data, fontWeight = if (selected) {
                                        FontWeight.Bold
                                    } else {
                                        FontWeight.Normal
                                    }
                                )
                            }
                        )
                    }
                }
                HorizontalPager(state = pagerState, count = tabData.size) { page ->
                    when (page) {
                        0 -> {
                            FoodRecipeOverview()
                        }

                        1 -> {

                            BottomSheetScaffold(
                                scaffoldState = scaffoldState,
                                sheetPeekHeight = BottomSheetScaffoldDefaults.SheetPeekHeight,
                                sheetContent = {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(BottomSheetScaffoldDefaults.SheetPeekHeight),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            if (recipeViewModel.selectedIngredients.size == 0) {
                                                Text(
                                                    text = "select Ingredient to add shopping list ",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    textAlign = TextAlign.Center
                                                )
                                            } else {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_add_shopping),
                                                    contentDescription = stringResource(R.string.search)
                                                )
                                                Text(text = "${recipeViewModel.selectedIngredients.size} Item(s) selected")
                                            }

                                        }
                                        LazyColumn() {
                                            recipeViewModel.selectedIngredients.forEach { item ->
                                                item {
                                                    selectedShoppingItem(
                                                        ingredient = item,
                                                        handleRemove = {
                                                            recipeViewModel.handleRemoveIngredient(
                                                                it
                                                            )
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }

                                }) { innerPadding ->
                                Box(Modifier.padding(innerPadding)) {
                                    LazyColumn() {
                                        recipe.extendedIngredients.forEach { item ->
                                            item {
                                                IngredientItemView(
                                                    ingredient = item,
                                                    recipeViewModel.selectedIngredients.contains(
                                                        item
                                                    ),
                                                    handleSelect = {
                                                        recipeViewModel.handleSelectIngredient(it)
                                                    })
                                            }
                                        }
                                    }
                                }

                            }

                        }

                        2 -> {
                            InstructionView()
                        }
                    }

                }
            }
        }


}


