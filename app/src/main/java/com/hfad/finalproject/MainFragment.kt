package com.hfad.finalproject

import Artist
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog


class MainFragment : Fragment() {

    private lateinit var loginButton: Button
    private lateinit var createUserButton: Button
    private lateinit var username: EditText
    private lateinit var password: EditText

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sqLiteDatabase : SQLiteDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        databaseHelper = DatabaseHelper(requireContext())
        sqLiteDatabase = databaseHelper.writableDatabase

        loginButton = view.findViewById(R.id.loginButton)
        createUserButton = view.findViewById(R.id.createUserButton)
        username = view.findViewById(R.id.username)
        password = view.findViewById(R.id.password)

        loginButton.setOnClickListener {
            val usernameInput = username.text.toString()
            val passwordInput = password.text.toString()
            var isAuthenticated = false;

            //comment out 50-54 for no login
            if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                showAlertDialog("Username or password cannot be empty")

                return@setOnClickListener
            }

            isAuthenticated = databaseHelper.userAuthentication(usernameInput, passwordInput)

            if (isAuthenticated) {
                User.currentUser = usernameInput

                val artist_or_not = databaseHelper.artist_or_viewer(usernameInput)

                if(artist_or_not){
                    Artist.currentArtist = databaseHelper.getArtistName(usernameInput)
                }
                else{
                    Artist.currentArtist = "viewer"
                }
                val intent = Intent(requireContext(), ArtistViewActivity::class.java)
                startActivity(intent)
            } else {
                showAlertDialog("Invalid username or password")
            }

            //uncomment 76-77 for no login
//            val intent = Intent(requireContext(), ArtistViewActivity::class.java)
//            startActivity(intent)
        }

        createUserButton.setOnClickListener {
            val intent = Intent(requireContext(), CreateUserActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun showAlertDialog(message: String){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setNegativeButton("Ok") {dialog, which ->
            //do nothing
        }
        builder.show()
    }
}