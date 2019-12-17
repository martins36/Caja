package com.sujeto36.caja

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.sujeto36.caja.db.main.IngEgDBHelper
import com.sujeto36.caja.db.main.IngEgModel
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import kotlinx.android.synthetic.main.activity_new.*
import java.text.SimpleDateFormat
import java.util.*

class New : AppCompatActivity() {

    private lateinit var ingEgDBHelper : IngEgDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)
        ingEgDBHelper = IngEgDBHelper(this)

        radio_group.setOnCheckedChangeListener { _, i ->
            val radio: RadioButton = findViewById(i)
            input_desc.text = null
            if (radio.text == "Sueldo")
                input_desc.visibility = View.GONE
            else
                input_desc.visibility = View.VISIBLE
        }

        btn_save.setOnClickListener {
            var type = "S"
            val sdf = SimpleDateFormat("dd/MM/yy", Locale.US)
            val currentDate = sdf.format(Date())
            val radio: RadioButton = findViewById(radio_group.checkedRadioButtonId)
            when(radio.text) {
                "Egreso" -> type = "E"
                "Ingreso" -> type = "I"
                "Sueldo" -> input_desc.setText(R.string.sueldo)
            }

            input_desc.nonEmpty {
                input_desc.error = "Campo requerido"
            }
            input_price.nonEmpty {
                input_price.error = "Campo requerido"
            }

            if (input_desc.nonEmpty() && input_price.nonEmpty()) {

                val ingEg = IngEgModel(
                    currentDate,
                    input_desc.text.toString(),
                    input_price.text.toString().toInt(),
                    type,
                    -1,
                    1)

                if (type != "S" && ingEgDBHelper.countSueldo() != 0) { //caso normal
                    ingEgDBHelper.insert(ingEg)
                }
                else if (type == "S") { //caso sueldo/primer sueldo (indiferente)
                    ingEgDBHelper.clear()
                    ingEgDBHelper.insert(ingEg)
                }
                else { //caso I/E primero
                    Toast.makeText(this, "Error: Debe ingresar un sueldo primero", Toast.LENGTH_SHORT).show()
                }

                onBackPressed()
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}