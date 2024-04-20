package dev.vinicius.busycardapp.data.remote.firebase.db.model

import android.net.Uri
import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class FirebaseCardModel(
    var id: String? = null,
    var name: String? = null,
    var owner: String? = null,
    var image: String? = null,
    var mainContact: String? = null,
)
