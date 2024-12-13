package com.capstone.free.education.data.remote.response


data class MateriResponse(
    val error: Boolean,
    val message: String,
    val data: List<MateriData>
)

data class MateriData(
    val id: Int,
    val kategori: String,
    val target: String,
    val link: String,
    val judul: String,
    val image: String
)
