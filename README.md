[![](https://jitpack.io/v/yehiareda4/MiraFilePiker.svg)](https://jitpack.io/#yehiareda4/MiraFilePiker)

# Preview

![](https://github.com/yehiareda4/MiraFilePiker/blob/master/preview/preview2.jpeg) ![](https://github.com/yehiareda4/MiraFilePiker/blob/master/preview/preview1.jpeg)
![](https://github.com/yehiareda4/MiraFilePiker/blob/master/preview/preview3.jpeg)


# MiraFilePicker
MiraFilePicker is an Android Library that lets you choose any kind of file easyly. It handles the storage permission itself.

# Minimum Sdk Version
    Api Level 19 or Above
# Max Sdk Version
    Api Level 30 (Compatible With Scope Storage)

# Installation

Add the following code snippet in your project level gradle file

    repositories {
        ...
        maven { url 'https://jitpack.io' }
      }
    
Add the dependency in your app gradle

    dependencies {
           ...
           implementation 'com.github.yehiareda4:MiraFilePiker:$latest_version' //choose latest version
      }
      
# Usage

### Start MiraFilePicker form activity and pass the MimeType of the target file
#### KOTLIN
    Intent intent=new Intent(this, MiraFilePickerActivity.class);
    intent.putExtra("multiple",true);
    intent.putExtra("type","*/*");
    startActivityForResult(intent,REQUEST_CODE);
#### KOTLIN
    Intent intent=new Intent(this, MiraFilePickerJavaActivity.class);
    intent.putExtra("multiple",true);
    intent.putExtra("type","*/*");
    startActivityForResult(intent,REQUEST_CODE);
      
### Override the onActivityResult
  
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE&&resultCode==RESULT_OK&&data!=null){
            if (data.getData()!=null){
                File file=FileUtils.getFile(this,data.getData());
                selectedFiles.add(file.getName());
            }
            if (data.getClipData()!=null){
                for(int i = 0; i < data.getClipData().getItemCount(); i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    File file=FileUtils.getFile(this,uri);
                    selectedFiles.add(file.getName());
                }
            }
        }
    }

### Start MiraFilePicker form Fragment and pass the MimeType of the target file
        val previewRequest =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    if (it.requestCode == REQUEST_CODE && it.resultCode == RESULT_OK && it.data != null){
                        if (it.resultCode == RESULT_OK) {
                            var files: File? = null
                            if (it.data?.data != null) {
                                val file: File = FileUtils.getFile(requireContext(), it.data?.data)
                            }
                            if (it.data?.clipData != null) {
                                for (i in 0 until it.data?.clipData?.itemCount!!) {
                                    val uri: Uri = it.data?.clipData?.getItemAt(i)?.uri!!
                                    val file: File = FileUtils.getFile(requireContext(), uri)
                                }
                            }
                        }
                    }
                }

### on Action
        val intent= Intent(this, MiraFilePickerActivity.class)
        intent.putExtra("multiple",true)
        intent.putExtra("type","*/*")
        previewRequest.launch(openFileIntent)