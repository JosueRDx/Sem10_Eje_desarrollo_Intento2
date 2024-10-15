package com.josuerdx.sem10_e_desarrollo_intento2.data

import com.google.gson.annotations.SerializedName

data class Producto(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("precio") val precio: String,
    @SerializedName("stock") val stock: Int,
    @SerializedName("pub_date") val pubDate: String,
    @SerializedName("categoria") val categoria: Int,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("imagen_url") val imagenUrl: String?
)