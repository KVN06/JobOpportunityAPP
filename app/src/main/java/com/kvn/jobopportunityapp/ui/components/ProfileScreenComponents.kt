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
	icon: ImageVector? = null,
	color: Color = Color.Unspecified,
	modifier: Modifier = Modifier
) {}

@Composable
fun ProfileActionCardStub(
	title: String,
	icon: ImageVector? = null,
	color: Color = Color.Unspecified,
	onClick: () -> Unit = {},
	modifier: Modifier = Modifier
) {}

@Composable
fun SettingsItemStub(
	title: String,
	subtitle: String? = null,
	icon: ImageVector? = null,
	onClick: () -> Unit = {},
	isDestructive: Boolean = false,
	modifier: Modifier = Modifier
) {}
