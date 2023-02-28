package powershell;

import java.io.*;

public class PowerConsole {

    private ProcessBuilder pb;
    Process p;
    boolean closed = false;
    PrintWriter writer;
    ByteArrayOutputStream normal_output_stream = new ByteArrayOutputStream();
    ByteArrayOutputStream error_output_stream = new ByteArrayOutputStream();
    Thread principal_thread;


    PowerConsole(String[] commandList, Thread principal_thread) {
        this.principal_thread =principal_thread;
        pb = new ProcessBuilder(commandList);
        try {
            p = pb.start();
        } catch (IOException ex) {
            throw new RuntimeException("Cannot execute PowerShell.exe", ex);
        }
        writer = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(p.getOutputStream())), true);
        Gobbler outGobbler = new Gobbler(p.getInputStream(), new PrintStream(this.normal_output_stream));
        Gobbler errGobbler = new Gobbler(p.getErrorStream(), new PrintStream(this.normal_output_stream));

        //MIO

        Thread outThread = new Thread(outGobbler);
        Thread errThread = new Thread(errGobbler);
        outThread.start();
        errThread.start();
    }

    public String execute(String command, int ns){
        return execute(command,ns,false);
    }

    public String execute(String command, boolean silent){
        return execute(command, 200, silent);
    }

    public String execute(String command){
        return execute(command, 200);
    }

    public String execute(String command, int ms, boolean silent) {

       //DA QUA
        if (!closed) {
            if(!silent) {
                System.out.println(command);
            }
            if(command.contains("exit")){
                try {
                    normal_output_stream.close();
                    writer.println(command);
                    writer.close();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            writer.println(command);
            writer.flush();


        } else {
            throw new IllegalStateException("Power console has ben closed.");
        }
        String datax = "";
        writer.println("Write-Output \"ENDRESPONSE\"");
        writer.flush();
        while(!(datax+= normal_output_stream.toString()).contains("ENDRESPONSE")){
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String data = datax.replace("ENDRESPONSE","");

        try {
            normal_output_stream.flush();
            normal_output_stream.reset();
        } catch (IOException e) {
            System.out.println("ERROR: " + normal_output_stream.toString());

        }
        return data;

    }

    public void close() {
        execute("exit");
        /*try {

            p.waitFor();
        } catch (InterruptedException ex) {
        }*/
    }

    public static void main(String[] args) throws IOException, InterruptedException {
//, "-NoExit"
        PowerConsole pc = new PowerConsole(new String[]{"powershell.exe", "-NoExit", "-Command", "-"}, Thread.currentThread());
        System.out.println("========== Executing dir");
        while(true){
            String r = pc.execute("Write-Output ciao");
            System.out.println(r);
        }

    }

}
