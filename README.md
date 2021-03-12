# Android Google Assistant POC 
<img width="200" alt="portfolio_view" src="https://logodownload.org/wp-content/uploads/2020/02/google-assistant-logo-2.png">

This project is the POC of Google Assistan which proofing developer can create a custom voice command and receive corresponding (e.g Hey Google! Open Example App and go to Causeway Bay!)

## Contents
- [Requirement](https://www.google.com)
- [Step of development](https://www.google.com)
- [Code Explaination](https://www.google.com)

## Requirement
- integrated Firebase Dynamic Link
- Install [App Actions Test Tool](https://developers.google.com/assistant/app/test-tool) plugin in Android Studio
- Understand the [Build in App Actions intents](https://developers.google.com/assistant/app/reference/built-in-intents) & [Custom intents](https://developers.google.com/assistant/app/custom-intents)
- Have a google developer account to publish the app

## Step of Development
1. Handle the deeplink (Firebase Dynamic Linking)
2. Declare different actions (Build in App Actions Intents & Custom Intent)
3. Upload the apk/aab file in Google Play Console
4. Make sure mobile/Android Studio/Google Assistant's google account and language is same
5. Turn on the App Actions Test Tool to update the action previews when run the project
6. Testing the voice command in google assistant or text command in test tool

## Code Explaination

### manifests
##### Declare the deeplink in Android manifests
```xml
<activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
            <intent-filter
                android:autoVerify="true"
                tools:targetApi="m">
                <action android:name="android.intent.action.VIEW" />

                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />

                <!--dynamic link of Firebase-->
                <data
                    android:host="poor.page.link"
                    android:scheme="https" />
            </intent-filter>
        </activity>
```

##### Declare the actions.xml file and located in manifest
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="com.andy8786.poor">

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Poor">
		...
        <!--declare the actions of google assistant-->
        <meta-data
            android:name="com.google.android.actions"
            android:resource="@xml/actions" />
		...
    </application>

</manifest>
```

### actions.xml
##### create the actions.xml under res->xml->actions.xml
- ##### using build in action intents
Learn More about how to use [App Actions](https://developer.android.com/guide/actions/index.html)
OPEN_APP_FEATURE is the build in intent for using voice command to open app. You can say "Hey Google! Open **[app name]** **[feature]**" to receive the feature parameters.
@array/ExampleQueries is placed in strings.xml
```xml
<?xml version ="1.0" encoding ="utf-8"?>
<!--  Learn More about how to use App Actions: https://developer.android.com/guide/actions/index.html -->
<actions>
    <!--Official intent that google provide for opening app-->
    <action intentName="actions.intent.OPEN_APP_FEATURE">
        <fulfillment urlTemplate="https://poor.page.link/access{?featureName}">
            <parameter-mapping
                intentParameter="feature"
                urlParameter="featureName" />
        </fulfillment>

        <parameter name="feature">
            <entity-set-reference entitySetId="FeatureEntitySet" />
        </parameter>
		
		<entity-set entitySetId="FeatureEntitySet">
			<entity
				name="TripSummary"
				identifier="Trip" />
			<entity
				name="Profile"
				identifier="Profile" />
			<entity
				name="History"
				identifier="History" />
			<entity
				name="Record"
				identifier="Record" />
		</entity-set>
    </action>
	...
</actions>
```

- #####using custom intents
Learn More about how to use [Custom Intents](https://developers.google.com/assistant/app/custom-intents)
EXAMPLE_INTENT is the custom intent for using custom voice command to open app and received location parameter. You can say "Hey Google! Open **[app name]** and go to **[location]**".
```xml
<?xml version ="1.0" encoding ="utf-8"?>
<actions>
	...
    <action intentName="custom.actions.intent.EXAMPLE_INTENT" queryPatterns="@array/ExampleQueries">
        <parameter name="location" type="https://schema.org/Text" />
        <fulfillment urlTemplate="https://poor.page.link/navigation{?locationName}">
            <parameter-mapping intentParameter="location" urlParameter="locationName" />
        </fulfillment>
    </action>
	...
</actions>
```

### strings.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">POC App</string>
    <string-array name="ExampleQueries">
        <!--you can declares more voice command for more accuracy-->
        <item>to $location</item>
        <item>go to $location</item>
    </string-array>
</resources>
```

### MainActivity.kt
The handleDeepLink function is receive the voice message parameters and pass back to the activity class.
```kotlin
class MainActivity : AppCompatActivity() {
    ...
    private fun handleDeepLink(data: Uri?){
        //handle the actions
        when(data?.path)
        {
            "/access" -> {   //for open app features
                val featureType = data?.getQueryParameter("featureName").orEmpty()
                mainBinding.inputField.setText("Feature Type: $featureType");
            }
            "/navigation" -> {  //for custom query location features
                val locationName = data?.getQueryParameter("locationName").orEmpty()
                mainBinding.inputField.setText("Location Name: $locationName");
            }
        }
    }
}
```
