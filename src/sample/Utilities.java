package sample;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;

import java.util.Random;

public final class Utilities {
    private Utilities(){}

    public static String randomColor() {
        Random r = new Random();
        int red = r.nextInt(255);
        int green = r.nextInt(255);
        int blue = r.nextInt(255);

        return String.format("#%02X%02X%02X",red,green,blue);
    }

    public static void startStop(Button startStop) {
        switch (startStop.getText()) {
            case "►":
                startStop.setText("▉");
                startStop.setTextFill(Color.DARKRED);
                break;
            case "▉":
                startStop.setText("►");
                startStop.setTextFill(Color.GREEN);
                break;
        }
    }


}
