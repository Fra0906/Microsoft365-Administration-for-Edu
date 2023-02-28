package powershell;

import java.awt.*;
import java.io.*;

class Gobbler implements Runnable {

    private PrintStream out;
    private String message;

    private BufferedReader reader;
    private InputStream inputStream; //ROB

    public Gobbler(InputStream inputStream, PrintStream out) {
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        this.out = out;
        this.message = ( null != message ) ? message : "";
        this.inputStream = inputStream; //ROB
    }

    public void run() {

        while(true) {
            try {
                String line;


                while (null != (line = this.reader.readLine())) {
                    //System.out.println("Sto inviando: " + message + line);
                    out.println(message + line);

                }

                this.reader.reset();
                //out.println("ENDRESPONSE");
                //this.reader = new BufferedReader(new InputStreamReader(inputStream)); //ROB
            } catch (IOException e) {
                System.err.println("ERROR: " + e.getMessage());
            }
        }
    }
}