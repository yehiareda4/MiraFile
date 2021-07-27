//package com.yehia.mira_file_picker
//
//import android.annotation.TargetApi
//import android.content.ContentUris
//import android.content.Context
//import android.content.Intent
//import android.database.Cursor
//import android.database.DatabaseUtils
//import android.graphics.Bitmap
//import android.net.Uri
//import android.os.Build
//import android.os.Environment
//import android.provider.DocumentsContract
//import android.provider.MediaStore
//import android.provider.OpenableColumns
//import android.util.Log
//import android.webkit.MimeTypeMap
//import java.io.File
//import java.io.FileFilter
//import java.io.FileOutputStream
//import java.io.InputStream
//import java.net.URLEncoder
//import java.text.DecimalFormat
//
//
//class FileUtilsKotlin {
//
//
//    /**
//     * TAG for log messages.
//     */
//    val TAG = "FileUtils"
//    private val DEBUG = true // Set to true to enable logging
//
//
//    val MIME_TYPE_AUDIO = "audio/*"
//    val MIME_TYPE_TEXT = "text/*"
//    val MIME_TYPE_IMAGE = "image/*"
//    val MIME_TYPE_VIDEO = "video/*"
//    val MIME_TYPE_APP = "application/*"
//
//    val HIDDEN_PREFIX = "."
//
//    /**
//     * Gets the extension of a file name, like ".png" or ".jpg".
//     *
//     * @param uri
//     * @return Extension including the dot("."); "" if there is no extension;
//     * null if uri was null.
//     */
//    fun getExtension(uri: String?): String? {
//        if (uri == null) {
//            return null
//        }
//        val dot = uri.lastIndexOf(".")
//        return if (dot >= 0) {
//            uri.substring(dot)
//        } else {
//            // No extension.
//            ""
//        }
//    }
//
//    /**
//     * @return Whether the URI is a local one.
//     */
//    fun isLocal(url: String?): Boolean {
//        return if ((url != null) && !url.startsWith("http://") && !url.startsWith("https://")) {
//            true
//        } else false
//    }
//
//    /**
//     * @return True if Uri is a MediaStore Uri.
//     * @author paulburke
//     */
//    fun isMediaUri(uri: Uri): Boolean {
//        return "media".equals(uri.authority, ignoreCase = true)
//    }
//
//    /**
//     * Convert File into Uri.
//     *
//     * @param file
//     * @return uri
//     */
//    fun getUri(file: File?): Uri? {
//        return if (file != null) {
//            Uri.fromFile(file)
//        } else null
//    }
//
//    /**
//     * Returns the path only (without file name).
//     *
//     * @param file
//     * @return
//     */
//    fun getPathWithoutFilename(file: File?): File? {
//        if (file != null) {
//            if (file.isDirectory) {
//                // no file to be split off. Return everything
//                return file
//            } else {
//                val filename = file.name
//                val filepath = file.absolutePath
//
//                // Construct path without file name.
//                var pathwithoutname = filepath.substring(
//                    0,
//                    filepath.length - filename.length
//                )
//                if (pathwithoutname.endsWith("/")) {
//                    pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length - 1)
//                }
//                return File(pathwithoutname)
//            }
//        }
//        return null
//    }
//
//    /**
//     * @return The MIME type for the given file.
//     */
//    fun getMimeType(file: File): String? {
//        val extension = getExtension(file.name)
//        return if (extension!!.length > 0) MimeTypeMap.getSingleton().getMimeTypeFromExtension(
//            extension.substring(1)
//        ) else "application/octet-stream"
//    }
//
//    /**
//     * @return The MIME type for the give Uri.
//     */
//    fun getMimeType(context: Context, uri: Uri): String? {
//        val file = File(getPath(context, uri))
//        return getMimeType(file)
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is [//LocalStorageProvider].
//     * @author paulburke
//     */
//    fun isLocalStorageDocument(uri: Uri?): Boolean {
////        return LocalStorageProvider.AUTHORITY.equals(uri.getAuthority());
//        return false
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is ExternalStorageProvider.
//     * @author paulburke
//     */
//    fun isExternalStorageDocument(uri: Uri): Boolean {
//        return "com.android.externalstorage.documents" == uri.authority
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is DownloadsProvider.
//     * @author paulburke
//     */
//    fun isDownloadsDocument(uri: Uri): Boolean {
//        return "com.android.providers.downloads.documents" == uri.authority
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is MediaProvider.
//     * @author paulburke
//     */
//    fun isMediaDocument(uri: Uri): Boolean {
//        return "com.android.providers.media.documents" == uri.authority
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is Google Photos.
//     */
//    fun isGooglePhotosUri(uri: Uri): Boolean {
//        return "com.google.android.apps.photos.content" == uri.authority
//    }
//
//    /**
//     * Get the value of the data column for this Uri. This is useful for
//     * MediaStore Uris, and other file-based ContentProviders.
//     *
//     * @param context       The context.
//     * @param uri           The Uri to query.
//     * @param selection     (Optional) Filter used in the query.
//     * @param selectionArgs (Optional) Selection arguments used in the query.
//     * @return The value of the _data column, which is typically a file path.
//     * @author paulburke
//     */
//    fun getDataColumn(
//        context: Context, uri: Uri?, selection: String?,
//        selectionArgs: Array<String?>?
//    ): String? {
//        return getColumn(context, uri, MediaStore.Files.FileColumns.DATA, selection, selectionArgs)
//    }
//
//    /**
//     * Get the value of the display name for this Uri
//     *
//     * @param context       The context.
//     * @param uri           The Uri to query.
//     * @param selection     (Optional) Filter used in the query.
//     * @param selectionArgs (Optional) Selection arguments used in the query.
//     * @return The value of the _data column, which is typically a file path.
//     * @author paulburke
//     */
//    fun getDisplayNameColumn(
//        context: Context, uri: Uri?, selection: String?,
//        selectionArgs: Array<String?>?
//    ): String? {
//        return getColumn(
//            context,
//            uri,
//            DocumentsContract.Document.COLUMN_DISPLAY_NAME,
//            selection,
//            selectionArgs
//        )
//    }
//
//    /**
//     * Get the value of the column for this Uri
//     *
//     * @param context       The context.
//     * @param //            The column.
//     * @param uri           The Uri to query.
//     * @param selection     (Optional) Filter used in the query.
//     * @param selectionArgs (Optional) Selection arguments used in the query.
//     * @return The value of the _data column, which is typically a file path.
//     */
//    fun getColumn(
//        context: Context, uri: Uri?, column: String, selection: String?,
//        selectionArgs: Array<String?>?
//    ): String? {
//        var cursor: Cursor? = null
//        val projection = arrayOf(
//            column
//        )
//        try {
//            cursor = context.contentResolver.query(
//                uri!!, projection, selection, selectionArgs,
//                null
//            )
//            if (cursor != null && cursor.moveToFirst()) {
//                if (DEBUG) DatabaseUtils.dumpCursor(cursor)
//                val column_index = cursor.getColumnIndexOrThrow(column)
//                return cursor.getString(column_index)
//            }
//        } catch (e: Exception) {
//            Log.w(
//                TAG,
//                "Error getting $column column", e
//            )
//        } finally {
//            cursor?.close()
//        }
//        return null
//    }
//
//    /**
//     * Attempt to get the file location from the base path and path if the file exists
//     *
//     * @param basePath
//     * @param path
//     * @return
//     */
//    private fun getFileIfExists(basePath: String, path: String): File? {
//        var result: File? = null
//        var file = File(basePath)
//        if (file.exists()) {
//            file = File(file, path)
//            if (file.exists()) {
//                result = file
//            }
//        }
//        return result
//    }
//
//    /**
//     * Get a file path from a Uri. This will get the the path for Storage Access
//     * Framework Documents, as well as the _data field for the MediaStore and
//     * other file-based ContentProviders.<br></br>
//     * <br></br>
//     * Callers should check whether the path is local before assuming it
//     * represents a local file.
//     *
//     * @param context The context.
//     * @param uri     The Uri to query.
//     * @author paulburke
//     * @see .isLocal
//     * @see .getFile
//     */
//    fun getPath(context: Context, uri: Uri): String? {
//        if (DEBUG) Log.d(
//            TAG + " File -",
//            "Authority: " + uri.authority +
//                    ", Fragment: " + uri.fragment +
//                    ", Port: " + uri.port +
//                    ", Query: " + uri.query +
//                    ", Scheme: " + uri.scheme +
//                    ", Host: " + uri.host +
//                    ", Segments: " + uri.pathSegments.toString()
//        )
//        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
//
//        // DocumentProvider
//        if (isKitKat && isDocumentUri(context, uri)) {
//            // LocalStorageProvider
//            if (isLocalStorageDocument(uri)) {
//                // The path is the id
//                return getDocumentId(uri)
//            } else if (isGoogleDriveUri(uri)) return getDriveFilePath(
//                uri,
//                context
//            ) else if (isExternalStorageDocument(uri)) {
//                val docId = getDocumentId(uri)
//                val split = docId.split(":").toTypedArray()
//                val type = split[0]
//                if ("primary".equals(type, ignoreCase = true)) {
//                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
//                }
//
//                // Handle SD cards
//                var file = getFileIfExists("/storage/extSdCard", split[1])
//                if (file != null) {
//                    return file.absolutePath
//                }
//                file = getFileIfExists("/storage/sdcard1", split[1])
//                if (file != null) {
//                    return file.absolutePath
//                }
//                file = getFileIfExists("/storage/usbcard1", split[1])
//                if (file != null) {
//                    return file.absolutePath
//                }
//                file = getFileIfExists("/storage/sdcard0", split[1])
//                if (file != null) {
//                    return file.absolutePath
//                }
//
//                // TODO handle non-primary volumes
//            } else if (isDownloadsDocument(uri)) {
//                val id = getDocumentId(uri)
//                val rawPrefix = "raw:"
//                if (id.startsWith(rawPrefix)) {
//                    return id.substring(rawPrefix.length)
//                }
//                try {
//                    val contentUriPrefixesToTry = arrayOf(
//                        "content://downloads/public_downloads",
//                        "content://downloads/my_downloads",
//                        "content://downloads/all_downloads"
//                    )
//                    for (contentUriPrefix: String? in contentUriPrefixesToTry) {
//                        val contentUri = ContentUris.withAppendedId(
//                            Uri.parse(contentUriPrefix),
//                            java.lang.Long.valueOf(id)
//                        )
//                        try {
//                            val path = getDataColumn(context, contentUri, null, null)
//                            if (path != null) {
//                                return path
//                            }
//                        } catch (e: Exception) {
//                        }
//                    }
//                } catch (e: NumberFormatException) {
//                }
//                val displayName = getDisplayNameColumn(context, uri, null, null)
//                if (displayName != null) {
//                    val file = File(
//                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//                        displayName
//                    )
//                    if (file.exists()) {
//                        return file.absolutePath
//                    }
//                }
//            } else if (isMediaDocument(uri)) {
//                val docId = getDocumentId(uri)
//                val split = docId.split(":").toTypedArray()
//                val type = split[0]
//                var contentUri: Uri? = null
//                if (("image" == type)) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//                } else if (("video" == type)) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
//                } else if (("audio" == type)) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//                }
//                val selection = "_id=?"
//                val selectionArgs = arrayOf<String?>(
//                    split[1]
//                )
//                return getDataColumn(context, contentUri, selection, selectionArgs)
//            }
//        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
//            // Return the remote address
//            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
//                context,
//                uri,
//                null,
//                null
//            )
//        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
//            return uri.path
//        }
//        return null
//    }
//
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    private fun isDocumentUri(context: Context, uri: Uri): Boolean {
//        return DocumentsContract.isDocumentUri(context, uri)
//    }
//
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    private fun getDocumentId(documentUri: Uri): String {
//        return DocumentsContract.getDocumentId(documentUri)
//    }
//
//    /**
//     * Convert Uri into File, if possible.
//     *
//     * @return file A local file that the Uri was pointing to, or null if the
//     * Uri is unsupported or pointed to a remote resource.
//     * @author paulburke
//     * @see .getPath
//     */
//    fun getFile(context: Context, uri: Uri?): File? {
//        if (uri != null) {
//            val path = getPath(context, uri)
//            if (path != null && isLocal(path)) {
//                return File(path)
//            }
//        }
//        return null
//    }
//
//    fun getDriveFilePath(context: Context, uri: Uri): String {
//        val returnUri = uri
//        val returnCursor: Cursor? = context.contentResolver.query(returnUri, null, null, null, null)
//        /*
//         * Get the column indexes of the data in the Cursor,
//         *     * move to the first row in the Cursor, get the data,
//         *     * and display it.
//         * */
//        val nameIndex: Int = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//        val sizeIndex: Int = returnCursor.getColumnIndex(OpenableColumns.SIZE)
//        returnCursor.moveToFirst()
//        val name: String = (returnCursor.getString(nameIndex))
//        val size = java.lang.Long.toString(returnCursor.getLong(sizeIndex))
//        val file = File(context.cacheDir, URLEncoder.encode(name, "utf-8"))
//        try {
//            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
//            val outputStream = FileOutputStream(file)
//            val read: Int = 0
//            val maxBufferSize: Int = 1 * 1024 * 1024
//            val bytesAvailable: Int = inputStream!!.available()       //int bufferSize = 1024;
//            val bufferSize: Int = Math.min(bytesAvailable, maxBufferSize)
//            val buffers = ByteArray(bufferSize) inputStream . use
//                    { inputStream:
//                      InputStream ->
//                        outputStream.use { fileOut ->
//                            while (true) {
//                                val length = inputStream.read(buffers)
//                                if (length <= 0)
//                                    break fileOut . write (buffers, 0, length)
//                            }
//                            fileOut.flush()
//                            fileOut.close()
//                        }
//                    }
//            inputStream.close()
//        } catch (e: Exception) {
//            Log.e("Exception", e.message.toString())
//        }
//        return file.path
//    }
//
//
//
//    /**
//     * Get the file size in a human-readable string.
//     *
//     * @param size
//     * @return
//     * @author paulburke
//     */
//    fun getReadableFileSize(size: Int): String? {
//        val BYTES_IN_KILOBYTES = 1024
//        val dec = DecimalFormat("###.#")
//        val KILOBYTES = " KB"
//        val MEGABYTES = " MB"
//        val GIGABYTES = " GB"
//        var fileSize = 0f
//        var suffix = KILOBYTES
//        if (size > BYTES_IN_KILOBYTES) {
//            fileSize = (size / BYTES_IN_KILOBYTES).toFloat()
//            if (fileSize > BYTES_IN_KILOBYTES) {
//                fileSize = fileSize / BYTES_IN_KILOBYTES
//                if (fileSize > BYTES_IN_KILOBYTES) {
//                    fileSize = fileSize / BYTES_IN_KILOBYTES
//                    suffix = GIGABYTES
//                } else {
//                    suffix = MEGABYTES
//                }
//            }
//        }
//        return dec.format(fileSize.toDouble()) + suffix
//    }
//
//    /**
//     * Attempt to retrieve the thumbnail of given File from the MediaStore. This
//     * should not be called on the UI thread.
//     *
//     * @param context
//     * @param file
//     * @return
//     * @author paulburke
//     */
//    fun getThumbnail(context: Context, file: File?): Bitmap? {
//        return getThumbnail(context, getUri(file), getMimeType(file!!)!!)
//    }
//
//    /**
//     * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
//     * should not be called on the UI thread.
//     *
//     * @param context
//     * @param uri
//     * @return
//     * @author paulburke
//     */
//    fun getThumbnail(context: Context, uri: Uri?): Bitmap? {
//        return getThumbnail(context, uri, getMimeType(context, uri!!)!!)
//    }
//
//    /**
//     * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
//     * should not be called on the UI thread.
//     *
//     * @param context
//     * @param uri
//     * @param mimeType
//     * @return
//     * @author paulburke
//     */
//    fun getThumbnail(context: Context, uri: Uri?, mimeType: String): Bitmap? {
//        if (DEBUG) Log.d(TAG, "Attempting to get thumbnail")
//        if (!isMediaUri(uri!!)) {
//            Log.e(TAG, "You can only retrieve thumbnails for images and videos.")
//            return null
//        }
//        var bm: Bitmap? = null
//        if (uri != null) {
//            val resolver = context.contentResolver
//            var cursor: Cursor? = null
//            try {
//                cursor = resolver.query(uri, null, null, null, null)
//                if (cursor!!.moveToFirst()) {
//                    val id = cursor.getInt(0)
//                    if (DEBUG) Log.d(
//                        TAG,
//                        "Got thumb ID: $id"
//                    )
//                    if (mimeType.contains("video")) {
//                        bm = MediaStore.Video.Thumbnails.getThumbnail(
//                            resolver,
//                            id.toLong(),
//                            MediaStore.Video.Thumbnails.MINI_KIND,
//                            null
//                        )
//                    } else if (mimeType.contains(MIME_TYPE_IMAGE)) {
//                        bm = MediaStore.Images.Thumbnails.getThumbnail(
//                            resolver,
//                            id.toLong(),
//                            MediaStore.Images.Thumbnails.MINI_KIND,
//                            null
//                        )
//                    }
//                }
//            } catch (e: java.lang.Exception) {
//                if (DEBUG) Log.e(TAG, "getThumbnail", e)
//            } finally {
//                cursor?.close()
//            }
//        }
//        return bm
//    }
//
//    /**
//     * File (not directories) filter.
//     *
//     * @author paulburke
//     */
//    var sFileFilter = FileFilter { file ->
//        val fileName = file.name
//        // Return files only (not directories) and skip hidden files
//        file.isFile && !fileName.startsWith(HIDDEN_PREFIX)
//    }
//
//    /**
//     * Folder (directories) filter.
//     *
//     * @author paulburke
//     */
//    var sDirFilter = FileFilter { file ->
//        val fileName = file.name
//        // Return directories only and skip hidden directories
//        file.isDirectory && !fileName.startsWith(HIDDEN_PREFIX)
//    }
//
//    /**
//     * Get the Intent for selecting content to be used in an Intent Chooser.
//     *
//     * @return The intent for opening a file with Intent.createChooser()
//     * @author paulburke
//     */
//    fun createGetContentIntent(type: String?, multiple: Boolean): Intent? {
//        // Implicitly allow the user to select a particular kind of data
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        // The MIME data type filter
//        if (type != null) {
//            intent.type = type
//        }
//        if (multiple) {
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//        }
//        // Only return URIs that can be opened with ContentResolver
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        return intent
//    }
//
//    fun isGoogleDriveUri(uri: Uri): Boolean {
//        return "com.google.android.apps.docs.storage" == uri.authority || "com.google.android.apps.docs.storage.legacy" == uri.authority
//    }
//
//    private fun getDriveFilePath(uri: Uri, context: Context): String? {
//        val returnCursor = context.contentResolver.query(uri, null, null, null, null)
//        /*
//         * Get the column indexes of the data in the Cursor,
//         *     * move to the first row in the Cursor, get the data,
//         *     * and display it.
//         * */
//        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
//        returnCursor.moveToFirst()
//        val name = returnCursor.getString(nameIndex)
//        val size = java.lang.Long.toString(returnCursor.getLong(sizeIndex))
//        val file = File(context.cacheDir, name)
//        try {
//            val inputStream = context.contentResolver.openInputStream(uri)
//            val outputStream = FileOutputStream(file)
//            var read = 0
//            val maxBufferSize = 1 * 1024 * 1024
//            val bytesAvailable = inputStream!!.available()
//
//            //int bufferSize = 1024;
//            val bufferSize = Math.min(bytesAvailable, maxBufferSize)
//            val buffers = ByteArray(bufferSize)
//            while (inputStream.read(buffers).also { read = it } != -1) {
//                outputStream.write(buffers, 0, read)
//            }
//            Log.e("File Size", "Size " + file.length())
//            inputStream.close()
//            outputStream.close()
//            Log.e("File Path", "Path " + file.path)
//            Log.e("File Size", "Size " + file.length())
//        } catch (e: java.lang.Exception) {
//            Log.e("Exception", e.message!!)
//        }
//        return file.path
//    }
//}
