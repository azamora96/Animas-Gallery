class User(var name: String? = null) {

    companion object {
        var currentUser = String()
    }

    override fun toString(): String {
        return name ?: ""
    }
}