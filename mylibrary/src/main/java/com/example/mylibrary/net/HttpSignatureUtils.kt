package com.example.mylibrary.net


import android.os.Build
import android.text.TextUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.EncryptUtils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.*
import kotlin.random.Random

object HttpSignatureUtils {

  val  JSON_MEDIA_TYPE="application/json;charset=UTF-8".toMediaType()
    /**
     * 未登录默认 F32DA18EF36EA7D908D59918B3423FA6
     */
    private var sessionKey = "F32DA18EF36EA7D908D59918B3423FA6"

    /**
     * 签名的秘钥
     */
    private val signkeys = arrayOf(
        "1001>>724E18F365F1123078413B515BE9D348",
        "1002>>3896AE7748DBB2C55A27B3C4D1560E1E",
        "1003>>4745CE309142087D451F5DF57D5EDF67"
    )


    fun sign(json: String, url: String): RequestBody {
        val signkeyArray = signkeys[Random.nextInt(signkeys.size)].split(">>")
        val jsonObjects = JSONObject(json)
        jsonObjects.put("cversion", AppUtils.getAppInfo().versionCode)
        jsonObjects.put("ctype", "2")
        jsonObjects.put("language", "en")
        jsonObjects.put("apptype", 21)
        jsonObjects.put("channel", 101)
        jsonObjects.put("device", Build.MODEL)
        jsonObjects.put("_timestamp", System.currentTimeMillis() / 1000)
        jsonObjects.put("_flsignid", signkeyArray[0])
        val userId =0L
        if (userId == 0L) {

        }
        var userSessionKey = ""
        if (userSessionKey==null||url.contains("/pub/")) {
            //白名单无sessionKey值时
            userSessionKey = sessionKey
        }
        val signKey = userSessionKey+signkeyArray[1]
        val stringBuilder =StringBuilder()
        for (key in jsonObjects.keys()) {
            val value = jsonObjects[key]
            if (!"_flsign".equals(key, ignoreCase = true) && !"_flsignid".equals(key, ignoreCase = true) && !TextUtils.isEmpty(value.toString())) {
                stringBuilder.append("&")
                stringBuilder.append(key)
                stringBuilder.append("=")
                stringBuilder.append(value)
            }
        }
        var signString = stringBuilder.toString()
        if (signString.indexOf("&") < 1) {
            signString = signString.substring(signString.indexOf("&") + 1, signString.length)
        }
        val flsign: String = EncryptUtils.encryptMD5ToString(signString + signKey).toLowerCase(
            Locale.ROOT
        )
        jsonObjects.put("_flsign",flsign)

        return jsonObjects.toString().toRequestBody(JSON_MEDIA_TYPE)
    }

}