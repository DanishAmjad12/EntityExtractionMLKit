 package com.android.entityextractionmlkit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.mlkit.nl.entityextraction.*

 class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val result = findViewById<TextView>(R.id.result)
        val extractButton = findViewById<Button>(R.id.extractButton)
        val editText = findViewById<EditText>(R.id.editText)


        //step1
        val entityExtractor =
            EntityExtraction.getClient(
                EntityExtractorOptions.Builder(EntityExtractorOptions.ENGLISH)
                    .build()
            )

        extractButton.setOnClickListener {

            entityExtractor
                .downloadModelIfNeeded()
                .addOnSuccessListener { _ ->

                    //step3
                    val params =
                        EntityExtractionParams.Builder(editText.text.toString())
                            .build()
                    entityExtractor
                        .annotate(params)
                        .addOnSuccessListener {

                            //step4
                            for (entityAnnotation in it) {
                                val entities: List<Entity> = entityAnnotation.entities
                                Log.d(
                                    "VALUE",
                                    "Range: ${entityAnnotation.start} - ${entityAnnotation.end}"
                                )
                                for (entity in entities) {
                                    when (entity) {
                                        is DateTimeEntity -> {
                                            result.text =
                                                "Granularity:  ${entity.dateTimeGranularity} \n TimeStamp:  ${entity.timestampMillis}"
                                            Log.d(
                                                "VALUE",
                                                "Granularity: ${entity.dateTimeGranularity}"
                                            )
                                            Log.d("VALUE", "TimeStamp: ${entity.timestampMillis}")
                                        }
                                        is FlightNumberEntity -> {
                                            result.text =
                                                "Airline Code: ${entity.airlineCode} \nFlight number: ${entity.flightNumber}"
                                            Log.d("VALUE", "Airline Code: ${entity.airlineCode}")
                                            Log.d("VALUE", "Flight number: ${entity.flightNumber}")
                                        }
                                        else -> {
                                            Log.d("VALUE", "  $entity")
                                        }
                                    }
                                }
                            }
                        }

                        .addOnFailureListener {


                        }

                }
                .addOnFailureListener {



                }

        }


    }
}