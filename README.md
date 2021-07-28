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
           implementation 'com.github.yehiareda4:MiraFilePiker:1.0.0' //choose latest version
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
                                if (it.data.getData()!=null){
                                    File file=FileUtils.getFile(this,data.getData())
                                    selectedFiles.add(file.getName())
                                }
                                if (it.data.getClipData()!=null){
                                    for(int i = 0; i < it.data.getClipData().getItemCount(); i++) {
                                        Uri uri = it.data.getClipData().getItemAt(i).getUri()
                                        File file=FileUtils.getFile(this,uri)
                                        selectedFiles.add(file.getName())
                                    }
                                }
                    }
        }

### on Action
        val intent= Intent(this, MiraFilePickerActivity.class)
        intent.putExtra("multiple",true)
        intent.putExtra("type","*/*")
        previewRequest.launch(openFileIntent)