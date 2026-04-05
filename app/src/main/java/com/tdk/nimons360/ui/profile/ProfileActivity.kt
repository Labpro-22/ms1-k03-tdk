package com.tdk.nimons360.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.tdk.nimons360.R
import com.tdk.nimons360.data.local.SessionManager
import com.tdk.nimons360.data.model.UpdateMeRequest
import com.tdk.nimons360.data.remote.RetrofitClient
import com.tdk.nimons360.ui.auth.LoginActivity
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private lateinit var tvAvatarInitial: TextView
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var btnEditAvatar: ImageView
    private lateinit var btnSignOut: TextView
    private lateinit var progressBar: ProgressBar

    private var currentName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        sessionManager = SessionManager(this)

        tvAvatarInitial = findViewById(R.id.tvAvatarInitial)
        tvName = findViewById(R.id.tvName)
        tvEmail = findViewById(R.id.tvEmail)
        btnEditAvatar = findViewById(R.id.btnEditAvatar)
        btnSignOut = findViewById(R.id.btnSignOut)
        progressBar = findViewById(R.id.progressBar)

        btnSignOut.setOnClickListener { signOut() }
        btnEditAvatar.setOnClickListener { showEditNameBottomSheet() }

        fetchProfile()
    }

    private fun fetchProfile() {
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val api = RetrofitClient.create(this@ProfileActivity)
                val response = api.getMe()

                if (response.isSuccessful && response.body() != null) {
                    val me = response.body()!!.data
                    currentName = me.fullName
                    displayProfile(me.fullName, me.email)
                } else if (response.code() in listOf(401, 403, 409)) {
                    handleUnauthorized()
                } else {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Failed to load profile",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("ProfileActivity", "Error fetching profile", e)
                Toast.makeText(
                    this@ProfileActivity,
                    "Failed to connect to server",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun displayProfile(fullName: String, email: String) {
        val initial = if (fullName.isNotBlank()) fullName.first().uppercaseChar().toString() else "?"
        tvAvatarInitial.text = initial
        tvName.text = fullName
        tvEmail.text = email
    }

    private fun showEditNameBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val sheetView = layoutInflater.inflate(R.layout.dialog_edit_name, null)
        bottomSheetDialog.setContentView(sheetView)

        val etDisplayName = sheetView.findViewById<TextInputEditText>(R.id.etDisplayName)
        val btnCancel = sheetView.findViewById<MaterialButton>(R.id.btnCancel)
        val btnSave = sheetView.findViewById<MaterialButton>(R.id.btnSave)

        etDisplayName.setText(currentName)
        etDisplayName.setSelection(currentName.length)

        btnCancel.setOnClickListener { bottomSheetDialog.dismiss() }

        btnSave.setOnClickListener {
            val newName = etDisplayName.text?.toString()?.trim().orEmpty()
            if (newName.isEmpty()) {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            btnSave.isEnabled = false
            btnCancel.isEnabled = false
            updateName(newName, bottomSheetDialog, btnSave, btnCancel)
        }

        bottomSheetDialog.show()
    }

    private fun updateName(
        newName: String,
        dialog: BottomSheetDialog,
        btnSave: MaterialButton,
        btnCancel: MaterialButton
    ) {
        lifecycleScope.launch {
            try {
                val api = RetrofitClient.create(this@ProfileActivity)
                val response = api.updateMe(UpdateMeRequest(fullName = newName))

                if (response.isSuccessful && response.body() != null) {
                    val me = response.body()!!.data
                    currentName = me.fullName
                    displayProfile(me.fullName, me.email)
                    dialog.dismiss()
                    Toast.makeText(
                        this@ProfileActivity,
                        "Name updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (response.code() in listOf(401, 403, 409)) {
                    dialog.dismiss()
                    handleUnauthorized()
                } else {
                    btnSave.isEnabled = true
                    btnCancel.isEnabled = true
                    Toast.makeText(
                        this@ProfileActivity,
                        "Failed to update name",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("ProfileActivity", "Error updating name", e)
                btnSave.isEnabled = true
                btnCancel.isEnabled = true
                Toast.makeText(
                    this@ProfileActivity,
                    "Failed to connect to server",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun signOut() {
        sessionManager.clearSession()
        startActivity(
            Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        )
        finish()
    }

    private fun handleUnauthorized() {
        sessionManager.clearSession()
        Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show()
        startActivity(
            Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        )
        finish()
    }
}
