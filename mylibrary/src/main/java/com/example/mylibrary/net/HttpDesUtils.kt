package com.example.mylibrary.net
import android.util.Base64
import com.blankj.utilcode.util.EncryptUtils
import java.nio.charset.Charset

object HttpDesUtils {


    fun decrypt(data: String): String {
        if (data.isEmpty()) {
            return data
        }
        val decryptDES = EncryptUtils.decryptDES(
            Base64.decode(data, Base64.DEFAULT),
            "37ad9f8e".toByteArray(Charset.defaultCharset()),
            "DES",
            ByteArray(
                0
            )
        ) ?: return " {\"msg\":\"Decryption failed\",\"result\":-1000}"
        return String(decryptDES)
    }


}