**TweetInsight** Is a java based tool that analyzes sentiment in tweets related to the COVID-19 pandemic.

## Features
- Sentiment analysis of tweets using Stanford NLP
- Word frequency calculation
- CSV parsing for specific tweet data
- Temporal filtering of tweets

## Technologies Used
- Java
- Stanford NLP
- SimpleDateFormater for date parsing
- BufferedReader for various file operations

## Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/tylerc122/TweetInsight.git
   cd TweetInsight
2. **Make sure you have JDK (Java Development Kit) installed:**
    If you don't happen to have it installed, you can find the download [here](https://www.oracle.com/java/technologies/downloads/)
3. **Install Stanford Core NLP:**
    Install the zip folder [here](https://nlp.stanford.edu/software/stanford-corenlp-full-2018-10-05.zip)
4. **Extract needed files:**
    
    From the zip folder, you only need two files:
    ```bash
    stanford-corenlp-3.9.2.jar
    stanford-english-corenlp-2018-10-05.jar
    ```
    The third file you will need can be found [here](https://repo1.maven.org/maven2/com/googlecode/efficient-java-matrix-library/ejml/0.23/ejml-0.23.jar)
5. **Create new folder for dependencies:**
    - In TweetInsight folder, create a new folder 'lib' or 'dependencies'
    - Place the three jar files in it
6. **Compile and run the project:**
    
    if you made your dependencies folder 'lib'
    ```bash
    javac -cp "lib/*:." -d . src/*.java
    ```
    'dependencies'
    ```bash
    javac -cp "dependencies/*:." -d . src/*.java
    ```
    Finally, run the project:
    ```bash
    java -cp "lib/*:." src.Driver
    ```

