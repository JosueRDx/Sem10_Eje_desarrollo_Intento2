package com.josuerdx.sem10_e_desarrollo_intento2.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.josuerdx.sem10_e_desarrollo_intento2.data.Producto
import com.josuerdx.sem10_e_desarrollo_intento2.data.ProductoApiService
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoEditarScreen(navController: NavController, apiService: ProductoApiService, id: Int) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var pubDate by remember { mutableStateOf("") } // Variable para manejar la fecha
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val response = apiService.getProducto(id)
            val producto = response.body()
            if (producto != null) {
                nombre = producto.nombre
                precio = producto.precio
                stock = producto.stock.toString()
                categoria = producto.categoria.toString()

                // Conversión de LocalDateTime a OffsetDateTime con formato adecuado
                val localDateTime = LocalDateTime.now()  // o producto.pubDate convertido, si está disponible
                val offsetDateTime = localDateTime.atOffset(ZoneOffset.UTC)
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
                pubDate = offsetDateTime.format(formatter)
            }
        }
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Editar Producto",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6200EE)
                )
            }

            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stock") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoría") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Muestra el campo de pubDate formateado si es necesario
            if (pubDate.isNotEmpty()) {
                Text(
                    text = "Fecha de Publicación: $pubDate",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        val producto = Producto(
                            id = id,
                            nombre = nombre,
                            precio = precio,
                            stock = stock.toIntOrNull() ?: 0,
                            pubDate = pubDate,
                            categoria = categoria.toIntOrNull() ?: 0,
                            imagen = null,
                            imagenUrl = null
                        )
                        val response = apiService.updateProducto(id, producto)
                        if (response.isSuccessful) {
                            navController.navigate("productos")
                        } else {
                            // Manejo de error en la respuesta
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Actualizar Producto", color = Color.White)
            }
        }
    }
}
