CC=gcc
CPP=g++

SRCDIR=./
HEADDIR=./
LIBDIR=obj
BINDIR=bin
DIR=./


# Les différentes options de compilations, soyons strictes !
CFLAGS= -I$(HEADDIR) -Wall
# Les différents FrameWorks et bibliothèques pour le linkage
GLLIBS = -lm -lncurses -lwiringPi -lpthread 

# Où trouver les différents sources *.c qu'il faudra compiler pour créer les objets correspondants
SRC= $(wildcard $(SRCDIR)/*.c)
OBJ= $(SRC:$(SRCDIR)/%.c=$(LIBDIR)/%.o)

# Où trouver les différents sources *.cpp qu'il faudra compiler pour créer les objets correspondants
SRCPP= $(wildcard $(SRCDIR)/*.cpp)
OBJCPP= $(SRCPP:$(SRCDIR)/%.cpp=$(LIBDIR)/%.o)

all : main 

#Création de l'exécutable
main: $(LIBDIR)/main.o $(LIBDIR)/AccelStepper.o
	@$(CPP) -o $(BINDIR)/$@ $^ $(CFLAGS) $(GLLIBS)
	
# Création des différents *.o à partir des *.c
$(LIBDIR)/%.o: $(SRCDIR)/%.c  
	$(CC) -o $@ -c $< $(CFLAGS)

# Création des différents *.o à partir des *.cpp
$(LIBDIR)/%.o: $(SRCDIR)/%.cpp 
	$(CPP) -o $@ -c $< $(CFLAGS)

# Nettoyage des objets => Tout sera recompiler !
clean:
	rm $(LIBDIR)/*.o

