package powershell;

import java.awt.desktop.SystemEventListener;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

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

    public String execute(String command) {

       //DA QUA
        if (!closed) {
            //String newcommand = "& {"+ command + "}; & {Write-Output \"STOPNOTIFY\"}";
            //System.out.println(command);
            writer.println(command);
            writer.flush();
        } else {
            throw new IllegalStateException("Power console has ben closed.");
        }
        String datax = "";
        //while(normal_output_stream.toString().length()==0 || !datax.equals(normal_output_stream.toString())){
        writer.println("Write-Output \"ENDRESPONSE\"");
        writer.flush();
        while(!(datax+= normal_output_stream.toString()).contains("ENDRESPONSE")){
            //datax += normal_output_stream.toString();
            //System.out.println("@  " + normal_output_stream.toString() + "[" + datax + "]");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String data = datax.replace("ENDRESPONSE","");

        try {
            //System.out.println(normal_output_stream.toString());
            normal_output_stream.flush();
            normal_output_stream.reset();
           // System.out.println("DOPO FLUSH: " + normal_output_stream.toString());
        } catch (IOException e) {
            System.out.println("ERROR: " + normal_output_stream.toString());

        }

        //principal_thread.start();
        //A QUA NON SI TOCCANN NUDD
        return data;

    }

    /*public String getResponse() throws IOException {

        final String utf8 = StandardCharsets.UTF_8.name();

        String data = normal_output_stream.toString();
        normal_output_stream.flush();
        return data;
    }*/

    public void close() {
        try {
            execute("exit");
            p.waitFor();
        } catch (InterruptedException ex) {
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
    /*   PowerConsole pc = new PowerConsole(new String[]{"/bin/bash"});

    PowerConsole pc = new PowerConsole(new String[]{"/bin/bash"});
    pc.execute("pwd");
    pc.execute("ls");
    pc.execute("cd /");
    pc.execute("ls -l");
    pc.execute("cd ~");
    pc.execute("find . -name 'test.*' -print");
    pc.close();
     */
        //      PowerConsole pc = new PowerConsole(new String[]{"cmd.exe"});
        powershell.PowerConsole pc = new powershell.PowerConsole(new String[]{"powershell.exe", "-NoExit", "-Command", "-"}, Thread.currentThread());
        System.out.println("========== Executing dir");
        pc.execute("Connect-MsolService");
        System.out.println("========== Executing cd\\");
        pc.execute("Get-MsolUser"); Thread.sleep(2000);

        pc.close();
    }

}
