package com.example.guitinxis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.widget.Button
import com.example.guitinxis.signIn.logInActivity

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    lateinit var connexion : Button
    lateinit var signIn: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialize Firebase Auth
        auth = Firebase.auth;

        signIn = Intent(this, logInActivity::class.java)

        connexion = findViewById(R.id.buttonLogIn)

        connexion.setOnClickListener() {
            startActivity(signIn)
        }
    }
}