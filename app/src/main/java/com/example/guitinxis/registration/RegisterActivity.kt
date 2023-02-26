package com.example.guitinxis.registration

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.guitinxis.MainActivity
import com.example.guitinxis.R
import com.example.guitinxis.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    lateinit var viewModel : RegistrationViewModele
    private lateinit var homeBouton : Button
    private lateinit var registerBouton : Button
    private lateinit var Email: EditText
    private lateinit var Pwd: EditText
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        viewModel = ViewModelProvider(this).get(RegistrationViewModele::class.java)
        auth = Firebase.auth

        val intent: Intent = Intent(this, MainActivity::class.java)

        val binding : ActivityRegisterBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_register
        )
        binding.viewModel = this.viewModel
        binding.lifecycleOwner = this

        homeBouton = findViewById(R.id.buttonHome)
        registerBouton = findViewById(R.id.buttonRegister)
        Email = findViewById(R.id.editTextTextEmailAddress2)
        Pwd = findViewById(R.id.editTextTextPassword)

        homeBouton.setOnClickListener {
            startActivity(intent)
        }

        registerBouton.setOnClickListener{
            var currentEmail: String = Email.text.toString()
            var currentPwd: String = Pwd.text.toString()

            createAccount(currentEmail, currentPwd)
        }
    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(baseContext, "Creation successed.", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        // [END create_user_with_email]
    }

    private fun updateUI(user: FirebaseUser?) {

    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}