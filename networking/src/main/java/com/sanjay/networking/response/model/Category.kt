package com.sanjay.networking.response.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.sanjay.networking.persistence.converters.CustomTypeConverters
import kotlinx.android.parcel.Parcelize

/**
 * JSON representation of a category response
 */

@Entity(tableName = "category")
@TypeConverters(CustomTypeConverters::class)
@Parcelize
data class Category(
        @PrimaryKey
        @SerializedName("id")
        var id: Long,

        @SerializedName("name")
        var name: String?,

        @SerializedName("description")
        var description: String?
) : Parcelable {

}