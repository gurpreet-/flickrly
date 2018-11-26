
  
    
    
# Flickrly  
  
A modern way to browse the public photos of Flickr.  
    
### Getting started  
You can either grab the APK file and install it using the [releases](https://github.com/gurpreet-/flickrly/releases) tab.   
  
Or you can `git clone` and then build. Using IntelliJ or Android is recommended.  
  
```sh  
$ git clone <this_repo_url>.git  
$ ./gradlew build  
$ ./gradlew installDebug  
```

### Running tests
You need a device that is connected via ADB for the tests to work.

```sh
$ ./gradlew connectedCheck
```

  
### Credits  
* The Flickr team for opening their [API](https://www.flickr.com/services/feeds/) and supporting a large variety of response formats.  
* [Paper](https://github.com/pilgr/Paper) for their really great database.  
* [Dagger](https://google.github.io/dagger/) and Google.  
* [Iconics](https://github.com/mikepenz/Android-Iconics) by MikePenz.