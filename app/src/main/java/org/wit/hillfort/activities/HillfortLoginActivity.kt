package org.wit.hillfort.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.helpers.sha256
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.UserModel

class HillfortLoginActivity: AppCompatActivity() {

  lateinit var app: MainApp

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    app = application as MainApp

    btn_login.setOnClickListener{
      login()
    }

    link_signup.setOnClickListener{
      startActivityForResult(intentFor<HillfortSignupActivity>(),0)
    }
  }

  fun login(){
    val email = input_email.text.toString()
    val passwordHash = sha256(input_password.text.toString())
    var userFound: UserModel? = app.users.findAll().find{ u ->
      u.email == email && u.passwordHash == passwordHash
    }

    if (userFound == null) {
      toast(R.string.login_error)
      return
    }

    toast(R.string.login_success)
    clearFields()
    startActivityForResult(intentFor<HillfortListActivity>(), 0)
  }

  private fun clearFields(){
    input_email.text.clear()
    input_password.text.clear()
  }
}