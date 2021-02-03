# legacy Makefile provided for non-ant users/systems

test:
	javac KBDCheck.java
	java  KBDCheck

jar:
	javac KBDCheck.java
	jar cfve kbd_check.jar KBDCheck *.java *.class

clean:
	-rm *.class 2> /dev/null
