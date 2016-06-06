#! /bin/sh
mkdir ToonNoltenAssignment4
cd src/TripLength
mvn install
mv target/Excercise1.jar ../../ToonNoltenAssignment4/
mvn clean
cd ../TripRevenue
mvn install
mv target/Excercise2.jar ../../ToonNoltenAssignment4/
mvn clean
cd ../../ToonNoltenAssignment4
cp ../README .
cp ../report.pdf .
cp ../src/ .
cd ..
tar czf Toon_Nolten-Assignment4.tar.gz ToonNoltenAssignment4
rm -r ToonNoltenAssignment4
