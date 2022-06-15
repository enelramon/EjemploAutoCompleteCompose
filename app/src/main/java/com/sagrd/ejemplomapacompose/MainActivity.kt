package com.sagrd.ejemplomapacompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.sagrd.ejemplomapacompose.models.SampleData
import com.sagrd.ejemplomapacompose.ui.theme.EjemploMapaComposeTheme
import com.sagrd.ejemplomapacompose.util.SagAutoComplete

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        setContent {
            EjemploMapaComposeTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        val list = listOf<SampleData>(
                            SampleData("Enmanuel","1" ),
                            SampleData("Mario","2" ),
                            SampleData("Enel","3" ),
                            SampleData("Leo","4" )
                        )
                        SagAutoComplete(sampleData = list, label = "Hola Mario")

                       // SagTextField(value = "", label = "Hola Samuel Klk", onValueChanged ={} )
                    }
                }
            }
        }
    }
}

@Composable
fun SagMap(){
    val markerPosition = LatLng(19.299457,-70.254999)

    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(markerPosition,15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ){// Generar y agregar el api key al proyecto para que funcione el mapa. Att: Samuel, dele  que usted es duro.
        Marker(
            position = markerPosition,
            title = "Title",
            snippet = "Snippet"
        )
    }
}

//@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EjemploMapaComposeTheme {
        //Greeting("Android")
    }
}