# **Firebase Feature Showcase App**
## **Overview**
This repository contains a sample Android application that demonstrates the integration and usage of various Firebase features. The app adopts a single activity architecture and follows the MVVM (Model-View-ViewModel) design pattern. Through this project, developers can explore the implementation of the following Firebase features:

* Firebase Firestore
* Firebase Realtime Database
* Firebase Remote Config
* Firebase Cloud Messaging
* Firebase Authentication (Google + Email/Password)
* Firebase Storage

## **Getting Started**
To get started with the app, follow these steps:

1. Clone the repository to your local machine:

`git clone https://github.com/Wenancjusz/FirebaseApp.git `  

Open the project in Android Studio.

2. Configure your Firebase project:

  - Create a new Firebase project on the Firebase Console.  
  - Add your Android app to the project using the package name specified in the app/build.gradle file.  
  - Download the google-services.json file and place it in the app/ directory.  
  - Enable the desired Firebase features in the Firebase Console.  

Build and run the app on an emulator or a physical device.

## **Features**
### **1. Firebase Authentication**
Understand the implementation of Firebase Authentication using Google Sign-In and Email/Password authentication. Examine the LoginRegisterFragment and LoginRegisterViewModel classes for authentication logic.

### **2. Firebase Cloud Messaging**
Explore Firebase Cloud Messaging (FCM) integration for sending push notifications. Review the FirebaseCloudMessagingService class to understand how FCM messages are handled.

### **3. Firebase Firestore**
The app showcases the integration of Firebase Firestore CRUD operations. Explore the FirestoreRepository and FirestoreViewModel classes to understand how data is retrieved, displayed, saved and deleted.

### **4. Firebase Realtime Database**
Learn about the integration of CRUD operations in Firebase Realtime Database through the RealtimeDatabaseFragment and RealtimeDatabaseViewModel classes.

### **5. Firebase Remote Config**
Discover how Firebase Remote Config is used to remotely configure the app's behavior without publishing an app update. Check out the RemoteConfigFragment class for implementation details.

### **6. Firebase Storage**
See how Firebase Storage is used for storing and retrieving files in the cloud. Check the StorageFragment and StorageViewModel classes for implementation details.

## **Architecture**
The app follows a single activity approach with the MVVM architecture. The project structure is organized to provide a clear separation of concerns, making it easy to understand and extend.
