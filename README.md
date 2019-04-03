# Sentiment Analysis Visualization Framework
### By Team 6: Xun Zhou, Siyue Jin
![](https://i.imgur.com/DSF3puE.png)
![](https://i.imgur.com/UrXKkRT.png)
## Overview
Sentiment Analysis Visualization Framework is a data visualization framework that applies sentiment analysis on text data using Google Natural Language API. Sentiment analysis aims to determine the attitude or emotional reaction of a writer with respect to the text content.

This documentation will go over the key features of the framework, the most important APIs of the framework and plugin, and how to implement and run new plugins for the framework.

The framework will need two API keys to run. One is a credential from Google Cloud Platform to run the Google Nautral Language API services. The other is a set of access tokens of Twitter API to run our Twitter data plugin. Instructions on how to obtain these API keys could be found at the bottom of this documentation.
## Key Features
- Supports a wide range of data input plugins. As long as the source data contains a **text content** and a **timestamp**, the framework will be able to process and visualize the data. Our team has developed sample data input plugins that could query data from Twitter (with Twitter4j API), Amazon product reviews (with Jsoup web scrapping) and Yelp review dataset (with json and csv reader).
- Supports a wide range of visualization plugins. There are many interesting ways to visualize the result of the sentiment analysis. Our team has developed sample visualization plugins that could display sentiment score over time as a line graph, display sentiment score and magnitude over time as a bubble chart, and display the proportion of different sentiment types as a pie chart.
- Supports simultaneous visualization of multiple data sources. It is very useful to compare the sentiment analysis of different data sources by putting them on the same chart.
- Supports simultaneous display of multiple visualization plugins. It is very useful to visualize the same data sources from different perspectives.
- Supports flexible and dynamic control of the visualization. We provide an API of control panel and control point that allows plugin writers to control how processed data should be displayed at run time. Some sample control points include timestamp range filter, sentiment range filter, location filter, keyword filter, etc.
- Supports plugin management. The framework GUI provides a Settings panel in the File menu. The user could choose from the loaded data plugins and visualization plugins.
![enter image description here](https://i.imgur.com/vnLMlYy.png)
- Supports data source management. All data sources loaded by the data plugins are recorded by the data source manager. The users could easily remove or reload a data source at run time. It is automatically taken care of by the framework, and the plugin writers don't have to worry about it.
![enter image description here](https://i.imgur.com/qK7wc4a.png)

## Important APIs of the Framework
### Post
- `timestamp`: `java.util.Date`. Indicates when the post was created
- `content`: `String`. Contains the text content of the post
- `location`: `Optional<String>`. Optional field that indicates where the post was created
##### SimplePost
Implements the `Post` interface. It contains no sentiment information. The data plugin will only need to pass a list of `SimplePost` to the framework.
##### AnalyzedPost
Implements the `Post` interface. In addition to the general `Post` methods, it also contains sentiment information. The framework will process `SimplePost` objects and turn those into `AnalyzedPost` objects, so the visualization plugin can display sentiment information associated with the posts.

------------


### Sentiment
- `score`: `double`. Ranges from -1.0 to 1.0. It indicates the overall emotion of the text.
- `magnitude`: `double`. Ranges from 0.0 to positive infinity. It indicates how much emotional content is present within the text content.
##### SentimentType
The `enum` type classifies the score of a sentiment into 5 categories. A sentiment score of -1.0 ~ -0.6 indicates `VERY_NEGATIVE`, -0.6 ~ -0.2 indicates `NEGATIVE`, -0.2 ~ 0.2 indicates `NEUTRAL`, 0.2 ~ 0.6 indicates `POSITIVE`, 0.6 ~ 1.0 indicates `VERY_POSITIVE`.

------------


### Account
An `Account` is simply a class consists of a list of `AnalyzedPost` and an account name. `Account` will be used as the **basis of data analysis and visualization**. It means that each time some posts are fetched from the data plugin, the framework will process them as the content of an `Account`. The visualization plugin is responsible for visualizing a list of accounts provided by the framework.

------------

### DataVisualizationFramework
There are only 2 core methods to keep in mind:
- `boolean loadAccount(Map<String, String> param)` Loads an account given a map of parameters, and returns whether the action is successful. The framework passes the parameters to the data plugin, and calls the `connect` and `queryPost` methods of the plugin. The framework then creates an account from the posts returned by the plugin, and adds it to the account list manager.
- `void changeVisual(VisualizationPlugin plugin)` Notifies the framework to change the visual of the given visualization plugin from the current account status. REMEMBER, this method will only change the visual of the visualization plugin passed in as the parameter. Other visualization plugins will NOT be affected. This enables the framework to perform visualization change efficiently without doing repeated work.

Generally, the data and visualization plugins will only need to call these two methods of the framework.

## Important APIs of the Plugin
### DataPlugin
The two core methods of a data plugin are `connect` and `queryPosts`. It is very important to clarify the post-conditions of these two methods.
- `boolean connect(Map<String, String> parameters)` will try to establish a connection given the `parameters` map. If the connection is established successfully, the data plugin should be ready to query posts from the established connection. Otherwise, it should return false.
- `List<Post> queryPosts(Map<String, String> parameters)` will try to fetch a list of posts given the `parameters` map. It is GUARANTEED that it will return a list of posts, whether empty or not.

The `parameters` map is generated from the control panel of the data plugin. The same `parameters` map will be used for both `connect` and `queryPosts` methods. More details could be found in the Tutorials section.

### VisualizationPlugin
The only core method of a visualization plugin is `onDisplay`, which returns a JPanel object that visualizes a list of accounts. The preferred size of the JPanel is 650 * 450.

### ControlPoint and ControlPanel
Control points grant users flexible control to the plugins at run time. It can be used in both data plugin and visualization plugin. A control panel comes with a plugin, and consists of multiple control points. One control point should only control one single parameter of the plugin, and therefore, by calling `getParameters` method of the control panel, one could get a map of parameters. More details could be found in the Tutorials section.

### ApplyButton
Data plugin writers should keep in mind that Google Natural Language Processing API could take a long time performing sentiment analysis (could be up to several minutes depends on the text length). Therefore, data plugins should restrict the number of posts passed into the framework to avoid long waiting time. Also, they should use the util class `ApplyButton` for performing `loadAccount` task, instead of writing a JButton and add a `loadAccount` action listener themselves.
The apply button will perform the task off the Event Dispatch Thread(EDT), to prevent freezing the GUI. It will also generate a waiting message telling the user to wait until the task is done. More details could be found in the Tutorials section.

## Tutorials on How to Implement and Run New Plugins
### Implement a DataPlugin
1. Create a package with the name of your plugin under `edu.cmu.cs.cs214.hw5.plugins.dataplugin`.

2. In the package, you should have at least two classes:
    - A class that implements DataPlugin
    - A class that implements ControlPanel

    But most of the time, we also need to implement some control points that control some parameters of the control panel. It is a good practice to separate control points from control panel.

    Now, we have these files in the data plugin package
    ![](https://i.imgur.com/trk7DcR.png)
    - AmazonPlugin: implements DataPlugin
    - AmazonControlPanel: extends ControlPanel
    - AmazonASINModule, AmazonReviewNumModule: implements ControlPoint
3. Let's first look look at AmazonPlugin class.
    - The `getPluginName` method is straightforward. We can simply `return "Amazon Review Plugin"`
    - In the `onRegister` method, we want to at least pass in the framework and set up the control panel. The reason we choose to set up the control panel at this stage is because we want to have a single control panel object for each plugin. And we could simply return this control panel object in the `getControlPanel` method. Now we have already implemented these methods:
![enter image description here](https://i.imgur.com/hLu1ots.png)
   - The remaining methods `connect`, `queryPosts` and `getAccountName` are plugin-specific. You could use any external libraries of your choice to complete these tasks. For the Amazon plugin, we use Jsoup for web scrapping.
   But please pay attention to the `parameters` Map in `connect` and `queryPosts` methods. If you look at the source code, we get the values of the keys `"ASIN/ISBN"` and `"ReviewNum"` out of the map. Later, we will talk about how these key and value pairs go into the map.
4. Then I will go over the code for the control point `AmazonASINModule`. This class controls an input text field where the user will type in the ASIN/ISBN of an Amazon product.
   Because ASIN/ISBN is a 10-character string, we will have our `AmazonASINModule` implements `ControlPoint<String>`.
   - Please pay very close attention to the two methods `getControlName` and `getCurrentValue`. The return value of these methods will be used to generate the `parameters` Map we have seen earlier. Take a look at the source code of `getParameters` method in the `ControlPanel` class to get a clearer understanding:
   ![enter image description here](https://i.imgur.com/Z5xRgGe.png)
   Remember we use the name `"ASIN/ISBN"` in the `AmazonPlugin` class. So these methods should be implemented as follows,
   ![enter image description here](https://i.imgur.com/u1HM9mm.png)
   - Now, go ahead and implement the `onDisplay` method. You should return a JPanel class for this method. Please look at the source code when in doubt.

5. The implementation of `AmazonReviewNumModule` is similar to `AmazonASINModule`. So I will skip the implementation details, and move on to `AmazonControlPanel` class.
   Since ControlPanel is an abstract class, we need to have `AmazonControlPanel` **extend** from the `ControlPanel` class. The core pattern methods have already been written for you, so you only need to implement the `onDisplay` abstract method.
   - Implementing `onDisplay` is very easy. It usually follows a verticle box layout. We just need to stack the JPanels of the control points vertically, and add an ApplyButton below. Remember loading accounts is a time-consuming task, so we **should** use the util `ApplyButton` class provided by the framework. We simply pass in a `Runnable` object, and the message to show when the user is waiting.
   ![enter image description here](https://i.imgur.com/r9p0Wst.png)

And we are done! Simple enough!

### Implement a Visualization Plugin
Implementing a visualization plugin is particularly simple after you finish implementing a data plugin, because the process is about the same. Since `ControlPoint` and `ControlPanel` are generic for both kinds of plugin, I will omit the details here.

The `onDisplay` method for `VisualizationPlugin` is plugin-specifc. You could use any external libraries of your choice to visualize the list of accounts. Please refer to the **Important APIs for the Framework** section about the `Account` API. Also, take a look at the source code when in doubt.

### Additional Information on implementing plugins
We provide the UML diagrams of our plugin, control point and control panel API to show a clearer hierachy. You may find this information helpful when implementing your own plugins.

![enter image description here](https://i.imgur.com/HjwsNRm.png)

### Run your Plugins
Simply add the package name to the file `/resources/META-INF/services/plugin.DataPlugin` or `/resources/META-INF/services/plugin.VisualizationPlugin` depending on the type of your plugin, and you are all set!

## Instructions on How to Obtain API Keys
### Google Cloud Platform Credentials

 1. Go to https://cloud.google.com/natural-language/docs/quickstart-client-libraries, and log in with your non-academic Google account.
 2. Follow the first step in the **Before you Begin** section, and you will get a json file containing your credential information. If this is your first time using Google Cloud Platform services, you will be told to enter your billing information. Please do so, because a Google Cloud Platform project will need a billing account set up to run. However, you will receive **$300 credit for free**, and it will be more than enough for running the framework.
 3. Move the json file to `framework/src/main/resources`.
 4. Copy and paste your full json file name, i.e, "your_file_name.json" to the `CREDENTIAL_FILE` field of the file `framework/src/main/java/edu.cmu.cs.cs214.hw5.framework/core/Config.java`, and you are good to go.

### Twitter API tokens
1. Follow the steps on https://stackoverflow.com/questions/1808855/getting-new-twitter-api-consumer-and-secret-keys to set up your Twitter Application.
2. Copy and paste your consumer key, consumer secret, access token, access token secret to the corresponding fields of the file `plugin/src/main/java/edu.cmu.cs.cs214.hw5.plugins/dataplugin/twitterplugin/Config.java`, and you are good to go.