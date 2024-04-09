package dev.vinicius.busycardapp.data.remote.firebase.mapper

data class MapperException(
    val msg: String
): RuntimeException(msg){
}