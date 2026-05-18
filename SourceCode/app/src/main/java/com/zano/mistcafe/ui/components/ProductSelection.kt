package com.zano.mistcafe.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zano.mistcafe.db.Product
import com.zano.mistcafe.ui.theme.myFonts

@Composable
fun ProductSection(
    title: String,
    products: List<Product>,
    addingProductId: Int?,
//    favoriteIds: Set<Int>,
    onAddToCart: (Product) -> Unit,
    onFavoriteClick: (Product) -> Unit
) {

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontFamily = myFonts
            )
//            Text(
//                text = "See all",
//                style = MaterialTheme.typography.labelLarge,
//                color = MaterialTheme.colorScheme.primary,
//                modifier = Modifier.clickable { }
//            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(products) { product ->
                ProductCard(
                    product = product,
                    isAdding = addingProductId == product.id,
//                    isFavorite = favoriteIds.contains(product.id),
                    onAddToCart = { onAddToCart(product) },
                    onFavoriteClick = { onFavoriteClick(product) }
                )

            }
        }
    }
}
