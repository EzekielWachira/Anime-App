package com.nryde.driver.utils.file

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import zerobranch.androidremotedebugger.AndroidRemoteDebugger
import java.io.*

object FileUtil {

    private val EOF = -1
    private val DEFAULT_BUFFER_SIZE = 1024 * 4

    @Throws(IOException::class)
    suspend fun from(context: Context, uri: Uri?): File? = withContext(Dispatchers.IO){
        return@withContext try {
            val inputStream: InputStream = context.contentResolver.openInputStream(uri!!)!!
    //        val fileName: String = uri.getNameFromUri(context)
    //            getFileName(context, uri)!!
            val fileName: String = getFileName(context, uri)!!
            val splitName = splitFileName(fileName)
            var tempFile: File = File.createTempFile(splitName[0], splitName[1])
            tempFile = rename(tempFile, fileName)
            AndroidRemoteDebugger.Log.w("FIle Name: ${tempFile.absolutePath}")
            tempFile.deleteOnExit()
            AndroidRemoteDebugger.Log.i("FIle Name: ${tempFile.absolutePath}")
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(tempFile)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            if (out != null) {
                copy(inputStream, out)
            }
            inputStream.close()
            out?.close()
            AndroidRemoteDebugger.Log.v("FIle Name: ${tempFile.absolutePath}")
            tempFile
        } catch (e: Exception) {
            AndroidRemoteDebugger.Log.v("FIle Name: ${e.message}")
            e.printStackTrace()
            if (e is IllegalArgumentException) {
                e.printStackTrace()
            }
            null
        }
    }

    private fun splitFileName(fileName: String): Array<String> {
        var name = fileName
        var extension = ""
        val i = fileName.lastIndexOf(".")
        if (i != -1) {
            name = fileName.substring(0, i)
            extension = fileName.substring(i)
        }
        return arrayOf(name, extension)
    }

    @SuppressLint("Range")
    private suspend fun getFileName(context: Context, uri: Uri): String?  = withContext(Dispatchers.IO){
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf(File.separator)
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        val fName = result.toString()
        val extension = fName.substring(fName.lastIndexOf("."))
        val frontName = fName.substring(0, fName.lastIndexOf(".") - 1)
            .filter { it.isLetterOrDigit() }
        AndroidRemoteDebugger.Log.d("$frontName$extension")
        "$frontName$extension"
    }

    private suspend fun rename(file: File, newName: String): File = withContext(Dispatchers.IO) {
        val newFile = File(file.parent, newName)
        if (newFile != file) {
            if (newFile.exists() && newFile.delete()) {
                AndroidRemoteDebugger.Log.d("FileUtil Delete old $newName file")
            }
            if (file.renameTo(newFile)) {
                AndroidRemoteDebugger.Log.d("FileUtil", "Rename file to $newName")
            }
        }
        newFile
    }

    @Throws(IOException::class)
    private suspend fun copy(input: InputStream, output: OutputStream): Long = withContext(Dispatchers.IO) {
        var count: Long = 0
        var n: Int
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        while (EOF != input.read(buffer).also { n = it }) {
            output.write(buffer, 0, n)
            count += n.toLong()
        }
        count
    }


}