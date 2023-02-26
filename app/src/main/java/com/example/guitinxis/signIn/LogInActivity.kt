package com.example.guitinxis.signIn

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.guitinxis.MainActivity
import com.example.guitinxis.R
import com.example.guitinxis.registration.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class logInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var mail : EditText
    lateinit var password : EditText
    lateinit var connexion : Button
    lateinit var inscription : Button
    lateinit var viewModel : SignInViewModel
    lateinit var home : Intent
    lateinit var signUp: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        // Initialize Firebase Auth
        auth = Firebase.auth;
        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)


        signUp = Intent(this, RegisterActivity::class.java)
        home = Intent(this, MainActivity::class.java)

        mail = findViewById(R.id.editTextEmailAddressConnexion)
        password = findViewById(R.id.editTextPasswordConnexion)
        connexion = findViewById(R.id.buttonConnexion)
        inscription = findViewById(R.id.buttonSignUp)

        connexion.setOnClickListener {
            var currentMail : String = mail.text.toString()
            var currentPassword : String = password.text.toString()

            signIn(currentMail, currentPassword)
        }
        inscription.setOnClickListener {
            startActivity(signUp)
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload()
        }
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(baseContext, "Authentication successed", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)
                    startActivity(home)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        // [END sign_in_with_email]
    }


    private fun updateUI(user: FirebaseUser?) {

    }

    private fun reload() {

    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}