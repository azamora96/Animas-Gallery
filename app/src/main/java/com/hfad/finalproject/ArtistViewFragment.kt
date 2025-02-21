package com.hfad.finalproject

import Artist
import ArtistCursorAdapter
import User
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView

class ArtistViewFragment : Fragment() {

    private lateinit var listView: ListView
    private var itemClickListener : AdapterView.OnItemClickListener? = null
    private lateinit var logout: Button
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sqLiteDatabase: SQLiteDatabase
    private lateinit var cursor: Cursor
    private lateinit var pageTitle: TextView
    private lateinit var instructionText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_artist_view, container, false)

        logout = view.findViewById(R.id.logoutButton)
        listView = view.findViewById(R.id.artistList)
        pageTitle = view.findViewById(R.id.pageTitle)
        instructionText = view.findViewById(R.id.instructionText)

        if(!User.currentUser.isEmpty()){
            val title = String.format("Welcome, " + User.currentUser + "!")
            pageTitle.text = title
        }

        databaseHelper = DatabaseHelper(requireContext())
        sqLiteDatabase = databaseHelper.readableDatabase

        cursor = databaseHelper.getAllArtists()

        val adapter = ArtistCursorAdapter(requireContext(), cursor)
        listView.adapter = adapter
        listView.onItemClickListener = itemClickListener

        logout.setOnClickListener {
            Artist.currentArtist = ""
            User.currentUser = ""

            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    fun updatePageTitle(userUpdate: String) {
        val title = String.format("Welcome, " + userUpdate + "!")
        pageTitle.text = title
    }

    fun setOnItemClickListener(onItemClickListener : AdapterView.OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cursor.close()
    }
}