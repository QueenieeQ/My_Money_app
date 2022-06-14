package com.queenieeq.mymoney.ui.signin


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.queenieeq.mymoney.R
import com.queenieeq.mymoney.ui.main.MainActivity

class MainActivity : AppCompatActivity() {
    private var signin_btn: Button? = null
    private var email_txt: EditText? = null
    private var pass_txt: EditText? = null
    private var firebaseAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        signin_btn = findViewById(R.id.sign_in)
        email_txt = findViewById(R.id.email_txt)
        pass_txt = findViewById(R.id.pass_txt)

        firebaseAuth = FirebaseAuth.getInstance()
        val reset: TextView = findViewById(R.id.reset)
        reset.setOnClickListener(View.OnClickListener {
            val signin_user = email_txt?.text.toString().trim()
            if (TextUtils.isEmpty(signin_user)) {
                Toast.makeText(applicationContext, "Enter Mail to reset Password", Toast.LENGTH_SHORT).show()
            }
            else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email_txt?.text.toString().trim()).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext, "Mail sent", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })

        val btn_click_me = findViewById(R.id.create_user) as Button

        btn_click_me.setOnClickListener {
            val intent = Intent(this@MainActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
    fun sigin_user(view: View) {
        val signin_user = email_txt?.text.toString().trim()
        val pass_user = pass_txt?.text.toString().trim()

        if (TextUtils.isEmpty(signin_user) || TextUtils.isEmpty(pass_user)) {
            Toast.makeText(applicationContext, "This field cannot be empty", Toast.LENGTH_SHORT)
                    .show()
        } else {
            firebaseAuth?.signInWithEmailAndPassword(signin_user, pass_user)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                    applicationContext,
                                    "You are logged",
                                    Toast.LENGTH_SHORT
                            )
                                    .show()
                            val intent = Intent(this@MainActivity, SignInActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                    applicationContext,
                                    "Login failed",
                                    Toast.LENGTH_SHORT
                            )
                                    .show()
                        }
                    }
        }
    }

}