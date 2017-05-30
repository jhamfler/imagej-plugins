// parameter
dir = getDirectory("image"); 
name=getTitle; 
quelle = dir+name; 
ziel = getArgument()

print("oeffne " + quelle)
//open(quelle)

run("Class 42a", quelle)

print("speichere " + ziel)
saveAs("Gif", ziel); 

print("fertig")
//eval("script", "System.exit(0);");
