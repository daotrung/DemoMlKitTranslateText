package com.daotrung.demomlkittranslatetext

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.*
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.mlkit.nl.translate.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var btnTranslate : Button
    lateinit var edtInput : EditText
    lateinit var tvResult : TextView
    lateinit var imgVoice : ImageView
    var originalString : String = ""
    lateinit var englishVNTranslater : Translator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initId
        btnTranslate = findViewById(R.id.btnTranslate)
        edtInput = findViewById(R.id.edtInput)
        tvResult = findViewById(R.id.txtResult)
        imgVoice = findViewById(R.id.ImgMicSay)

        // setUpProgressDialog
        setUpProgressDialog()
        // ClickLister
        btnTranslate.setOnClickListener {
            originalString = edtInput.text.toString()
            prepareTranslateModel()
        }

        imgVoice.setOnClickListener {
            getSpeechInput()
        }
    }

    private fun getSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault())
        if(intent.resolveActivity(packageManager)!=null){
            startActivityForResult(intent,10)
        }else{
            Toast.makeText(this,"Your device Doesn't Support Speech Input",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            10 -> if(resultCode == RESULT_OK && data != null){
                val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                      edtInput.setText(result?.get(0).toString(),TextView.BufferType.EDITABLE)
            }
        }
    }

    private fun setUpProgressDialog() {
      //gwwww
    }

    private fun prepareTranslateModel() {
        val options : TranslatorOptions = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.VIETNAMESE)
            .build()
        englishVNTranslater = Translation.getClient(options)

        // first Download
        englishVNTranslater.downloadModelIfNeeded().addOnSuccessListener {
            translateText()
        }.addOnFailureListener {

            tvResult.text = "Error ${it.message}"
        }
    }

    private fun translateText() {

        englishVNTranslater.translate(originalString).addOnSuccessListener {

            tvResult.text = it
        }.addOnFailureListener {

            tvResult.text = "Error ${it.message}"
        }
    }
}