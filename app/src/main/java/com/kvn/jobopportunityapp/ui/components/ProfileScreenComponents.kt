package com.kvn.jobopportunityapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ProfileHeaderStub(modifier: Modifier = Modifier) {}

@Composable
fun ProfileStatsStub(modifier: Modifier = Modifier) {}

@Composable
fun ProfileActionsStub(modifier: Modifier = Modifier) {}

@Composable
fun ProfileStatItemStub(
	title: String,
	value: String,
	modifier: Modifier = Modifier,
	icon: ImageVector? = null,
	color: Color = Color.Unspecified
) {}

@Composable
fun ProfileActionCardStub(
	title: String,
	modifier: Modifier = Modifier,
	icon: ImageVector? = null,
	color: Color = Color.Unspecified,
	onClick: () -> Unit = {}
) {}

@Composable
fun SettingsItemStub(
	title: String,
	modifier: Modifier = Modifier,
	subtitle: String? = null,
	icon: ImageVector? = null,
	onClick: () -> Unit = {},
	isDestructive: Boolean = false
) {}
