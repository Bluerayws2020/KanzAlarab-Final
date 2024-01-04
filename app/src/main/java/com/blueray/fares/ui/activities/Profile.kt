package com.blueray.fares.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.window.SplashScreen
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import com.blueray.fares.R
import com.blueray.fares.databinding.ActivityMainBinding
import com.blueray.fares.databinding.ActivityProfileBinding
import com.blueray.fares.databinding.EditProfilessBinding
import com.blueray.fares.databinding.FragmentYourChannelBinding
import com.blueray.fares.helpers.HelperUtils
import com.blueray.fares.helpers.ViewUtils.hide
import com.blueray.fares.helpers.ViewUtils.show
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.ui.viewModels.AppViewModel
import com.blueray.fares.videoliveeventsample.util.showToast
import com.bumptech.glide.Glide
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Profile : BaseActivity() {
    private val IMAGES_CAMERA_REQUEST_CODE = 1002
    private val IMAGES_REQUEST_CODE = 100

    private lateinit var binding : EditProfilessBinding
    private val mainViewModel by viewModels<AppViewModel>()
    private lateinit var navController: NavController


    private lateinit var currentPhotoPath: String

    private lateinit var imageData: String
    private var imageFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = EditProfilessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel.retriveViewUserProfile()
getUserProifle()
        binding.tollbars.back.setOnClickListener {
            onBackPressed()
        }

getUpdateUserProfile()

        binding.tollbars .logout.show()
        binding.tollbars .logout.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(title)
                builder.setMessage("هل انت متاكد من تسجيل الخروج ؟")

                builder.setPositiveButton("نعم") { dialog, _ ->
                    val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)

                    sharedPreferences.edit().apply {
                        putString(HelperUtils.UID_KEY, "0")




                    }.apply()            // go to home activity
                    startActivity(Intent(this,com.blueray.fares.ui.activities.SplashScreen::class.java))
                }

                builder.setNegativeButton("لا") { dialog, _ ->
                    dialog.dismiss()
                }

                val dialog = builder.create()
                dialog.show()



        }


        binding.userImagee.setOnClickListener {
            showImagePickerDialog(pickImageLauncher, IMAGES_REQUEST_CODE)

        }
        binding.edits.setOnClickListener {
            binding.progressBar.show()
            imageFile?.let { it1 ->
                mainViewModel.updateUserProfile(binding.nameEt.text.toString(),"",binding.emailTxt.text.toString(),binding.phoneTxt.text.toString(),
                    it1,binding.genderEt.text.toString(),binding.birthDateTxt.text.toString()
                )
            }
        }



    }
    fun getUpdateUserProfile(){


        mainViewModel.getUpdateUserLive().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
binding.progressBar.hide()
                  showToast(result.data.status.msg.toString())
                }

                is NetworkResults.Error -> {

                    Log.d("ERRRRor",result.exception.toString())
                }
                is NetworkResults.NoInternet -> TODO()
            }
        }

    }

    fun getUserProifle(){


        mainViewModel.getUserProfile().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {

                    val  data = result.data[0]
                    Glide.with(this).load(result.data[0].user_picture).placeholder(R.drawable.logo2).into(binding.userImagee)
                    binding.userNameEt.setText(data.username)

                    binding.phoneTxt.setText(data.phone_number)
                    binding.emailTxt.setText(data.profile_data.mail)
binding.nameEt.setText(data.profile_data.first_name )

binding.genderEt.setText(data.profile_data.gender.toString())
                    binding.birthDateTxt.setText(data.profile_data.birth_date.toString())

                }

                is NetworkResults.Error -> {

                    Log.d("ERRRRor",result.exception.toString())
                }
                is NetworkResults.NoInternet -> TODO()
            }
        }

    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { handleFirstImageUpload(it) }
        }
    }

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun showImagePickerDialog(launcher: ActivityResultLauncher<Intent>, requestCode: Int) {
        val options = arrayOf( "المعرض")
        AlertDialog.Builder(this)
            .setTitle("يرجى الاختيار")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> { // Take Photo option

//                        launchCameraIntent(IMAGES_CAMERA_REQUEST_CODE)
                        requestGalleryPermissionAndLaunchPicker(launcher, requestCode)

                    }
                    1 -> { // Choose from Gallery option
                        requestGalleryPermissionAndLaunchPicker(launcher, requestCode)
                    }
                }
            }
            .show()
    }
    private fun requestGalleryPermissionAndLaunchPicker(launcher: ActivityResultLauncher<Intent>, requestCode: Int) {
        val currentPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, currentPermission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(currentPermission), requestCode)
        } else {
            launcher.launch(Intent(Intent.ACTION_PICK).apply { type = "image/*" })
        }
    }

    private fun launchCameraIntent(requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, ask for it
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), requestCode)
        } else {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Ensure that there's a camera activity to handle the intent
                takePictureIntent.resolveActivity(packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.blueray.fares.fileprovider",  // replace with your FileProvider's authority
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(
                            takePictureIntent,
                            requestCode
                        )  // Use the passed requestCode here
                    }
                }
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGES_CAMERA_REQUEST_CODE -> {
                    handleFirstImageUploadFromCamera()
                }

                // Handle other request codes here...
            }
        }
    }


    private fun getFilePathFromUri(uri: Uri?): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = uri?.let { contentResolver?.query(it, projection, null, null, null) }

        return if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val filePath = cursor.getString(columnIndex)
            cursor.close()
            filePath
        } else {
            uri?.path ?: ""
        }
    }

    private fun handleFirstImageUpload(data: Intent) {
        val uri = data.data
        imageData = getFilePathFromUri(uri)
        imageFile = File(imageData)
//        binding.userImagee.setIma
//        binding.txtphto.text = "تم التحميل"
//        imgFilePhto = imageFile
        Glide.with(this).load(uri).into(binding.userImagee)

    }

    private fun handleFirstImageUploadFromCamera() {
        imageData = currentPhotoPath
        imageFile = File(imageData)

    }

}