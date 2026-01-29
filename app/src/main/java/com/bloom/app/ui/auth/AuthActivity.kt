package com.bloom.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bloom.app.BloomApplication
import com.bloom.app.data.model.LoginRequest
import com.bloom.app.data.model.RegisterRequest
import com.bloom.app.data.remote.ApiClient
import com.bloom.app.databinding.ActivityAuthBinding
import com.bloom.app.ui.main.MainActivity
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAuthBinding
    private var isLoginMode = true
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
    }
    
    private fun setupUI() {
        binding.btnSubmit.setOnClickListener {
            if (isLoginMode) {
                performLogin()
            } else {
                performRegister()
            }
        }
        
        binding.tvToggleMode.setOnClickListener {
            toggleMode()
        }
    }
    
    private fun toggleMode() {
        isLoginMode = !isLoginMode
        
        if (isLoginMode) {
            binding.etName.visibility = android.view.View.GONE
            binding.etPhone.visibility = android.view.View.GONE
            binding.btnSubmit.text = "Login"
            binding.tvToggleMode.text = "Don't have an account? Register"
        } else {
            binding.etName.visibility = android.view.View.VISIBLE
            binding.etPhone.visibility = android.view.View.VISIBLE
            binding.btnSubmit.text = "Register"
            binding.tvToggleMode.text = "Already have an account? Login"
        }
    }
    
    private fun performLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        
        binding.btnSubmit.isEnabled = false
        
        lifecycleScope.launch {
            try {
                val response = ApiClient.getApiService().login(LoginRequest(email, password))
                
                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!
                    
                    // Save token and user
                    val prefsManager = BloomApplication.instance.preferencesManager
                    prefsManager.saveToken(authResponse.token)
                    prefsManager.saveUser(authResponse.user)
                    
                    // Navigate to main
                    startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@AuthActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AuthActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.btnSubmit.isEnabled = true
            }
        }
    }
    
    private fun performRegister() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }
        
        binding.btnSubmit.isEnabled = false
        
        lifecycleScope.launch {
            try {
                val response = ApiClient.getApiService().register(
                    RegisterRequest(name, email, password, phone.ifEmpty { null })
                )
                
                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!
                    
                    // Save token and user
                    val prefsManager = BloomApplication.instance.preferencesManager
                    prefsManager.saveToken(authResponse.token)
                    prefsManager.saveUser(authResponse.user)
                    
                    // Navigate to main
                    startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@AuthActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AuthActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.btnSubmit.isEnabled = true
            }
        }
    }
}