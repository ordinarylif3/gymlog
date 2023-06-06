Gym Log is our mobile application that makes it easier to track your workouts and progress in the gym as well as see your friends’ workouts and be a part of the gym community.

As we proceeded with implementing the features after the midterm report, we realized that some of the features we wanted to implement would be challenging to do. We did accomplish the core functions to a certain extent but others were outside of the scope of this project.
Below we will describe in detail what those were and what challenges we faced.

Tasks Accomplished for Midterm Report:
Navigation for the header is fully set up and currently working on the individual navigation for all the clickables.
We were able to get Firebase to authenticate users and create user accounts. As well as using Firebase real-time database to store user information such as full name and email which is different from the auth services. With this, we can query posts and text for every user and their workouts.
Profile page implemented a feature to allow the user to upload their own user profile images.

Tasks Accomplished for Final Report:
The following two pages are fully implemented with a lot of database query features.

Tracking Page:
	In this page we were able to get 2 different dialogs to popup. If the user pressed on the “plus” button. It will show a dialog box to enter the session name. Then it will take the user to the actual tracking page to input the exercise name, repetition, set and weight value. It will add to the database under that unique sessionID. Once the workout is added, the tracking page will query the session and grab the new workout and display the new workout in the lazy column. The column will update with every new exercise being added.

Home Page:
	On the home page, this displays all the workout that the user added to the database. If the user pressed on any of the sessions, it will take them to a workout detail page which will display that session's information. That means they will be able to see all the different workouts they did for that one session.


What we could not get done:

There are some features that we were unable to implement such as the friend’s feed, friend list as well as the chart. As we spent most of our time trying to query the data from the database, we realized that if we wanted to implement what we wanted to do we would need to add a couple more collections and possibly restructure the database. Something to uniquely identify all friends, a collection to hold the post in the feed page by itself and creating a new way to hold the workout information so when we query the data for the charts, we do not need to rearrange everything and use multiple data structures to make it work.

All these challenges could be overcome and features implemented with more time but we were unable to do so within the project timeline.
