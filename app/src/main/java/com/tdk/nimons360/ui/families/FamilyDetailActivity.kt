package com.tdk.nimons360.ui.families

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class FamilyDetailActivity : ComponentActivity() {

    companion object {
        private const val EXTRA_FAMILY_ID = "extra_family_id"
        private const val EXTRA_FAMILY_NAME = "extra_family_name"
        private const val EXTRA_IS_MEMBER = "extra_is_member"

        fun newIntent(
            context: Context,
            familyId: Int,
            familyName: String,
            isMember: Boolean
        ): Intent {
            return Intent(context, FamilyDetailActivity::class.java).apply {
                putExtra(EXTRA_FAMILY_ID, familyId)
                putExtra(EXTRA_FAMILY_NAME, familyName)
                putExtra(EXTRA_IS_MEMBER, isMember)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val familyName = intent.getStringExtra(EXTRA_FAMILY_NAME) ?: "Family Detail"
        val isMember = intent.getBooleanExtra(EXTRA_IS_MEMBER, false)

        setContent {
            Scaffold { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(familyName, style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(16.dp))

                    if (isMember) {
                        Text("Dummy Family Detail (Joined)")
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(onClick = { finish() }) {
                            Text("Back")
                        }
                    } else {
                        Text("Dummy Family Detail (Not Joined)")
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { finish() }) {
                            Text("Join Family")
                        }
                    }
                }
            }
        }
    }
}