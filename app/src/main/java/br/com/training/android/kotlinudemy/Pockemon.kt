package br.com.training.android.kotlinudemy

import android.location.Location

class Pockemon {
    var name: String? = null
    var description: String? = null
    var image: Int? = null
    var power: Double? = null
    var location: Location? = null
    var isCatch: Boolean? = false

    constructor(name: String, description: String, image: Int, power: Double, lat: Double, longitude: Double, catch: Boolean) {
        this.name = name
        this.description = description
        this.image = image
        this.power = power
        this.isCatch = catch
        this.location = Location(this.name)
        this.location!!.latitude = lat
        this.location!!.longitude = longitude
    }

}
