package com.hfad.finalproject

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible

class CreateUserFragment : Fragment() {
    private lateinit var userUsername: EditText
    private lateinit var userPassword: EditText
    private lateinit var createUserButton: Button
    private lateinit var radioGroup: RadioGroup
    private lateinit var artistButton: RadioButton
    private lateinit var viewerButton: RadioButton
    private lateinit var artistName: EditText
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sqLiteDatabase: SQLiteDatabase
    private var artist = false
    private var viewer = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_user, container, false)

        userUsername = view.findViewById(R.id.userUsername)
        userPassword = view.findViewById(R.id.userPassword)
        createUserButton = view.findViewById(R.id.createUserButton)
        radioGroup = view.findViewById(R.id.radioGroup)
        artistButton = view.findViewById(R.id.artistButton)
        viewerButton = view.findViewById(R.id.viewerButton)
        artistName = view.findViewById(R.id.artistName)

        databaseHelper = DatabaseHelper(requireContext())
        sqLiteDatabase = databaseHelper.writableDatabase

        artistName.isVisible = false

        artistButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                artist = true
                artistName.isVisible = true
            }
        }
        viewerButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                artist = false
                artistName.isVisible = false
            }
        }

        createUserButton.setOnClickListener {
            val username = userUsername.text.toString()
            val password = userPassword.text.toString()
            var artistName = artistName.text.toString()

            if(username.isEmpty() || password.isEmpty()) {
                showAlertDialog("Please enter a value for all fields" )
            }
            else if (!artistButton.isChecked && !viewerButton.isChecked) {
                showAlertDialog("Please select Artist or Viewer" )
            }
            else if (artistButton.isChecked && artistName.isEmpty()){
                showAlertDialog("Please enter an Artist Name")
            }
            else {
                if (artistName.isEmpty()){
                    viewer = true
                    artistName = "n/a"
                    Artist.currentArtist = "viewer"
                }

                val usernameExists = databaseHelper.usernameExists(username)
                val artistExists = databaseHelper.artistExists(artistName)

                if (usernameExists){
                    showAlertDialog("Username Already Exists. Please Try Again")
                }
                else if (artistButton.isChecked && artistExists) {
                    showAlertDialog("Artist Name Taken. Please Try Again")
                }
                else {
                    databaseHelper.insertUser(username, password, artist, artistName)
                    User.currentUser = username

                    val intent = Intent(requireContext(), ArtistViewActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        return view
    }

    private fun showAlertDialog(message: String){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Invalid Entry")
        builder.setMessage(message)
        builder.setNegativeButton("Ok") {dialog, which ->
            //do nothing
        }
        builder.show()
    }
}