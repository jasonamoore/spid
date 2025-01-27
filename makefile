compile:
	javac -sourcepath src -d bin src/**/*.java
demo:
	java -cp bin demo/Demo
clean:
	rm bin/**/*.class