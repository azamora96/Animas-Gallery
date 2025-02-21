class Artist(var name: String? = null) {

    companion object {
        var currentArtist = String()
    }

    override fun toString(): String {
        return name ?: ""
    }

}