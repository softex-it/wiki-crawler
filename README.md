### About the project ###

The goal of the project is to help download pages and media resources from wiki-based web sites
and compose offline FDB dictionary bases that later can be viewed by [Dictan for Android](https://play.google.com/store/apps/details?id=info.softex.dictan) and [Dictan4J for PC](http://www.softex.info/downloads). 

Currently available build instructions:
- [Wikipedia](#how-to-download-wikipedia)
- [Lurkmore](#how-to-download-lurkmore)


### How to download [Wikipedia](http://www.wikipedia.org) ###

1. Download the XML dump of Wikipedia that you would like to build. E.g. to download the English one, go
to http://dumps.wikimedia.org/enwiki/, click on the reference with the latest date, find the file 
under the title of "Recombine all pages, current versions only", and download it. Its name will be 
similar to "enwiki-20150304-pages-meta-current.xml.bz2".

   It's recommended to unzip the BZ2 file to XML because processing may be very slow with BZ2.
   
   To download Wikipedia for any other language, you just need to change the language code. E.g. to download
   for Russian, go to http://dumps.wikimedia.org/ruwiki/

2. Download Dictan Converter from http://www.softex.info/downloads, and convert the file obtained at #1 to KEYS
format. To do it:

    *	Choose WIKI as the source format and point it to the downloaded dump file (XML is preferred instead of BZ2)
    *	Choose KEYS as the target format and point it to any empty folder 

    As a result of this step you will have the 'articles_keys.txt' file in the output folder. This file should be used for 
    further processing.

3. Download Git from http://git-scm.com/downloads, unzip it, and add to the PATH system variable

4. Download Maven from https://maven.apache.org/download, unzip it, and add to the PATH system variable

5. Run the following commands at your terminal:

    **git clone https://github.com/softex-it/wiki-crawler.git**
   
    **cd wiki-crawler**

6. Run the following command to download all pages for the keys obtained at #2:

    **mvn clean install exec:java -Dexec.mainClass="info.softex.web.crawler.tools.ListDownloader" -Dexec.args="-i ./wiki/articles_keys.txt -o ./wiki -u http://en.m.wikipedia.org/wiki"**

    Parameters of the command:
      *	-i identifies the input file, it should be the one obtained at #2
      *	-o identifies the output folder for the downloaded pages, it actually will create the 'downloaded' sub folder
      *	-u identifies the http host that will be used for resolution. You should change the first 2 letters to the language that you are downloading. E.g. for Russian it will be http://ru.m.wikipedia.org. Make sure you add '/wiki' after the host name. It's needed because Wiki locates all keys under this folder.

    Downloading may take some time dependent on the speed of your Internet connection. Also the firewall of the website
    may start blocking you for downloading too much. In this case the size of 'articles-error.txt' file will start growing
    reporting about errors. Also the messages of 'Read timed out' will be logged in the terminal. In this case you may 
    interrupt the downloader, and restart in 20-30 minutes. The downloader will resume from the place where it stopped,
    i.e. it won't download the processed pages again.
    
    As a result of this command, there will be a folder of downloaded pages and a set of log files. The file of
    'articles-error.txt' is not supposed to be big. If it's big, you need to run the process again as it was mentioned 
    previously.

7. Run the following command to process all downloaded pages and download media resources:

    **mvn clean install exec:java -Dexec.mainClass="info.softex.web.crawler.wiki.WikiProcessor" -Dexec.args="-i ./wiki/downloaded -o ./wiki -m light -u http://en.m.wikipedia.org/wiki"**

    Parameters of the command:
      *	-i identifies the input folder, it should be the 'downloaded' folder obtained at #6
      *	-o identifies the output folder for the processed pages, the processor will use it to create 'articles_html' and 'media' sub folders
      *	-m sets the level of media download, currently 'full' (download all media), 'light' (download only essential resources) and 'none' (don't download media) are supported
      *	-u identifies the http host that will be used for resolution. You should change the first 2 letters to the language that you are downloading. E.g. for Russian it will be http://ru.m.wikipedia.org. Make sure you add '/wiki' after the host name. It's needed because Wiki locates all keys under this folder.

	It's recommended to use 'light' media download rate (-m) because 'full' will significantly increase the size of the base, and 'none' will not include essential images, e.g. math formulas that are needed for normal reading of the articles.
    
8. Once all steps are done, you can convert the downloaded content to FDB base. You need to use Dictan Converter 
and set the source as HTML pointing to the folder which contains 'articles_html' and 'media' sub folders. The output format should be FDB.


### How to download [Lurkmore](http://lurkmore.to) ###

1. Download Git from http://git-scm.com/downloads, unzip it, and add to the PATH system variable

2. Download Maven from https://maven.apache.org/download, unzip it, and add to the PATH system variable

3. Run the following commands at your terminal:

    **git clone https://github.com/softex-it/wiki-crawler.git**
   
    **cd wiki-crawler**

4. Run the following command to start the resolution of the keys:

    **mvn clean install exec:java -Dexec.mainClass="info.softex.web.crawler.wiki.LurkLinksResolver" -Dexec.args="-o ./lurk/resolved_keys.txt -u http://lurkmore.to -k Лулз -r 20"**
   
    Parameters of the command: 
      *	-o identifies the output file that will be used to store the resolved keys
      *	-u identifies the http host that will be used for resolution. Currently the Lurk host is http://lurkmore.to
      *	-k the initial key to start the resolution from
      *	-r the depth of the recursion for the links resolution, i.e. how deep to follow the links
      
    The resolution will take some time dependent on your Internet connection. It's recommended to give it 2-3 hours.
    If the process doesn't finish, it's absolutely safe to interrupt the process. Once the process is interrupted,
    you may start it with a different initial key (-k) that you may find missing in the resolved keys (-o). 
    Doing it will increase the coverage of resolution.
    
    All files with resolved keys should be appended to one file in any order.

5. It's also recommended to extract the keys from your current Lurk (if you have one) with Dictan Converter,
and append the extracted key to the file obtained at #4. This step is optional though it may increase coverage
by adding all keys resolved previously.

6. Run the following command to download all pages for the resolved keys:

    **mvn clean install exec:java -Dexec.mainClass="info.softex.web.crawler.tools.ListDownloader" -Dexec.args="-i ./lurk/resolved_keys.txt -o ./lurk -u http://lurkmore.to"**

    Parameters of the command:
      *	-i identifies the input file, it should be the one obtained at #4 or #5
      *	-o identifies the output folder for the downloaded pages, it actually will create the 'downloaded' sub folder
      *	-u identifies the http host that will be used for resolution. Currently the Lurk host is http://lurkmore.to

    Downloading may take some time dependent on the speed of your Internet connection. Also the firewall of the website
    may start blocking you for downloading too much. In this case the size of 'articles-error.txt' file will start growing
    reporting about errors. Also the messages of 'Read timed out' will be logged in the terminal. In this case you may 
    interrupt the downloader, and restart in 20-30 minutes. The downloader will resume from the place where it stopped,
    i.e. it won't download the processed pages again.
    
    As a result of this command, there will be a folder of downloaded pages and a set of log files. The file of
    'articles-error.txt' is not supposed to be big. If it's big, you need to run the process again as it was mentioned 
    previously.

7. Run the following command to process all downloaded pages and download media resources:

    **mvn clean install exec:java -Dexec.mainClass="info.softex.web.crawler.wiki.LurkProcessor" -Dexec.args="-i ./lurk/downloaded -o ./lurk -m full -u http://lurkmore.to"**
        
    Parameters of the command:
      *	-i identifies the input folder, it should be the 'downloaded' folder obtained at #6
      *	-o identifies the output folder for the processed pages, the processor will use it to create 'articles_html' and 'media' sub folders
      *	-m sets the level of media download, currently 'full' (download all media) and 'none' (don't download media) are supported
      *	-u identifies the http host that will be used for linking. Currently the Lurk host is http://lurkmore.to

	If you set the 'full' media download rate (-m), processing will take 5-6 hours to finish because it will be downloading all images.

8. Once all steps are done, you can convert the downloaded content to FDB base. You need to download Dictan Converter
from http://www.softex.info/downloads and set the source as HTML pointing to the folder which contains 'articles_html' 
and 'media' sub folders. The output format should be FDB.
