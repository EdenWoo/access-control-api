package com.github.leon.aws.s3


import java.io.Serializable


class UploadData : Serializable {

    private val name: String? = null
    private val data: ByteArray? = null

    companion object {

        private const val serialVersionUID = -166575150661617870L
    }
}
