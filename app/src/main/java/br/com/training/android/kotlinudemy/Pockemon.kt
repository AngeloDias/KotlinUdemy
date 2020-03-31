package br.com.training.android.kotlinudemy

class Pockemon {
    var name: String? = null
    var description: String? = null
    var image: Int? = null
    var power: Double? = null
    var lat: Double? = null
    var long: Double? = null
    var isCatch: Boolean? = false

    constructor(name: String, description: String, image: Int, power: Double, lat: Double, long: Double, catch: Boolean) {
        this.name = name
        this.description = description
        this.image = image
        this.power = power
        this.lat = lat
        this.long = long
        this.isCatch = catch
    }

}
