package hillelauto.Robert_cools.Utils.connectionToSkyfence;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import hillelauto.Robert_cools.Utils.helpers.Tools;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Properties;

public class SkyfenceSshVerification {

    private static String username = SkyfenceConnectionSettings.getUserNameConsole();
    private static String password = SkyfenceConnectionSettings.getPasswordConsole();
    private static String hostname = SkyfenceConnectionSettings.getHostName();
    private static String logPath = "/var/log/automationLog/templog"+ Tools.timeStamp()+".tmp";
    private static ChannelExec channel;
    private static Session session;
    // Indicates if it is the first time current test failed
    private static boolean firstFailure = true;

    public static void startMappingUpdateLogging() {
        String command = "(tail -n0 -f /var/log/gateway.log > " + logPath + ") & sleep 30 ; kill $!";
        sendCommand(command);
    }
    public static void makeDirForLogs() {
        String command = "mkdir /var/log/automationLog";
        sendCommand(command);
    }
    public static boolean checkMappingUpdateLog() {
        stopLogging(15);

        String pattern = "Configuration update success";
        System.out.println("Looking for pattern:" + pattern);

        return getLog().contains(pattern);
    }

    public static Session getSession() {
        if (session == null || !session.isConnected()) {
            System.out.println("Session started");
            session = connect(hostname, username, password);
        }
        return session;
    }

    private static Session connect(String hostname, String username, String password) {
        JSch jSch = new JSch();
        try {
            session = jSch.getSession(username, hostname, 22);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(password);
            session.connect(60000);
            System.out.println("Connected to " + hostname + " with username: " + username);
        } catch (Exception e) {
            System.out.println("An error occurred while connecting to " + hostname + ": " + e);
        }
        return session;
    }

    public static void sendCommand(String command) {
        try {
            channel = (ChannelExec) SkyfenceSshVerification.getSession().openChannel("exec");

            System.out.println("Sending command:\n" + command);
            channel.setPty(true);
            channel.setCommand(command);
            channel.connect(60000); // Max connection time
            System.out.println("execChannel connected");

            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // run before each test
    public static void startActivityLogging() {
        String command = "date > " + logPath + " ; " + "tail -f -n0 /var/log/gateway.log | grep --line-buffered 'Activity:\\|empty.username' >> " + logPath;
        sendCommand(command);
    }
    public static void deleteTempLogs() {
        String command = "rm " + logPath;
        sendCommand(command);
    }


    // run after each test
    public static void saveActivityLog(String testDesc, String xmlTestName, String testName, boolean actionSuccessful) {
        // TODO cleanup all actionSuccessful checks
        stopLogging(actionSuccessful ? 8 : 0);

//        if (!actionSuccessful && !firstFailure)
//            return;
        System.out.println("Saving log for " + testDesc);
        if (actionSuccessful || (!firstFailure && !actionSuccessful)) {
            MappingReports.addActivityReports(testDesc.trim(),xmlTestName, testName, actionSuccessful, actionSuccessful ? getLog() : "");
            firstFailure = true;
        }else firstFailure = actionSuccessful;
    }

    private static void stopLogging(int delay) {
        System.out.println("Disconnecting from reading channel after " + delay + " seconds");

        try {
            Thread.sleep(delay * 1000);
            OutputStream out = channel.getOutputStream();
            // Sending CTRL + C
            out.write(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        channel.disconnect();
        System.out.println("execChannel disconnected");
    }

    private static String getLog() {
        String gwLog = "";

        try {
            ChannelSftp sftpChannel = (ChannelSftp) SkyfenceSshVerification.getSession().openChannel("sftp");
            sftpChannel.connect(60000);
            System.out.println("sftpChannel connected");
            InputStream input = sftpChannel.get(logPath);
            StringWriter writer = new StringWriter();
            IOUtils.copy(input, writer, "UTF-8");
            gwLog = writer.toString();
            sftpChannel.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("sftpChannel disconnected");
        SkyfenceSshVerification.getSession().disconnect();
        System.out.println("Session dropped");
        return gwLog;
    }
}