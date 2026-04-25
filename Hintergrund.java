import basis.*;

public class Hintergrund {
    // Grundlegende Steuerelemente
    private Tastatur t;                  // Für Tastatureingaben
    private Fenster fenster;             // Das Spielfenster
    private Stift s;                     // Zum zeichnen
    private BeschriftungsFeld scoreAnzeigeL, scoreAnzeigeR;  // Punkteanzeigen
    private Ball ball;                   // Der Spielball

    // Punktezähler für beide Spieler
    private int scoreL = 0;              // Punkte von linke Spieler 
    private int scoreR = 0;              // Punkte des rechten Spielers

    // Daten für die Schläger (Paddles)
    private double leftPaddleY;          // Y-Position des linke Schlägers
    private double rightPaddleY;         // Y-Position des rechten Schlägers
    private final int paddleWidth = 10;  // Breite der Schläger
    private final int paddleHeight = 100; // Höhe der Schläger
    private int moveDelta = 25;     // bewegungsschritt
    private int bewegungsSchritt; // Bewegungsgeschwindigkeit der Schläger
    private int torCounter = 0; // Zählt die Tore für abwechselndes Zuspiel

    
    private int rechteckGroesse;         // Bewegungsschrittgröße
    private int l;                       // Zählvariable für Hintergrundzeichnung
    private String richtungLinks, richtungRechts;  // Speichert Bewegungsrichtungen
    

    public Hintergrund() {
        // Erstelle das Spielfenster und alle benötigten Objekte
        fenster = new Fenster("Pong", 1500, 1000);
        s = new Stift();
        t = new Tastatur();
        rechteckGroesse = moveDelta;
        richtungLinks = "";
        richtungRechts = "";

        // Erstelle die Punkteanzeigen (links und rechts)
        scoreAnzeigeL = new BeschriftungsFeld("Spieler 1: 0 ", 130, 50, 200, 60);
        scoreAnzeigeL.setzeSchriftFarbe(Farbe.GELB);
        scoreAnzeigeL.setzeSchriftGroesse(30);
        scoreAnzeigeL.setzeHintergrundFarbe(Farbe.SCHWARZ);

        scoreAnzeigeR = new BeschriftungsFeld("Spieler 2: 0 ", 1180, 50, 200, 60);
        scoreAnzeigeR.setzeSchriftFarbe(Farbe.GELB);
        scoreAnzeigeR.setzeSchriftGroesse(30);
        scoreAnzeigeR.setzeHintergrundFarbe(Farbe.SCHWARZ);

        // Setze die Schläger auf ihre Startposition (mittig)
        leftPaddleY = fenster.hoehe() / 2.0 - paddleHeight / 2.0;
        rightPaddleY = fenster.hoehe() / 2.0 - paddleHeight / 2.0;

        // Stelle der Hintergrund schwarz ein
        fenster.setzeHintergrundFarbe(Farbe.SCHWARZ);

        // Zeichne der Hintergrund und die Schläger
        Background();
        zeichneSchlaeger();

        // Erstelle der Ball
        ball = new Ball(fenster, s);
        //ball2 = new Ball(fenster, s);
        
        // Hauptspielschleife - läuft immer wieder
        while (true) {
            steuerung();                     // Lese Tastatureingaben
            steuerung();                        // Bewege die Schläger
            ball.update();                   // aktualisiere Ballposition
            aktualisiereScoreUndPaddleCollision();  // Prüfe Kollisionen und Tore

            clearScreen();                   // Lösche der Bildschirm
            Background();                    // Zeichne Hintergrund neu
            zeichneSchlaeger();              // Zeichne die Schläger neu
            ball.draw();                     // Zeichne der Ball neu
            //ball2.draw();

            Hilfe.pause(20);                 // Kurze Pause für flüssige Animation
        }
    }

    public void aktualisiereScoreUndPaddleCollision() {
        // Prüfe, ob der Ball an der LINKEN Seite ist
        if (ball.x - ball.r <= 10 + paddleWidth) {
            // Prüfe, ob der Ball den linken Schläger trifft
            if (ball.y >= leftPaddleY && ball.y <= leftPaddleY + paddleHeight) {
                // Ball trifft Schläger - er prallt nach rechts ab
                ball.vx = Math.abs(ball.vx);
                ball.x = 10 + paddleWidth + ball.r; //UGUGVIU
            } 
            // Ball verfehlt Schläger und erreicht den linken Rand
            else if (ball.x - ball.r <= 0) {
                // Tor für Spieler 2!
                scoreR++;
                scoreAnzeigeR.setzeText("Spieler 2: " + scoreR);
                torCounter++; // Erhöhe den Torzähler

                // Berechne die neue Geschwindigkeit
                double newVx = Math.abs(ball.vx) + 1;
                if (newVx > 28) {
                    newVx = 28;
                }

                // Entscheide wer bekommt Ball: 1 ungerade, 0 gerade
                if (torCounter % 2 == 1) { // Ungerade Tore: Spieler 1 bekommt Ball , berechnet den Divisionsrest von zahl durch 2. 
                    ball.vx = -newVx;  // Ball fliegt nach links
                } else { // Gerade Tore: Spieler 2 bekommt Ball
                    ball.vx = newVx;   // Ball fliegt nach rechts
                }
                // Setze der Ball wieder in die Mitte
                ball.x = fenster.breite() / 2.0;
                ball.y = fenster.hoehe() / 2.0;

                // Erhöhe die Schlägergeschwindigkeit
                bewegungsSchritt = bewegungsSchritt + 1;
                rechteckGroesse = bewegungsSchritt;
            }
        }

        // Prüfe, ob der Ball an RECHTER Seite ist
        if (ball.x + ball.r >= 1480) {
            // Prüfe, ob der Ball den rechten Schläger trifft
            if (ball.y >= rightPaddleY && ball.y <= rightPaddleY + paddleHeight) {
                // Ball trifft den Schläger - er prallt nach links ab
                ball.vx = -Math.abs(ball.vx);
                ball.x = 1480 - ball.r;
            } 
            // Ball verfehlt den Schläger , erreicht den rechten Rand
            else if (ball.x + ball.r >= fenster.breite()) {
                // Tor für Spieler 1!
                scoreL++;
                scoreAnzeigeL.setzeText("Spieler 1: " + scoreL);
                torCounter++; // Erhöhe den Torzähler

                // Erhöhe die Ballgeschwindigkeit, maximal 28
                double newVx = Math.abs(ball.vx) + 1;
                if (newVx > 28) {
                    newVx = 28;
                }

                // Entscheide basierend auf Torzähler, wer bekommt der Ball
                if (torCounter % 2 == 1) { // Ungerade Tore: Spieler 1 bekommt Ball
                    ball.vx = -newVx;  // Ball fliegt nach links
                } else { // Gerade Tore: Spieler 2 bekommt Ball
                    ball.vx = newVx;   // Ball fliegt nach rechts
                }

                // Erhöhe auch die vertikale Geschwindigkeit
                if (ball.vy >= 0) {
                    ball.vy = ball.vy + 1;
                    if (ball.vy > 28)
                        ball.vy = 28;
                } else {
                    ball.vy = ball.vy - 1;
                    if (ball.vy < -28)
                        ball.vy = -28;
                }

                // Setze den Ball wieder in die Mitte
                ball.x = fenster.breite() / 2.0;
                ball.y = fenster.hoehe() / 2.0;

                // Erhöhe die Schlägergeschwindigkeit
                moveDelta = moveDelta + 1;
                //rechteckGroesse = moveDelta;
            }
        }
    }

    // Prüft tasteneingaben und bewegt die Schläger mit angepasster Geschwindigkeit
    public void steuerung() {
        // reduziere Bewegungsgeschwindigkeit für langsamere Schläger
        bewegungsSchritt = 17;  // Ist für Schläger, kleinerer Wert für langsamere Bewegung

        // LINKER SCHLÄGER: W = nach oben, S = nach unten
        if (t.istGedrueckt('W') || t.istGedrueckt('w')) {
            // Bewege der linken Schläger nach oben
            leftPaddleY = leftPaddleY - bewegungsSchritt;
        }
        if (t.istGedrueckt('S') || t.istGedrueckt('s')) {
            // Bewege den linken Schläger nach unten
            leftPaddleY = leftPaddleY + bewegungsSchritt;
        }

        // RECHTER SCHLÄGER: O = nach oben, L = nach unten
        if (t.istGedrueckt('O') || t.istGedrueckt('o')) {
            // Bewege den rechten Schläger nach oben
            rightPaddleY = rightPaddleY - bewegungsSchritt;
        }
        if (t.istGedrueckt('L') || t.istGedrueckt('l')) {
            // Bewege den rechten Schläger nach unten
            rightPaddleY = rightPaddleY + bewegungsSchritt; 
        }

        // Halte die Schläger im spielfeld
        // LINKER SCHLÄGER:
        if (leftPaddleY < 0) {
            leftPaddleY = 0;  // Nicht über den oberen Rand
        }
        if (leftPaddleY > fenster.hoehe() - paddleHeight) {
            leftPaddleY = fenster.hoehe() - paddleHeight;  // Nicht über den unteren Rand 
        }

        // RECHTER SCHLÄGER:
        if (rightPaddleY < 0) {
            rightPaddleY = 0;  // Nicht über den oberen Rand 
        }
        if (rightPaddleY > fenster.hoehe() - paddleHeight) {
            rightPaddleY = fenster.hoehe() - paddleHeight;  // Nicht über den unteren Rand 
        }
    }

    // Löscht der Bildschirm, indem jede Zeile schwarz übermalt wird
    public void clearScreen() {
        s.setzeFarbe(Farbe.SCHWARZ);
        for (int j = 0; j < fenster.hoehe(); j++) {
            s.hoch();
            s.bewegeBis(0, j);
            s.runter();
            s.bewegeBis(fenster.breite(), j);
        }
    }

    // Zeichnet der Hintergrund mit die gestrichelte Mittellinie
    public void Background() {   
        s.setzeFarbe(Farbe.WEISS);
        for (l = 0; l < 45; l++) {
            double xpos = fenster.breite() / 2.0;  // Mitte des Bildschirms
            int ypos = 50 + (l * 20);  // Abstand zwischen den Strichen
            s.hoch();
            s.bewegeBis(xpos, ypos);
            s.runter();
            s.zeichneRechteck(5, 15);  // Zeichne kleine weiße Rechtecke als Strichlinie
        }
    }

    // Zeichnet die beiden Schläger (Paddles) an ihren aktuellen positionen
    public void zeichneSchlaeger() {
        s.setzeFarbe(Farbe.WEISS);

        // Zeichne der linken Schläger
        s.hoch();
        s.bewegeBis(10, leftPaddleY);  
        s.runter();
        s.zeichneRechteck(paddleWidth, paddleHeight);  // Zeichne der Schläger

        // Zeichne den RECHTEN Schläger
        s.hoch();
        s.bewegeBis(1480, rightPaddleY);  
        s.runter();
        s.zeichneRechteck(paddleWidth, paddleHeight);  // Zeichne den Schläger
    }

    
}
