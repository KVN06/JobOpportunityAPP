package com.kvn.jobopportunityapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

import com.kvn.jobopportunityapp.ui.screens.CourseItem

@Composable
fun TrainingCategoryTabs(selectedCategory: Int, onCategorySelected: (Int) -> Unit) {}



@Composable
fun CourseCard(course: CourseItem, modifier: Modifier = Modifier) {}

@Composable
fun CourseList(courses: List<CourseItem>, modifier: Modifier = Modifier) {}

@Composable
fun CourseItemCard(course: CourseItem, modifier: Modifier = Modifier) {}

@Composable
fun InstructorCard(instructor: String, modifier: Modifier = Modifier) {}

@Composable
fun AddCourseDialog(onDismiss: () -> Unit = {}, onAdd: (CourseItem) -> Unit = {}) {}

@Composable
fun CourseInfoChip(icon: ImageVector, text: String, modifier: Modifier = Modifier) {}
