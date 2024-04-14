package dev.vinicius.busycardapp.data.remote.firebase.model

import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class FirebaseCardModel(
    var id: String? = null,
    var name: String? = null,
    var owner: String? = null,
    var fields: List<Map<String, Any>>? = null,
) {
}
