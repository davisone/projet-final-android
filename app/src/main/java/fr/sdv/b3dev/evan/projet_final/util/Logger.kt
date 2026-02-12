package fr.sdv.b3dev.evan.projet_final.util

import timber.log.Timber

/**
 * Utilitaire de logging utilisant Timber avec tag personnalisé SNKRS_
 */
object Logger {
    private const val TAG_PREFIX = "SNKRS_"

    fun d(tag: String, message: String) {
        Timber.tag("$TAG_PREFIX$tag").d(message)
    }

    fun i(tag: String, message: String) {
        Timber.tag("$TAG_PREFIX$tag").i(message)
    }

    fun w(tag: String, message: String) {
        Timber.tag("$TAG_PREFIX$tag").w(message)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            Timber.tag("$TAG_PREFIX$tag").e(throwable, message)
        } else {
            Timber.tag("$TAG_PREFIX$tag").e(message)
        }
    }

    fun v(tag: String, message: String) {
        Timber.tag("$TAG_PREFIX$tag").v(message)
    }

    // Logs spécifiques par fonctionnalité
    object Database {
        private const val TAG = "Database"
        fun insert(entity: String) = d(TAG, "Insert: $entity")
        fun update(entity: String) = d(TAG, "Update: $entity")
        fun delete(entity: String) = d(TAG, "Delete: $entity")
        fun query(query: String) = d(TAG, "Query: $query")
        fun error(message: String, e: Throwable? = null) = e(TAG, message, e)
    }

    object Navigation {
        private const val TAG = "Navigation"
        fun navigate(destination: String) = d(TAG, "Navigate to: $destination")
        fun back() = d(TAG, "Navigate back")
    }

    object Camera {
        private const val TAG = "Camera"
        fun opened() = i(TAG, "Camera opened")
        fun closed() = i(TAG, "Camera closed")
        fun barcodeScanned(barcode: String) = i(TAG, "Barcode scanned: $barcode")
        fun photoTaken(path: String) = i(TAG, "Photo taken: $path")
        fun error(message: String, e: Throwable? = null) = e(TAG, message, e)
    }

    object VoiceSearch {
        private const val TAG = "VoiceSearch"
        fun started() = i(TAG, "Voice search started")
        fun result(text: String) = i(TAG, "Voice search result: $text")
        fun error(message: String, e: Throwable? = null) = e(TAG, message, e)
    }

    object Payment {
        private const val TAG = "Payment"
        fun started(method: String, amount: Double) = i(TAG, "Payment started: $method - $amount€")
        fun success(orderId: String) = i(TAG, "Payment success: Order #$orderId")
        fun failed(reason: String) = w(TAG, "Payment failed: $reason")
    }

    object Cart {
        private const val TAG = "Cart"
        fun itemAdded(sneakerId: Long, size: String) = d(TAG, "Item added: $sneakerId, size: $size")
        fun itemRemoved(sneakerId: Long) = d(TAG, "Item removed: $sneakerId")
        fun cleared() = d(TAG, "Cart cleared")
    }

    object Favorites {
        private const val TAG = "Favorites"
        fun added(sneakerId: Long) = d(TAG, "Added to favorites: $sneakerId")
        fun removed(sneakerId: Long) = d(TAG, "Removed from favorites: $sneakerId")
    }
}
