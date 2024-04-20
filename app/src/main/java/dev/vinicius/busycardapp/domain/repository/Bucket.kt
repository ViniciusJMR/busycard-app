package dev.vinicius.busycardapp.domain.repository

import android.net.Uri
import java.io.File

interface Bucket {
    suspend fun uploadFile(uri: Uri, toPath: String): Uri
}