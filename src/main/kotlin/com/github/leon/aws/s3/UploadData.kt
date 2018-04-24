package com.github.leon.aws.s3


import java.io.Serializable


data class UploadData(
        val name: String? = null,
        val data: ByteArray? = null

) : Serializable {


    companion object {

        private const val serialVersionUID = -166575150661617870L
    }
}
