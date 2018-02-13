JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	IP_And_Port.java \
	GroupServer.java \
	GroupServerImpl.java \
	StartServer.java \
	Client.java \
	UDPServer.java \
	TestClass.java

default: classes

classes: $(CLASSES:.java=.class)

clean: 
	$(RM) *.class

