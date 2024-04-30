package dev.vinicius.busycardapp.domain.repository

import android.net.Uri

interface Bucket {
    suspend fun uploadFile(uri: Uri, toPath: String): Uri
}