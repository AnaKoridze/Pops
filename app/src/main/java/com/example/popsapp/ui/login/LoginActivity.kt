package com.example.popsapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.popsapp.R
import com.example.popsapp.data.Result
import com.example.popsapp.ui.orders.OrderListActivity
import com.example.popsapp.utils.SharedPrefHelper
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * If user is not logged, open [LoginActivity]
         */
        if (SharedPrefHelper.getSessionToken(this) != null) {
            startActivity(Intent(this, OrderListActivity::class.java))
            finish()
        }

        setContentView(R.layout.activity_login)

        //
        username.setText("techtesting-dev")
        password.setText("GFVSK-oNHB6")

        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            when (it) {
                is Result.Success -> {
                    loading.cancelAnimation()
                    loading.visibility = View.GONE

                    SharedPrefHelper.setSessionToken(this, it.data.data.sessionToken)

                    startActivity(Intent(this, OrderListActivity::class.java))
                    finish()
                }
                is Result.Progress -> {
                    loading.visibility = View.VISIBLE
                    loading.playAnimation()


                }
                is Result.Error -> {
                    loading.cancelAnimation()
                    loading.visibility = View.GONE

                    Toast.makeText(applicationContext, R.string.error_login, Toast.LENGTH_SHORT).show()
                }
            }
        })

        login.setOnClickListener {
            loginViewModel.login(username.text.toString(), password.text.toString())
        }
    }

    override fun onBackPressed() {
        finish()
    }
}