package trans.rights.client.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import trans.rights.TransRights.Companion.LOGGER
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.nio.file.Files
import java.nio.file.Path

object FileHelper {
    private val gson: Gson = GsonBuilder().setLenient().setPrettyPrinting().create()

    fun writeToJson(element: JsonObject, path: Path) {
        val writer = BufferedWriter(OutputStreamWriter(Files.newOutputStream(path)))

        writer.write(gson.toJson(element))
        writer.close()
    }

    fun read(path: Path): String {
        return try {
            Files.readString(path)
        } catch (e: IOException) {
            LOGGER.error("Couldn't read $path")

            ""
        }
    }

    fun fromJson(path: Path, clearIfException: Boolean = false): JsonObject {
        return try {
            gson.fromJson(read(path), JsonObject::class.java)
        } catch (e: RuntimeException) {
            if (clearIfException) clearJson(path)

            JsonObject()
        }
    }

    fun clearJson(path: Path) = writeToJson(JsonObject(), path)
}
