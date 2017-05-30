#!/bin/bash
# funktioniert nicht mit 1.51n
# mit 1.51j8 getestet und funktioniert
ls -l /home/jhamfler/DigitaleBildverarbeitung/bilder/
echo ""
if [ "$1" == "exit" ]
then
	imagej --headless /home/jhamfler/DigitaleBildverarbeitung/bilder/muenzen-sw-400.gif -macro $(pwd)/42b-exit.ijm /home/jhamfler/DigitaleBildverarbeitung/bilder/muenzen-neu.gif
else
	imagej --headless /home/jhamfler/DigitaleBildverarbeitung/bilder/muenzen-sw-400.gif -macro $(pwd)/42b.ijm /home/jhamfler/DigitaleBildverarbeitung/bilder/muenzen-neu.gif
fi
echo ""
ls -l /home/jhamfler/DigitaleBildverarbeitung/bilder/
