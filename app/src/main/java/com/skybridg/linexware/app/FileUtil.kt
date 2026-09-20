package com.skybridg.linexware.app

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.io.*
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*

object FileUtil {

    private fun createNewFile(path: String) {
        val lastSep = path.lastIndexOf(File.separator)
        if (lastSep > 0) {
            val dirPath = path.substring(0, lastSep)
            makeDir(dirPath)
        }
        val file = File(path)
        try {
            if (!file.exists()) file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun readFile(path: String): String {
        createNewFile(path)
        val sb = StringBuilder()
        var fr: FileReader? = null
        try {
            fr = FileReader(File(path))
            val buff = CharArray(1024)
            var length: Int
            while (fr.read(buff).also { length = it } > 0) {
                sb.append(String(buff, 0, length))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            fr?.let {
                try {
                    it.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return sb.toString()
    }

    @JvmStatic
    fun writeFile(path: String, str: String) {
        createNewFile(path)
        var fileWriter: FileWriter? = null
        try {
            fileWriter = FileWriter(File(path), false)
            fileWriter.write(str)
            fileWriter.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fileWriter?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun copyFile(sourcePath: String, destPath: String) {
        if (!isExistFile(sourcePath)) return
        createNewFile(destPath)
        var fis: FileInputStream? = null
        var fos: FileOutputStream? = null
        try {
            fis = FileInputStream(sourcePath)
            fos = FileOutputStream(destPath, false)
            val buff = ByteArray(1024)
            var length: Int
            while (fis.read(buff).also { length = it } > 0) {
                fos.write(buff, 0, length)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fis?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun copyDir(oldPath: String, newPath: String) {
        val oldFile = File(oldPath)
        val files = oldFile.listFiles() ?: return
        val newFile = File(newPath)
        if (!newFile.exists()) {
            newFile.mkdirs()
        }
        for (file in files) {
            if (file.isFile) {
                copyFile(file.path, "$newPath/${file.name}")
            } else if (file.isDirectory) {
                copyDir(file.path, "$newPath/${file.name}")
            }
        }
    }

    @JvmStatic
    fun moveFile(sourcePath: String, destPath: String) {
        copyFile(sourcePath, destPath)
        deleteFile(sourcePath)
    }

    @JvmStatic
    fun deleteFile(path: String) {
        val file = File(path)
        if (!file.exists()) return
        if (file.isFile) {
            file.delete()
            return
        }
        val fileArr = file.listFiles()
        if (fileArr != null) {
            for (subFile in fileArr) {
                if (subFile.isDirectory) {
                    deleteFile(subFile.absolutePath)
                }
                if (subFile.isFile) {
                    subFile.delete()
                }
            }
        }
        file.delete()
    }

    @JvmStatic
    fun isExistFile(path: String): Boolean {
        val file = File(path)
        return file.exists()
    }

    @JvmStatic
    fun makeDir(path: String) {
        if (!isExistFile(path)) {
            val file = File(path)
            file.mkdirs()
        }
    }

    @JvmStatic
    fun listDir(path: String, list: ArrayList<String>?) {
        val dir = File(path)
        if (!dir.exists() || dir.isFile) return
        val listFiles = dir.listFiles() ?: return
        if (list == null) return
        list.clear()
        for (file in listFiles) {
            list.add(file.absolutePath)
        }
    }

    @JvmStatic
    fun isDirectory(path: String): Boolean {
        if (!isExistFile(path)) return false
        return File(path).isDirectory
    }

    @JvmStatic
    fun isFile(path: String): Boolean {
        if (!isExistFile(path)) return false
        return File(path).isFile
    }

    @JvmStatic
    fun getFileLength(path: String): Long {
        if (!isExistFile(path)) return 0
        return File(path).length()
    }

    @JvmStatic
    fun getExternalStorageDir(): String {
        return Environment.getExternalStorageDirectory().absolutePath
    }

    @JvmStatic
    fun getPackageDataDir(context: Context): String {
        return context.getExternalFilesDir(null)?.absolutePath ?: ""
    }

    @JvmStatic
    fun getPublicDir(type: String): String {
        return Environment.getExternalStoragePublicDirectory(type).absolutePath
    }

    @JvmStatic
    fun convertUriToFilePath(context: Context, uri: Uri): String? {
        var path: String? = null
        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":")
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    path = Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":")
                val type = split[0]
                if ("raw".equals(type, ignoreCase = true)) {
                    return split[1]
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && "msf".equals(type, ignoreCase = true)) {
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])
                    path = getDataColumn(context, MediaStore.Downloads.EXTERNAL_CONTENT_URI, selection, selectionArgs)
                } else {
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        docId.toLongOrNull() ?: 0L
                    )
                    path = getDataColumn(context, contentUri, null, null)
                }
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":")
                val type = split[0]
                var contentUri: Uri? = null
                when (type) {
                    "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                if (contentUri != null) {
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])
                    path = getDataColumn(context, contentUri, selection, selectionArgs)
                }
            }
        } else if (ContentResolver.SCHEME_CONTENT.equals(uri.scheme, ignoreCase = true)) {
            path = getDataColumn(context, uri, null, null)
        } else if (ContentResolver.SCHEME_FILE.equals(uri.scheme, ignoreCase = true)) {
            path = uri.path
        }

        if (path != null) {
            try {
                return URLDecoder.decode(path, "UTF-8")
            } catch (e: Exception) {
                return null
            }
        }
        return null
    }

    private fun getDataColumn(context: Context, uri: Uri, selection: String?, selectionArgs: Array<String>?): String? {
        val column = MediaStore.Images.Media.DATA
        val projection = arrayOf(column)
        try {
            val cursor: Cursor? = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndexOrThrow(column)
                    return it.getString(columnIndex)
                }
            }
        } catch (e: Exception) {
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun saveBitmap(bitmap: Bitmap, destPath: String) {
        createNewFile(destPath)
        try {
            FileOutputStream(File(destPath)).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun getScaledBitmap(path: String, max: Int): Bitmap {
        val src = BitmapFactory.decodeFile(path)
        var width = src.width
        var height = src.height
        if (width > height) {
            val rate = max / width.toFloat()
            height = (height * rate).toInt()
            width = max
        } else {
            val rate = max / height.toFloat()
            width = (width * rate).toInt()
            height = max
        }
        return Bitmap.createScaledBitmap(src, width, height, true)
    }

    @JvmStatic
    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    @JvmStatic
    fun decodeSampleBitmapFromPath(path: String, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(path, options)
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(path, options)
    }

    @JvmStatic
    fun resizeBitmapFileRetainRatio(fromPath: String, destPath: String, max: Int) {
        if (!isExistFile(fromPath)) return
        val bitmap = getScaledBitmap(fromPath, max)
        saveBitmap(bitmap, destPath)
    }

    @JvmStatic
    fun resizeBitmapFileToSquare(fromPath: String, destPath: String, max: Int) {
        if (!isExistFile(fromPath)) return
        val src = BitmapFactory.decodeFile(fromPath)
        val bitmap = Bitmap.createScaledBitmap(src, max, max, true)
        saveBitmap(bitmap, destPath)
    }

    @JvmStatic
    fun resizeBitmapFileToCircle(fromPath: String, destPath: String) {
        if (!isExistFile(fromPath)) return
        val src = BitmapFactory.decodeFile(fromPath)
        val bitmap = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val color = -0xbdbebe
        val paint = Paint().apply {
            isAntiAlias = true
            this.color = color
        }
        val rect = Rect(0, 0, src.width, src.height)
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(src.width / 2f, src.height / 2f, src.width / 2f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(src, rect, rect, paint)
        saveBitmap(bitmap, destPath)
    }

    @JvmStatic
    fun resizeBitmapFileWithRoundedBorder(fromPath: String, destPath: String, pixels: Int) {
        if (!isExistFile(fromPath)) return
        val src = BitmapFactory.decodeFile(fromPath)
        val bitmap = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val color = -0xbdbebe
        val paint = Paint().apply {
            isAntiAlias = true
            this.color = color
        }
        val rect = Rect(0, 0, src.width, src.height)
        val rectF = RectF(rect)
        val roundPx = pixels.toFloat()
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(src, rect, rect, paint)
        saveBitmap(bitmap, destPath)
    }

    @JvmStatic
    fun cropBitmapFileFromCenter(fromPath: String, destPath: String, w: Int, h: Int) {
        if (!isExistFile(fromPath)) return
        val src = BitmapFactory.decodeFile(fromPath)
        val width = src.width
        val height = src.height
        if (width < w && height < h) return
        var x = 0
        var y = 0
        if (width > w) x = (width - w) / 2
        if (height > h) y = (height - h) / 2
        var cw = w
        var ch = h
        if (w > width) cw = width
        if (h > height) ch = height
        val bitmap = Bitmap.createBitmap(src, x, y, cw, ch)
        saveBitmap(bitmap, destPath)
    }

    @JvmStatic
    fun rotateBitmapFile(fromPath: String, destPath: String, angle: Float) {
        if (!isExistFile(fromPath)) return
        val src = BitmapFactory.decodeFile(fromPath)
        val matrix = Matrix().apply { postRotate(angle) }
        val bitmap = Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
        saveBitmap(bitmap, destPath)
    }

    @JvmStatic
    fun scaleBitmapFile(fromPath: String, destPath: String, x: Float, y: Float) {
        if (!isExistFile(fromPath)) return
        val src = BitmapFactory.decodeFile(fromPath)
        val matrix = Matrix().apply { postScale(x, y) }
        val bitmap = Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
        saveBitmap(bitmap, destPath)
    }

    @JvmStatic
    fun skewBitmapFile(fromPath: String, destPath: String, x: Float, y: Float) {
        if (!isExistFile(fromPath)) return
        val src = BitmapFactory.decodeFile(fromPath)
        val matrix = Matrix().apply { postSkew(x, y) }
        val bitmap = Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
        saveBitmap(bitmap, destPath)
    }

    @JvmStatic
    fun setBitmapFileColorFilter(fromPath: String, destPath: String, color: Int) {
        if (!isExistFile(fromPath)) return
        val src = BitmapFactory.decodeFile(fromPath)
        val bitmap = Bitmap.createBitmap(src.width - 1, src.height - 1, Bitmap.Config.ARGB_8888)
        val p = Paint().apply {
            colorFilter = LightingColorFilter(color, 1)
        }
        val canvas = Canvas(bitmap)
        canvas.drawBitmap(bitmap, 0f, 0f, p)
        saveBitmap(bitmap, destPath)
    }

    @JvmStatic
    fun setBitmapFileBrightness(fromPath: String, destPath: String, brightness: Float) {
        if (!isExistFile(fromPath)) return
        val src = BitmapFactory.decodeFile(fromPath)
        val cm = ColorMatrix(
            floatArrayOf(
                1f, 0f, 0f, 0f, brightness,
                0f, 1f, 0f, 0f, brightness,
                0f, 0f, 1f, 0f, brightness,
                0f, 0f, 0f, 1f, 0f
            )
        )
        val bitmap = Bitmap.createBitmap(src.width, src.height, src.config ?: Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(cm)
        }
        canvas.drawBitmap(src, 0f, 0f, paint)
        saveBitmap(bitmap, destPath)
    }

    @JvmStatic
    fun setBitmapFileContrast(fromPath: String, destPath: String, contrast: Float) {
        if (!isExistFile(fromPath)) return
        val src = BitmapFactory.decodeFile(fromPath)
        val cm = ColorMatrix(
            floatArrayOf(
                contrast, 0f, 0f, 0f, 0f,
                0f, contrast, 0f, 0f, 0f,
                0f, 0f, contrast, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
        val bitmap = Bitmap.createBitmap(src.width, src.height, src.config ?: Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(cm)
        }
        canvas.drawBitmap(src, 0f, 0f, paint)
        saveBitmap(bitmap, destPath)
    }

    @JvmStatic
    fun getJpegRotate(filePath: String): Int {
        var rotate = 0
        try {
            val exif = ExifInterface(filePath)
            when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
            }
        } catch (e: IOException) {
            return 0
        }
        return rotate
    }

    @JvmStatic
    fun createNewPictureFile(context: Context): File {
        val date = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val fileName = "${date.format(Date())}.jpg"
        return File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM)?.absolutePath + File.separator + fileName)
    }
}
