package com.josuerdx.sem10_e_desarrollo_intento2.data

import com.google.gson.annotations.SerializedName

data class Categoria(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("pub_date") val pubDate: String? = null,
    @SerializedName("imagen") val imagen: String? = null,
    @SerializedName("imagen_url") val imagenUrl: String? = null
)
