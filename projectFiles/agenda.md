Höchst wahrscheinlich:


Demnächst:
- Dokumentieren
- StarLayer Geschwindigkeit anpassen
- Power Ups
- Godmode mehr Funktionen einbauen
- Stottern verhindern durch DeltaTime
  - MovementHandler und Painter
  - DeltaTime durch Multiplikator ersetzen? (0.02d)

Langfristig / Vielleicht:
- Endgegner
- Optionenmenü
- Für Mobilgeräte verfügbar machen
  - IPA erstellen
  - APK erstellen
- Backend
- Refactoring von spawnFighterLaser -> In Controls-Klasse auslagern?
- Anpassen von Schwierigkeitsgrad?
- Touch-Steuerung verbessern -> 

Refactoring:
- Configs objektorientiert
- nanoTime auf ms umsteigen
- Godmode in eigene Klasse auslagern


PowerUp-Ideen:
- Aufsammeln von PowerUp
- Painter
  - Farbakzent für Schiff nach Aufsammeln
  - Hintergrundfarbe verändern
  - Alternative Laser (Textur, Farbe, etc.)
  - Farbiges Overlay
- Schrifteinblendung
- PowerUp-Handler-Klasse
  - Gültigkeitszeit von PowerUp verwalten
  - Aufruf von Config-Methoden um Effekte zu verwalten
  - 