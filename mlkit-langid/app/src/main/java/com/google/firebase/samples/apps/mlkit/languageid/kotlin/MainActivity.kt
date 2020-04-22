package com.google.firebase.samples.apps.mlkit.languageid.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.samples.apps.mlkit.languageid.R
import com.google.firebase.samples.apps.mlkit.languageid.databinding.ActivityMainBinding
import java.util.ArrayList
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            buttonIdLanguage.setOnClickListener {
                val input = inputText.text?.toString()
                input?.let {
                    inputText.text?.clear()
                    identifyLanguage(it)
                }
            }

            buttonIdAll.setOnClickListener {
                val input = inputText.text?.toString()
                input?.let {
                    inputText.text?.clear()
                    identifyPossibleLanguages(input)
                }
            }
        }
    }

    private fun identifyPossibleLanguages(inputText: String) {
        val languageIdentification = FirebaseNaturalLanguage
            .getInstance().languageIdentification
        languageIdentification
            .identifyPossibleLanguages(inputText)
            .addOnSuccessListener(this@MainActivity) { identifiedLanguages ->
                val detectedLanguages = ArrayList<String>(identifiedLanguages.size)
                for (language in identifiedLanguages) {
                    detectedLanguages.add(
                        String.format(
                            Locale.US,
                            "%s (%3f)",
                            language.languageCode,
                            language.confidence
                        )
                    )
                }
                binding.outputText.append(
                    String.format(
                        Locale.US,
                        "\n%s - [%s]",
                        inputText,
                        TextUtils.join(", ", detectedLanguages)
                    )
                )
            }
            .addOnFailureListener(this@MainActivity) { e ->
                Log.e(TAG, "Language identification error", e)
                Toast.makeText(
                    this@MainActivity, R.string.language_id_error,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun identifyLanguage(inputText: String) {
        val languageIdentification = FirebaseNaturalLanguage
            .getInstance().languageIdentification
        languageIdentification
            .identifyLanguage(inputText)
            .addOnSuccessListener(this@MainActivity) { s ->
                binding.outputText.append(
                    String.format(
                        Locale.US,
                        "\n%s - %s",
                        inputText,
                        s
                    )
                )
            }
            .addOnFailureListener(this@MainActivity) { e ->
                Log.e(TAG, "Language identification error", e)
                Toast.makeText(
                    this@MainActivity, R.string.language_id_error,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
