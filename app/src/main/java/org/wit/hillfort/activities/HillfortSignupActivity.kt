package org.wit.hillfort.activities

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.helpers.sha256
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.UserModel

class HillfortSignupActivity: AppCompatActivity() {

  private var user = UserModel()
  lateinit var app : MainApp

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_signup)

    app = application as MainApp
    clearFields()
    input_name.requestFocus()

    btn_signup.setOnClickListener{
      signup()
    }
  }

  fun signup(){

    if (!valid()) return

    user.name = input_name.text.toString()
    user.email = input_email.text.toString()
    user.phone = input_mobile.text.toString()
    user.passwordHash = sha256(input_password.text.toString())
    app.users.create(user.copy())

    toast(R.string.signup_success)
    clearFields()
    setResult(Activity.RESULT_OK, null)
    finish()
  }

  private fun clearFields(){
    input_name.text.clear()
    input_email.text.clear()
    input_mobile.text.clear()
    input_password.text.clear()
    input_reEnterPassword.text.clear()
  }

  private fun valid(): Boolean {

    if (input_name.text.isEmpty()) {
      toast(R.string.signup_error_name)
      return false
    }

    if (input_email.text.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(input_email.text).matches()) {
      toast(R.string.signup_error_email)
      return false
    }

    if (input_mobile.text.isEmpty() || input_mobile.text.length < 10){
      toast(R.string.signup_error_mobile)
      return false
    }

    if (input_password.text.isEmpty() || input_password.text.length < 5){
      toast(R.string.signup_error_password)
      input_password.text.clear()
      input_reEnterPassword.text.clear()
      return false
    }

    if (input_reEnterPassword.text.toString() != input_password.text.toString()){
      toast(R.string.signup_error_repassword)
      input_password.text.clear()
      input_reEnterPassword.text.clear()
      return false
    }

    return true
  }
}