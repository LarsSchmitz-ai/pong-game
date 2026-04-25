import basis.*;

public class Ball {
    // Fenster und Zeichenwerkzeug
    private Fenster fenster;
    private Stift stift;

    // position, Geschwindigkeit und Größe des Balls
    public double x;        // X-Position (Mittelpunkt)
    public double y;        // Y-Position (Mittelpunkt)
    public double vx;       // Geschwindigkeit in X-Richtung (Pixel pro Schritt)
    public double vy;       // Geschwindigkeit in Y-Richtung (Pixel pro Schritt) 
    public double r;        // Radius des Balls

    // Erstellt einen neuen Ball im Zentrum des Fensters
    public Ball(Fenster f, Stift s) {
        // Speichere Fenster und Stift
        fenster = f;
        stift = s;

        // setze Ball in Mitte des Fensters
        x = fenster.breite() / 2.0;
        y = fenster.hoehe() / 2.0;

        // Setze Größe und Geschwindigkeit
        r = 10;             // Ball mit Radius 10 Pixel
        vx = 20;            // 20 Pixel pro Schritt nach rechts
        vy = 20;            // 20 Pixel pro Schritt nach unten
    }

    // aktualisiert die Position des Balls und prüft auf Kollisionen
    public void update() {

        x = x + vx; // Geschwindigkeit wird zur aktuellen position addiert
        y = y + vy; // Geschwindigkeit wird zur aktuellen position addiert


        // Prüft, ob Ball oberen Rand berührt
        if (y - r < 0) {
            y = r;          // Setze Ball genau auf den oberen Rand
            vy = -vy;       // Mit der geschwindigkeit, mit der der Ball an die Wand prallt schießt der wieder zurück
        } 
        // Prüfe ob der Ball den unteren Rand berührt
        else if (y + r > fenster.hoehe()) {
            y = fenster.hoehe() - r;  // Setze Ball genau auf den unteren Rand
            vy = -vy;                 // Lasse den Ball nach oben abprallen
        }

        // Prüfe, ob der Ball den linken Rand berührt
        if (x - r < 0) {
            x = r;          // Setze Ball genau auf den linken Rand
            vx = -vx;       // Lasse den Ball nach rechts abprallen
        } 
        // Prüfe ob der Ball den rechten Rand berührt
        else if (x + r > fenster.breite()) {
            x = fenster.breite() - r;  // Setze Ball genau auf den rechten Rand
            vx = -vx;                  // Lasse den Ball nach links abprallen
        }
    }

    public void draw() {
    
    stift.setzeFarbe(Farbe.WEISS);

    // Jetzt wird Zeile für Zeile von oben nach unten durchgegangen,
    // also vom oberen Rand vom Kreis bis zum unteren.
    for (int yLine = (int)(y - r); yLine <= (int)(y + r); yLine++) {

        // dy ist wie weit die Zeile vom Mittelpunkt weg is.
        double dy = yLine - y;

        // Dann wird berechnet wie breit der Kreis da gerade ist, also von der Mitte nach links oder rechts.
        double half = Math.sqrt(r * r - dy * dy);

        // Der Startpunkt der Linie, also wo die Linie links anfangen soll.
        int startX = (int)(x - half);

        // Und hier das Ende, wo sie rechts wieder aufhört.
        int endX = (int)(x + half);

        
        stift.hoch();

        // Stift an den Anfang von der Linie.
        stift.bewegeBis(startX, yLine);

        
        stift.runter();

        // Dann zeichnet der Stift die Linie von links nach rechts.
        stift.bewegeBis(endX, yLine);
    }
}


}
