package org.wit.hillfort.helpers

import com.google.android.gms.common.util.Hex
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

fun sha256(input: String): String{
  var md = MessageDigest.getInstance("SHA-256")
  var hash = md.digest(input.toByteArray(StandardCharsets.UTF_8))
  return Hex.bytesToStringUppercase(hash)
}