package com.hfad.finalproject

import android.app.DatePickerDialog
import android.content.ContentResolver
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.util.Calendar
import androidx.activity.result.contract.ActivityResultContracts
import java.io.ByteArrayOutputStream
import android.util.Base64
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible

class CreateWorkFragment : Fragment() {

    private lateinit var workName: EditText
    private lateinit var workDesc: EditText
    private lateinit var workDate: Button
    private lateinit var artistName: EditText
    private lateinit var imagePreview: ImageView
    private lateinit var uploadButton: Button
    private lateinit var createWorkButton: Button
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sqLiteDatabase: SQLiteDatabase
    private lateinit var contentResolver: ContentResolver
    private lateinit var dateSelected: TextView
    private lateinit var dateSelectedText: TextView

    private val IMG_TAG = 1

    private var encoded: String = ""
    private lateinit var name: String
    private lateinit var desc: String
    private lateinit var date: String
    private lateinit var artist: String

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val bitmap = contentResolver.openInputStream(it)?.use { stream ->
                    BitmapFactory.decodeStream(stream)
                }
                bitmap?.let { bmp ->
                    encoded = encodeImageToBase64(bmp)
                    encoded.let {
                        val decodedBytes = Base64.decode(it, Base64.DEFAULT)
                        val bitmapPreview = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                        imagePreview.setImageBitmap(bitmapPreview)
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_create_work, container, false)

        contentResolver = requireContext().contentResolver

        workName = view.findViewById(R.id.workName)
        workDesc = view.findViewById(R.id.workDescription)
        workDate = view.findViewById(R.id.workDate)
        artistName = view.findViewById(R.id.artistName)
        imagePreview = view.findViewById(R.id.imagePreview)
        uploadButton = view.findViewById(R.id.uploadButton)
        createWorkButton = view.findViewById(R.id.createWorkButton)
        dateSelectedText = view.findViewById(R.id.dateSelectedText)
        dateSelected = view.findViewById(R.id.dateSelected)
        databaseHelper = DatabaseHelper(requireContext())
        sqLiteDatabase = databaseHelper.writableDatabase

        artistName.setText(Artist.currentArtist)

        workDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                date = String.format("%d/%d/%d", selectedMonth + 1, selectedDay, selectedYear)

                dateSelected.text = date
                dateSelected.isVisible = true
            }, year, month, day)

            datePickerDialog.show()
        }

        uploadButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        createWorkButton.setOnClickListener {
            name = workName.text.toString()
            desc = workDesc.text.toString()
            date = dateSelected.text.toString()
            artist = artistName.text.toString()

            if(name.isEmpty() || desc.isEmpty() || date.isEmpty() || artist.isEmpty() || encoded.isEmpty()){
                showAlertDialog()
            }
            else {
                databaseHelper.insertWorks(name, desc, date, encoded, artist)
                val intent = Intent(requireContext(), ArtistViewActivity::class.java)
                startActivity(intent)
            }
        }
        return view
    }

    private fun encodeImageToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    private fun showAlertDialog(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("All Fields Required")
        builder.setMessage("Enter a value in all fields and upload a photo please.")
        builder.setNegativeButton("Ok") {dialog, which ->
            //do nothing
        }
        builder.show()
    }
}