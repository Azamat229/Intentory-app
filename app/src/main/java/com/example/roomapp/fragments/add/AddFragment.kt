package com.example.roomapp.fragments.add

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.roomapp.R
import com.example.roomapp.model.User
import com.example.roomapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import java.io.File
import java.io.IOException

class AddFragment : Fragment() {

    private lateinit var mUserViewModel: UserViewModel

    private lateinit var mImageView: ImageView
    companion object{
        val SELECT_IMAGE_CODE = 100
    }

    lateinit var photoUrl:Uri
//    var photoUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        view.open_g.setOnClickListener { openGallery() }


        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        view.add_btn.setOnClickListener {
            insertDataToDatabase()
        }

        return view
    }

    private fun insertDataToDatabase() {
        val productName = addProductName_et.text.toString()
        val description = addDescriptionN_et.text.toString()
        val price = addPrice_et.text

        if(inputCheck(productName, description, price)){
            // Create User Object
            val user = User(
                0,
                productName,
                description,
                Integer.parseInt(price.toString()),
                photoUrl.toString()

            )
            // Add Data to Database
            mUserViewModel.addUser(user)
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()
            // Navigate Back
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(firstName: String, lastName: String, age: Editable): Boolean{
        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && age.isEmpty())

    }


    fun openGallery(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture.."), SELECT_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == SELECT_IMAGE_CODE && resultCode == AppCompatActivity.RESULT_OK){
            photoUrl = data?.data!!
            photoUrl.let {

                view?.add_image?.setImageURI(photoUrl)
            }
//            if(data != null){
//                try {
//                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
//                        requireActivity().application.contentResolver,
//                        data.data
//                    )

//                    view?.add_image?.setImageBitmap(bitmap)
//                    Log.v("bitmap", bitmap.toString())
//                    photoUrl = bitmap.toString()
////                    val f = File(bitmap.toString())
////                    val yourUri = Uri.fromFile(f)
////                    photoUrl = yourUri
//                    mImageView.setImageBitmap(bitmap)
//                    mImageView.setImageBitmap(bitmap)
//                }
//                catch (exp: IOException){
//                    exp.printStackTrace()
//                }
//            }
//            else if(resultCode == AppCompatActivity.RESULT_CANCELED){
//                Toast.makeText(requireContext(), "Canceled", Toast.LENGTH_LONG).show()
//            }
        }

    }

}