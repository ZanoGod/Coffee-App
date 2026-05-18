package com.zano.mistcafe.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.zano.mistcafe.db.Product
import com.zano.mistcafe.network.ApiConfig
import com.zano.mistcafe.ui.theme.MistNavy
import com.zano.mistcafe.ui.theme.myFonts

@Composable
fun ProductCard(
    product: Product,
    isAdding: Boolean,
    onAddToCart: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    val imageUrl = ApiConfig.IMAGE_BASE_URL + product.imageUrl
    val isFavorite = product.isFavorite

    val scale by animateFloatAsState(
        targetValue = if (isFavorite) 1.2f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "heart"
    )

    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(6.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            // Use Surface so it's White in Light mode / Darker in Dark mode
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            // --- Image Section ---
            Box(
                modifier = Modifier.height(140.dp).fillMaxWidth()
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(36.dp)
                        .shadow(elevation = 4.dp, shape = CircleShape)
                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                        .graphicsLayer { scaleX = scale; scaleY = scale }
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color(0xFFFF5252) else Color.Gray,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // --- Details Section ---
            Column(modifier = Modifier.padding(14.dp).fillMaxWidth()) {
                Text(
                    text = product.name,
                    fontFamily = myFonts,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(4.dp))
                RatingStars(rating = product.rating)
                Spacer(modifier = Modifier.height(12.dp))

                // Price (Use Branding Color)
                Text(
                    text = "$ ${product.price}",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = myFonts,
                    color = MistNavy
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Add to Cart (Brand Color)
                Surface(
                    onClick = onAddToCart,
                    enabled = !isAdding,
                    shape = RoundedCornerShape(12.dp),
                    color = MistNavy, // Brand Color
                    modifier = Modifier.fillMaxWidth().height(44.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (isAdding) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp, modifier = Modifier.size(18.dp), color = Color.White
                            )
                        } else {
                            Text(text = "Add to Cart", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = myFonts)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(imageVector = Icons.Default.AddShoppingCart, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RatingStars(
    rating: Double,
    modifier: Modifier = Modifier,
    starSize: Int = 14
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        val fullStars = rating.toInt()
        val hasHalfStar = rating - fullStars >= 0.5
        repeat(fullStars) { Icon(Icons.Filled.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(starSize.dp)) }
        if (hasHalfStar) { Icon(Icons.AutoMirrored.Filled.StarHalf, null, tint = Color(0xFFFFC107), modifier = Modifier.size(starSize.dp)) }
        repeat(5 - fullStars - if (hasHalfStar) 1 else 0) { Icon(Icons.Outlined.StarBorder, null, tint = Color.LightGray, modifier = Modifier.size(starSize.dp)) }
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = rating.toString(), fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
    }
}