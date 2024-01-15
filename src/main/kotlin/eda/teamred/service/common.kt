package eda.teamred.service

fun Int.toByteArray() = byteArrayOf(
    this.toByte(),
    (this ushr 8).toByte(),
    (this ushr 16).toByte(),
    (this ushr 24).toByte()
)