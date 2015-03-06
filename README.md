
Example of running Wiki Downloader from Maven:

mvn exec:java -Dexec.mainClass="info.softex.web.crawler.wiki.WikiListDownloader" -Dexec.args="-i /wiki/articles_keys.txt -o /wiki/downloaded -u http://ru.m.wikipedia.org"