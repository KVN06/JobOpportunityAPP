package com.kvn.jobopportunityapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import com.kvn.jobopportunityapp.ui.screens.ServiceItem
import com.kvn.jobopportunityapp.ui.screens.ProductItem
import com.kvn.jobopportunityapp.ui.screens.VehicleItem
import com.kvn.jobopportunityapp.ui.screens.PropertyItem
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kvn.jobopportunityapp.ui.theme.*
import androidx.compose.runtime.Composable

@Composable
fun CategoryTabs(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf("Servicios", "Productos", "Vehículos", "Inmuebles")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        categories.forEach { category ->
            CategoryTab(
                text = category,
                isSelected = selectedCategory == category,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
fun CategoryTab(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        color = if (isSelected) Primary else SurfaceVariant,
        tonalElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = if (isSelected) TextPrimary else TextSecondary
            )
        }
    }
}

@Composable
fun ServicesGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val services = listOf(
            ServiceItem(title = "Desarrollo Web", icon = Icons.Default.Code, price = "$400.000-1.200.000"),
            ServiceItem(title = "Diseño Gráfico", icon = Icons.Default.Brush, price = "$150.000-600.000"),
            ServiceItem(title = "Marketing Digital", icon = Icons.Default.TrendingUp, price = "$250.000-800.000"),
            ServiceItem(title = "Traducción", icon = Icons.Default.Translate, price = "$50/palabra"),
            ServiceItem(title = "Fotografía", icon = Icons.Default.CameraAlt, price = "$120.000-400.000"),
            ServiceItem(title = "Consultoría", icon = Icons.Default.Business, price = "$40.000-120.000/hr"),
            ServiceItem(title = "Redacción", icon = Icons.Default.Edit, price = "$30/palabra"),
            ServiceItem(title = "Tutorías", icon = Icons.Default.School, price = "$15.000-40.000/hr")
        )
        items(services.size) { index ->
            ServiceCard(services[index])
        }
    }
}

@Composable
fun ProductsGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val products = listOf(
            ProductItem(title = "iPhone 14 Pro", price = "$2.800.000", details = "Excelente estado"),
            ProductItem(title = "MacBook Air M2", price = "$3.200.000", details = "Como nuevo"),
            ProductItem(title = "PlayStation 5", price = "$1.450.000", details = "Con garantía"),
            ProductItem(title = "Monitor 4K", price = "$750.000", details = "27 pulgadas"),
            ProductItem(title = "Teclado Mecánico", price = "$180.000", details = "RGB Gaming"),
            ProductItem(title = "Auriculares", price = "$350.000", details = "Noise Cancelling")
        )
        items(products.size) { index ->
            ProductCard(products[index])
        }
    }
}

@Composable
fun VehiclesGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val vehicles = listOf(
            VehicleItem(title = "Mazda 3", price = "$45.000.000", condition = "2022, 10.000km"),
            VehicleItem(title = "Toyota Hilux", price = "$120.000.000", condition = "2021, 20.000km"),
            VehicleItem(title = "Renault Duster", price = "$60.000.000", condition = "2020, 30.000km"),
            VehicleItem(title = "Kia Picanto", price = "$35.000.000", condition = "2019, 40.000km")
        )
        items(vehicles.size) { index ->
            VehicleCard(vehicles[index])
        }
    }
}

@Composable
fun PropertiesGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val properties = listOf(
            PropertyItem(title = "Apartamento Centro", price = "$350.000.000", details = "3 habitaciones, 2 baños"),
            PropertyItem(title = "Casa Campestre", price = "$800.000.000", details = "5 habitaciones, piscina"),
            PropertyItem(title = "Oficina Norte", price = "$600.000.000", details = "200m2, parqueadero"),
            PropertyItem(title = "Local Comercial", price = "$400.000.000", details = "100m2, zona transitada")
        )
        items(properties.size) { index ->
            PropertyCard(properties[index])
        }
    }
}

@Composable
fun ServiceCard(service: ServiceItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .clickable { /* Navegar a detalles del servicio */ },
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
                Icon(
                    imageVector = service.icon ?: Icons.Default.Info,
                    contentDescription = service.title,
                    tint = Primary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = service.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = service.price,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextPrimary
                )
        }
    }
}

@Composable
fun ProductCard(product: ProductItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .clickable { /* Navegar a detalles del producto */ },
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.price,
                style = MaterialTheme.typography.bodySmall,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.details ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun VehicleCard(vehicle: VehicleItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .clickable { /* Navegar a details */ },
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = vehicle.title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = vehicle.price,
                style = MaterialTheme.typography.bodySmall,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = vehicle.condition ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun PropertyCard(property: PropertyItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .clickable { /* Navegar a details */ },
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = property.title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = property.price,
                style = MaterialTheme.typography.bodySmall,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = property.details ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}
