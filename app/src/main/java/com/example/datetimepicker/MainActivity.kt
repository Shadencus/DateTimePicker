package com.example.datetimepicker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.datetimepicker.ui.theme.DateTimePickerTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DateTimePickerTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Exercise2()
                }
            }
        }
    }
}
@Composable
fun Exercise2(){
    /**
     * Erstelle eine Aktivity mit der es möglich ist ein Datum als Text anzugeben und dies als
     * korrektes Datum anzuzeigen
     * Achte dabei auf die Validierung
     */

    var errorText by remember {
        mutableStateOf("")
    }

    var text by remember {
        mutableStateOf("")
    }

    var formattedDate by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    fun formateStringToDate(dateString: String): String{
        if(dateString.isBlank()){
            throw DateTimeParseException(
                "Das Datumsfeld ist leer",
                dateString,
                0
            )
        }
        val formatter = DateTimeFormatter.ofPattern(
            "dd.MMM.yyyy"
        ).withLocale(
            Locale("DE")
        )
        val localDate = LocalDate.parse(
            dateString,
            formatter
        )
        return localDate.toString()
    }

    Column{
        Row {
            TextField(
                value = text,
                onValueChange = {
                    newText: String -> text = newText
                },
                label = { Text(text = "Datum eingeben: ") },
                isError = errorText.isNotBlank()
            )
            Button(onClick = {
                try{
                    formattedDate = formateStringToDate(text)
                    errorText = ""
                }catch (e: DateTimeParseException){
                    errorText = "Gebe ein gültiges Datum ein"
                    Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show()
                }
            }
            ){
                Text(text ="OK")
            }

        }
        Row{
            Text(text = "Formattiertes Datum: ${
                if (formattedDate==""){
                    "no valid"
                }else{
                    formattedDate
                }
            }")
        }
    }


}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Exercise() {
    /**
     * Aufgabe 1
     * Erstelle ine Activity die anzeigen soll wie viele Tage/Stunden/Sekunden von der jetzigen Zeit
     * bis zu einem bestimmten Datum in der Zukunft noch vorhanden sind
     * Benutze dafür die Picker
     */

    val calender = Calendar.getInstance()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = calender.timeInMillis
    )
    var showDatePicker by remember{
        mutableStateOf(false)
    }
    var selectedDate by remember {
        mutableLongStateOf(calender.timeInMillis)
    }

    fun dateFromEpoch(selectedDate: Long): LocalDate{


        return LocalDate.ofEpochDay(selectedDate/1000/86400)
    }

    if (showDatePicker){
        DatePickerDialog(
            dismissButton = {
                            TextButton(onClick = {
                                showDatePicker = false
                            }){
                                Text(text = "Abbrechen")
                            }
            },
            onDismissRequest = {
                               showDatePicker = false
            },
            confirmButton = {
               TextButton(onClick = {
                   showDatePicker = false
                   selectedDate = datePickerState.selectedDateMillis!!

               }) {
                   Text(text = "Bestätigen")
               }
            }){
    DatePicker(state = datePickerState)
        }
    }

    Column{
        Row{
            Text(text="Tage bis: ")
            Text(text = LocalDate.now().until(
                dateFromEpoch(selectedDate),
                ChronoUnit.DAYS).toString())

        }
        Row{
            Column {
                Row{
                    Text(text = "Heutiges Datum: "+LocalDateTime.now().toString())
                }
                Row {
                    Text(text = "Random Date: "+LocalDate.ofEpochDay(1700577540/1000/86400).toString()) //Millisekunden Timestamp durch Millisekunden und Tag in Sekunden
                }
                Row{
                    Button(onClick = {
                        showDatePicker = true
                    }){
                        Text(text = "Our Button")
                    }
                }
                Row{
                    Text(text = "Ausgewähltes Date: ")
                    Text(text = LocalDate.ofEpochDay(selectedDate/1000/86400).toString())
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExercisePreview() {
    DateTimePickerTheme {
        Exercise()
    }
}