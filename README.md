[![](https://jitpack.io/v/yehiareda4/MiraFile.svg)](https://jitpack.io/#yehiareda4/MiraFile)

# Preview

![](https://github.com/yehiareda4/MiraFilePiker/blob/master/preview/preview2.jpeg) ![](https://github.com/yehiareda4/MiraFilePiker/blob/master/preview/preview1.jpeg)
![](https://github.com/yehiareda4/MiraFilePiker/blob/master/preview/preview3.jpeg)

# MiraFilePicker

MiraFilePicker is an Android Library that lets you choose any kind of file easyly. It handles the
storage permission itself.

# Minimum Sdk Version

    Api Level 19 or Above

# Max Sdk Version

    Api Level 30 and Above (Compatible With Scope Storage)

# Installation

Add the following code snippet in your project level gradle file

    repositories {
        ...
        maven { url 'https://jitpack.io' }
      }

Add the dependency in your app gradle

    dependencies {
           ...
           implementation 'com.github.yehiareda4:MiraFile:$latest_version' //choose latest version
      }

# Usage

### Permission And Manifest

    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission
        android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        tools:ignore="ScopedStorage" />
    <application
         ...
         android:preserveLegacyExternalStorage="true"
         android:requestLegacyExternalStorage="true"/>

### Start MiraFilePicker form activity and pass the MimeType of the target file

### Override the OnCreate

    @SuppressLint("NotifyDataSetChanged")
    private fun setFilesSheet() {
        toast =
            requireContext().createToast(getString(R.string.upload_file_error), ToastType.WARNING)
        val types: MutableList<String> = ArrayList()
        types.add(PickerTypesSheet.MIME_TYPE_IMAGE)
        types.add(PickerTypesSheet.MIME_TYPE_PDF)
        typesSheet = PickerTypesSheet(
            requireActivity() as AppCompatActivity,
            this,
            types,
            camera = true,
            multiple = true,
            multipleCount = maxCount
        ) { file, max ->
            listFiles.add(file)
            listFilesPaths.add(file.path)
            filesAdapter.notifyDataSetChanged()

            if (max) {
                toast?.show()
            }
        }
    }

### on Action

        typesSheet.show()

### or

        typesSheet.show(sizeList)

### Default

        typesSheet.show(sizeList: Int = 0, type: Type? = null, cleanImages: Boolean = false)

### To change multi or single select

        typesSheet.multiple = BOOLEAN_VALUE
        typesSheet.multipleCount = INT_VALUE

#### Configuration file returned:

Property | Type | Description
--- | --- | ---
file | File | Selected file file
name | String | Selected file name 
size | String | Selected file size 
path | String | Selected file path 
extension | String | Selected file extension 
filePart | MultipartBody.Part? | Selected file filePart 
mediaType | String | Selected file mediaType 
Thumbnail | String | Selected file Thumbnail 
ThumbnailPart | MultipartBody.Part? | Selected file ThumbnailPart 
duration | String | Selected file duration 

#### Version Log:

* ***v1.0.0*** support Java and Kotlin and migrate to AndroidX
