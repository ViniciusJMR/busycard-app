package dev.vinicius.busycardapp.data.repository.impl

import android.net.Uri
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dev.vinicius.busycardapp.domain.repository.Bucket
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BucketRepository @Inject constructor(
): Bucket {
    private val storage = Firebase.storage.reference

    companion object {
        val TAG = "BucketRepository"
    }

    override suspend fun uploadFile(uri: Uri, toPath: String): Uri {
        val ref = storage.child(toPath)
        val uploadTask = ref.putFile(uri)
        uploadTask.await()

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                Log.d(TAG, "Error when uploading file: ${task.exception}")
            }
            ref.downloadUrl
        }.await()
        return urlTask
    }


}