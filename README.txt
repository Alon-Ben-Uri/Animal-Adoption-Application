I'd like to start by saying that this project is meant to be made by 2-3 people, in roughly 2 or 3 months time.
Because of bad time management on my side, I only had a little over a week to program it alone from scratch.
Therefore there are many things I would've changed had I more time - which I'll go over in the last part of the video - but 
I never got to it. Of course that in real world application I would never do such a thing, but in this case I had to 
have something to present to the university (the final grade was 80/100).

There are two side to this project, a server side and a client side. The server side operates from the PC while the client operates 
from a smartphone. The server thread starts a clienthandler thread for each client that starts up the app. The client communicates with
the server and database through the clienthandler, which receives requests from the client and handles them. 
On the client side (which was written in JAVA using Android Studio) the client is first introduced to an activity (which is another word for screen)
which gives the client 3 options: sign in, sign up and enter as guest.
If the client chooses to sign up, a request with the corresponding details is sent to the clienthandler, and it in returns checks that the 
username and email aren't already registered in the database. If everything is well, the new user is added to the database.
The client can also enter the app using the sign in options or even enter as a guest, which allows him to view animals that are set for 
adoptiong and their owner's info.
The only difference between a guest and a user is that the use can set animals for adoption.
The set for adoption button takes the user to a new screen in which he's required to add the type, age, breed, etc. Each animal has it's own 
unique animal_id, and the same goes for users of course.
After animals have been set for adoption, a client can search for animals by attributes to find an animal that suites him most.

There are many things I'd change if I had the time, such as make an entire new User class and separate
the username email from the Animal class. I'd also set a lock to the database object so client requests won't overlap. I'd change the request 
handling in clienthandler so it's more readable by making a function for reach request, instead of them being very large if statements in an
already large enough while loop, etc. This project doesn't reflect my programming skills, but rather my ability to learn and use new technologies. 


