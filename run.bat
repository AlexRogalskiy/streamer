@Echo Off
start javaw -Xms1024m -Xmx1024m -jar target/streamer-1.0.0-jar-with-dependencies.jar -in src/main/resources/INPUT.txt -out src/main/resources/OUTPUT.txt