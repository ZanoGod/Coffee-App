package com.zano.mistcafe.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FilterChipDefaults.filterChipBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zano.mistcafe.ui.theme.MistBeige
import com.zano.mistcafe.ui.theme.MistNavy

@Composable
fun CategoryTabs(
    categories: List<String>,
    selected: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(categories) { category ->
            val isSelected = category == selected

            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                colors = FilterChipDefaults.filterChipColors(
                    // --- SELECTED STATE ---
                    // Light Mode: Navy Background, White Text
                    // Dark Mode: Beige Background, Navy Text (High Contrast)
                    selectedContainerColor = if (isSystemInDarkTheme()) MistBeige else MistNavy,
                    selectedLabelColor = if (isSystemInDarkTheme()) MistNavy else Color.White,

                    // --- UNSELECTED STATE ---
                    containerColor = Color.Transparent,
                    labelColor = MaterialTheme.colorScheme.onSurface // Adapts to White in Dark Mode
                ),
                border = filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    // [FIX] Border Color:
                    // If Unselected in Dark Mode -> Use Light Border (onSurface) so it's visible on Navy bg
                    // If Unselected in Light Mode -> Use Navy Border
                    borderColor = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(24.dp)
            )
        }
    }
}

// Helper to detect theme inside Composable (or you can pass isDarkTheme param)
@Composable
fun isSystemInDarkTheme(): Boolean {
    return androidx.compose.foundation.isSystemInDarkTheme()
}