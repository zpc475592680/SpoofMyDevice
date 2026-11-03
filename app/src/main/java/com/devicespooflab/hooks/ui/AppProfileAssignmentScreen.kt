package com.devicespooflab.hooks.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.progressSemantics
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.devicespooflab.hooks.R
import com.devicespooflab.hooks.data.AppProfileStore

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
fun AppProfileAssignmentLoadingScreen(onBack: () -> Unit) {
    AssignmentScaffold(
        title = stringResource(R.string.app_profile_assignment_title),
        onBack = onBack,
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LoadingIndicator(modifier = Modifier.progressSemantics())
                Spacer(Modifier.height(MdSpacing.sm))
                Text(
                    text = stringResource(R.string.app_profile_loading_profiles),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
fun AppProfileAssignmentScreen(
    label: String,
    packageName: String,
    icon: Bitmap,
    profiles: List<AppProfileStore.Profile>,
    assignedProfileId: String?,
    onProfileSelected: (String?) -> Unit,
    onCreateProfile: () -> Unit,
    onEditProfile: (String) -> Unit,
    onClearAppData: () -> Unit,
    onForceStopApp: () -> Unit,
    onOpenAppSettings: () -> Unit,
    dataActionMessage: String?,
    onBack: () -> Unit,
) {
    AssignmentScaffold(
        title = stringResource(R.string.app_profile_assignment_title),
        onBack = onBack,
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 20.dp),
        ) {
            item {
                ListItem(
                    supportingContent = { Text(packageName) },
                    leadingContent = {
                        Image(
                            bitmap = icon.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(MaterialTheme.shapes.medium),
                        )
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                ) {
                    Text(label)
                }
                SectionTitle(stringResource(R.string.app_profile_select))
            }
            item {
                SegmentedListItem(
                    onClick = { onProfileSelected(null) },
                    shapes = connectedListItemShapes(0, profiles.size + 1),
                    modifier = Modifier.padding(bottom = ListItemDefaults.SegmentedGap),
                    leadingContent = {
                        Icon(Icons.Outlined.Block, contentDescription = null)
                    },
                    supportingContent = {
                        Text(stringResource(R.string.app_profile_none_summary))
                    },
                    trailingContent = {
                        RadioButton(
                            selected = assignedProfileId == null,
                            onClick = null,
                        )
                    },
                    colors = ListItemDefaults.segmentedColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        supportingContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                ) {
                    Text(stringResource(R.string.app_profile_none))
                }
            }
            itemsIndexed(
                items = profiles,
                key = { _, profile -> profile.id },
            ) { index, profile ->
                SegmentedListItem(
                    onClick = { onProfileSelected(profile.id) },
                    shapes = connectedListItemShapes(index + 1, profiles.size + 1),
                    modifier = if (index < profiles.lastIndex) {
                        Modifier.padding(bottom = ListItemDefaults.SegmentedGap)
                    } else {
                        Modifier
                    },
                    leadingContent = {
                        Icon(Icons.Outlined.AccountBox, contentDescription = null)
                    },
                    trailingContent = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { onEditProfile(profile.id) }) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = stringResource(R.string.profile_edit_title),
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                            RadioButton(
                                selected = profile.id == assignedProfileId,
                                onClick = null,
                            )
                        }
                    },
                    colors = ListItemDefaults.segmentedColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                ) {
                    Text(profile.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            item {
                FilledTonalButton(
                    onClick = onCreateProfile,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MdSpacing.sm),
                ) {
                    Icon(Icons.Outlined.Add, contentDescription = null)
                    Spacer(Modifier.size(MdSpacing.xs))
                    Text(stringResource(R.string.app_profile_create))
                }
            }
            item {
                SectionTitle(stringResource(R.string.app_data_title))
                Text(
                    text = stringResource(R.string.app_data_summary, label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(MdSpacing.xs))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    FilledTonalButton(
                        onClick = onClearAppData,
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(Icons.Outlined.DeleteSweep, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text(stringResource(R.string.app_data_clear))
                    }
                    FilledTonalButton(
                        onClick = onForceStopApp,
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(Icons.Filled.Cancel, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text(stringResource(R.string.app_data_force_stop))
                    }
                }
                Spacer(Modifier.height(8.dp))
                FilledTonalButton(
                    onClick = onOpenAppSettings,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(Icons.Outlined.Info, contentDescription = null)
                    Spacer(Modifier.width(MdSpacing.xs))
                    Text(stringResource(R.string.app_data_open_settings))
                }
                if (!dataActionMessage.isNullOrBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = dataActionMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Text(
                    text = stringResource(R.string.app_data_need_root),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
            item { Spacer(Modifier.height(MdSpacing.xl)) }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
private fun AssignmentScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable (androidx.compose.foundation.layout.PaddingValues) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeFlexibleTopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        content = content,
    )
}
