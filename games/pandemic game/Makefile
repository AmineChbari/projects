jeu.jar: cls
	jar cvfm jeu.jar manifest.txt -C classes pandemic *
	
	mv jeu.jar ./jars
	
test: cls
	find ./test -name "*.java" > test.txt
	javac -cp test4poo.jar @test.txt
	rm test.txt
	
clean:
	find ./classes -name '*.class' -delete
	find ./test -name '*.class' -delete
	rm -rf docs
	
doc: cls
	javadoc -cp jars/*:src -d docs -subpackages pandemic main

cls:
	find src/pandemic -name "*.java" > src.txt
	javac -cp src:jars/json-20220924.jar -d classes @src.txt src/main/Livrable4Main.java
	rm src.txt

.PHONY: clean doc cls
