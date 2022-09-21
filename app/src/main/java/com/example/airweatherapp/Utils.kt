package com.example.airweatherapp

fun weatherText(place: Place): String = """
    |Weather in ${place.name}, ${place.sys.country}:
    |- Temperature: ${place.main.temp}
    |- Min - max: ${place.main.tempMin} - ${place.main.tempMax}
    |- Weather: ${place.weather[0].main}
    |- Detail: ${place.weather[0].description}""".trimMargin()