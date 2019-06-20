package info.androidhive.recyclerviewswipe.service;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import info.androidhive.recyclerviewswipe.MyApplication;
import timber.log.Timber;

public class FTPService {

    private static final String server = "78.46.228.244";
    private static final int port = 21;
    private static final String login = "ftp_juvelirgradru_upl_files";
    private static final String password = "ftp1jvlr1j08c7a6fb4a1";
    private static final String fileName = "file_2.xml";

    public static Boolean downloadAndSaveFile(String server, int portNumber,
                                        String user, String password, String filename, File localFile)
            throws IOException {
        FTPClient ftp = null;

        try {
            ftp = new FTPClient();ftp.connect(server, portNumber);
            Timber.d( "Connected. Reply: " + ftp.getReplyString());

            ftp.login(user, password);
            Timber.d("Logged in");
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            Timber.d("Downloading");
            ftp.enterLocalPassiveMode();

            OutputStream outputStream = null;
            boolean success = false;
            try {
                outputStream = new BufferedOutputStream(new FileOutputStream(
                        localFile));
                success = ftp.retrieveFile(filename, outputStream);
                Timber.e( String.valueOf(ftp.getReplyCode()));
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }

            return success;
        } finally {
            if (ftp != null) {
                ftp.logout();
                ftp.disconnect();
            }
        }
    }

    static class MyTask extends AsyncTask<FTPDownloadCallback, Void, Void> {

        FTPDownloadCallback ftpDownloadCallback;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(FTPDownloadCallback... params) {
            this.ftpDownloadCallback = params[0];
            try {
                FTPService.downloadAndSaveFile(server, port, login
                        , password, fileName, new File(MyApplication.appPath, fileName));
            }
            catch (Exception e){
                Timber.e("Update base error: "+ e.getMessage());
                MyApplication.getInstance().makeToast("Ошибка при обновлении базы!", Toast.LENGTH_LONG);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ftpDownloadCallback.complete();
        }
    }

    public void updateBase(FTPDownloadCallback ftpDownloadCallback){
        new MyTask().execute(ftpDownloadCallback);
    }


}
