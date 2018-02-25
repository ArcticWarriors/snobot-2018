package com.snobot.vision_app.app2018;

import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileCleanupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_cleanup);

        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File dir = new File (root.getAbsolutePath() + "/SnobotVision");

        List<File> directories = listVisionFolders(dir);
        System.out.println(directories);

        for(File directory : directories) {
            zipFolder(dir, directory);
        }

        Toast.makeText(this, "Done zipping!", Toast.LENGTH_LONG).show();
    }

    private void zipFolder(File aOutputFolder, File aFolderToZip)
    {
        Toast.makeText(this, "Zipping '" + aFolderToZip.getName() + "'", Toast.LENGTH_SHORT).show();
        List<File> filesToZip = walkDirectory(aFolderToZip);
        System.out.println("FILES:" + aFolderToZip.getName());

        final int BUFFER = 1024;

        try
        {
            String outputFile = aOutputFolder + "/" + aFolderToZip.getName() + ".zip";
            System.out.println("Dumping to " + outputFile);

            BufferedInputStream origin;
            FileOutputStream dest = new FileOutputStream(outputFile);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            byte data[] = new byte[BUFFER];

            for (File file : filesToZip)
            {
                Log.i("Compress", "Adding: " + file.getAbsolutePath());
                FileInputStream fi = new FileInputStream(file);
                origin = new BufferedInputStream(fi, BUFFER);


                ZipEntry entry = new ZipEntry(file.getAbsolutePath());
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.println();
    }

    private List<File> walkDirectory(File aDirectory)
    {
        List<File> output = new ArrayList<>();
        File[] listOfFiles = aDirectory.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            File file = listOfFiles[i];
            if (file.isDirectory()) {
                output.addAll(walkDirectory(file));
            }
            else if (file.isFile()) {
                output.add(file);
            }
        }

        return output;
    }

    private List<File> listVisionFolders(File aDirectory)
    {
        List<File> output = new ArrayList<>();
        File[] listOfFiles = aDirectory.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isDirectory()) {
                output.add(listOfFiles[i]);
            }
        }

        return output;
    }
}
