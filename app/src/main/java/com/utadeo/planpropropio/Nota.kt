package com.utadeo.planpropropio

data class Nota(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val fechaRegistro: String = "",
    val fechaFinalizacion: String = "" ,
    val finalizado: Boolean = false
)
