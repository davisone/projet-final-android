package fr.sdv.b3dev.evan.projet_final.camera

enum class CameraMode(val value: String) {
    SCAN("scan"),
    PROFILE("profile");

    companion object {
        fun fromRoute(value: String?): CameraMode {
            return entries.firstOrNull { it.value == value } ?: SCAN
        }
    }
}
