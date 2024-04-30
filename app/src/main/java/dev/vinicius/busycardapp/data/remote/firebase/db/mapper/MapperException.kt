package dev.vinicius.busycardapp.data.remote.firebase.db.mapper

data class MapperException(
    val msg: String
): RuntimeException(msg)