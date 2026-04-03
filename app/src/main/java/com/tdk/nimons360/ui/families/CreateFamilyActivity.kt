package com.tdk.nimons360.ui.families

import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import coil3.load
import coil3.request.crossfade
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.tdk.nimons360.R
import com.tdk.nimons360.data.local.SessionManager
import com.tdk.nimons360.data.model.CreateFamilyRequest
import com.tdk.nimons360.data.remote.RetrofitClient
import com.tdk.nimons360.utils.FamilyIcons
import kotlinx.coroutines.launch

class CreateFamilyActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private lateinit var btnCancel: MaterialButton
    private lateinit var btnCreate: MaterialButton
    private lateinit var ivSelectedIcon: ImageView
    private lateinit var tvUserInitial: TextView
    private lateinit var etFamilyName: TextInputEditText
    private lateinit var iconGrid: LinearLayout

    private val iconCells = mutableListOf<FrameLayout>()
    private var selectedIconIndex = 0
    private var isSubmitting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
        setContentView(R.layout.activity_create_family)

        btnCancel = findViewById(R.id.btnCancel)
        btnCreate = findViewById(R.id.btnCreate)
        ivSelectedIcon = findViewById(R.id.ivSelectedIcon)
        tvUserInitial = findViewById(R.id.tvUserInitial)
        etFamilyName = findViewById(R.id.etFamilyName)
        iconGrid = findViewById(R.id.iconGrid)

        btnCreate.isEnabled = false

        btnCancel.setOnClickListener { finish() }
        btnCreate.setOnClickListener { submitCreate() }

        etFamilyName.addTextChangedListener {
            val hasText = it?.toString()?.isNotBlank() == true
            btnCreate.isEnabled = hasText && !isSubmitting
            btnCreate.setTextColor(
                ContextCompat.getColor(
                    this,
                    if (hasText && !isSubmitting) R.color.blue_primary else R.color.text_secondary
                )
            )
        }

        buildIconGrid()
        loadUserInitial()
    }

    private fun buildIconGrid() {
        val icons = FamilyIcons.urls
        val rows = icons.chunked(5)

        rows.forEachIndexed { rowIdx, rowIcons ->
            val rowLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).also { it.bottomMargin = dp(8) }
            }

            rowIcons.forEachIndexed { colIdx, iconUrl ->
                val globalIdx = rowIdx * 5 + colIdx
                val cell = createIconCell(globalIdx, iconUrl)
                val params = LinearLayout.LayoutParams(0, dp(64)).apply {
                    weight = 1f
                    if (colIdx < rowIcons.size - 1) marginEnd = dp(8)
                }
                cell.layoutParams = params
                rowLayout.addView(cell)
                iconCells.add(cell)
            }

            // Fill remaining slots so spacing is consistent
            val remaining = 5 - rowIcons.size
            repeat(remaining) {
                val spacer = Space(this).apply {
                    layoutParams = LinearLayout.LayoutParams(0, dp(64)).apply { weight = 1f }
                }
                rowLayout.addView(spacer)
            }

            iconGrid.addView(rowLayout)
        }

        // Select the first icon by default
        updateSelection(0)
    }

    private fun createIconCell(index: Int, iconUrl: String): FrameLayout {
        val cell = FrameLayout(this).apply {
            background = ContextCompat.getDrawable(this@CreateFamilyActivity, R.drawable.bg_icon_cell)
            isClickable = true
            isFocusable = true
        }

        val imageView = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(dp(36), dp(36), Gravity.CENTER)
            scaleType = ImageView.ScaleType.FIT_CENTER
            contentDescription = "Family icon ${index + 1}"
            load(iconUrl) { crossfade(true) }
        }

        cell.addView(imageView)
        cell.setOnClickListener { updateSelection(index) }
        return cell
    }

    private fun updateSelection(newIndex: Int) {
        // Deselect previous
        if (selectedIconIndex < iconCells.size) {
            iconCells[selectedIconIndex].isSelected = false
        }

        selectedIconIndex = newIndex

        // Select new
        if (newIndex < iconCells.size) {
            iconCells[newIndex].isSelected = true
        }

        // Update preview
        ivSelectedIcon.load(FamilyIcons.urls[newIndex]) { crossfade(true) }
    }

    private fun loadUserInitial() {
        lifecycleScope.launch {
            try {
                val api = RetrofitClient.create(this@CreateFamilyActivity)
                val response = api.getMe()
                if (response.isSuccessful && response.body() != null) {
                    val fullName = response.body()!!.data.fullName
                    val initial = fullName.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "U"
                    tvUserInitial.text = initial
                }
            } catch (_: Exception) { }
        }
    }

    private fun submitCreate() {
        val name = etFamilyName.text?.toString()?.trim().orEmpty()
        if (name.isEmpty() || isSubmitting) return

        isSubmitting = true
        btnCreate.isEnabled = false
        btnCreate.text = "..."

        lifecycleScope.launch {
            try {
                val api = RetrofitClient.create(this@CreateFamilyActivity)
                val response = api.createFamily(
                    CreateFamilyRequest(
                        name = name,
                        iconUrl = FamilyIcons.urls[selectedIconIndex]
                    )
                )

                if (response.isSuccessful) {
                    Toast.makeText(this@CreateFamilyActivity, "Family created!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@CreateFamilyActivity, "Failed to create family", Toast.LENGTH_SHORT).show()
                    resetCreateButton()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CreateFamilyActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                resetCreateButton()
            }
        }
    }

    private fun resetCreateButton() {
        isSubmitting = false
        val hasText = etFamilyName.text?.isNotEmpty() == true
        btnCreate.text = "Create"
        btnCreate.isEnabled = hasText
        btnCreate.setTextColor(
            ContextCompat.getColor(this, if (hasText) R.color.blue_primary else R.color.text_secondary)
        )
    }

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()
}
