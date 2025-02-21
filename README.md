# Animas-Gallery
Animas Gallery README File:

Description: This app is intended to be used as a free virtual gallery viewing platform. Artists can create an account and upload pieces of their own to be displayed to the public. Viewers may create an account to browse artist galleries. App is preloaded with guest profile as well as two artist profiles with work in their "gallery" page. 

This is a Kotlin project, running it in Android Studio or your preferred IDE with an emulator will be the best way to view this project. 

Premade Artist account:
	- Username: animas_photos
	- password: test123
	- must have photos on your device to upload a photo/create a piece

Premade Viewer account:
	- Username: guest
	- password: guest

**How to use in view only mode as a developer / code reviewer: **

	How to enter without a login:
	- Open MainFragment -> Comment out lines 50-54 -> Uncomment lines 76-77 -> Run App -> Click login
	
	* viewing only
		- will not be able to update an accounts username/password
		- will not be able to upload a piece 

**How to use with full functionality:**
		
	Creating an artist account:
	- If you want to be able to upload art: create user -> enter fields -> click Artist -> enter Artist name -> submit

	Creating a viewer account:
	- If you want to browse art only: create user -> enter fields -> click Viewer -> submit

	Uploading art:
	- Login as an artist (must have created an artist account) -> Hit plus button in toolbar -> enter required fields -> submit

	How to view art:
	- Login -> Click an artist name -> Click on a piece to view details 

	How to edit username and password:
	- Login -> Click settings button in toolbar -> Edit values -> Submit

	How to logout:
	- Login -> Click logout button on home page

	Home button:
	- If anywhere other than home (viewing artist names), click home button in toolbar to return to home page(artist name list)

	


